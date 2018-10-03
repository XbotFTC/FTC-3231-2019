package org.xbot.ftc.robotcore;

import org.xbot.ftc.robotcore.subsystems.RobotSubsystemManager;
import org.xbot.ftc.robotcore.subsystems.drive.Drive;

public class XbotSubsystemRegister {

    public void registerListeners(RobotSubsystemManager robotSubsystemManager) {
        robotSubsystemManager.registerSubsystem(Drive.getInstance());
    }
}
