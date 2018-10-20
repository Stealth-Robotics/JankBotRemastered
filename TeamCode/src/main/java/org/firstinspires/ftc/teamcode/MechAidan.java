package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MechAidan implements MechOperator
{
    //TODO find actual limits
    private final int LIFT_LOWER_LIMIT = 0;
    private final int LIFT_UPPER_LIMIT = 500;

    //TODO find actual limits
    private final int EXTEND_LOWER_LIMIT = 0;
    private final int EXTEND_UPPER_LIMIT = 1000;

    Gamepad gamepad;

    private boolean yTracker;
    private boolean isIntakeDeployed;

    public MechAidan(Gamepad gamepad)
    {
        this.gamepad = gamepad;

        yTracker = false;
        isIntakeDeployed = true;
    }

    @Override
    public int liftPosition()
    {
        return (int)(gamepad.left_trigger * LIFT_UPPER_LIMIT);
    }

    @Override
    public int extendPosition()
    {
        return (int)(-gamepad.left_stick_y * EXTEND_UPPER_LIMIT);
    }

    @Override
    public boolean runIntake()
    {
        return gamepad.y;
    }

    @Override
    public boolean toggleDeployIntake()
    {
        if (gamepad.y && !yTracker)
        {
            isIntakeDeployed = !isIntakeDeployed;
        }

        yTracker = gamepad.y;

        return isIntakeDeployed;
    }
}
