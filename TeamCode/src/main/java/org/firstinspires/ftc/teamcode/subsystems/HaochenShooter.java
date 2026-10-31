package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/* Goal:
    Shooter Branch that checks whether the user has pressed the button
    Final Speed Variable for Motor
    PID:

    Encoder and set velocity
    method to increase shooter velocity offset
    In teleop: when driver presses button, then call the method
 */

public class HaochenShooter {
    double offset = 0;
    public DcMotorEx shooterMotor1;
    public DcMotorEx shooterMotor2;
    public HaochenShooter(HardwareMap hardwareMap){
        shooterMotor1 = hardwareMap.get(DcMotorEx.class,"sm 1");//sm1 is shooter motor 1
        shooterMotor2 = hardwareMap.get(DcMotorEx.class,"sm 2");//sm2 is shooter motor 2

        shooterMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void VelocityIncrease(){
        offset = offset + ConstantFile.ShooterConstants.Increase;
    }

    public void VelocityDecrease(){
        offset = offset + ConstantFile.ShooterConstants.Decrease;
    }
    public void FlywheelVelocity(boolean forward){
        if(forward){
            shooterMotor1.setVelocity(ConstantFile.ShooterConstants.TargetVelocity + offset);
            shooterMotor2.setVelocity(ConstantFile.ShooterConstants.TargetNegVelocity - offset);
        } else if(forward = false){
            shooterMotor1.setVelocity(ConstantFile.ShooterConstants.Stop);
            shooterMotor2.setVelocity(ConstantFile.ShooterConstants.Stop);
        }
    }




    //variable for offset outside as a method. Increase offset or decrease.Put these as seperate methods.

    //look at resource tab: jinqi


}
