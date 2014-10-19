package object_detector;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.example.blob_detect_test.Color;
import com.example.blob_detect_test.CubeInfo;


/**
 * This class provides an interface for frame processing tasks.
 * The class requires the existence of the CubeInfo singleton class for feeding new data into it. 
 * The main method is processFrame(Mat src), which looks for the required object in the frame
 * and updates the CubeInfo singleton accordingly.
 * Getter methods for receiving processed images are also available.
 * @author Pavel Rubinson
 *
 */
public class ImgController {
	private static Frame frame; //Encapsulates a single frame. 
	private Color color; //The color to be searched for. 
	private Block lastBlock; //The detected block (null if none was detected)
	private Mat lastHSV; //A binary image after color separation, with the block contour drawn on top
	private Mat lastRGB; //The source RGB image, with the block contour drawn on top
	private List<MatOfPoint> auxContours = new ArrayList<MatOfPoint>(); //An auxiliary array of contours used for drawing tasks
	
	/**
	 * Constructor.
	 */
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
	
//	private Mat getMat(int matType){
//		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		//Block block = frame.getObjects();
//		contours.clear();
//		if (this.lastBlock != null){
//			contours.add(this.lastBlock.getCountour());
//		}
//		if (matType == 1){
//			result = frame.getThreshed();
//		} else if (matType == 2){
//			result = frame.getRGB();
//		}
//		if (block != null){
//			if(block!= null){
//				Imgproc.drawContours(result, contours, 0, new Scalar(0, 255,0),5);
//			}
//		}
//		return result;
//	}
	
	public void processFrame(Mat src){
		
		
		if (this.color != CubeInfo.getInstance().getColor()){
			frame.setColor(CubeInfo.getInstance().getColor());
		}
		frame.updateFrame(src);
		frame.processFrame();
		this.lastHSV = frame.getThreshed();
		this.lastRGB = frame.getRGB();
		this.lastBlock = frame.getObjects();
		if (this.lastBlock != null){
			CubeInfo.getInstance().setFound(true);
			this.auxContours.clear();
			auxContours.add(this.lastBlock.getCountour());
			Imgproc.drawContours(this.lastHSV, auxContours, 0, new Scalar(0, 255,0),5);
			Imgproc.drawContours(this.lastRGB, auxContours, 0, new Scalar(0, 255,0),5);
			double blockHorizontalCenter = this.lastBlock.getCenter().x;
			double centerDiff = blockHorizontalCenter - frame.getWidth()/2;
			double distance = frame.getBlockDistance(this.lastBlock);
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
	
	public Mat getThreshed(){
		return this.lastHSV;
	}
	
	public Mat getRGB(){
		return this.lastRGB;
		
	}
	
	public static void setThresh(int positionInList, int progress){
		frame.setThresh(positionInList, progress);
	}
	
	public void cropStrip(Mat src){
		int cols  = src.cols();
		int rows = src.rows();
		//Mat dest = new Mat(rows, cols,  src.type());
		//Mat dest = Mat.zeros(rows, cols, CvType.CV_64F);
		//Mat mask = src.clone();
		//Mat mask = Mat.zeros(rows, cols, src.type());
		Core.rectangle(src, new Point(0, 0), new Point(cols, rows-rows/10), new Scalar(255,255,255), -1);
		//Core.rectangle(src, new Point(0, rows/2+rows/10), new Point(cols, rows), new Scalar(255,255,255), -1);
		//Core.rectangle(mask, new Point(0, rows/2), new Point(cols, rows/2+rows/10), new Scalar(255,255,255), -1);
		//Core.bitwise_and(src, mask, dest);
		//src.copyTo(dest, mask);
		//return src;
	}
	
	
}
