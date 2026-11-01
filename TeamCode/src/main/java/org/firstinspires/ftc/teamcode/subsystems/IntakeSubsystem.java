package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem {
    private DcMotor motor;
    private boolean running, wasPressed;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, IntakeSettings.MOTOR_NAME);
        motor.setPower(0);
        motor.setDirection(IntakeSettings.MOTOR_DIRECTION);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void start(boolean buttonPressed) {
        stop();
        wasPressed = buttonPressed;
    }

    public void update(boolean buttonPressed) {
        if (buttonPressed && !wasPressed) {
            running = !running;
            motor.setPower(running ? IntakeSettings.INTAKE_POWER : 0);
        }
        wasPressed = buttonPressed;
    }

    public void stop() {
        running = false;
        motor.setPower(0);
    }
}
