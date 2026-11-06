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

@Autonomous(name = "Cuttlefish Auto", group = "Cuttlefish")
public class Auto extends LinearOpMode {
    @Override
    public void runOpMode(){
        DcMotor fl = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_LEFT_NAME);
        DcMotor fr = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_RIGHT_NAME);
        DcMotor bl = hardwareMap.get(DcMotor.class, DriveConstants.BACK_LEFT_NAME);
        DcMotor br = hardwareMap.get(DcMotor.class, DriveConstants.BACK_RIGHT_NAME);

        fl.setDirection(DriveConstants.FRONT_LEFT_DIRECTION);
        fr.setDirection(DriveConstants.FRONT_RIGHT_DIRECTION);
        bl.setDirection(DriveConstants.BACK_LEFT_DIRECTION);
        br.setDirection(DriveConstants.BACK_RIGHT_DIRECTION);

        DcMotor shooterMotor1;
        DcMotorEx shooterMotor2;

        shooterMotor1 = hardwareMap.get(DcMotor.class,"shooter1");//sm1 is shooter motor 1
        shooterMotor2 = hardwareMap.get(DcMotorEx.class,"shooter2");//sm2 is shooter motor 2
        shooterMotor1.setDirection(DcMotor.Direction.FORWARD);
        shooterMotor2.setDirection(DcMotorEx.Direction.FORWARD);

        shooterMotor1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
            if (runtime.seconds()>6.25) {
                fl.setPower(0);
                fr.setPower(0);
                bl.setPower(0);
                br.setPower(0);

            }else if(runtime.seconds() > 5) {
                shooterMotor1.setPower(0);
                shooterMotor2.setVelocity(0);
                motor.setPower(0);

                fl.setPower(0.25);
                fr.setPower(0.25);
                bl.setPower(0.25);
                br.setPower(0.25);
            } else if(runtime.seconds() > 2){
                shooterMotor1.setPower(0.8);
                shooterMotor2.setVelocity(900);
                motor.setPower(1);
            }else {
                shooterMotor1.setPower(0.8);
                shooterMotor2.setVelocity(900);
            }


            motor.setPower(1);

            sleep(4000);

            shooterMotor1.setPower(0);
            shooterMotor2.setPower(0);
            motor.setPower(0);

            fl.setPower(0.25);
            fr.setPower(0.25);
            bl.setPower(0.25);
            br.setPower(0.25);

            sleep(1250);

            fl.setPower(0);
            fr.setPower(0);
            bl.setPower(0);
            br.setPower(0);

            if(isStopRequested()){
                return;
            }
            idle();
        }



    }
}
