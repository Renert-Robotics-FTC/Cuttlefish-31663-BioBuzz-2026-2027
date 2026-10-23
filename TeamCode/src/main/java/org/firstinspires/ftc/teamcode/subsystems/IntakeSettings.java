package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class IntakeSettings {
    public static boolean ENABLE_IN_TELEOP = false;
    public static String MOTOR_NAME = "intake";
    public static String[] SENSOR_NAMES = {"ball1", "ball2", "ball3", "ball4"};
    public static DcMotorSimple.Direction MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static double MOTOR_TARGET_RPM = 1800;
    public static double FEED_RPM = 1800;
    public static double REVERSE_RPM = 450;
    public static double MOTOR_TICKS_PER_REV = Double.NaN;

    // need to change NaN to values for sensor
    public static double[] BALL_PRESENT = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    public static double SENSOR_GAIN = 1;

}
