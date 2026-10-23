package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSettings;

@TeleOp(name = "Intake Sensor Check", group = "Cuttlefish")
public class IntakecolorSensorSetup extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        NormalizedColorSensor[] sensors = new NormalizedColorSensor[4];
        for (int i = 0; i < 4; i++) {
            sensors[i] = hardwareMap.get(NormalizedColorSensor.class, IntakeSettings.SENSOR_NAMES[i]);
            sensors[i].setGain((float) IntakeSettings.SENSOR_GAIN);
        }
        telemetry.addLine("Sensors only. Record each empty slot and each ball color's brightness.");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                NormalizedRGBA color = sensors[i].getNormalizedColors();
                telemetry.addData(IntakeSettings.SENSOR_NAMES[i], "R %.4f G %.4f B %.4f | sum %.4f",
                        color.red, color.green, color.blue, color.red + color.green + color.blue);
            }
            telemetry.update();
            idle();
        }
    }
}
