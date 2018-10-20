package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake
{
    //TODO find actual limits
    private final double UNDEPLOYED_LIMIT = 0;
    private final double DEPLOYED_LIMIT = 0.5;

    private CRServo leftWheel;
    private CRServo rightWheel;

    private Servo leftDeploy;
    private Servo rightDeploy;

    public Intake(CRServo leftWheel, CRServo rightWheel,
                  Servo leftDeploy, Servo rightDeploy)
    {
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;

        this.rightWheel.setDirection(CRServo.Direction.REVERSE);

        this.leftDeploy = leftDeploy;
        this.rightDeploy = rightDeploy;

        this.leftDeploy.scaleRange(UNDEPLOYED_LIMIT, DEPLOYED_LIMIT);
        this.rightDeploy.scaleRange(UNDEPLOYED_LIMIT, DEPLOYED_LIMIT);

        this.rightDeploy.setDirection(Servo.Direction.REVERSE);
    }

    public void run()
    {
        leftWheel.setPower(1);
        rightWheel.setPower(1);
    }

    public void stop()
    {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }

    public void deploy()
    {
        leftDeploy.setPosition(1);
        rightDeploy.setPosition(1);
    }

    public void undeploy()
    {
        leftDeploy.setPosition(0);
        rightDeploy.setPosition(0);
    }

    public boolean isDeployed()
    {
        return leftDeploy.getPosition() > 0.5;
    }
}
