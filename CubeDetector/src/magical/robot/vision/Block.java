package magical.robot.vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Represents a single detected block.
 * Allows for getting the block's basic properties.
 * @author Pavel Rubinson
 *
 */
public class Block {
	//The contour of the block
	private MatOfPoint contour;
	
	//The width of the block in real life (in cm)
	private double realWidth;
	
	/**
	 * Initializes a new block with a given contour and width
	 * @param contour The contour of the block
	 * @param realWidth The width of the block (in reality)
	 */
	public Block(MatOfPoint contour, double realWidth){
		this.contour = contour;
		this.realWidth = realWidth;
	}
	
	/**
	 * Get the center coordinates of the block
	 * @return The Point representing block's center coordinates
	 */
	public Point getCenter(){
		Moments blockMoments = Imgproc.moments(this.contour, false);
		Point blockCenter = new Point(blockMoments.get_m10()/blockMoments.get_m00(), 
				blockMoments.get_m01()/blockMoments.get_m00());
		return blockCenter;
	}
	
	/**
	 * Get the block's contour
	 * @return The block's contour
	 */
	public MatOfPoint getCountour(){
		return this.contour;
	}
	
	/** Get the blocks width in pixels
	 * @return The block's width in pixels (number of pixels)
	 */
	public double getPixelWidth(){
		Rect boundingBox = Imgproc.boundingRect(contour);
		return boundingBox.width;
	}
	
	/**
	 * Get the block's real width 
	 * @return the block's real width in cm
	 */
	public double getRealWidth(){
		return this.realWidth;
	}

}
