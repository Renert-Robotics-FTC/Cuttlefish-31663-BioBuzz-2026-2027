package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
@TeleOp(name = "Cuttlefish Drive", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);
        ShooterSubsystem shooter = new ShooterSubsystem(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            double speed = DriveConstants.DRIVE_POWER;
            if (gamepad1.left_bumper) speed = DriveConstants.SLOW_POWER;
            drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                    deadband(gamepad1.right_stick_x), speed);

            shooter.update(gamepad1.right_bumper);
            intake.update(gamepad1.right_bumper || gamepad1.left_bumper);
            idle();
        }

        drive.stop();
        intake.update(false);
        shooter.stop();
    }
    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        if (magnitude <= DriveConstants.STICK_DEADBAND) return 0;
        return Math.copySign((magnitude - DriveConstants.STICK_DEADBAND)
                / (1 - DriveConstants.STICK_DEADBAND), value);
    }
}
