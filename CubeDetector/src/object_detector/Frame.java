package object_detector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.example.blob_detect_test.Color;

public class Frame {
	private double focalLength;
	private double width;
	private double height;
	private double objectWidth;
	private Mat frame;
	private Mat hsvMat;
	private Mat hsvThreshed;
	private Mat filteredBlock;
	private Scalar hsvMinRange ;//= new Scalar(49,23,0);
	private Scalar hsvMaxRange ;//= new Scalar(97,111,109);
	private double[] hsvMinRangeArr = {0,0,0};
	private double[] hsvMaxRangeArr = {255,255,255};
	private Block detectedCube;
	private List<Mat> hsvPlanes = new ArrayList<Mat>();
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private Mat hierarchy = new Mat();
	
	private Rect boundingBox = null;
	private double aspectRatio;
	private double extent;
	
	public Frame(double focalLength, double objectWidth){
		this.focalLength = focalLength;
		this.objectWidth = objectWidth;
		this.hsvMat = new Mat();
		this.hsvThreshed = new Mat();
		this.filteredBlock = new Mat();
	}
	
	public void setColor(Color color){
		this.hsvMinRange = color.getHsvMin();
		this.hsvMaxRange = color.getHsvMax();
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
		return this.detectedCube;
	}
	
	public void processFrame(){
		//TODO: change to vector of Blocks later on
		
		//Convert frame to HSV image
		Imgproc.cvtColor(this.frame, this.hsvMat, Imgproc.COLOR_BGR2HSV);
		
		//Equalize histogram
		//List<Mat> hsvPlanes = new ArrayList<Mat>();
		
		//Core.split(this.hsvMat, hsvPlanes);
		//Imgproc.equalizeHist(hsvPlanes.get(2), hsvPlanes.get(2));
		//Core.merge(hsvPlanes, hsvMat);
		
		//Filter by HSV range
		Core.inRange(this.hsvMat, this.hsvMinRange , this.hsvMaxRange, this.hsvThreshed);
		
		//Apply morphological filters for noise removal
		morphOps(hsvThreshed);
		
		//Convert to BGR for bitwise and
		//Imgproc.cvtColor(this.frame, this.frame, Imgproc.COLOR_HSV2BGR);
		//Imgproc.cvtColor(this.hsvThreshed, hsvThreshed, Imgproc.COLOR_GRAY2BGR);
		//Core.bitwise_and(this.frame, this.hsvThreshed, this.filteredBlock);
	
		//Convert to grayscale to find contours;
		//Imgproc.cvtColor(this.filteredBlock, this.filteredBlock, Imgproc.COLOR_BGR2GRAY);
		
		//Find contours
		contours = new ArrayList<MatOfPoint>();
		//Mat hierarchy = new Mat();
		//TODO: this changes filteredBlock. Is it ok?
		//this.frame = this.hsvThreshed;
		this.filteredBlock = this.hsvThreshed.clone();
		//Convert threshed image to BGR for possible later color drawing on top of it
		Imgproc.cvtColor(this.hsvThreshed, hsvThreshed, Imgproc.COLOR_GRAY2BGR);
		Imgproc.findContours(this.filteredBlock, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
		
		/*
		//If no contours are found, return null
		if (contours.size() == 0){
			this.detectedCube = null;
		}
		*/
		
		//Find the largest contour
		double contourArea, maxArea = -1;
		MatOfPoint maxContour = null;
		for (MatOfPoint contour : contours){
			contourArea = Imgproc.contourArea(contour);
			boundingBox = Imgproc.boundingRect(contour);
			aspectRatio = (double) boundingBox.width/boundingBox.height;
			extent = contourArea/(boundingBox.width*boundingBox.height);
			//Log.i("", "Aspect ratio: " + aspectRatio);
			//Log.i("", "Extent: " + extent);
			if (contourArea > maxArea && contourArea > 100 && aspectRatio > 0.1 && aspectRatio < 5 && extent > 0.1 && extent < 5){
				maxArea = contourArea;
				//Log.i("contour area", String.valueOf(maxArea));
				maxContour = contour;
			}
		}
		
		if (maxContour != null){
			this.detectedCube = new Block(maxContour, this.objectWidth);	
			//return new Block(maxContour, this.objectWidth);	
		}
		else this.detectedCube = null;
		
	}
	
	
	public Mat getThreshed(){
		return this.hsvThreshed;
	}
	
	public Mat getRGB(){
		return this.frame;
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
		Imgproc.erode(target, target, erodeElement);
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
