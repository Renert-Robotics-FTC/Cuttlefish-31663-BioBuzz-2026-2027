package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

//Controls a four-wheel drivetrain. pushing the
//stick forward drives toward the robot's front, regardless of field position.

public final class DriveSubsystem {
    private final DcMotor fl, fr, bl, br;

    // Look up motors by the names in the Robot Controller configuration. */
    public DriveSubsystem(HardwareMap hardwareMap) {
        this(hardwareMap.get(DcMotor.class, "FL"), hardwareMap.get(DcMotor.class, "FR"),
                hardwareMap.get(DcMotor.class, "BL"), hardwareMap.get(DcMotor.class, "BR"));
    }

    public DriveSubsystem(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        this.fl = fl; this.fr = fr; this.bl = bl; this.br = br;
        for (DcMotor motor : new DcMotor[] {fl, fr, bl, br}) {
            //Start still, use direct power rather than encoder-based speed control.
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            // Brake helps prevent the robot from coasting after the sticks are released.
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        // Opposite sides usually need opposite motor directions to move forward.
        // These directions are provisional, verify with the wheels off the ground.
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double forward, double right, double clockwise, double scale) {
        //Combined commands can exceed 1. Divide them together to keep every
        //wheel within the legal power range while preserving steering ratios.
        double denominator = Math.max(1, Math.abs(forward) + Math.abs(right) + Math.abs(clockwise));
        double gain = Math.max(0, Math.min(1, scale)) / denominator;

        fl.setPower((forward + right + clockwise) * gain);
        fr.setPower((forward - right - clockwise) * gain);
        bl.setPower((forward - right + clockwise) * gain);
        br.setPower((forward + right - clockwise) * gain);
    }

    public void stop() { drive(0, 0, 0, 0); }
}
