package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlowerIntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.HaochenShooter;
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
        HaochenShooter shoot = new HaochenShooter(hardwareMap);
        try {
            // Disabled by default until the actual intake hardware is calibrated.

            waitForStart();
            boolean ShooterVelocityCoolDown = false;

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

            boolean ShouldShoot = false;
            boolean ShootCoolDown = true;
            boolean BlockCoolDown = true;
            boolean ShouldBlock = false;

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


            if(ShouldShoot){
                shoot.FlywheelVelocity(true);
            }else if(!ShouldShoot){
                shoot.FlywheelVelocity(false);
            }


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

                idle();
            }
        } }finally {

            try {
                drive.stop();
            } finally {
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
