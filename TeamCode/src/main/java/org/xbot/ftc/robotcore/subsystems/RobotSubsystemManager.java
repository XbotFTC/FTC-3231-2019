package org.xbot.ftc.robotcore.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.xbot.ftc.robotcore.utils.GameClock;
import org.xbot.ftc.robotcore.XbotSubsystemRegister;

import java.util.HashMap;
import java.util.Map;

public class RobotSubsystemManager {

    private static RobotSubsystemManager instance = null;
    private static boolean initialized = false;

    private static Map<String, XbotSubsystem> registeredSubsystemsMap = new HashMap<>();

    private GameClock gameClock;

    private RobotSubsystemManager() {
    }

    public void registerSubsystem(XbotSubsystem subsystem) {
        registeredSubsystemsMap.put(subsystem.getClassName(), subsystem);
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        if (initialized) return;

        new XbotSubsystemRegister().registerListeners(this);
        gameClock = GameClock.getInstance();
        for (XbotSubsystem subsystem : registeredSubsystemsMap.values())
            subsystem.init(hardwareMap, telemetry);
        gameClock.resetClock();

        initialized = true;
    }

    public void stop() {
        for (XbotSubsystem subsystem : registeredSubsystemsMap.values())
            subsystem.shutdownSubystem();
        initialized = false;
    }

    public void setActiveOpMode(LinearOpMode opMode) {
        for (XbotSubsystem subsystem : registeredSubsystemsMap.values())
            subsystem.setActiveOpMode(opMode);
    }

    public XbotSubsystem getSubsystem(String className) {
        return registeredSubsystemsMap.get(className);
    }

    public GameClock getGameClock() {
        return gameClock;
    }

    public static RobotSubsystemManager getInstance() {
        if (instance == null) {
            instance = new RobotSubsystemManager();
        }
        return instance;
    }
}
