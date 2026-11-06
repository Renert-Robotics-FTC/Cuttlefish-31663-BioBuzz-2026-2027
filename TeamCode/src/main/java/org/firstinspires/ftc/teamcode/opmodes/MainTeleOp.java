package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.HaochenShooter;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Cuttlefish Teleop", group = "Cuttlefish")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        HaochenShooter shoot = new HaochenShooter(hardwareMap);
        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);

        boolean ShouldShoot = false;
        boolean ShootCoolDown = true;
        boolean BlockCoolDown = true;
        boolean ShouldBlock = false;
        boolean ShooterVelocityCoolDown = false;

        waitForStart();

        while (opModeIsActive()) {

            double speed = DriveConstants.DRIVE_POWER;
            if ((gamepad2.dpad_up) && (!ShooterVelocityCoolDown)){
                shoot.VelocityIncrease();
                ShooterVelocityCoolDown = true;
            }else if((gamepad2.dpad_down) && (!ShooterVelocityCoolDown)){
                shoot.VelocityDecrease();
                ShooterVelocityCoolDown = true;
            }

            if(!gamepad2.dpad_up && !gamepad2.dpad_down){
                ShooterVelocityCoolDown = false;
            }



            if(gamepad2.b && ShootCoolDown){
                ShouldShoot = !ShouldShoot;
                ShootCoolDown = false;
            }
            if(!gamepad2.b){
                ShootCoolDown = true;
            }

            if(gamepad2.right_bumper && BlockCoolDown){
                ShouldBlock = !ShouldBlock;
                BlockCoolDown = false;
            }
            if(!gamepad2.right_bumper){
                BlockCoolDown = true;
            }


            if(ShouldShoot){
                shoot.FlywheelVelocity(true);
            }else if(!ShouldShoot){
                shoot.FlywheelVelocity(false);
            }

            if (ShouldBlock) {
                shoot.Block();
            } else {
                shoot.Unblock();
            }
            telemetry.addData("Servo Blocker Position: ", shoot.servoPosition());
            telemetry.addData("shooter velcoity:", shoot.shooterMotor2.getVelocity());
            if (gamepad1.left_bumper) speed = DriveConstants.SLOW_POWER;
            drive.drive(deadband(-gamepad1.left_stick_y), deadband(gamepad1.left_stick_x),
                    deadband(gamepad1.right_stick_x), speed);
            intake.update(gamepad1.right_bumper || gamepad1.left_bumper);
            telemetry.update();
            idle();
        }

        drive.stop();
        intake.update(false);
    }
    private static double deadband(double value) {
        double magnitude = Math.abs(value);
        if (magnitude <= DriveConstants.STICK_DEADBAND) return 0;
        return Math.copySign((magnitude - DriveConstants.STICK_DEADBAND)
                / (1 - DriveConstants.STICK_DEADBAND), value);
    }
}
