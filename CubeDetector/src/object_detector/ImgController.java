package object_detector;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.example.blob_detect_test.Color;
import com.example.blob_detect_test.CubeInfo;



public class ImgController {
	private static Frame frame;
	private Color color;
	private Mat result;
	//List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	
	public ImgController(){
		frame = new Frame(720,3);
		this.color = null;
	};
	
	public void detectObjects(Mat src){
		frame.updateFrame(src);
		Block block = frame.getObjects();
		if (block != null){
			double blockHorizontalCenter = block.getCenter().x;
			Core.circle(src, block.getCenter(), 20, new Scalar(0, 255,0));
			double centerDiff = blockHorizontalCenter - frame.getWidth();
			//Log.i("", String.valueOf(centerDiff));
		}
	}
	
	private Mat getMat(int matType){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Block block = frame.getObjects();
		contours.clear();
		if (block != null){
			contours.add(block.getCountour());
		}
		if (matType == 1){
			result = frame.getThreshed();
		} else if (matType == 2){
			result = frame.getRGB();
		}
		if (block != null){
			if(block!= null){
				Imgproc.drawContours(result, contours, 0, new Scalar(0, 255,0),5);
			}
		}
		return result;
	}
	
	public void processFrame(Mat src){
		if (this.color != CubeInfo.getInstance().getColor()){
			frame.setColor(CubeInfo.getInstance().getColor());
		}
		frame.updateFrame(src);
		frame.processFrame();
		Block block = frame.getObjects();
		if (block != null){
			CubeInfo.getInstance().setFound(true);
			double blockHorizontalCenter = block.getCenter().x;
			double centerDiff = blockHorizontalCenter - frame.getWidth()/2;
			double distance = frame.getBlockDistance(block);
			//Rect boundingBox = Imgproc.boundingRect(block.getCountour());
			//double contArea  = Imgproc.contourArea(block.getCountour());
			//double rectArea = boundingBox.width*boundingBox.height;
			//double extent = contArea/rectArea;
			//Log.i("", "Extend: " + extent);
			//MatOfInt hull = new MatOfInt();
			/*
			Imgproc.convexHull(block.getCountour(),  hull);
			MatOfPoint2f hullCont = null;
			Imgproc.approxPolyDP(hull, hullCont, 0.001, true);
			//double hullArea = Imgproc.contourArea();
			double hullArea = Imgproc.contourArea(hull);
			*/
			//double solidity = (double) area/hullArea;
			//Log.i("", "Solidity " + solidity);
			//update cube info
			CubeInfo.getInstance().setHorizontalLocation(centerDiff);
			CubeInfo.getInstance().setDistance(distance);
		} else {
			CubeInfo.getInstance().setFound(false);
		}
		
	}
	
	
	public Mat getProcessedFrame(Mat src){
		/*
		if (this.colorIndex != CubeInfo.getInstance().getColorIndex()){
			this.colorIndex = CubeInfo.getInstance().getColorIndex();
			frame.setColor(hsvRanges[this.colorIndex][0], hsvRanges[this.colorIndex][1]);
		} */
		if (this.color != CubeInfo.getInstance().getColor()){
			frame.setColor(CubeInfo.getInstance().getColor());
		}
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		frame.updateFrame(src);
		frame.processFrame();
		Block block = frame.getObjects();
		if (block != null){
			contours.add(block.getCountour());
		}
		result = frame.getThreshed();
		if (block != null){
			CubeInfo.getInstance().setFound(true);
			double blockHorizontalCenter = block.getCenter().x;
			if(block!= null){
				Imgproc.drawContours(result, contours, 0, new Scalar(0, 255,0),5);
			}
			//Core.circle(result, block.getCenter(), 20, new Scalar(0, 255,0));
			double centerDiff = blockHorizontalCenter - frame.getWidth()/2;
			double distance = frame.getBlockDistance(block);
			
			//update cube info
			CubeInfo.getInstance().setHorizontalLocation(centerDiff);
			CubeInfo.getInstance().setDistance(distance);
			
		
		} else {
			CubeInfo.getInstance().setFound(false);
			//CubeInfo.getInstance().setDistance(10000);
			//CubeInfo.getInstance().setHorizontalLocation(100000);
		}
		return result;
	}
	
	public Mat getThreshed(){
		return getMat(1);
	}
	
	public Mat getRGB(){
		return getMat(2);
		
	}
	
	public static void setThresh(int positionInList, int progress){
		frame.setThresh(positionInList, progress);
	}
	
	
}
