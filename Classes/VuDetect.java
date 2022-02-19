package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.robot.Robot;
import java.io.File;
import java.io.FileOutputStream;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import java.util.Arrays;
import java.lang.Object;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;
//import com.vuforia.HINT;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

//import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
//import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


public class VuDetect {

    public android.graphics.Bitmap bitmap;
    public android.graphics.BitmapFactory bmFactory;
    public boolean imageGrabbed = false;
    public int stone1Colour = 0;
    public int stone2Colour = 0;
    public int stone3Colour = 0;
    public Image image;
    public android.view.View overlay;
    public long pixLen;
    public static VuforiaLocalizer vuforiaLocalizer;
    public LinearOpMode op;
    private HardwareMap hm;
    private WebcamName webcamName;
    
    public VuDetect() {
        
    }
    
    public void setup(LinearOpMode newOp, HardwareMap newHm) throws InterruptedException {
        hm = newHm;
        op = newOp;
        int skystonePos = 0;
        //vuforiaLocalizer = ClassFactory.createVuforia(initVuforia());
        //vuforiaLocalizer.setFrameQueueCapacity(5);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        //overlay = getLayoutInflator().inflate(hardwareMap.appContext.getResources().getIdentifier(layout.startup_screen, null));
        //addContentView(overlay, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        //image = new Image();
        //tom.initAutoOp(this, hardwareMap);
 
    }

    public void setup(LinearOpMode newOp, HardwareMap newHm, WebcamName camName) throws InterruptedException {
        hm = newHm;
        op = newOp;
        webcamName = camName;
        int skystonePos = 0;
        
        //vuforiaLocalizer.setFrameQueueCapacity(5);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
 
    }

