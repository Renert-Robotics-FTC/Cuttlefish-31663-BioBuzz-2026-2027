package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class FlowerIntakeSubsystem {
    // Keep this small mechanism's settings here, in one file. Enable only after
    // confirming a position-controlled servo and measuring both arm positions.
    public static final boolean ENABLE_IN_TELEOP = false;
    public static final String SERVO_NAME = "flowerArm";
    public static final double RAISED_POSITION = Double.NaN;
    public static final double LOWERED_POSITION = Double.NaN;

    private final Servo arm;
    private boolean lowered;
    private boolean wasPressed;
    private boolean started;

    public FlowerIntakeSubsystem(HardwareMap hardwareMap) {
        //NaN means unknown
        if (!validPosition(RAISED_POSITION) || !validPosition(LOWERED_POSITION)
                || RAISED_POSITION == LOWERED_POSITION) {
            throw new IllegalStateException("Measure the flower arm's two servo positions first.");
        }
        arm = hardwareMap.get(Servo.class, SERVO_NAME);
        arm.setDirection(Servo.Direction.FORWARD);
        //No position command during INIT. This expects a position servo, not CRServo.
    }

    public void start(boolean buttonPressed) {
        arm.setPosition(RAISED_POSITION);
        lowered = false;
        wasPressed = buttonPressed;
        started = true;
    }

    public void update(boolean buttonPressed) {
        if (!started) throw new IllegalStateException("Start the flower arm after OpMode START.");
        if (buttonPressed && !wasPressed) {
            boolean nextLowered = !lowered;
            arm.setPosition(nextLowered ? LOWERED_POSITION : RAISED_POSITION);
            lowered = nextLowered;
        }
        // Remember this loop's button state so a release is needed before toggling again.
        wasPressed = buttonPressed;
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Flower arm target", lowered ? "LOWERED" : "RAISED");
        telemetry.addLine("Gamepad 2 A: toggle flower arm.");
    }

    private static boolean validPosition(double value) {
        return Double.isFinite(value) && value >= 0 && value <= 1;
    }

}
