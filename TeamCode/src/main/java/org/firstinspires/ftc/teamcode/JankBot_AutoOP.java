
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="AutoOP", group="Linear Opmode")
public class JankBot_AutoOP extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private Arm arm;
    private DriveBase base;

    private PixyCam pixy;

    @Override
    public void runOpMode()
    {

        arm = new Arm(hardwareMap.get(DcMotor.class, "lift"), hardwareMap.get(DcMotor.class, "extend"),
                hardwareMap.get(DigitalChannel.class, "liftLimit"), hardwareMap.get(DigitalChannel.class, "extendLimit"),
                hardwareMap.get(CRServo.class, "leftWheelIntake"), hardwareMap.get(CRServo.class, "rightWheelIntake"),
                hardwareMap.get(Servo.class, "leftDeployIntake"), hardwareMap.get(Servo.class, "rightDeployIntake"),
                hardwareMap.get(Servo.class, "releaseLatch"), hardwareMap.get(DcMotor.class, "winchLatch"));

        base = new DriveBase(hardwareMap.get(DcMotor.class, "left1"), hardwareMap.get(DcMotor.class, "left1"),
                hardwareMap.get(DcMotor.class, "right1"), hardwareMap.get(DcMotor.class, "right2"),
                hardwareMap.get(BNO055IMU.class, "imu"));

        pixy = new PixyCam(hardwareMap.get(I2cDeviceSynch.class, "pixy"));

        waitForStart();

        arm.zero();
        while (arm.zeroThread.isAlive())
        {
            if (!opModeIsActive())
            {
                arm.cancelZero();
            }
        }

        arm.descend();

        base.setTargetHeading(pixy.firstBlockAngle());
        base.setSpeed(0.5);
        long startTime = System.currentTimeMillis();
        //TODO make function to simplify this
        while(System.currentTimeMillis() - startTime <= 1000)
        {
            base.update();
        }

        base.setSpeed(0.5);

        while(System.currentTimeMillis() - startTime <= 1000)
        {
            base.update();
        }

        base.setSpeed(0);
        base.setTargetHeading(-45);
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime <= 3000)
        {
            base.update();
        }
        base.setSpeed(0);

        //Deploy Marker
    }
}
