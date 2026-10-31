package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsytem;
@TeleOp(name = "Cuttlefish Drive", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        TurretSubsytem turret = null;
        try {
            turret = new TurretSubsytem(hardwareMap);
            waitForStart();
            if (isStopRequested()) return;

            while (opModeIsActive()) {
                double speed = DriveConstants.DRIVE_POWER;
                if (gamepad1.left_bumper) speed = DriveConstants.SLOW_POWER;
                drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                        deadband(gamepad1.right_stick_x), speed);

                turret.update(gamepad2.left_stick_x);

                idle();
            }
        } finally {
            try {
                drive.stop();
            } finally {
                if (turret != null) turret.stop();
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
