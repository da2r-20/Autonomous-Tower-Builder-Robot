package object_detector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.util.Log;


public class ImgController {
	private static Frame frame;// = new Frame(20,20/*TODO: pass focal length and object width*/);
	private String directions = "";
	
	
	public ImgController(){
		frame = new Frame(720,3);
	};
	public void detectObjects(Mat src){
		//Log.i("test", null);
		frame.updateFrame(src);
		Block block = frame.getObjects();
		if (block != null){
			double blockHorizontalCenter = block.getCenter().x;
			Core.circle(src, block.getCenter(), 20, new Scalar(0, 255,0));
			double centerDiff = blockHorizontalCenter - frame.getWidth();
			Log.i("", String.valueOf(centerDiff));
		}
	}
	
	public Mat getProcessedFrame(Mat src){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		frame.updateFrame(src);
		Block block = frame.getObjects();
		if (block != null){
			contours.add(block.getCountour());
		}
		Mat result = frame.getThreshed();
		if (block != null){
			double blockHorizontalCenter = block.getCenter().x;
			if(block!= null){
				Imgproc.drawContours(result, contours, 0, new Scalar(0, 255,0),5);
			}
			//Core.circle(result, block.getCenter(), 20, new Scalar(0, 255,0));
			double centerDiff = blockHorizontalCenter - frame.getWidth()/2;
			double distance = frame.getBlockDistance(block);
			Log.i("", "Block center x coordinate: " + String.valueOf(blockHorizontalCenter));
			Log.i("", "Frame half width: " + String.valueOf(frame.getWidth()/2));
			Log.i("","Distance from center: " + String.valueOf(centerDiff));
			Log.i("", "Distamce from camera: " + String.valueOf(frame.getBlockDistance(block)));
			if (centerDiff < 30 && centerDiff > -30){
				if (distance > 10){
					directions = "Go!";
				} else {
					directions = "Stop!";
				}
			} else if (centerDiff > 30){
				directions = "Turn right";
			} else {
				directions = "Turn left";
			}
			
		
		}
		return result;
	}
	
	public static void setThresh(int positionInList, int progress){
		frame.setThresh(positionInList, progress);
	}
	
	public Mat getThreshed(){
		return frame.getThreshed();
		
	}
	public String getDirections() {
		return this.directions;
	}

}
