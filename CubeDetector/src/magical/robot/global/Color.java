package magical.robot.global;


import org.opencv.core.Scalar;

/**
 * An enum that holds a collection of HSV ranges for colors we are interested in detecting.
 * Those values can be later used for object detection.
 * The ranges for each color were individually measured and might need to be measured again
 * if light conditions change significantly.
 * @author Pavel Rubinson
 *
 */
public enum Color {
	GREEN (new Scalar(45,98,0), new Scalar(94,255,255)),
	BLUE (new Scalar(95,0,0), new Scalar(149,255,255)),
	YELLOW (new Scalar(13,46,0), new Scalar(54,255,255)),
	LINE_COLOR (new Scalar(0,0,0), new Scalar(255,255,70));
	
	public final Scalar hsvMin;
	public final Scalar hsvMax;
	
	/**
	 * Create a new Color 
	 * @param hsvMin the minimum HSV values
	 * @param hsvMax the maximum HSV values
	 */
	private Color(Scalar hsvMin, Scalar hsvMax){
		this.hsvMin = hsvMin;
		this.hsvMax = hsvMax;
	}
	
	/**
	 * Get the min values for a given Color
	 * @return
	 */
	public Scalar getHsvMin(){
		return this.hsvMin;
	}
	
	/**
	 * Get the max values for a given Color
	 * @return
	 */
	public Scalar getHsvMax(){
		return this.hsvMax;
	}
}
