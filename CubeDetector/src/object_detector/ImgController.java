package object_detector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.example.blob_detect_test.CubeInfo;

import android.util.Log;


public class ImgController {
	private static Frame frame;// = new Frame(20,20/*TODO: pass focal length and object width*/);
	private String directions = "";
	private static Scalar hsvMinRangeGreen = new Scalar(49,23,0);
	private static Scalar hsvMaxRangeGreen = new Scalar(97,111,109);
	//private ArrayList<Scalar[]> hsvRanges;
	private Scalar[][] hsvRanges = {{new Scalar(49,47,0), new Scalar(100,217,109)},
			{new Scalar(103,80,24), new Scalar(134,164,148)}};
	
	
	private Scalar hsvMinRange;
	private Scalar hsvMaxRange;
	private int colorIndex;
	
	public ImgController(){
		//hsvRanges.add(new Scalar[] {new Scalar(49,23,0), new Scalar(97,111,109)});
		frame = new Frame(720,3);
		this.colorIndex = CubeInfo.getInstance().getColorIndex();
		frame.setColor(hsvRanges[0][0], hsvRanges[0][1]);
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
		if (this.colorIndex != CubeInfo.getInstance().getColorIndex()){
			this.colorIndex = CubeInfo.getInstance().getColorIndex();
			frame.setColor(hsvRanges[this.colorIndex][0], hsvRanges[this.colorIndex][1]);
		}
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
			
			//update cube info
			CubeInfo.getInstance().setHorizontalLocation(centerDiff);
			CubeInfo.getInstance().setDistance(distance);
			
			if (centerDiff < 30 && centerDiff > -30){
				if (distance > 10){
					directions = "Go!";
				} else {
					directions = "Stop!";
					/*
					if (CubeInfo.getInstance().getColorIndex()==0){
						CubeInfo.getInstance().setColorIndex(1);
					} else {
						CubeInfo.getInstance().setColorIndex(0);
					}*/
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
