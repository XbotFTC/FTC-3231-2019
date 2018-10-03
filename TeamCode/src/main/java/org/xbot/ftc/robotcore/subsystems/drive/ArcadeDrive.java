package org.xbot.ftc.robotcore.subsystems.drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

public class ArcadeDrive {

    private Drive drive;

    protected ArcadeDrive(Drive drive) {
        this.drive = drive;
    }

    public void drive(Gamepad gamepad) {
        double drivePower = gamepad.left_stick_y;
        double turnPower = gamepad.right_stick_x;
        drive.setMotorPowers(Range.clip(drivePower - turnPower, -1.0, 1.0),
                Range.clip(drivePower + turnPower, -1.0, 1.0));
    }

    public void stop() {
        drive.setMotorPowers(0);
    }
}
