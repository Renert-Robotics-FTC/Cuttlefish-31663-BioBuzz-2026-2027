package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlowerIntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeController;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSettings;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;


@TeleOp(name = "Cuttlefish Drive", group = "Cuttlefish")
public final class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware lookup happens during INIT. A missing configuration name
        // produces an error here instead of waiting until the robot is moving.
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        IntakeSubsystem intake = null;
        FlowerIntakeSubsystem flowerIntake = null;
        try {
            // Disabled by default until the actual intake hardware is calibrated.
            if (IntakeSettings.ENABLE_IN_TELEOP) {
                intake = new IntakeSubsystem(hardwareMap);
            }
            // The flower arm can be enabled independently of the ball intake.
            if (FlowerIntakeSubsystem.ENABLE_IN_TELEOP) {
                flowerIntake = new FlowerIntakeSubsystem(hardwareMap);
            }
            // Show the driver what layout and controls this starting code expects.
            telemetry.addLine("Assumes four mecanum wheels: FL, FR, BL, BR.");
            telemetry.addLine("Left stick: move. Right stick X: turn.");
            telemetry.addLine("Hold left bumper for slow mode.");
            if (intake == null) {
                telemetry.addLine("Intake disabled: review IntakeSettings.");
            } else {
                telemetry.addLine("Auto intake after START: stop at 4.");
                telemetry.addLine("Gamepad 2: LB reverse, right trigger feed, hold X stop.");
            }
            if (flowerIntake == null) {
                telemetry.addLine("Flower arm disabled: configure FlowerIntakeSubsystem.");
            } else {
                telemetry.addLine("Flower arm raises at START. Gamepad 2 A toggles down/up.");
            }
            telemetry.update();
            // The robot stops here until START is pressed.
            waitForStart();
            if (isStopRequested()) {
                return;
            }
            // Raise once after START a button held during START must be released first.
            if (flowerIntake != null) {
                flowerIntake.start(gamepad2.a);
            }
            while (opModeIsActive()) {
                // Use reduced power for initial testing, bumper gives finer control.
                double speed = 0.4;
                if (gamepad1.left_bumper) {
                    speed = 0.2;
                }
                // Pushing the stick forward gives negative Y, so flip its sign.
                // The left stick moves/strafe, the right stick rotates the robot.
                drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                        deadband(gamepad1.right_stick_x), speed);
                if (intake != null) {
                    // Collect automatically whenever below capacity, after sensor warmup.
                    // X or simultaneous feed/reverse stops, releasing returns to auto intake.
                    //FEED only runs the shared motor, shooter speed/aim control comes later.
                    boolean eject = gamepad2.left_bumper;
                    boolean feed = gamepad2.right_trigger > 0.5;
                    IntakeController.Request request;
                    if (gamepad2.x || (feed && eject)) {
                        request = IntakeController.Request.STOP;
                    } else if (feed) {
                        request = IntakeController.Request.FEED;
                    } else if (eject) {
                        request = IntakeController.Request.EJECT;
                    } else {
                        request = IntakeController.Request.COLLECT;
                    }
                    intake.update(request);
                    intake.addTelemetry(telemetry);
                } else {
                    telemetry.addLine("Intake disabled: review IntakeSettings.");
                }
                if (flowerIntake != null) {
                    flowerIntake.update(gamepad2.a);
                    flowerIntake.addTelemetry(telemetry);
                }
                telemetry.addData("Drive power cap", "%.0f%%", speed * 100);
                telemetry.update();
                idle();
            }
        } finally {

            try {
                drive.stop();
            } finally {
                // Stop the intake even if stopping the drivetrain raises an error.
                if (intake != null) {
                    intake.stop();
                }
            }
        }
    }

    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        //Ignore tiny stick movements
        if (magnitude <= 0.05) {
            return 0;
        }
        //scale the remaining stick travel smoothly from zero to full power.
        double adjustedMagnitude = (magnitude - 0.05) / 0.95;
        return Math.copySign(adjustedMagnitude, value);
    }
}
