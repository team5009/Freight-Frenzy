package org.firstinspires.ftc.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



public class Test123 extends OpMode
{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LEFT = null;
    private DcMotor RIGHT = null;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        LEFT  = hardwareMap.get(DcMotor.class, "LEFT");
        RIGHT = hardwareMap.get(DcMotor.class, "RIGHT");

        LEFT.setDirection(DcMotor.Direction.FORWARD);
        RIGHT.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        
        double leftPower;
        double rightPower;

        leftPower  = -gamepad1.left_stick_y ;
        rightPower = -gamepad1.right_stick_y ;

        LEFT.setPower(leftPower);
        RIGHT.setPower(rightPower);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    @Override
    public void stop() {
    }

}
