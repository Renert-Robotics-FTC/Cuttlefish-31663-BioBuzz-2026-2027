package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlowerIntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSettings;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
@TeleOp(name = "Cuttlefish Drive", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        IntakeSubsystem intake = null;
        FlowerIntakeSubsystem flower = null;
        try {
            if (IntakeSettings.ENABLE_IN_TELEOP) intake = new IntakeSubsystem(hardwareMap);
            if (FlowerIntakeSubsystem.ENABLE_IN_TELEOP) flower = new FlowerIntakeSubsystem(hardwareMap);
            telemetry.addData("Intake enabled", intake != null);
            telemetry.addData("Flower arm enabled", flower != null);
            telemetry.addLine("Enabled intake collects automatically after START. Flower arm starts raised.");
            telemetry.update();
            waitForStart();
            if (isStopRequested()) return;
            if (flower != null) flower.start(gamepad2.a);

            while (opModeIsActive()) {
                double speed = DriveConstants.DRIVE_POWER;
                if (gamepad1.left_bumper) speed = DriveConstants.SLOW_POWER;
                drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                        deadband(gamepad1.right_stick_x), speed);

                //X stop, RT feed, LB reverse, A toggle flower arm.
                if (intake != null) {
                    intake.update(gamepad2.x, gamepad2.right_trigger > 0.5, gamepad2.left_bumper);
                    intake.addTelemetry(telemetry);
                }
                if (flower != null) flower.update(gamepad2.a);
                telemetry.addData("Drive power cap", speed);
                telemetry.update();
                idle();
            }
        } finally {
            try {
                drive.stop();
            } finally {
                if (intake != null) intake.stop();
            }
        }
    }
    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        if (magnitude <= DriveConstants.STICK_DEADBAND) return 0;
        return Math.copySign((magnitude - DriveConstants.STICK_DEADBAND)
                / (1 - DriveConstants.STICK_DEADBAND), value);
    }
}
