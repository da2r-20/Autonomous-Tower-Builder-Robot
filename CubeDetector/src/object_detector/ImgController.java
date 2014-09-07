package object_detector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.example.blob_detect_test.Color;
import com.example.blob_detect_test.CubeInfo;

import android.util.Log;


public class ImgController {
	private static Frame frame;
	private Color color;
	
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
			Log.i("", String.valueOf(centerDiff));
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
		Mat result = frame.getThreshed();
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
		}
		return result;
	}
	
	public static void setThresh(int positionInList, int progress){
		frame.setThresh(positionInList, progress);
	}
	
	public Mat getThreshed(){
		return frame.getThreshed();
		
	}

}
