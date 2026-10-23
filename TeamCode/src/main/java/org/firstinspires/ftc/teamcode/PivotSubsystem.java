package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
//P
//I need to control the pivot arm, the pivot arm has multiple different positions it needs to go to. Once done maybe add switch servo
//I need an Intake_Position, Core_Target_Position, and Node_Target_Position.
//To do this I need to get the current position.
//Establish the starting point
//calculate where it needs to be
//provide power to get there
//slow down when it is close

//Make method that changes target position

//D
//Find the derivative
//Store the previous error and current errer to find change in error
//Find a way to know how much time elasped when traveling(figured it out there is a elapsed time function that FTC already has)
//divide the change in error and time elasped to get the derivative
//Times the derivative with kD to get the dContribution
//then finally add the dContribution and the pContribution to get the final amount of power needed

//Do switch servo if have time
public class PivotSubsystem {
    private DcMotorEx pivotMotor;
    private int targetDestination;
    private double kP = 0.001;
    private int previousError;
    private double kD = 0.0005;


    private int INTAKE_POSITION = 0;
    private int CORE_POSITION = 800;
    private int NODE_POSITION = 1900;

    private ElapsedTime timer = new ElapsedTime();

    public PivotSubsystem(DcMotorEx pivotMotor){
        this.pivotMotor = pivotMotor;//kinda like a variable for the motor so it can be used elsewhere
    }

    public void setPivotMotor(double pivotPower) {
        pivotMotor.setPower(pivotPower);
    }

    public int getPivotPosition() {
        return pivotMotor.getCurrentPosition();
    }



    //zeroPivotEncoder defines the starting point so the pivot always knows where it is at
    public void zeroPivotEncoder(){
        pivotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void updatePivot() {
        int error = targetDestination - getPivotPosition();//find the error
        double pContribution = kP * error;//how much power you need to get there(slows the closer it is)
        int changeInError = error - previousError;//Need for derivative
        double deltaTime = timer.seconds();//find the amount of time it takes to move a distance
        timer.reset();
        double derivative = 0;
        if (deltaTime > 0) {
            derivative = changeInError / deltaTime;
        }
        double dContribution = derivative *  kD;
        double power = pContribution + dContribution;
        power = Range.clip(power,-1,1);//make sure it stays in the range of how FTC motors work
        setPivotMotor(power);//moves the motor
        previousError = error;//need for derivative
    }


    //method for changing the target
public void setTargetPosition(int target) {
        targetDestination = target;
}


    public void goToCore(){
        setTargetPosition(CORE_POSITION);
    }
    public void goToIntake(){
        setTargetPosition(INTAKE_POSITION);
    }
    public void goToNode(){
        setTargetPosition(NODE_POSITION);
    }


}

