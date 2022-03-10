package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

@Autonomous(name = "Camtest", group = "")
public class Camtest extends LinearOpMode {

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
    waitForStart();
    mRuntime.reset();
    if (opModeIsActive()) {
      int barcode;
      barcode = cam.startStackCount();
      cam.closeCamera();
    // Initialize Vuforia
    telemetry.addData("Status", "Initializing Vuforia. Please wait...");
    telemetry.update();
    initVuforia();
    cameraName = vuforiaUltimateGoal.getSwitchableCamera(hardwareMap);
    // Activate here for camera preview.
    vuforiaUltimateGoal.activate();
    telemetry.addData(">>", "Vuforia initialized, press start to continue...");
    telemetry.update();
      vuforiaUltimateGoal.setActiveCamera(cameraName);
      telemetry.addData("Ring Count", "%d", barcode);
      telemetry.update();
    }
 cam.saveBitmap();
    // Don't forget to deactivate Vuforia.
    vuforiaUltimateGoal.deactivate();

    vuforiaUltimateGoal.close();
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
