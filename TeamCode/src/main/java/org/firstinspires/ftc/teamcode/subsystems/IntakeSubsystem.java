package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem {
    private DcMotorEx motor;
    private NormalizedColorSensor[] sensors = new NormalizedColorSensor[4];
    private int ballCount;
    private double rpm;
    public IntakeSubsystem(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, IntakeSettings.MOTOR_NAME);
        motor.setPower(0);
        motor.setDirection(IntakeSettings.MOTOR_DIRECTION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        for (int i = 0; i < 4; i++) {
            sensors[i] = hardwareMap.get(NormalizedColorSensor.class, IntakeSettings.SENSOR_NAMES[i]);
            sensors[i].setGain((float) IntakeSettings.SENSOR_GAIN);
            // need to know what this is ^
        }
    }
    public void update(boolean stop, boolean feed, boolean reverse) {
        try {
            ballCount = 0;
            for (int i = 0; i < 4; i++) {
                NormalizedRGBA color = sensors[i].getNormalizedColors();
                // need to know what this ^
                double brightness = color.red + color.green + color.blue;
                if (!Double.isFinite(brightness) || brightness < 0 || brightness > 3) {
                    // need to know what this is ^
                    ballCount = -1;
                    stop();
                    return;
                }
                if (brightness >= IntakeSettings.BALL_PRESENT[i]) ballCount++;
            }
            rpm = 0;
            if (stop || (feed && reverse)) rpm = 0;
            else if (feed) rpm = IntakeSettings.FEED_RPM;
            else if (reverse) rpm = -IntakeSettings.REVERSE_RPM;
            else if (ballCount < 4) rpm = IntakeSettings.MOTOR_TARGET_RPM;
            motor.setVelocity(rpm * IntakeSettings.MOTOR_TICKS_PER_REV / 60.0);
        } catch (RuntimeException error) {
            stop();
            throw error;
        }
    }
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Balls (-1 = sensor error)", ballCount);
        telemetry.addData("Intake RPM target", rpm);
    }
    public void stop() {
        rpm = 0;
        try {
            motor.setVelocity(0);
        } finally {
            motor.setPower(0);
        }
    }
}
