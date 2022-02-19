package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.annotation.Target;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.Classes.Cam;

import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Auto1", group = "")
public class Auto1 extends LinearOpMode {

  private VuforiaCurrentGame vuforiaUltimateGoal;
  private Cam cam = new Cam(this);
  private RobotClass r = new RobotClass(this);
  public boolean camReady = false;
  public ElapsedTime mRuntime = new ElapsedTime();
  //private WebcamName cameraName;
  private CameraName cameraName;
  VuforiaBase.TrackingResults vuforiaResults;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    vuforiaUltimateGoal = new VuforiaCurrentGame();
    camReady = cam.setUp();
    r.setup(this, hardwareMap);
    int barcode;
    barcode = cam.startStackCount();
    camReady = false;
    camReady = cam.setUp();
    telemetry.update();
    telemetry.addData("Barcode", "%d", barcode);
    telemetry.update();
    waitForStart();
    mRuntime.reset();
    if (opModeIsActive()) {
    // Initialize Vuforia
    //telemetry.addData("Status", "Initializing Vuforia. Please wait...");
    //telemetry.update();
    //initVuforia();
    //cameraName = vuforiaUltimateGoal.getSwitchableCamera(hardwareMap);
    barcode = cam.startStackCount();
    cam.closeCamera();
    // Activate here for camera preview.
    //vuforiaUltimateGoal.activate();
    //telemetry.addData(">>", "Vuforia initialized, press start to continue...");
    //telemetry.update();
      //vuforiaUltimateGoal.setActiveCamera(cameraName);
      telemetry.addData("Barcode", "%d", barcode);
      telemetry.update();
      if (opModeIsActive()) {
        r.drive(1.75, -0.5, true);
        sleep(500);
        r.strafe(17.5, 0.8);
        r.pivot(45, -0.7);
        sleep(500);
        r.duck(2300, 0.95);
        r.pivot(47, 0.7);
        sleep(500);
        r.strafe(48, -0.8);
        r.resetEncoders();
        sleep(500);
        r.drive(15, 0.4, true);
        sleep(250);
        if (barcode == 1) {
        r.arm2(0.2);
        r.drive(20, -0.7, true);
        sleep(1000);
        } else if (barcode == 2) { 
        r.arm2(0.4);
        r.drive(19, -0.7, true);
        sleep(1000);
        } else { //Nothing detected
        r.arm2(0.6);
        r.drive(18, -0.7, true);
        sleep(1000);
        }
        r.grabber();
        sleep(500);
        telemetry.addData("Ring Count", "%d", barcode);
        telemetry.update();
        r.drive(12, 0.6, true);
        r.grabber();
        sleep(250);
        r.arm2(0.1);
        r.pivot(90, -0.8);
        r.drive(70, 0.98, true);
      }
    }
    
    cam.saveBitmap();
    // Don't forget to deactivate Vuforia.
    //vuforiaUltimateGoal.deactivate();

    //vuforiaUltimateGoal.close();
  }

  /**
   * Describe this function...
   */
  private void initVuforia() {
    // Initialize using external web camera.
    vuforiaUltimateGoal.initialize(
        "", // vuforiaLicenseKey
        hardwareMap.get(WebcamName.class, "Webcam"), // cameraName
        "", // webcamCalibrationFilename
        false, // useExtendedTracking
        true, // enableCameraMonitoring
        VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES, // cameraMonitorFeedback
        0, // dx
        0, // dy
        0, // dz
        0, // xAngle
        0, // yAngle
        0, // zAngle
        true); // useCompetitionFieldTargetLocations
  }

  /**
   * Check to see if the target is visible.
   */
  private boolean isTargetVisible(String trackableName) {
    boolean isVisible;

    // Get vuforia results for target.
    vuforiaResults = vuforiaUltimateGoal.track(trackableName);
    // Is this target visible?
    if (vuforiaResults.isVisible) {
      isVisible = true;
    } else {
      isVisible = false;
    }
    return isVisible;
  }

  /**
   * This function displays location on the field and rotation about the Z
   * axis on the field. It uses results from the isTargetVisible function.
   */
  private void processTarget() {
    // Display the target name.
    telemetry.addData("Target Detected", vuforiaResults.name + " is visible.");
    telemetry.addData("X (in)", Double.parseDouble(JavaUtil.formatNumber(displayValue(vuforiaResults.x, "IN"), 2)));
    telemetry.addData("Y (in)", Double.parseDouble(JavaUtil.formatNumber(displayValue(vuforiaResults.y, "IN"), 2)));
    telemetry.addData("Z (in)", Double.parseDouble(JavaUtil.formatNumber(displayValue(vuforiaResults.z, "IN"), 2)));
    telemetry.addData("Rotation about Z (deg)", Double.parseDouble(JavaUtil.formatNumber(vuforiaResults.zAngle, 2)));
  }

  /**
   * By default, distances are returned in millimeters by Vuforia.
   * Convert to other distance units (CM, M, IN, and FT).
   */
  private double displayValue(float originalValue, String units) {
    double convertedValue;

    // Vuforia returns distances in mm.
    if (units.equals("CM")) {
      convertedValue = originalValue / 10;
    } else if (units.equals("M")) {
      convertedValue = originalValue / 1000;
    } else if (units.equals("IN")) {
      convertedValue = originalValue / 25.4;
    } else if (units.equals("FT")) {
      convertedValue = (originalValue / 25.4) / 12;
    } else {
      convertedValue = originalValue;
    }
    return convertedValue;
  }
}
