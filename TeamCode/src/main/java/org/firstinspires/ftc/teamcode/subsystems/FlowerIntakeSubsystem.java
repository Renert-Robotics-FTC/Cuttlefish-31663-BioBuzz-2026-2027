package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerIntakeSubsystem {
    public static boolean ENABLE_IN_TELEOP = false;
    public static String SERVO_NAME = "flowerArm";
    public static double RAISED_POSITION = Double.NaN;
    public static double LOWERED_POSITION = Double.NaN;

    private Servo arm;
    private boolean lowered, wasPressed;

    public FlowerIntakeSubsystem(HardwareMap hardwareMap) {
        if (!validPosition(RAISED_POSITION) || !validPosition(LOWERED_POSITION)
                || RAISED_POSITION == LOWERED_POSITION) {
            throw new IllegalStateException("Measure the flower arm's two servo positions first.");
        }
        arm = hardwareMap.get(Servo.class, SERVO_NAME);
        arm.setDirection(Servo.Direction.FORWARD);
    }

    public void start(boolean buttonPressed) {
        arm.setPosition(RAISED_POSITION);
        lowered = false;
        wasPressed = buttonPressed;
    }

    public void update(boolean buttonPressed) {
        if (buttonPressed && !wasPressed) {
            lowered = !lowered;
            arm.setPosition(lowered ? LOWERED_POSITION : RAISED_POSITION);
        }
        wasPressed = buttonPressed;
    }

    private static boolean validPosition(double value) {
        return Double.isFinite(value) && value >= 0 && value <= 1;
    }

}
