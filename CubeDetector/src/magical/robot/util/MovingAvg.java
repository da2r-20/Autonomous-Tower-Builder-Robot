package magical.robot.util;


public class MovingAvg {
	private int index = 0;
	private int size;
	private int count = 0;
	private double total = 0;
	private double data[];
	
	public MovingAvg(int size){
		this.size = size;
		data = new double[size];
		for (int i=0; i<size; i++){
			data[i] = 0;
		}
	}
	
	public void update(double newItem){
		if (this.count < this.size){
			this.count ++;
		}
		this.total -= this.data[index];
		this.total += newItem; 
		this.data[index] = newItem;
		this.index = (this.index + 1)%this.size;
	}
	
	public double getAvg(){
		return this.total/this.count;
	}

}
