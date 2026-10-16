package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public final class IntakeSettings {
    private IntakeSettings() { }

    // Keep false until the names, encoder, and sensor calibration below are checked.
    // Drive still runs while this is false, no intake hardware is looked up or moved.
    public static final boolean ENABLE_IN_TELEOP = false;
    public static final boolean HARDWARE_REVIEWED = false;

    // Proposed Robot Controller configuration names, match these to the wiring.
    public static final String MOTOR_NAME = "intake";
    public static final String[] SENSOR_NAMES = {"ball1", "ball2", "ball3", "ball4"};
    // One sensor per ball position. Automatically collect below four, stop when full.
    public static final int BALL_CAPACITY = 4;
    public static final double MOTOR_TARGET_RPM = 1800;
    // Provisional feeding speed: the same motor runs both mechanisms toward the shooter.
    // Check this speed with the completed shooter before enabling motion.
    public static final double FEED_RPM = 1800;
    public static final double REVERSE_RPM = 450;
    public static final DcMotorSimple.Direction MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;

    // TODO: encoder counts for one revolution of the MOTOR OUTPUT shaft.
    // The suspected bare goBILDA motor uses 28 counts/rev, verify the actual part first.
    public static final double MOTOR_TICKS_PER_REV = Double.NaN;

    // Four sensors, one beside each ball position.
    // Run Intake Sensor Check with empty slots and each ball color under expected
    // lighting. This method requires a ball to read brighter than an empty slot.
    // NaN means unknown, the subsystem refuses to run with guessed thresholds.
    // Stuff that says NaN need the numbers either from teh sensors or threshoulds for the colours sensors on what colours would count as a ball
    public static final double[] BALL_PRESENT = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    public static final double[] BALL_ABSENT = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    public static final double SENSOR_GAIN = 1.0f;
    public static final double SENSOR_DEBOUNCE_SECONDS = 0.10;

    public static void validateForMotion() {
        if (!HARDWARE_REVIEWED) {
            throw new IllegalStateException("Review IntakeSettings before enabling intake motion.");
        }
        requirePositive(MOTOR_TARGET_RPM, "Motor target RPM");
        requirePositive(FEED_RPM, "Feed RPM");
        requirePositive(REVERSE_RPM, "Reverse RPM");
        requirePositive(MOTOR_TICKS_PER_REV, "Encoder ticks per revolution");
        requirePositive(SENSOR_GAIN, "Sensor gain");
        if (SENSOR_NAMES.length != 4) throw new IllegalStateException("Four ball sensors required.");
        new IntakeController(BALL_PRESENT, BALL_ABSENT, SENSOR_DEBOUNCE_SECONDS, BALL_CAPACITY);
    }

    private static void requirePositive(double value, String name) {
        if (!Double.isFinite(value) || value <= 0) throw new IllegalStateException(name + " must be set.");
    }
}
