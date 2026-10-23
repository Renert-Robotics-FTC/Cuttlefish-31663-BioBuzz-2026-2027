package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveSubsystem {
    DcMotor FR;
    DcMotor BR;
    DcMotor FL;
    DcMotor BL;

    GoBildaPinpointDriver pinpoint;


    private double straight;
    private double turn;
    private double strafe;

    public DriveSubsystem(HardwareMap hardwareMap) {
        FR = hardwareMap.get(DcMotor.class, "FR");
           BR = hardwareMap.get(DcMotor.class, "BR");
            FL = hardwareMap.get(DcMotor.class, "FL");
                BL = hardwareMap.get(DcMotor.class, "BL");
        // sensor that tracks robots position and direction
                pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        // reset pinpoint position and calibrate heading while the robot is not moving
        pinpoint.resetPosAndIMU();

        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //reverse left motors bc they are opposite right motors
        BL.setDirection(DcMotor.Direction.REVERSE);
         FL.setDirection(DcMotor.Direction.REVERSE);
    }

    public void updateInputs(double newStraight, double newStrafe, double newTurn) {
        straight = newStraight;
        strafe = newStrafe;
        turn = newTurn;

    }
    public void update() {
       //refresh pinpoint odo data before reading robot heading
        pinpoint.update();
        //used ai to help with this line, kept getting error and couldnt figure it out
        double heading = pinpoint.getPosition().getHeading(AngleUnit.RADIANS);

        double robotStraight = straight * Math.cos(heading) - strafe * Math.sin(heading);
        double robotStrafe = straight * Math.sin(heading) + strafe * Math.cos(heading);
        //keep motor power between -1 and 1
        double bottomnumber = Math.max(
                1.0,
                Math.abs(robotStraight) + Math.abs(robotStrafe) + Math.abs(turn)
        );
        double speedcapfactor = 1.0 / bottomnumber;
        //Convert the driver's field direction into the robot's current direction.
        BL.setPower((robotStraight + robotStrafe + turn) * speedcapfactor);
        FL.setPower((robotStraight - robotStrafe + turn) * speedcapfactor);
        BR.setPower((robotStraight - robotStrafe - turn) * speedcapfactor);
        FR.setPower((robotStraight + robotStrafe - turn) * speedcapfactor);


    }
}