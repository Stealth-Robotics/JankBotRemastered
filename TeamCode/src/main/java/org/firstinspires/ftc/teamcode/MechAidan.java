package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MechAidan implements MechOperator
{
    //TODO find actual limits
    private final int LIFT_LOWER_LIMIT = 0;
    private final int LIFT_UPPER_LIMIT = 500;

    private final int EXTEND_LOWER_LIMIT = 0;
    private final int EXTEND_UPPER_LIMIT = 1000;

    private final int TILT_LOWER_LIMIT = 0;
    private final int TILT_UPPER_LIMIT = 100;

    Gamepad gamepad;

    public MechAidan(Gamepad gamepad)
    {
        this.gamepad = gamepad;
    }

    @Override
    public int liftPosition()
    {
        return (int)(gamepad.left_trigger * LIFT_UPPER_LIMIT);
    }

    @Override
    public int extendPosition()
    {
        return (int)(gamepad.right_trigger * EXTEND_UPPER_LIMIT);
    }

    @Override
    public boolean runIntake()
    {
        return gamepad.y;
    }

    @Override
    public boolean reverseIntake()
    {
        return gamepad.x;
    }

    @Override
    public int tiltIntakePosition()
    {
        return 0;//(int)(-gamepad.right_stick_y * TILT_UPPER_LIMIT + TILT_UPPER_LIMIT / 2);
    }

}
