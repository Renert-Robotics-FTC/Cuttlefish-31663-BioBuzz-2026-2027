package org.firstinspires.ftc.teamcode.subsystems;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;

import static java.lang.Double.min;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
public class HaochenAutoAlign {
    public GoBildaPinpointDriver pinpoint;
    public DistanceUnit distanceUnit;
    public HaochenAutoAlign(HardwareMap hardwareMap){
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");
    }
    public double WheelTurn(){
        pinpoint.update();
        Pose2D currentPose = pinpoint.getPosition();
        double currentX = currentPose.getX(DistanceUnit.MM);
        double currentY = currentPose.getY(DistanceUnit.MM);

        double OffsetX = ConstantFile.AlignConstants.HiveX-currentX;
        double OffsetY = ConstantFile.AlignConstants.HiveY-currentY;
        double AngleToHive = Math.toDegrees(Math.atan2(OffsetY,OffsetX));
        double CurrentAngle = currentPose.getHeading(AngleUnit.DEGREES);
        double TurnNeeded = CurrentAngle - AngleToHive;

        double WheelPower = TurnNeeded*ConstantFile.AlignConstants.PowerConstant;
        return min(WheelPower,1);



        //take coordinate of the robot currently
        //calcuate the direct distance to the hive
        //find current angle the robot is facing, and correct it to the correct angle
    }

}
