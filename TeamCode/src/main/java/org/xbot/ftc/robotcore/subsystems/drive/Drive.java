package org.xbot.ftc.robotcore.subsystems.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.xbot.ftc.robotcore.utils.GameClock;
import org.xbot.ftc.robotcore.XbotRobotConstants;
import org.xbot.ftc.robotcore.subsystems.XbotSubsystem;

public class Drive extends XbotSubsystem {

    private static XbotSubsystem instance = null;
    private static boolean initialized = false;

    private DcMotor leftRearDriveMotor = null;
    private DcMotor rightRearDriveMotor = null;
    private DcMotor leftFrontDriveMotor = null;
    private DcMotor rightFrontDriveMotor = null;

    private DcMotor[] motorArr;

    private double motorPowerMultiplier = 1.0;

    private static final double COUNTS_PER_MOTOR_REV = 280;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 4.0 ;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                    (WHEEL_DIAMETER_INCHES * Math.PI);

    private ArcadeDrive arcadeDrive;

    public enum DrivePower {
        FULL(1.0), HALF(0.5), QUARTER(0.25);

        public final double POWER;

        DrivePower(double power) {
            this.POWER = power;
        }
    }

    public enum TurnDirection {
        LEFT(1, -1), RIGHT(-1, 1);

        public final double LEFT_POWER;
        public final double RIGHT_POWER;

        TurnDirection(double leftPower, double rightPower) {
            this.LEFT_POWER = leftPower;
            this.RIGHT_POWER = rightPower;
        }
    }

    private Drive() {
    }

    @Override
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        if (initialized) return;

        super.init(hardwareMap, telemetry);
        leftRearDriveMotor = hardwareMap.get(DcMotor.class, XbotRobotConstants.BACK_LEFT_MOTOR);
        rightRearDriveMotor = hardwareMap.get(DcMotor.class, XbotRobotConstants.BACK_RIGHT_MOTOR);
        leftFrontDriveMotor = hardwareMap.get(DcMotor.class, XbotRobotConstants.FRONT_LEFT_MOTOR);
        rightFrontDriveMotor = hardwareMap.get(DcMotor.class, XbotRobotConstants.FRONT_RIGHT_MOTOR);

        motorArr = new DcMotor[4];
        motorArr[0] = leftRearDriveMotor;
        motorArr[1] = leftFrontDriveMotor;
        motorArr[2] = rightRearDriveMotor;
        motorArr[3] = rightFrontDriveMotor;

        for (DcMotor motor : motorArr) {
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        arcadeDrive = new ArcadeDrive(this);

        initialized = true;
    }

    public void setMotorPowerMultiplier(double motorPowerMultiplier) {
        this.motorPowerMultiplier = Range.clip(motorPowerMultiplier, 0.0, 1.0);
    }

    public void setMotorPowers(double leftPower, double rightPower) {
        leftPower *= motorPowerMultiplier;
        rightPower *= motorPowerMultiplier;
        leftRearDriveMotor.setPower(leftPower);
        leftFrontDriveMotor.setPower(leftPower);
        rightRearDriveMotor.setPower(rightPower);
        rightRearDriveMotor.setPower(rightPower);
    }

    public void setMotorPowers(double power) {
        setMotorPowers(power, power);
    }

    public void turn(TurnDirection direction, DrivePower drivePower) {
        setMotorPowers(direction.LEFT_POWER * drivePower.POWER,
                direction.RIGHT_POWER * drivePower.POWER);
    }

    public void stop() {
        setMotorPowers(0);
    }

    public void encoderDrive(double power, double leftInches, double rightInches, double timeout) {
        int leftRearTarget = leftRearDriveMotor.getCurrentPosition() +
                (int)(leftInches * COUNTS_PER_INCH);
        int leftFrontTarget = leftFrontDriveMotor.getCurrentPosition() +
                (int)(leftInches * COUNTS_PER_INCH);
        int rightRearTarget = rightRearDriveMotor.getCurrentPosition() +
                (int)(rightInches * COUNTS_PER_INCH);
        int rightFrontTarget = rightFrontDriveMotor.getCurrentPosition() +
                (int)(rightInches * COUNTS_PER_INCH);
        leftFrontDriveMotor.setTargetPosition(leftFrontTarget);
        leftRearDriveMotor.setTargetPosition(leftRearTarget);
        rightFrontDriveMotor.setTargetPosition(rightFrontTarget);
        rightRearDriveMotor.setTargetPosition(rightRearTarget);

        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        double previousMultiplier = motorPowerMultiplier;
        setMotorPowerMultiplier(1.0);
        setMotorPowers(power);

        double startTime = GameClock.getInstance().getTimeElapsed();
        while (opMode.opModeIsActive() &&
                GameClock.getInstance().getTimeElapsed() - startTime < timeout &&
                areMotorsBusy()) {
        }

        setMotorPowers(0);
        setMotorPowerMultiplier(previousMultiplier);

       for (DcMotor motor : motorArr) {
           motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       }
    }

    private boolean areMotorsBusy() {
        for (DcMotor motor : motorArr) {
            if (motor.isBusy()) {
                return true;
            }
        }
        return false;
    }

    public DcMotor getLeftRearDriveMotor() {
        return leftRearDriveMotor;
    }

    public DcMotor getLeftFrontDriveMotor() {
        return leftFrontDriveMotor;
    }

    public DcMotor getRightRearDriveMotor() {
        return rightRearDriveMotor;
    }

    public DcMotor getRightFrontDriveMotor() {
        return rightFrontDriveMotor;
    }

    public ArcadeDrive getArcadeDrive() {
        return arcadeDrive;
    }

    @Override
    public void shutdownSubsystem() {
        setMotorPowers(0);
        initialized = false;
    }

    @Override
    public String getClassName() {
        return Drive.class.getName();
    }

    public static XbotSubsystem getInstance() {
        if (instance == null)
            instance = new Drive();
        return instance;
    }
}
