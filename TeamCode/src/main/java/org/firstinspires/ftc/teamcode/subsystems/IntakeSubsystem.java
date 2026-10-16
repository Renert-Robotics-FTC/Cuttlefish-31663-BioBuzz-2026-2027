package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class IntakeSubsystem {
    private final DcMotorEx motor;
    private final NormalizedColorSensor[] sensors = new NormalizedColorSensor[4];
    private final double[] brightness = new double[4];
    private final IntakeController controller;
    private final ElapsedTime clock = new ElapsedTime();
    private double commandedRpm;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        IntakeSettings.validateForMotion();
        controller = new IntakeController(IntakeSettings.BALL_PRESENT, IntakeSettings.BALL_ABSENT,
                IntakeSettings.SENSOR_DEBOUNCE_SECONDS, IntakeSettings.BALL_CAPACITY);
        motor = hardwareMap.get(DcMotorEx.class, IntakeSettings.MOTOR_NAME);
        motor.setPower(0);
        motor.setDirection(IntakeSettings.MOTOR_DIRECTION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = hardwareMap.get(NormalizedColorSensor.class, IntakeSettings.SENSOR_NAMES[i]);
            sensors[i].setGain((float) IntakeSettings.SENSOR_GAIN);
        }
    }

    public void update(IntakeController.Request request) {
        try {
            for (int i = 0; i < sensors.length; i++) {
                NormalizedRGBA color = sensors[i].getNormalizedColors();
                brightness[i] = color.red + color.green + color.blue;
            }
            // First decide what to do, then choose the motor speed for that action.
            controller.update(brightness, clock.seconds(), request);
            IntakeController.State state = controller.getState();

            if (state == IntakeController.State.FEEDING) {
                commandedRpm = IntakeSettings.FEED_RPM;
            } else if (state == IntakeController.State.COLLECTING) {
                commandedRpm = IntakeSettings.MOTOR_TARGET_RPM;
            } else if (state == IntakeController.State.EJECTING) {
                commandedRpm = -IntakeSettings.REVERSE_RPM;
            } else {
                commandedRpm = 0;
            }

            double ticksPerSecond = IntakeController.rpmToTicksPerSecond(
                    commandedRpm, IntakeSettings.MOTOR_TICKS_PER_REV);
            motor.setVelocity(ticksPerSecond);
        } catch (RuntimeException error) {
            stop();
            throw error;
        }
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Intake state", controller.getState());
        telemetry.addData("Ball count (-1 = unknown)", controller.getBallCount());
        telemetry.addData("Collection limit", IntakeSettings.BALL_CAPACITY);
        telemetry.addData("Intake full", controller.isFull());
        telemetry.addData("Intake target RPM", commandedRpm);
        telemetry.addData("Intake actual RPM", motor.getVelocity() * 60 / IntakeSettings.MOTOR_TICKS_PER_REV);
        for (int i = 0; i < 4; i++) {
            telemetry.addData(IntakeSettings.SENSOR_NAMES[i], "brightness %.4f | occupied %s",
                    brightness[i], controller.isPresent(i));
        }
        if (controller.getState() == IntakeController.State.SENSOR_FAULT) {
            telemetry.addData("Intake fault", "Invalid sensor reading");
        }
    }

    public void stop() {
        commandedRpm = 0;
        controller.stop();
        try {
            motor.setVelocity(0);
        } finally {
            motor.setPower(0);
        }
    }
}
