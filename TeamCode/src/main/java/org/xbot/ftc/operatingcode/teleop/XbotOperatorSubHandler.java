package org.xbot.ftc.operatingcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.xbot.ftc.robotcore.subsystems.RobotSubsystemManager;

public abstract class XbotOperatorSubHandler {

    protected RobotSubsystemManager robotSystemsManager = RobotSubsystemManager.getInstance();

    public abstract void start();
    public abstract void handle(Gamepad gamepad1, Gamepad gamepad2);
    public abstract void stop();
    public abstract void updateTelemetry();
}