    public Image waitForImage(long timeOut)throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        //Image image = getImageFromFrame(vuforiaLocalizer.getFrameQueue().take(), PIXEL_FORMAT.RGB565);
        imageGrabbed = false;
        while (!imageGrabbed && System.currentTimeMillis() - beginTime < timeOut && op.opModeIsActive()) {
            op.idle();
            image = getImageFromFrame(vuforiaLocalizer.getFrameQueue().take(), PIXEL_FORMAT.RGB565);
            imageGrabbed = (image != null);
        }
        return image;
    }
    
    public int detectStartStack(long timeOut)throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        imageGrabbed = false;
        //vuforiaLocalizer.setFrameQueueCapacity(5);
        while (!imageGrabbed && System.currentTimeMillis() - beginTime < timeOut && op.opModeIsActive()) {
            op.idle();
            image = getImageFromFrame(vuforiaLocalizer.getFrameQueue().take(), PIXEL_FORMAT.RGB565);
            imageGrabbed = (image != null && image.getPixels() != null);
        }
        if(imageGrabbed){
            return 1;
        }
        return -1;
    }
    
    
    public int processStone(int x, int y, int testW, Bitmap stones) {
        int stoneColour = 0;
        stone1Colour = -9999;
            
        for(;x<x+testW; x++) {
            for(;y<stones.getHeight()&& y<y+testW; y++) {
                int colour = stones.getPixel(x,y);
                if (Color.red(colour)+Color.green(colour)>2*Color.blue(colour)) {
                    stones.setPixel(x,y,Color.YELLOW);
                    stoneColour -= 1;
                } else {
                    stones.setPixel(x,y,Color.BLUE);
                    stoneColour += 1;
                }
            }
        }
        return stoneColour;
    }
    
    public int getSkystonePosition() {
        int skystonePos = 1;
        if (image != null && image.getPixels() != null) {
            
            // put the camera image into a bitmap and save it
            Bitmap bm = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.RGB_565);
            java.nio.ByteBuffer buffer =  image.getPixels(); 
            bm.copyPixelsFromBuffer(buffer);
            int w = 115;
            int h = 720;
            int testW = 80;
            int stoneColour = 0;
            int blueCol = 0;
            int yellowCol = 0;
            int blueTot = 0;
            int yellowTot = 0;
            
            Bitmap stones = Bitmap.createBitmap(bm, 525, 0, w, h);
            
            for(int x = 20; x<90; x++) {
                for(int y = 110;y<190; y++) {
                    int colour = stones.getPixel(x,y);
                    blueCol += 2 * Color.blue(colour);
                    yellowCol += Color.red(colour)+Color.green(colour);
                    if (Color.red(colour)+Color.green(colour)>2*Color.blue(colour)) {
                        stones.setPixel(x,y,Color.YELLOW);
                        stoneColour -= 1;
                        
                    } else {
                        stones.setPixel(x,y,Color.BLUE);
                        stoneColour += 1;
                    }
                }
            }
            stone1Colour = blueCol - yellowCol;
            
            blueTot += blueCol;
            yellowTot += yellowCol;
            blueCol = 0;
            yellowCol = 0;
            stoneColour = 0;
            for(int x = 20; x<90; x++) {
                for(int y = 350;y<430; y++) {
                    int colour = stones.getPixel(x,y);
                    blueCol += 2 * Color.blue(colour);
                    yellowCol += Color.red(colour)+Color.green(colour);
                    if (Color.red(colour)+Color.green(colour)>2*Color.blue(colour)) {
                        stones.setPixel(x,y,Color.YELLOW);
                        stoneColour -= 1;
                    } else {
                        stones.setPixel(x,y,Color.BLUE);
                        stoneColour += 1;
                        
                    }
                }
            }
            stone2Colour = blueCol - yellowCol;
            
            blueTot += blueCol;
            yellowTot += yellowCol;
            blueCol = 0;
            yellowCol = 0;
            stoneColour = 0;
            for(int x = 20; x<90; x++) {
                for(int y = 590;y<670; y++) {
                    int colour = stones.getPixel(x,y);
                    blueCol += 2 * Color.blue(colour);
                    yellowCol += Color.red(colour)+Color.green(colour);
                    if (Color.red(colour)+Color.green(colour)>2*Color.blue(colour)) {
                        stones.setPixel(x,y,Color.YELLOW);
                        stoneColour -= 1;
                    } else {
                        stones.setPixel(x,y,Color.BLUE);
                        stoneColour += 1;
                    }
                    
                }
            }
            stone3Colour = blueCol - yellowCol;
            skystonePos = stone1Colour > stone2Colour && stone1Colour > stone3Colour ? 1 : stone2Colour > stone1Colour && stone2Colour > stone3Colour ? 2 : stone3Colour > stone2Colour && stone3Colour > stone1Colour ? 3 : 1;
            
            blueTot += blueCol;
            yellowTot += yellowCol;
            
            
            //stone4Colour = processStone(0, testW*4, testW, stones);
            //stone5Colour = processStone(0, testW*8, testW, stones);
            
            //put the bitmap into a canvas and draw on it
            Paint lineType = new Paint();
            lineType.setStrokeWidth((float)0.0);
            lineType.setColor(0xff0000ff);
            Canvas canvas =new Canvas(bm);
            canvas.setBitmap(bm);
            
            Rect r1 = new Rect(525,0,640,720);
            //canvas.drawRGB(0,255,0);
            canvas.drawRect(r1, lineType);
            canvas.drawText("Hello",1,4,(float)0.5,(float)0.5, lineType);
            canvas.save();
            canvas.drawRect(r1, lineType);
            
            int y1 = skystonePos == 1 ? 80: skystonePos == 2 ? 320: 560;
            int y2 = skystonePos == 1 ? 160: skystonePos == 2 ? 400: 640;
            Rect r2 = new Rect(20,90,y1,y2);
            canvas.drawRGB(0,255,0);
            canvas.drawRect(r2, lineType);
            canvas.save();
            canvas.drawRect(r2, lineType);
            
            saveImage(stones);

            
        } else {
            op.telemetry.addData("Image Not Found",1);
            op.telemetry.update();
        }
        op.telemetry.addData("Skystone Pos", skystonePos);
        op.telemetry.addData("Stone3 ",stone1Colour);
        op.telemetry.addData("Stone4 ",stone2Colour);
        op.telemetry.addData("Stone5 ",stone3Colour);
        op.telemetry.update();
        return skystonePos;
    }
    
    

    private static void saveImage(android.graphics.Bitmap finalBitmap) {
    
        String root = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
    
        String fname = "LatestSkyStone.png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    
    public Image getImageFromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {

        long numImgs = frame.getNumImages();
        for (int i = 0; i < numImgs; i++) {
            if (frame.getImage(i).getFormat() == format) {
                image = frame.getImage(i);
                imageGrabbed = true;
                //frame.close();
            }
        }
        return null;
    }
    
    private VuforiaLocalizer.Parameters initVuforia () {
        int cameraMonitorViewId = hm.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hm.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //parameters.cameraMonitorViewIdParent = cameraMonitorViewId;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraName = webcamName;
        parameters.vuforiaLicenseKey = "AfZdcpz/////AAAAGeFAEIQ7eEL9ilMArx0PrTpfGi14uY5DxJNi9A/pNhrpWpMLBsZIt21zn61HlpOEsX4SW/GyN//S+CJpHALNQkDftlrlJ3+cGtzrVC0ZZcEpltXAdp/5CkO+M7Q3rDOtKBeFhCBnjDUVswvmD0sU9mRgjVhn5TvOSXcSuJIJymIy5x15BUxbqsZe+5Rkzt4a/4ltQvr3jN13s4RECp03x+zfPWKR7S79x1+VSITaBB5lrv43p9ZEBJeIaWlAXQTST8O0uf2YhNXCuzrBxuAgL5onSpOWmBUzyFxE8cooXOgyktMm/mtYHG+vujg4gG9FpxFFLutypcN3hLaBOqfS1DyNrQD1i05cpRLwJ4M0Gszc";
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        return parameters;
    }


}
