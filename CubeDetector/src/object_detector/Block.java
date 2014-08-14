package object_detector;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class Block {
	private MatOfPoint contour;
	private double realWidth;
	
	public Block(MatOfPoint contour, double realWidth){
		this.contour = contour;
		this.realWidth = realWidth;
	}
	
	public Point getCenter(){
		Moments blockMoments = Imgproc.moments(this.contour, false);
		Point blockCenter = new Point(blockMoments.get_m10()/blockMoments.get_m00(), 
				blockMoments.get_m01()/blockMoments.get_m00());
		return blockCenter;
	}
	
	public MatOfPoint getCountour(){
		return this.contour;
	}
	
	public double getPixelWidth(){
		Rect boundingBox = Imgproc.boundingRect(contour);
		return boundingBox.width;
	}
	
	public double getRealWidth(){
		return this.realWidth;
	}

}
