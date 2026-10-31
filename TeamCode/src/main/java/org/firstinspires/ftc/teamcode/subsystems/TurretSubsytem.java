package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class TurretSubsytem {
    public static double TURN_POWER = 0.2;
    private DcMotor turretMotor;

    public TurretSubsytem(HardwareMap hardwareMap){
        turretMotor = hardwareMap.get(DcMotor.class, "turret");

        turretMotor.setPower(0);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public void update(double turn) {
        if (Math.abs(turn) <= 0.05) turn = 0;
        turretMotor.setPower(turn * TURN_POWER);
    }

    public void stop() {
        turretMotor.setPower(0);
    }
}
