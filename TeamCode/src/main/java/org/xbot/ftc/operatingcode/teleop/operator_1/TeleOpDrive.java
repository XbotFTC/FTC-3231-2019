package org.xbot.ftc.operatingcode.teleop.operator_1;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.xbot.ftc.robotcore.subsystems.drive.ArcadeDrive;
import org.xbot.ftc.robotcore.utils.XbotTelemetry;
import org.xbot.ftc.operatingcode.teleop.XbotOperatorSubHandler;
import org.xbot.ftc.robotcore.subsystems.drive.Drive;

public class TeleOpDrive extends XbotOperatorSubHandler {

    private Drive drive;
    private ArcadeDrive arcadeDrive;

    @Override
    public void start() {
        drive = (Drive) robotSystemsManager.getSubsystem(Drive.class.getName());
        arcadeDrive = drive.getArcadeDrive();
        drive.setMotorPowerMultiplier(1.0);
    }

    @Override
    public void handle(Gamepad gamepad1, Gamepad gamepad2) {
        arcadeDrive.drive(gamepad1);
    }

    @Override
    public void stop() {
        drive.stop();
    }

    @Override
    public void updateTelemetry() {
    }
}
