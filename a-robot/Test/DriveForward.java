package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "DriveForward", group = "")
public class DriveForward extends LinearOpMode {
private RobotClass r = new RobotClass(this);
  
    @Override
  public void runOpMode() {
    r.setup(this, hardwareMap);
    waitForStart();
    if (opModeIsActive()) {
      r.mainArm(1, -0.6);
      sleep(1000);
    }
  }
}
