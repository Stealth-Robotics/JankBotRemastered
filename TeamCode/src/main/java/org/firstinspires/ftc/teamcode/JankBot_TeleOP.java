
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOP", group="Iterative Opmode")
public class JankBot_TeleOP extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    DriveOperator driveOperator;
    MechOperator mechOperator;

    private Arm arm;
    private DriveBase driveBase;

    private I2cDeviceSynch pixy;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        driveOperator = new DriveGage(gamepad1);
        mechOperator = new MechAidan(gamepad2);

        arm = new Arm(hardwareMap.get(DcMotor.class, "lift"), hardwareMap.get(DcMotor.class, "extend"),
                hardwareMap.get(DigitalChannel.class, "liftLimit"), hardwareMap.get(DigitalChannel.class, "extendLimit"),
                hardwareMap.get(CRServo.class, "leftWheelIntake"), hardwareMap.get(CRServo.class, "rightWheelIntake"),
                hardwareMap.get(Servo.class, "leftDeployIntake"), hardwareMap.get(Servo.class, "rightDeployIntake"),
                hardwareMap.get(Servo.class, "releaseLatch"), hardwareMap.get(DcMotor.class, "winchLatch"));

        driveBase = new DriveBase(hardwareMap.get(DcMotor.class, "left1"), hardwareMap.get(DcMotor.class, "left2"),
                hardwareMap.get(DcMotor.class, "right1"), hardwareMap.get(DcMotor.class, "right2"),
                hardwareMap.get(BNO055IMU.class, "imu"));

        pixy = hardwareMap.get(I2cDeviceSynch.class, "pixy");

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start()
    {
        arm.zero();
        while(arm.zeroThread.isAlive());
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        driveBase.setSpeed(driveOperator.speed());
        driveBase.setRotSpeed(driveOperator.rotSpeed());
        driveBase.update();

        if (driveOperator.climb())
        {
            arm.ascend();
        }

        arm.setLiftPosition(mechOperator.liftPosition());
        arm.setExtendPostion(mechOperator.extendPosition());

        if (mechOperator.runIntake())
        {
            arm.intake.run();
        }

        if (mechOperator.toggleDeployIntake())
        {
            if (arm.intake.isDeployed())
            {
                arm.intake.undeploy();
            }
            else
            {
                arm.intake.deploy();
            }
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        if (arm.zeroThread.isAlive())
        {
            arm.cancelZero();
        }
    }

}
