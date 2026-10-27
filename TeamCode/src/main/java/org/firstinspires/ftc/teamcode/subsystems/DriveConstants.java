package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
public class DriveConstants {
    public static String FRONT_LEFT_NAME = "FL";
    public static String FRONT_RIGHT_NAME = "FR";
    public static String BACK_LEFT_NAME = "BL";
    public static String BACK_RIGHT_NAME = "BR";
    public static DcMotorSimple.Direction FRONT_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction FRONT_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction BACK_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction BACK_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;

    public static double DRIVE_POWER = 0.4;
    public static double SLOW_POWER = 0.2;
    public static double STICK_DEADBAND = 0.05;
}
