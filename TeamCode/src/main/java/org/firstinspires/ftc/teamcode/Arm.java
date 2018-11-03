package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {

    //TODO find actual limits
    private final int LIFT_LOWER_LIMIT = 0;
    private final int LIFT_UPPER_LIMIT = 500;

    //TODO find actual limits
    private final int EXTEND_LOWER_LIMIT = 0;
    private final int EXTEND_UPPER_LIMIT = 1000;

    private boolean isZeroed = false;

    private DcMotor lift;
    private DcMotor extend;

    private DigitalChannel liftLimitSwitch;
    private DigitalChannel extendLimitSwitch;

    //public Hopper hopper;
    public Intake intake;
    public Latch latch;

    public Thread zeroThread;
    private boolean cancel = false;

    public Arm (DcMotor lift, DcMotor extend, DigitalChannel liftLimitSwitch, DigitalChannel extendLimitSwitch,
                CRServo leftWheelIntake, CRServo rightWheelIntake, Servo leftDeployIntake, Servo rightDeployIntake,
                Servo latchRelease, DcMotor winch)
    {
        this.lift = lift;
        this.extend = extend;

        this.liftLimitSwitch = liftLimitSwitch;
        this.extendLimitSwitch = extendLimitSwitch;

        //hopper = new Hopper(leftHopper, rightHopper, tiltHopper);
        intake = new Intake(leftWheelIntake, rightWheelIntake, leftDeployIntake, rightDeployIntake);
        latch = new Latch(latchRelease, winch);
    }

    public void setLiftMode(DcMotor.RunMode mode)
    {
        lift.setMode(mode);
    }

    public void setLiftPower(double power)
    {
        int currentPosition = lift.getCurrentPosition();
        if (power >= 0 && currentPosition <= LIFT_UPPER_LIMIT ||
                power <= 0 && currentPosition >= LIFT_LOWER_LIMIT &&
                isZeroed)
        {
            lift.setPower(power);
        }
    }

    public void setLiftPosition(int position)
    {
        if (position >= LIFT_LOWER_LIMIT && position <= LIFT_UPPER_LIMIT && isZeroed)
        {
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lift.setTargetPosition(position);
        }
    }

    public void setExtendMode(DcMotor.RunMode mode)
    {
        extend.setMode(mode);
    }

    public void setExtendPower(double power)
    {
        int currentPosition = extend.getCurrentPosition();
        if (power >= 0 && currentPosition <= EXTEND_UPPER_LIMIT ||
                power <= 0 && currentPosition >= EXTEND_LOWER_LIMIT &&
                isZeroed)
        {
            extend.setPower(power);
        }
    }

    public void setExtendPostion(int position)
    {
        if (position >= EXTEND_LOWER_LIMIT && position <= EXTEND_UPPER_LIMIT && isZeroed)
        {
            extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            extend.setTargetPosition(position);
        }
    }

    public void liftToScore()
    {
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setTargetPosition(LIFT_UPPER_LIMIT);
        lift.setPower(0.5);
    }

    public void ascend()
    {
        intake.undeploy();

        //motors are floated so they don't interfere with ascent
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        latch.unrelease();

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void descend()
    {
        //motors are floated so they don't interfere with descent
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        latch.release();

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.deploy();
    }

    public void zero()
    {
        //this creates its own thread so that it can be stopped with the OpMode
        Runnable zeroRunnable = new Runnable() {
            @Override
            public void run()
            {
                {
                    boolean liftLimit;
                    boolean extendLimit;



                    do
                    {
                        liftLimit = liftLimitSwitch.getState();
                        extendLimit = extendLimitSwitch.getState();

                        if (!liftLimit)
                        {
                            lift.setPower(-0.5);
                        }
                        else
                        {
                            lift.setPower(0);
                        }

                        if (extendLimit)
                        {
                            extend.setPower(-0.5);
                        }
                        else
                        {
                            extend.setPower(0);
                        }

                        if (cancel)
                        {
                            break;
                        }
                    } while (!liftLimit || !extendLimit);

                    if (!cancel)
                    {
                        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        isZeroed = true;
                    }
                }
            }
        };

        zeroThread = new Thread(zeroRunnable);
        zeroThread.start();
    }

    public void cancelZero()
    {
        cancel = true;
    }
}
