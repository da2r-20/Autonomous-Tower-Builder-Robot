package magical.robot.global;

/**
 * 
 * @author Pavel Rubinson
 *
 */
public interface ObjectInfoContainer {
	
	/**
	 * Set the horizontal location (X axis coordinate) of the object.
	 * @param location
	 */
	public void setHorizontalLocation(double location);
	
	/**
	 * Set the approximate distance from camera, in cm, of the object
	 * @param dist
	 */
	public void setDistance(double dist);
	
	/**
	 * Set the color we are expecting the object to be. 
	 * i.e. the color we are searching for.
	 * @param color a Color object with the HSV range for the color
	 */
	public void setColor(Color color);
	
	/**
	 * Set if the object was found/not found
	 * @param found True if found, false otherwise
	 */
	public void setFound(boolean found);
	
	/**
	 * Set the expected aspect ratio (width/height) of the object 
	 * @param min The minimum expected aspect ratio
	 * @param max The maximum expected aspect ratio
	 */
	public void setAspectRatio(double min, double max);
	
	/**
	 * Set the expected extent ([bounding box area]/[object contour area]) of the object.
	 * @param min The minimum expected extent
	 * @param max The maximum expected extent
	 */
	public void setExtent(double min, double max);
	
	/**
	 * Check if the object was found/not found
	 * @return True if found, false otherwise
	 */
	public boolean getFound();
	
	/**
	 * Get the horizontal location (X axis coordinates) of the object
	 * @return
	 */
	public double getHorizontalLocation();
	
	/**
	 * Get the approximate distance from camera of the object
	 * @return approximate distance in cm
	 */
	public double getDistance();
	
	/**
	 * Get the expected color of the object
	 * @return A Color object with an HSV range of the color.
	 */
	public Color getColor();
	
	/**
	 * Get the minimum expected aspect ratio (width/height) of the object
	 * @return
	 */
	public double getAspectRatioMin();
	
	/**
	 * Get the maximum expected aspect ratio (width/height) of the object
	 * @return
	 */
	public double getAspectRatioMax();
	
	/**
	 * Get the minimum expected extent ([bounding box area]/[object contour area]) of the object
	 * @return
	 */
	public double getExtentMin();
	
	/**
	 * Get the maximum expected extent ([bounding box area]/[object contour area]) of the object
	 * @return
	 */
	public double getExtentMax();

}
