package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.TouchSensor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.util.ElapsedTime;


public class RobotClass {
  //initialization of robot motors
  private DcMotor FR;
  private DcMotor FL; 
  private DcMotor BR;
  private DcMotor BL; 
  private DcMotor Arm;
  private DcMotor Picky; 
  private DcMotor Carousel;
  private Servo big;
  private Servo small;
  public WebcamName webcamName;
  private HardwareMap hm;
  private LinearOpMode op;
  private double radius;
  private double inchToTick;
  private boolean isOpen = false;
  public ElapsedTime mRuntime = new ElapsedTime();
  
  
  
  public RobotClass(LinearOpMode newOp) {
  }
  
  //access robot hardware and setup preset motor behaviour
  public void setup(LinearOpMode newOp, HardwareMap newHm) {
  hm = newHm;
  
  FR = hm.dcMotor.get("FR");
  FL = hm.dcMotor.get("FL");
  BR = hm.dcMotor.get("BR");
  BL = hm.dcMotor.get("BL");
  Carousel = hm.dcMotor.get("Carousel");
  Arm = hm.dcMotor.get("Arm");
  Picky = hm.dcMotor.get("Picky");
  big = hm.servo.get("big");
  small = hm.servo.get("small");
  op = newOp;
  radius = 9.097358;
  small.setPosition(0.6);
  big.setPosition(0.07);
  
  
  //set direction of each drive motor
  FR.setDirection(DcMotorSimple.Direction.FORWARD);
  FL.setDirection(DcMotorSimple.Direction.REVERSE);
  FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  BR.setDirection(DcMotorSimple.Direction.FORWARD);
  BL.setDirection(DcMotorSimple.Direction.REVERSE);
  BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  Picky.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  Carousel.setDirection(DcMotorSimple.Direction.REVERSE);
  Carousel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  
  //make sure encoders values are default
  resetEncoders();
  }
  
  public void resetEncoders() {
    FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }
  
  public void stop(boolean ZPB) {
    if (ZPB) {
      FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
      BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    } else {
      //TODO: Fix braking behaviour in this mode
      FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
      FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
      BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
      BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    FR.setPower(0);
    FL.setPower(0);
    BR.setPower(0);
    BL.setPower(0);
  }
  
  //calculates target distance for specific motors/robot setup
  //TODO: set up fuction with class variables to make more efficient
  private double targetDistance(double inches) {
    double inchToTic = 95.1744;
    return inches * inchToTic;
  }
  
  //calculates distance required to complete a number
  //TODO: set up function with class variables to make it more efficient
  private double targetDegrees(double degrees) {
    return targetDistance((radius * 3.141592 * degrees)/180);
  }
  
  //moves robot a specific distance
  public void drive(double targetInches, double power, boolean brake) {
    double targetTics = targetDistance(targetInches);  
    FR.setPower(power);
    FL.setPower(power);
    BR.setPower(power);
    BL.setPower(power);
    while (op.opModeIsActive() && Math.abs(FR.getCurrentPosition()) < targetTics && Math.abs(FL.getCurrentPosition()) < targetTics ) {
       op.telemetry.addData("Target Tics", targetTics);
       op.telemetry.addData("FR", FR.getCurrentPosition());
       op.telemetry.update();
    }
    //TODO: why change a boolean into an integer?
    stop(brake);
    resetEncoders();
  }
  
  public void pivot(double degrees, double power) {
    double targetDeg = targetDegrees(degrees);  
    FR.setPower(-power);
    FL.setPower(power);
    BR.setPower(-power);
    BL.setPower(power);
     while (op.opModeIsActive() && Math.abs(FR.getCurrentPosition()) < targetDeg) {
       op.telemetry.addData("Target Degrees", targetDeg);
       op.telemetry.addData("FR", FR.getCurrentPosition());
       op.telemetry.update();
     } 
    stop(true);
    resetEncoders();
  }
  
  public void strafe(double targetInches, double power) {
    double targetTics = targetDistance(targetInches);  
    FR.setPower(-power);
    FL.setPower(power);
    BR.setPower(power);
    BL.setPower(-power);
    while (op.opModeIsActive() && Math.abs(FR.getCurrentPosition()) < targetTics && Math.abs(FL.getCurrentPosition()) < targetTics ) {
       op.telemetry.addData("Target Tics", targetTics);
       op.telemetry.addData("FR", FR.getCurrentPosition());
       op.telemetry.update();
    }
    stop(true);
    resetEncoders();
  }
  
  public void duck(long time, double power) {
    Carousel.setPower(power);
    op.sleep(time);
    Carousel.setPower(0);
  }
  
  public void arm2(double position) {
    big.setPosition(position);
  }
  
  public void grabber() {
    if(isOpen) {
      small.setPosition(0.6);
      isOpen = false;
    } else {
      small.setPosition(0.9);
      isOpen = true;
    }
  }
  public void pickup(long time, double power) {
      Picky.setPower(power);
      op.sleep(time);
      Picky.setPower(0);
  }
}
