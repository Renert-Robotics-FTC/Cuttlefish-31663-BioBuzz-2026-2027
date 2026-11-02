package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ShooterSubsystem {
    private DcMotorEx shooter1, shooter2;
    private Servo blocker;

    public ShooterSubsystem(HardwareMap hardwareMap) {
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        blocker = hardwareMap.get(Servo.class, "servoblocker");
        shooter1.setPower(0);
        shooter2.setPower(0);
        shooter1.setDirection(DcMotor.Direction.FORWARD);
        shooter2.setDirection(DcMotor.Direction.FORWARD);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blocker.setPosition(1);
    }

    public void update(boolean feed) {
        if (shooter1.getVelocity() > -1800) {
            shooter1.setPower(-1);
            shooter2.setPower(1);
        } else {
            shooter1.setPower(0);
            shooter2.setPower(0);
        }
        if (feed) blocker.setPosition(0.5);
        else blocker.setPosition(1);
    }

    public void stop() {
        shooter1.setPower(0);
        shooter2.setPower(0);
        blocker.setPosition(1);
    }
}
//