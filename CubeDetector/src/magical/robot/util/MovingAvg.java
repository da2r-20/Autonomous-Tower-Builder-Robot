package magical.robot.util;

/**
 * A helper utility class for calculating a moving average
 * @author Pavel Rubinson
 *
 */
public class MovingAvg {
	
	//Next array index to be updated
	private int index = 0;
	
	//The size of the array (number of values for the moving average)
	private int size;
	
	//The current number of the array
	private int count = 0;
	
	//The sum of all the values in the array
	private double total = 0;
	
	//The values array
	private double data[];
	
	/**
	 * Create a new instance of size @param size
	 * @param size The number of values we wish to calculate a moving average for
	 */
	public MovingAvg(int size){
		this.size = size;
		data = new double[size];
		for (int i=0; i<size; i++){
			data[i] = 0;
		}
	}
	
	/**
	 * Add a new value to the moving average
	 * @param newItem 
	 */
	public void update(double newItem){
		if (this.count < this.size){
			this.count ++;
		}
		this.total -= this.data[index];
		this.total += newItem; 
		this.data[index] = newItem;
		this.index = (this.index + 1)%this.size;
	}
	
	/**
	 * Get the current moving average
	 * @return
	 */
	public double getAvg(){
		return this.total/this.count;
	}

}
