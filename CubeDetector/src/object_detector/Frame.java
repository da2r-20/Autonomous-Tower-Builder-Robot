package object_detector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class Frame {
	private double focalLength;
	private double width;
	private double height;
	private double objectWidth;
	private Block block;
	private Mat frame;
	private Mat hsvMat;
	private Mat hsvThreshed;
	private Mat filteredBlock;
	private Scalar hsvMinRange = new Scalar(49,23,0);
	private Scalar hsvMaxRange = new Scalar(97,111,109);
	private double[] hsvMinRangeArr = {0,0,0};
	private double[] hsvMaxRangeArr = {255,255,255};
	
	public Frame(double focalLength, double objectWidth){
		this.focalLength = focalLength;
		this.objectWidth = objectWidth;
		this.hsvMat = new Mat();
		this.hsvThreshed = new Mat();
		this.filteredBlock = new Mat();
	}
	
	public void setColor(Scalar hsvMin, Scalar hsvMax){
		this.hsvMinRange = hsvMin;
		this.hsvMaxRange = hsvMax;
	}
	
	private void arrToScalars(double[] minArr, double[] maxArr){
		this.hsvMinRange.set(minArr);
		this.hsvMaxRange.set(maxArr);		
	}
	
	public void updateFrame(Mat frame){
		this.frame = frame;
		this.width = frame.size().width;
		this.height = frame.size().height;
	}
	
	public void setThresh(int positionInList, int progress){
		if (positionInList > 2){
			this.hsvMaxRangeArr[positionInList-3] = progress;
		} else {
			this.hsvMinRangeArr[positionInList] = progress;
		}
		this.arrToScalars(this.hsvMinRangeArr, this.hsvMaxRangeArr);
	}
	
	public Block getObjects(){
		//TODO: change to vector of Blocks later on
		//this.hsvMat = new Mat();
		//this.hsvThreshed = new Mat();
		//this.filteredBlock = new Mat();
		//Convert frame to HSV image
		Imgproc.cvtColor(this.frame, this.hsvMat, Imgproc.COLOR_BGR2HSV);
		
		//Equalize histogram
		List<Mat> hsvPlanes = new ArrayList<Mat>();
		Core.split(this.hsvMat, hsvPlanes);
		Imgproc.equalizeHist(hsvPlanes.get(2), hsvPlanes.get(2));
		Core.merge(hsvPlanes, hsvMat);
		
		//Filter by HSV range
		Core.inRange(this.hsvMat, this.hsvMinRange , this.hsvMaxRange, this.hsvThreshed);
		
		//Apply morphological filters for noise removal
		morphOps(hsvThreshed);
		
		//Convert to BGR for bitwise and
		//Imgproc.cvtColor(this.frame, this.frame, Imgproc.COLOR_HSV2BGR);
		Imgproc.cvtColor(this.hsvThreshed, hsvThreshed, Imgproc.COLOR_GRAY2BGR);
		Core.bitwise_and(this.frame, this.hsvThreshed, this.filteredBlock);
	
		//Convert to grayscale to find contours;
		//TODO: this isn't necessarily needed. We can find contours on hsvThreshed instead.
		Imgproc.cvtColor(this.filteredBlock, this.filteredBlock, Imgproc.COLOR_BGR2GRAY);
		
		//Find contours
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		//TODO: this changes filteredBlock. Is it ok?
		this.frame = this.hsvThreshed;
		Imgproc.findContours(filteredBlock, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
		
		//If no contours are found, return null
		if (contours.size() == 0){
			return null;
		}
		
		//Find the largest contour
		double contourArea, maxArea = -1;
		MatOfPoint maxContour = null;
		for (MatOfPoint contour : contours){
			contourArea = Imgproc.contourArea(contour);
			if (contourArea > maxArea && contourArea > 500){
				maxArea = contourArea;
				Log.i("contour area", String.valueOf(maxArea));
				maxContour = contour;
			}
		}
		
		if (maxContour != null){
			return new Block(maxContour, this.objectWidth);	
		}
		else return null;
		
	}
	
	
	public Mat getThreshed(){
		return this.hsvThreshed;
	}
	
	public double getWidth(){
		return this.frame.width();
	}
	
	public double getBlockDistance(Block block){
		double blockPixelWidth = block.getPixelWidth();
		double blockRealWidth = block.getRealWidth();
		double distance = (blockRealWidth*this.focalLength)/blockPixelWidth;
		return distance;
	}
	
	private static void morphOps(Mat target){
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
		
		Imgproc.erode(target, target, erodeElement);
		//Imgproc.erode(target, target, erodeElement);
		//Imgproc.erode(target, target, erodeElement);
		//Imgproc.erode(target, target, erodeElement);
		//Imgproc.erode(target, target, erodeElement);
		//Imgproc.erode(target, target, erodeElement);
		
		Imgproc.dilate(target, target, dilateElement);
		//Imgproc.dilate(target, target, dilateElement);
		//Imgproc.dilate(target, target, dilateElement);
		//Imgproc.dilate(target, target, dilateElement);
		
		//TODO: optional: Gaussian blur/ something else
	}
	
	
}
