package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSettings;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "Cuttlefish Drive", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {

    DcMotorEx shooter;
    DcMotorEx shooter2;

    Servo blocker;

    DcMotor intake;



    @Override
    public void runOpMode()  {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        //IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);
        shooter = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        intake = hardwareMap.get(DcMotor.class, "intake");
        blocker = hardwareMap.get(Servo.class, "servoblocker");
        waitForStart();



            while (opModeIsActive()) {
                double speed = DriveConstants.DRIVE_POWER;
                if (gamepad1.left_bumper) speed = DriveConstants.SLOW_POWER;
                drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                        deadband(gamepad1.right_stick_x), speed);

                // Gamepad 2: A toggles intake/transfer.


                if ( shooter.getVelocity() > -1720) {
                    shooter.setPower(-1);
                    shooter2.setPower(1);
                } else {
                    shooter.setPower(0);
                    shooter2.setPower(0);
                }

                if (gamepad1.right_bumper) {
                    blocker.setPosition(0.5);
                } else {
                    blocker.setPosition(1);
                }

                if (gamepad1.right_bumper || gamepad1.left_bumper) {
                    intake.setPower(1);
                } else {
                    intake.setPower(0);
                }
                telemetry.addData("speed", shooter.getVelocity());
                telemetry.update();






            }

    }
    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        if (magnitude <= DriveConstants.STICK_DEADBAND) return 0;
        return Math.copySign((magnitude - DriveConstants.STICK_DEADBAND)
                / (1 - DriveConstants.STICK_DEADBAND), value);
    }
}
