package org.xbot.ftc.operatingcode.teleop;

import org.xbot.ftc.operatingcode.teleop.operator_1.TeleOpDrive;

public class XbotTeleOpSubHandlerRegister {

    public void registerHandlers(XbotTeleOp xbotTeleOp) {
        xbotTeleOp.registerHandler(new TeleOpDrive());
    }
}
