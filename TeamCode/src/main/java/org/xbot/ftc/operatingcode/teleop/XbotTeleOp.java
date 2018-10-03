package org.xbot.ftc.operatingcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.xbot.ftc.operatingcode.BaseRobot;
import org.xbot.ftc.robotcore.utils.GameClock;
import org.xbot.ftc.robotcore.utils.XbotTelemetry;
import org.xbot.ftc.robotcore.subsystems.RobotSubsystemManager;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Main: TeleOp", group="Main")
public class XbotTeleOp extends LinearOpMode {

    private List<XbotOperatorSubHandler> handlers = new ArrayList<>();

    public void registerHandler(XbotOperatorSubHandler listener) {
        handlers.add(listener);
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Listeners:", "Registering");
        telemetry.update();
        BaseRobot.initOpMode(this, hardwareMap, telemetry);
        new XbotTeleOpSubHandlerRegister().registerHandlers(this);
        telemetry.addData("Listeners:", "Registered");
        telemetry.update();

        waitForStart();

        GameClock gameClock = RobotSubsystemManager.getInstance().getGameClock();
        gameClock.resetClock();

        for (XbotOperatorSubHandler handler : handlers) {
            handler.start();
        }

        while (opModeIsActive()) {
            for (XbotOperatorSubHandler handler : handlers) {
                handler.handle(gamepad1, gamepad2);
                handler.updateTelemetry();

                for (XbotTelemetry telemetryData : XbotTelemetry.getDataToAddToTelemetry()) {
                    telemetry.addData(telemetryData.getCaption(), telemetryData.getValue());
                }
                telemetry.addData("Game Clock: ", gameClock.getTimeElapsed());
                XbotTelemetry.clearData();
                telemetry.update();
            }
        }

        for (XbotOperatorSubHandler handler : handlers) {
            handler.stop();
        }
        RobotSubsystemManager.getInstance().stop();
    }
}
