package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

// Field-centric mecanum drive, using the direction at INIT as field-forward.

public class DriveSubsystem {
    private DcMotor fl, fr, bl, br;
    private GoBildaPinpointDriver pinpoint;

    public DriveSubsystem(HardwareMap hardwareMap) {
        fl = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_LEFT_NAME);
        fr = hardwareMap.get(DcMotor.class, DriveConstants.FRONT_RIGHT_NAME);
        bl = hardwareMap.get(DcMotor.class, DriveConstants.BACK_LEFT_NAME);
        br = hardwareMap.get(DcMotor.class, DriveConstants.BACK_RIGHT_NAME);
        for (DcMotor motor : new DcMotor[]{fl, fr, bl, br}) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        fl.setDirection(DriveConstants.FRONT_LEFT_DIRECTION);
        fr.setDirection(DriveConstants.FRONT_RIGHT_DIRECTION);
        bl.setDirection(DriveConstants.BACK_LEFT_DIRECTION);
        br.setDirection(DriveConstants.BACK_RIGHT_DIRECTION);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, DriveConstants.PINPOINT_NAME);
        pinpoint.resetPosAndIMU();
    }

    public void drive(double forward, double right, double clockwise, double scale) {
        pinpoint.update();
        double heading = pinpoint.getPosition().getHeading(AngleUnit.RADIANS);
        if (pinpoint.getDeviceStatus() != GoBildaPinpointDriver.DeviceStatus.READY
                || !Double.isFinite(heading)) {
            stop();
            return;
        }

        double robotForward = forward * Math.cos(heading) - right * Math.sin(heading);
        double robotRight = forward * Math.sin(heading) + right * Math.cos(heading);
        double bottomnumber = Math.max(1, Math.abs(robotForward) + Math.abs(robotRight) + Math.abs(clockwise));
        double gain = Math.max(0, Math.min(1, scale)) / bottomnumber;

        fl.setPower((robotForward + robotRight + clockwise) * gain);
        fr.setPower((robotForward - robotRight - clockwise) * gain);
        bl.setPower((robotForward - robotRight + clockwise) * gain);
        br.setPower((robotForward + robotRight - clockwise) * gain);
    }

    public void stop() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
//
