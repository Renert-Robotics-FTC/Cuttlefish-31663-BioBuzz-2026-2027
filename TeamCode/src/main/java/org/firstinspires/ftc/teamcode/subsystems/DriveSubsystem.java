package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

//Controls a four-wheel drivetrain. pushing the
//stick forward drives toward the robot's front, doesnt matter field position

public class DriveSubsystem {
    private DcMotor fl, fr, bl, br;

    public DriveSubsystem(HardwareMap hardwareMap) {
        fl = hardwareMap.get(DcMotor.class, "FL");
        fr = hardwareMap.get(DcMotor.class, "FR");
        bl = hardwareMap.get(DcMotor.class, "BL");
        br = hardwareMap.get(DcMotor.class, "BR");
        for (DcMotor motor : new DcMotor[]{fl, fr, bl, br}) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double forward, double right, double clockwise, double scale) {
        double denominator = Math.max(1, Math.abs(forward) + Math.abs(right) + Math.abs(clockwise));
        double gain = Math.max(0, Math.min(1, scale)) / denominator;

        fl.setPower((forward + right + clockwise) * gain);
        fr.setPower((forward - right - clockwise) * gain);
        bl.setPower((forward - right + clockwise) * gain);
        br.setPower((forward + right - clockwise) * gain);
    }

    public void stop() { drive(0, 0, 0, 0); }
}
