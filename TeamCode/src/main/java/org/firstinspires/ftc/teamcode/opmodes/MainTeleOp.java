package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.HaochenShooter;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name = "Cuttlefish Teleop", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        HaochenShooter shoot = new HaochenShooter(hardwareMap);
        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);

        boolean shouldShoot = false;
        boolean shouldBlock = false;
        boolean wasBPressed = false;
        boolean wasBumperPressed = false;
        boolean wasDpadPressed = false;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad2.b && !wasBPressed) {
                shouldShoot = !shouldShoot;
            }
            wasBPressed = gamepad2.b;
            if (gamepad2.right_bumper && !wasBumperPressed) {
                shouldBlock = !shouldBlock;
            }
            wasBumperPressed = gamepad2.right_bumper;
            if (!wasDpadPressed) {
                if (gamepad2.dpad_up) {
                    shoot.VelocityIncrease();
                } else if (gamepad2.dpad_down) {
                    shoot.VelocityDecrease();
                }
            }
            wasDpadPressed = gamepad2.dpad_up || gamepad2.dpad_down;

            shoot.FlywheelVelocity(shouldShoot);

            if (shouldBlock) {
                shoot.Block();
            } else {
                shoot.Unblock();
            }
            drive.drive(
                    deadband(-gamepad1.left_stick_y),
                    deadband(gamepad1.left_stick_x),
                    deadband(gamepad1.right_stick_x),
                    DriveConstants.DRIVE_POWER
            );
            intake.update(gamepad1.right_bumper || gamepad1.left_bumper);

            telemetry.addData("Blocker position", shoot.servoPosition());
            telemetry.addData("Shooter velocity", shoot.shooterVelocity());
            telemetry.update();
            idle();
        }

        drive.stop();
        intake.update(false);
        shoot.FlywheelVelocity(false);
        shoot.Block();
    }

    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        if (magnitude <= DriveConstants.STICK_DEADBAND) return 0;

        return Math.copySign(
                (magnitude - DriveConstants.STICK_DEADBAND)
                        / (1 - DriveConstants.STICK_DEADBAND),
                value
        );
    }
}
