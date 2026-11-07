package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.ConstantFile;
import org.firstinspires.ftc.teamcode.subsystems.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.HaochenShooter;

@Autonomous(name = "Cuttlefish Auto", group = "Cuttlefish")
public class Auto extends LinearOpMode {
    @Override
    public void runOpMode(){
        HaochenShooter shoot = new HaochenShooter(hardwareMap);
        DcMotor fl = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_LEFT_NAME);
        DcMotor fr = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_RIGHT_NAME);
        DcMotor bl = hardwareMap.get(DcMotor.class, DriveConstants.BACK_LEFT_NAME);
        DcMotor br = hardwareMap.get(DcMotor.class, DriveConstants.BACK_RIGHT_NAME);

        fl.setDirection(DriveConstants.FRONT_LEFT_DIRECTION);
        fr.setDirection(DriveConstants.FRONT_RIGHT_DIRECTION);
        bl.setDirection(DriveConstants.BACK_LEFT_DIRECTION);
        br.setDirection(DriveConstants.BACK_RIGHT_DIRECTION);

        DcMotor motor;
        motor = hardwareMap.get(DcMotor.class, "intake");
        motor.setPower(0);
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ElapsedTime runtime = new ElapsedTime(3000);
        runtime.reset();


        waitForStart();
        while (opModeIsActive()){
            if (runtime.seconds()>8.25) {
                fl.setPower(0);
                fr.setPower(0);
                bl.setPower(0);
                br.setPower(0);

            }else if(runtime.seconds() > 7) {
                shoot.FlywheelVelocity(false);
                motor.setPower(0);

                fl.setPower(0.25);
                fr.setPower(0.25);
                bl.setPower(0.25);
                br.setPower(0.25);
            } else if(runtime.seconds() > 4){
                shoot.FlywheelVelocity(true);
                motor.setPower(1);
            }else {
                shoot.FlywheelVelocity(true);
            }
            telemetry.addData("time",runtime.seconds());
            telemetry.update();


            if(isStopRequested()){
                return;
            }
            idle();
        }



    }
}
