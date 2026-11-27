package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/* Goal:
    Shooter Branch that checks whether the user has pressed the button
    Final Speed Variable for Motor
    PID:/

    Encoder and set velocity
    method to increase shooter velocity offset
    In teleop: when driver presses button, then call the methodpackage org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/* Goal:
    Shooter Branch that checks whether the user has pressed the button
    Final Speed Variable for Motor
    PID:/

    Encoder and set velocity
    method to increase shooter velocity offset
    In teleop: when driver presses button, then call the method
 */

public class HaochenShooter {
    double offset = 0;
    public DcMotor shooterMotor1;
    public DcMotorEx shooterMotor2;
    public Servo ServoBlocker;

    public HaochenShooter(HardwareMap hardwareMap){
        shooterMotor1 = hardwareMap.get(DcMotor.class,"shooter1");//sm1 is shooter motor 1
        shooterMotor2 = hardwareMap.get(DcMotorEx.class,"shooter2");//sm2 is shooter motor 2
        ServoBlocker = hardwareMap.get(Servo.class,"servoblocker");//servo blocker
        shooterMotor1.setDirection(DcMotor.Direction.FORWARD);
        shooterMotor2.setDirection(DcMotorEx.Direction.FORWARD);

        shooterMotor1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void Unblock(){
        ServoBlocker.setPosition(ConstantFile.ShooterConstants.Unblocked);
    }
    public double servoPosition() {
        return ServoBlocker.getPosition();
    }
    public void Block(){
        ServoBlocker.setPosition(ConstantFile.ShooterConstants.Blocked);
    }
    public void VelocityIncrease(){
        offset = offset + ConstantFile.ShooterConstants.Increase;
    }

    public void VelocityDecrease(){
        offset = offset + ConstantFile.ShooterConstants.Decrease;
    }
    public double shooterVelocity() {
        return shooterMotor2.getVelocity();
    }
    public boolean check(){
        if(shooterMotor2.getVelocity() < ConstantFile.ShooterConstants.TargetVelocity - offset){
            return true;

        }else{
            return false;
        }

    }
    public void FlywheelVelocity(boolean forward){
        if(forward){
            if(check()) {
                shooterMotor2.setPower(0);
            }else if(!check()){
                shooterMotor2.setPower(1);
            }
            shooterMotor1.setPower(ConstantFile.ShooterConstants.TargetNegVelocity);
        } else if(!forward){
            shooterMotor1.setPower(ConstantFile.ShooterConstants.Stop);
            shooterMotor2.setPower(ConstantFile.ShooterConstants.Stop);
        }
    }






    //variable for offset outside as a method. Increase offset or decrease.Put these as seperate methods.

    //look at resource tab: jinqi


}
