package magical.robot.vision;

import java.util.ArrayList;
import java.util.List;

import magical.robot.global.Color;
import magical.robot.global.ObjectInfoContainer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

/**
 * Encapsulates a single frame and responsible for processing that frame 
 * in order to detect the required object
 * @author Pavel Rubinson
 *
 */
public class Frame {
	//The camera focal length
	private double focalLength;
	
	//Frame dimensions
	private double width;
	private double height;
	
	//Object width (in the real world)
	private double objectWidth;
	
	//Images to save the different stages in the processing
	private Mat frame;
	private Mat hsvMat;
	private Mat hsvThreshed;
	private Mat filteredBlock;
	private List<Mat> hsvPlanes = new ArrayList<Mat>();
	private Mat hierarchy = new Mat();
	
	//Color ranges for thresholding
	private Scalar hsvMinRange ;
	private Scalar hsvMaxRange ;
	private double[] hsvMinRangeArr = {0,0,0};
	private double[] hsvMaxRangeArr = {255,255,255};
	
	//The detected block object
	private Block detectedCube;
	
	//Container for saving contours
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	
	//Will be used to hold the bounding box of each contour
	private Rect boundingBox = null;
	
	//Will hold the aspect ratio and extent of the detected contours
	private double aspectRatio;
	private double extent;
	
	//Expected aspect ratio and extent values for contour filtering
	private double extentMin;
	private double extentMax;
	private double aspectMin;
	private double aspectMax;
	
	
	/**
	 * Initializes a new Frame instance.
	 * @param focalLength The focal length of the camera
	 * @param objectWidth The real-world width of the object we are looking for
	 * @param aspectMin Minimum expected aspect ratio value
	 * @param aspectMax Maximum expected aspect ratio value
	 * @param extentMin Minimum expected extent value
	 * @param extentMax Maximum expected extent value
	 */
	public Frame(double focalLength, double objectWidth, double aspectMin, double aspectMax, double extentMin, double extentMax){
		this.focalLength = focalLength;
		this.objectWidth = objectWidth;
		this.hsvMat = new Mat();
		this.hsvThreshed = new Mat();
		this.filteredBlock = new Mat();
		this.aspectMin = aspectMin;
		this.aspectMax = aspectMax;
		this.extentMin = extentMin;
		this.extentMax = extentMax;
	}
	
	/**
	 * Sets the color to look for
	 * @param color A Color object
	 */
	public void setColor(Color color){
		this.hsvMinRange = color.getHsvMin();
		this.hsvMaxRange = color.getHsvMax();
	}
	
	/**
	 * Set extent min and max values
	 * @param extentMin
	 * @param extentMax
	 */
	public void setExtent(double extentMin, double extentMax){
		this.extentMin = extentMin;
		this.extentMax = extentMax;
	}
	
	/**
	 * Set aspect ratio min and max values
	 * @param aspectMin
	 * @param aspectMax
	 */
	public void setAspectRatio(double aspectMin, double aspectMax){
		this.aspectMin = aspectMin;
		this.aspectMax = aspectMax;
	}
	
	
	/**
	 * Saves the actual image into the encapsulating Frame object
	 * @param frame The image we wish to process
	 */
	public void updateFrame(Mat frame){
		this.frame = frame;
		this.width = frame.size().width;
		this.height = frame.size().height;
	}
	
	/**
	 * Used to update the color threshold values according to the trackbar values
	 * @param positionInList
	 * @param progress
	 */
	public void setThresh(int positionInList, int progress){
		if (positionInList > 2){
			this.hsvMaxRangeArr[positionInList-3] = progress;
		} else {
			this.hsvMinRangeArr[positionInList] = progress;
		}
		this.arrToScalars(this.hsvMinRangeArr, this.hsvMaxRangeArr);
	}
	
	/**
	 * Private method to help update the threshold values using the trackbar values
	 * @param minArr
	 * @param maxArr
	 */
	private void arrToScalars(double[] minArr, double[] maxArr){
		this.hsvMinRange.set(minArr);
		this.hsvMaxRange.set(maxArr);		
	}
	
	/**
	 * Get the detected object.
	 * @return The detected object, or null if no object was detected
	 */
	public Block getObjects(){
		return this.detectedCube;
	}
	
	/**
	 * The main frame processing, filtering, contour detection and eventually object detection
	 */
	public void processFrame(){		
		
		Log.i("","blah " + this.aspectMin);
		//Convert frame to HSV image
		Imgproc.cvtColor(this.frame, this.hsvMat, Imgproc.COLOR_BGR2HSV);
		
		//Equalize histogram - NOTE: OPTIONAL. MIGHT DAMAGE COLORS!
		List<Mat> hsvPlanes = new ArrayList<Mat>();
		Core.split(this.hsvMat, hsvPlanes);
		Imgproc.equalizeHist(hsvPlanes.get(2), hsvPlanes.get(2));
		Core.merge(hsvPlanes, hsvMat);
		
		//Filter by HSV range
		Core.inRange(this.hsvMat, this.hsvMinRange , this.hsvMaxRange, this.hsvThreshed);
		
		//Apply morphological filters for noise removal
		morphOps(hsvThreshed);
		
		//Find contours
		contours = new ArrayList<MatOfPoint>();
		
		this.filteredBlock = this.hsvThreshed.clone();
		
		//Convert threshed image to BGR for possible later color drawing on top of it
		Imgproc.cvtColor(this.hsvThreshed, hsvThreshed, Imgproc.COLOR_GRAY2BGR);
		Imgproc.findContours(this.filteredBlock, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
		

		//Filter contours by size and properties
		double contourArea, maxArea = -1;
		MatOfPoint maxContour = null;
		
		for (MatOfPoint contour : contours){
			contourArea = Imgproc.contourArea(contour);
			boundingBox = Imgproc.boundingRect(contour);
			aspectRatio = (double) boundingBox.width/boundingBox.height;
			extent = contourArea/(boundingBox.width*boundingBox.height);
			if (contourArea > maxArea && contourArea > 200 &&
					aspectRatio > this.aspectMin &&
					aspectRatio < this.aspectMax &&
					extent > this.extentMin && extent < this.extentMax){
				maxArea = contourArea;
				maxContour = contour;
			}
		}
		 
		//Create Block object to represent the detected object
		if (maxContour != null){
			this.detectedCube = new Block(maxContour, this.objectWidth);	
		}
		else this.detectedCube = null;
		
	}
	
	/**
	 * Get thresholded image
	 * @return
	 */
	public Mat getThreshed(){
		return this.hsvThreshed;
	}
	
	/**
	 * Get RGB image
	 * @return
	 */
	public Mat getRGB(){
		return this.frame;
	}
	
	/**
	 * Get frame width
	 * @return
	 */
	public double getWidth(){
		return this.frame.width();
	}
	
	/**
	 * Get distance approximation from a given Block object
	 * @param block The object we want to know the distancce to
	 * @return distance Distance in cm (or something close to it)
	 */
	public double getBlockDistance(Block block){
		double blockPixelWidth = block.getPixelWidth();
		double blockRealWidth = block.getRealWidth();
		double distance = (blockRealWidth*this.focalLength)/blockPixelWidth;
		return distance;
	}
	
	/**
	 * Morphological operations - a stage in the image processing
	 * @param target The operations will be applied to this
	 */
	private static void morphOps(Mat target){
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
		
		Imgproc.erode(target, target, erodeElement);
		Imgproc.erode(target, target, erodeElement);
	
		Imgproc.dilate(target, target, dilateElement);
		//TODO: optional: Gaussian blur/ something else
	}
	
	
}
