package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;

@TeleOp(name="TeleOp1", group="")
public class TeleOp1 extends LinearOpMode {

  //defining robot hardware
  
  private DcMotor FR;
  private DcMotor FL;
  private DcMotor BR;
  private DcMotor BL;
  private DcMotor Carousel;
  private DcMotor Picky;
  private DcMotor Arm;
  private Servo Big;
  private Servo Small;
  
  //defining variables 
  private double L;
  private double R;
  private double sign_var;


  
@Override
public void runOpMode() {
    FR = hardwareMap.dcMotor.get("FR");
    FL = hardwareMap.dcMotor.get("FL");
    BR = hardwareMap.dcMotor.get("BR");
    BL = hardwareMap.dcMotor.get("BL");
    Carousel = hardwareMap.dcMotor.get("Carousel");
    Picky = hardwareMap.dcMotor.get("Picky");
    Arm = hardwareMap.dcMotor.get("Arm");
    Big = hardwareMap.servo.get("Big");
    Small = hardwareMap.servo.get("Small");
    Big.setPosition(0.07);
    Small.setPosition(0.05);
  
    
    

  // initialization
  
     setup();
     waitForStart();
     if (opModeIsActive()) {
       while (opModeIsActive()) {
         
      // tank drive equation
      
       L = Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y);
       R = Math.sqrt(gamepad1.right_stick_x * gamepad1.right_stick_x + gamepad1.right_stick_y * gamepad1.right_stick_y);
       driveSlowly();
          FR.setPower(Math.pow(R * sign(-gamepad1.right_stick_x - gamepad1.right_stick_y), 3));
          FL.setPower(Math.pow(L * sign(gamepad1.left_stick_x - gamepad1.left_stick_y), 3));
          BR.setPower(Math.pow(R * sign(-gamepad1.right_stick_x - gamepad1.right_stick_y), 3));
          BL.setPower(Math.pow(L * sign(gamepad1.left_stick_x - gamepad1.left_stick_y), 3));
          //pivotSlowly();
          driveSlowly();
          carousel();
          pickuptruck();
          arm_pickup();
          small_arm();
          big_arm();
          
          }
       }
      }
      
  

 //all functions used/needed in this code

  private void setup() {
    FR.setDirection(DcMotorSimple.Direction.FORWARD);
    FL.setDirection(DcMotorSimple.Direction.REVERSE);
    BR.setDirection(DcMotorSimple.Direction.FORWARD);
    BL.setDirection(DcMotorSimple.Direction.REVERSE);
    FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }
  
  private void Reset_Encoders() {
    FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }
  
  //functions for things on the robot (driving, motors, servos, etc.)
  
  private void drive(double drive_power) {
    FR.setPower(drive_power);
    FL.setPower(drive_power);
    BR.setPower(drive_power);
    BL.setPower(drive_power);
  }
  
  private void driveSlowly() {
    if (gamepad1.dpad_up) {
            FR.setPower(0.6);
            FL.setPower(0.6);
            BR.setPower(0.6);
            BL.setPower(0.6);
            
          }
          else if (gamepad1.dpad_left) {
            FR.setPower(0.6);
            FL.setPower(-0.6);
            BR.setPower(-0.6);
            BL.setPower(0.6);
            
          }
          else if (gamepad1.dpad_down) {
            FR.setPower(-0.6);
            FL.setPower(-0.6);
            BR.setPower(-0.6);
            BL.setPower(-0.6);
            
          }
          else if (gamepad1.dpad_right) {
            FR.setPower(-0.6);
            FL.setPower(0.6);
            BR.setPower(0.6);
            BL.setPower(-0.6);
          
          }
          else if (gamepad1.left_bumper) {
            FR.setPower(0.6);
            FL.setPower(-0.6);
            BR.setPower(0.6);
            BL.setPower(-0.6);
          
          }
          else if (gamepad1.right_bumper) {
            FR.setPower(-0.6);
            FL.setPower(0.6);
            BR.setPower(-0.6);
            BL.setPower(0.6);
          
          }
      }
  private void carousel() {
    if (gamepad2.a) {
      Carousel.setPower(1);
    } else if (gamepad2.b){
      Carousel.setPower(-1);
    } else {
      Carousel.setPower(0);
    }
  }
  
  private void pickuptruck() {
    if (gamepad2.x) {
      Picky.setPower(0.95);
    } else if (gamepad2.y) {
      Picky.setPower(-0.95);
    } else {
      Picky.setPower(0);
    }
  }
  private void arm_pickup() {
    if (gamepad2.dpad_up) {
      Arm.setPower(0.75);
    } else if (gamepad2.dpad_down) {
      Arm.setPower(-0.75);
    } else {
      Arm.setPower(0);
    }
  }
  
  private void big_arm() {
    if (gamepad2.right_bumper) {
      Big.setPosition(0.65);
    } else if (gamepad2.right_bumper) {
      Big.setPosition(0.07);
    } else {
      Big.setPosition(0.07);
    }
  }
  
  private void small_arm() {
    if (gamepad2.left_bumper){
      Small.setPosition(0.9);
    } else {
      Small.setPosition(0.4);
    }
  }  
  private double sign(double x) {
    sign_var = 1;
    if (x < 0) {
      sign_var = -1;
    }
    return sign_var;
  } 
} 



