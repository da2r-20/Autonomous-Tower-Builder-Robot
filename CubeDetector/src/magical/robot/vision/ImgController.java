package magical.robot.vision;

import java.util.ArrayList;
import java.util.List;

import magical.robot.global.Color;
import magical.robot.global.CubeInfo;
import magical.robot.global.ObjectInfoContainer;

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
	private boolean first = true; //True before the first time time a frame is processed. False afterwards
	
	/**
	 * Constructor. Initializes a new ImgController.
	 */
	public ImgController(){
		frame = new Frame(720,3, CubeInfo.getInstance().getAspectRatioMin(),  CubeInfo.getInstance().getAspectRatioMax(),
				 CubeInfo.getInstance().getExtentMin(),  CubeInfo.getInstance().getExtentMax());
		this.color = null;
	};
	
	
	/**
	 * Processes the @param src image,
	 * searches for the required object, and updates the cube info (in the info container).
	 * @param src The source image.
	 */
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
			CubeInfo.getInstance().setHorizontalLocation(centerDiff);
			CubeInfo.getInstance().setDistance(distance);
		} else {
			CubeInfo.getInstance().setFound(false);
		}
	}
	
	/**
	 * Get a thresholded binary image with the detected object shown as a contour on top of it.
	 * @return
	 */
	public Mat getThreshed(){
		return this.lastHSV;
	}
	
	/**
	 * Get a the source RGB image with the detected object shown as a contour on top of it.
	 * @return
	 */
	public Mat getRGB(){
		return this.lastRGB;
		
	}
	
	/**
	 * Update color threshold values
	 * @param positionInList
	 * @param progress
	 */
	public static void setThresh(int positionInList, int progress){
		frame.setThresh(positionInList, progress);
	}
	
	
	/**
	 * Paints everything white except for a narrow strip at the bottom of the image.
	 * Used as a pre-processing stage for line tracing.
	 * @param src The source image
	 */
	public void lineTrackingCrop(Mat src){
		int cols  = src.cols();
		int rows = src.rows();
		Core.rectangle(src, new Point(0, 0), new Point(cols, rows-rows/10), new Scalar(255,255,255), -1);
	}
	
	
}
