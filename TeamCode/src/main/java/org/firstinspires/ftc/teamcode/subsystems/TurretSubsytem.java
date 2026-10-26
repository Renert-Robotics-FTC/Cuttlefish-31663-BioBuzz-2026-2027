package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
// Stuff included with the turret subsystem
// Turret Motor: Motor that turns the entire shooter left/right through 50t to 150t gear
// the hood servo: changes the angle of launch through 45 t gear to 286t hood gear
// Absolute encoder measures turret position, through a second 50t gear
// probably limelight,
public class TurretSubsytem {
    private DcMotor turretMotor;
    private Servo hoodServo;
    private Servo gateServo;

    public TurretSubsytem(HardwareMap hardwareMap){
        turretMotor = hardwareMap.get(DcMotor.class, "turret");
        hoodServo = hardwareMap.get(Servo.class, "hood");
        gateServo = hardwareMap.get(Servo.class, "gate");
        turretMotor.setPower(0);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
