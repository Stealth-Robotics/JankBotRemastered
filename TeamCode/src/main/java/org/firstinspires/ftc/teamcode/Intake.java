package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake
{
    //TODO find actual limits
    private final int LOWER_LIMIT = 0;
    private final int UPPER_LIMIT = 500;

    private CRServo leftWheel;
    private CRServo rightWheel;

    private DcMotor tilt;

    public Intake(CRServo leftWheel, CRServo rightWheel,
                  DcMotor tilt)
    {
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;

        this.rightWheel.setDirection(CRServo.Direction.REVERSE);

        this.tilt = tilt;
        this.tilt.setDirection(DcMotor.Direction.REVERSE);
        this.tilt.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void run()
    {
        leftWheel.setPower(1);
        rightWheel.setPower(1);
    }

    public void reverse()
    {
        leftWheel.setPower(-1);
        rightWheel.setPower(-1);
    }

    public void stop()
    {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }

    public void setTiltPosition(int pos)
    {
        if (pos >= LOWER_LIMIT && pos <= UPPER_LIMIT)
        {
            tilt.setTargetPosition(pos);
        }
    }

    public void setTiltPower(double power)
    {
        tilt.setPower(power);
    }

    public int getTiltPosition()
    {
        return tilt.getCurrentPosition();
    }
}
