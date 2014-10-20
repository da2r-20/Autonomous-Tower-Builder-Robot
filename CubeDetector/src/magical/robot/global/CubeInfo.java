package magical.robot.global;

import magical.robot.util.MovingAvg;

/**
 * A singleton container of data for the cube/detected-object.
 * Holds both information coming from the vision module (such as current horizontal location and distance),
 * and basic information required by other modules to function properly - such as the 
 * current expected cube color (to be searched for), aspect ratio and extent.
 * @author Pavel Rubinson
 *
 */
public class CubeInfo implements ObjectInfoContainer{
	
	private static CubeInfo instance = null;
	private double horizontalLocation;
	private double distance;
	private Color color;
	private boolean found;
	private MovingAvg sensDistAvg = new MovingAvg(5);
	private double aspectRatio[] = new double[2];
	private double extent[] = new double[2];
	
	protected CubeInfo(){
		this.horizontalLocation = -100;
		this.distance = -1;
		found = false;
	}
	
	
	public void setHorizontalLocation(double location){
		this.horizontalLocation = location;
	}
	
	
	public void setDistance(double dist){
		this.distance = dist;
	}
	
	/**
	 * Updates the moving average of cube distances given by the proximity sensor
	 * @param dist The last distance reading
	 */
	public void updateSensorDistanceAvg(double dist){
		this.sensDistAvg.update(dist);
	}
	
	
	public void setColor(Color color){
		this.color = color;
	}
	
	
	public void setFound(boolean found){
		this.found = found;
		if (!found){
			this.distance = -1;
			this.horizontalLocation = -1000;
		}
	}
	
	
	public boolean getFound(){
		return this.found;
	}
	
	
	public double getHorizontalLocation(){
		return this.horizontalLocation;
	}
	

	public double getDistance(){
		return this.distance;
	}
	
	/**
	 * Get the current moving average of the proximity sensor distance readings.
	 * @return
	 */
	public double getSensorDistanceAvg(){
		return this.sensDistAvg.getAvg();
	}
	
	public Color getColor(){
		return this.color;
	}
	
	/**
	 * Get the instance of the CubeInfo class.
	 * @return
	 */
	public static CubeInfo getInstance(){
		if (instance == null){
			instance = new CubeInfo();
		} 
		return instance;
	}


	
	public void setAspectRatio(double min, double max) {
		aspectRatio[0] = min;
		aspectRatio[1] = max;
	}


	public void setExtent(double min, double max) {
		extent[0] = min;
		extent[1] = max;
	}


	public double getAspectRatioMin() {
		return aspectRatio[0];
	}


	public double getAspectRatioMax() {
		return aspectRatio[1];
	}


	public double getExtentMin() {
		return extent[0];
	}


	public double getExtentMax() {
		return extent[1];
	}
		
}
