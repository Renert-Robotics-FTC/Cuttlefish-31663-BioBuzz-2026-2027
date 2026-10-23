package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem {
    private DcMotor motor;
    private NormalizedColorSensor[] sensors = new NormalizedColorSensor[4];
    private int ballCount;
    private double power;
    public IntakeSubsystem(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, IntakeSettings.MOTOR_NAME);
        motor.setPower(0);
        motor.setDirection(IntakeSettings.MOTOR_DIRECTION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        for (int i = 0; i < 4; i++) {
            sensors[i] = hardwareMap.get(NormalizedColorSensor.class, IntakeSettings.SENSOR_NAMES[i]);
            sensors[i].setGain((float) IntakeSettings.SENSOR_GAIN);
            // need to know what this is ^
        }
    }
    public void update(boolean stop, boolean feed, boolean reverse) {
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
        power = 0;
        if (stop || (feed && reverse)) power = 0;
        else if (feed) power = IntakeSettings.FEED_POWER;
        else if (reverse) power = -IntakeSettings.REVERSE_POWER;
        else if (ballCount < 4) power = IntakeSettings.INTAKE_POWER;
        motor.setPower(power);
    }
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Balls (-1 = sensor error)", ballCount);
        telemetry.addData("Intake power", power);
    }
    public void stop() {
        power = 0;
        motor.setPower(0);
    }
}
