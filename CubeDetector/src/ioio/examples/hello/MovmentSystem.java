package ioio.examples.hello;

import java.util.Timer;
import java.util.TimerTask;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * this class handles all functionality of the robot's movement
 * @author гешеп
 */
public class MovmentSystem implements Stoppable{
	private ChassisFrame _chassis;
	private RoboticArmEdge _arm;
	private AnalogInput _wristPosition;
	private AnalogInput _sholderPosition;
	private AnalogInput _elbowPosition;
	private AnalogInput _distance;
	private Timer _stopTimer = new Timer("Stop Timer");
	private static final float y1 = 75;
	private static final float x2 = (float) 0.607;
	private static final float diferential = -y1/x2;  


	
	public MovmentSystem(IOIO ioio, ChassisFrame chassis, RoboticArmEdge arm, int wristPositionPin, int sholderPositionPin, int elbowPositionPin, int distancePin) {
		_chassis = chassis;
		_arm = arm;
		
		try {
			_wristPosition = ioio.openAnalogInput(wristPositionPin);
			_sholderPosition = ioio.openAnalogInput(sholderPositionPin);
			_elbowPosition = ioio.openAnalogInput(elbowPositionPin);
			_distance = ioio.openAnalogInput(distancePin);
		} catch (ConnectionLostException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * simple getter
	 * @return
	 */
	public RoboticArmEdge get_arm() {
		return _arm;
	}
	
	/**
	 * simple getter
	 * @return
	 */
	public ChassisFrame get_chassis() {
		return _chassis;
	}
	
	/**
	 * gets the elbow position by the potentiometer on the arm
	 * @return the elbow position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_elbowPosition() throws InterruptedException, ConnectionLostException {
		return _elbowPosition.read();
	}
	
	/**
	 * gets the shoulder position by the potentiometer on the arm
	 * @return the shoulder position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_sholderPosition() throws InterruptedException, ConnectionLostException {
		return _sholderPosition.read();
	}
	
	/**
	 * gets the wrist position by the potentiometer on the arm
	 * @return the wrist position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_wristPosition() throws InterruptedException, ConnectionLostException {
		return _wristPosition.read();
	}
	
	/**
	 * gets the distance given by the distance sensor on the front of the robot
	 * @return returns the distance from the object in front of the rover
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_distance() throws InterruptedException, ConnectionLostException {
		return _distance.read();
	}
	
	
	/**
	 * closes all relevant digital pins
	 */
	public void close() {
		_elbowPosition.close();
		_sholderPosition.close();
		_sholderPosition.close();
		_distance.close();
		_chassis.close();
		_arm.close();
		
	}
	
	
	/**
	 * move the shoulder in a certain degree
	 * @param degree the degree to move the arm, the sign of degree will determine the direction
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveSholder(double degree) throws ConnectionLostException, InterruptedException{
		if(degree>90){
			degree=90;
		}
		double PositionToGet=(degree*(RobotSettings.sholderMov))+0.65786904;
		float sholderPosition=get_sholderPosition();
		if(sholderPosition>PositionToGet){
		  while(get_sholderPosition()>PositionToGet){
			  _arm.sholderDown();
		  }
		}
		else{
			if(sholderPosition<PositionToGet){
				  while(get_sholderPosition()<PositionToGet){
					  _arm.sholderUp();
				  }
				}
		}
		  _arm.stop();
	}
	
	/**
	 * move the elbow in a certain degree
	 * @param degree the degree to move the arm, the sign of degree will determine the direction
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveElbow(double degree) throws ConnectionLostException, InterruptedException{
		if(degree>90){
			degree=90;
		}
		double PositionToGet=0.7722385-(degree*(RobotSettings.elbowMov));
		float elbowPosition=get_elbowPosition();
		if(elbowPosition<PositionToGet){
		  while(get_elbowPosition()<PositionToGet){
			  _arm.elbowDown();
			  
		  }
		}
		else {
			if(elbowPosition>PositionToGet){
				  while(get_elbowPosition()>PositionToGet){
					  _arm.elbowUp();
				  }
			}
		}
		  _arm.stop();
	}
	

	/**
	 * this function lowers the arm to grab the cube infront of the rover 
	 * @param distance rover distance from the cube
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveArm(double distance) throws ConnectionLostException, InterruptedException{
		double [] cube= new double [2];
		cube[0]=0;
		cube[1] = RobotSettings.cubeSize;
		double [] D1_base={distance,18};
		double d1=9;
		double d2=11;
		double d3=4.5;
		double a3=(43.43)*(Math.PI/180);
		double [] D0=D1_base;
		double [] D3=cube;
		double b3=Math.sqrt(Math.pow(d2, 2)+Math.pow(d3, 2)-2*d2*d3*Math.cos(Math.PI-a3));

		double [] xx= new double [2];
		xx[0]=D0[0]-D3[0];
		xx[1]=D0[1]-D3[1];
		double b0 =Math.sqrt(Math.pow(xx[0], 2)+Math.pow(xx[1], 2));
		if(b0>d1+b3){
		this.moveForward(b0-(d1+b3)+3);
		this.moveArm(distance-(b0-(d1+b3)+3));
		return;
		}
		double beta0=Math.acos((Math.pow(b0, 2) - Math.pow(d1, 2)- Math.pow(b3, 2))/(-2*d1*b3));
		//System.out.println(beta0);
		double gamma3=Math.asin(d3/(b3*Math.sin(Math.PI-a3)));
		double a2=Math.PI-beta0-gamma3;
		double beta3=Math.asin(b3/(b0*Math.sin(beta0)));
		double betax=Math.atan(xx[0]/xx[1]);
		double a1=Math.PI-betax-beta3;

		double a1_degrees=a1*(180/Math.PI);
		double a2_degrees=a2*(180/Math.PI);

		this.moveSholder(90-a1_degrees);
		this.moveElbow(180-a2_degrees);
		//System.out.println("sholder need to move:" + (90-a1_degrees));
		//System.out.println("elbow need to move:" + (90-a2_degrees));
	}

	/**
	 * this function moves the arm to the position on the tower
	 * @param distance from the cube layed in-front of the rover
	 * @param amountOfCube the number of cube in the tower
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveArmToPutCube(double distance, int amountOfCube) throws ConnectionLostException, InterruptedException{
		double[] cube = new double [2];
		cube[0] = 0;
		cube[1] = RobotSettings.cubeSize * amountOfCube;
		double[] D1_base = {distance, 18};
		double d1 = 9;
		double d2 = 11;
		double d3 = 4.5;
		double a3 = (43.43) * (Math.PI / 180);
		double[] D0 = D1_base;
		double[] D3 = cube;
		double b3 = Math.sqrt(Math.pow(d2, 2) + Math.pow(d3, 2) - 2 * d2 * d3 * Math.cos(Math.PI - a3));

		double[] xx = new double [2];
		xx[0] = D0[0]-D3[0];
		xx[1] = D0[1]-D3[1];
		double b0 = Math.sqrt(Math.pow(xx[0], 2)+Math.pow(xx[1], 2));
		if(b0 > d1 + b3){
		this.moveForward(b0 - (d1 + b3) + 3);
		this.moveArm(distance - (b0 - (d1 + b3) + 3));
		return;
		}
		double beta0 = Math.acos((Math.pow(b0, 2) - Math.pow(d1, 2) - Math.pow(b3, 2)) / (-2*d1*b3));
		//System.out.println(beta0);
		double gamma3 = Math.asin(d3 / (b3 * Math.sin(Math.PI - a3)));
		double a2 = Math.PI - beta0 - gamma3;
		double beta3 = Math.asin(b3 / ( b0*Math.sin(beta0)));
		double betax = Math.atan(xx[0] / xx[1]);
		double a1 =  Math.PI - betax - beta3;

		double a1_degrees = a1 * (180 / Math.PI);
		double a2_degrees = a2 * (180 / Math.PI);

		this.moveSholder(90-a1_degrees);
		this.moveElbow(180-a2_degrees);
		//System.out.println("sholder need to move:" + (90-a1_degrees));
		//System.out.println("elbow need to move:" + (90-a2_degrees));

	}

	/**
	 * turn around in place
	 * @param dgree a degree to be turned
	 * @throws ConnectionLostException
	 */
	public void turnAround(double dgree) throws ConnectionLostException{
		long driveTime = (long) (RobotSettings.turnaroundTime * Math.abs(dgree));
		
		if (dgree < 0){
			_chassis.turnLeft();
		}
		else {
			_chassis.turnRight();
		}
		System.out.println(driveTime);
		
		_stopTimer.schedule(new StopMovment(_chassis), driveTime * 1000);
	}
	
	public void turnRight() throws ConnectionLostException{
		_chassis.turnRight();
	}
	
	public void turnLeft() throws ConnectionLostException{
		_chassis.turnLeft();
	}
	
	/**
	 * moves the robot forwards x centimeters
	 * @param centimeters centimeters to move
	 * @throws ConnectionLostException
	 */
	public void moveForward(double centimeters) throws ConnectionLostException{
		long driveTime = (long) (RobotSettings.movmentSpeed / centimeters * 1000);
		_chassis.driveForward();
		_stopTimer.schedule(new StopMovment(_chassis), driveTime);
	}
	
	
	
	/**
	 * moves the robot forward (without stopping)
	 * @throws ConnectionLostException
	 */
	public void moveForwardCont() throws ConnectionLostException{
		_chassis.driveForward();	
	}
	
	/**
	 * command the hand to grab a cube
	 * @throws ConnectionLostException
	 */
	public void grabCube() throws ConnectionLostException{
		long driveTime =(long) (RobotSettings.clawTime * 1000);
		_arm.closeHand();
		
		_stopTimer.schedule(new StopMovment(_arm), driveTime);
	}

	/**
	 * open the arm to release a cube
	 * @throws ConnectionLostException
	 */
	public void releaseCube() throws ConnectionLostException{
		long driveTime =(long) (RobotSettings.clawTime * 1000);
		_arm.openHand();
		
		_stopTimer.schedule(new StopMovment(_arm), driveTime);
	}

	/**
	 * moves the robot backwards x centimeters
	 * @param centimeters centimeters to move
	 * @throws ConnectionLostException
	 */
	public void moveBackwards(double centimeters) throws ConnectionLostException{
		long driveTime = (long) (RobotSettings.movmentSpeed / centimeters * 1000);
		_chassis.driveBackwards();
		_stopTimer.schedule(new StopMovment(_chassis), driveTime);
	}
	
	public void driveBackwardsCont() throws ConnectionLostException{
		_chassis.driveBackwards();
	}
	
	@Override
	public void stop() throws ConnectionLostException {
		_arm.stop();
		_chassis.stop();
	}

	/**
	 * sets the drive speed of the rover
	 * @param speed the new speed value
	 * @throws ConnectionLostException
	 */
	public void setRoverSpeed(float speed) throws ConnectionLostException {
		_chassis.setSpeed(speed);
	}
	
	
	/**
	 * this classes implements the TimerTask abstract class
	 * the goal of this class is to stop a certain stoppable object 
	 * @author гешеп
	 *
	 */
	public class StopMovment extends TimerTask{
		private Stoppable _obj;
		/**
		 * @param obj an object to be stopped
		 */
		public StopMovment(Stoppable obj) {
			_obj = obj;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("stoping...");
				_obj.stop();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// run()
	}// StopMovment
	
	
	/**
	 * this function brings up the arm to a certain position after grabbing a cube
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void bringArmUp() throws ConnectionLostException, InterruptedException {
		this.moveSholder(90);
		this.moveElbow(70);
	}
	
	/**
	 * picks up a cube, releases it and brings back up the arm
	 * @throws InterruptedException 
	 * @throws ConnectionLostException
	 */
	public void takeCube() throws ConnectionLostException, InterruptedException{
		this.moveArm(this.getDistanceCentimeters());
		this.grabCube();
		this.bringArmUp();
	}
	
	/**
	 * this function places a cube in a certain level.
	 * pre-assumeing that the cube is held by the robot
	 * @param level the level of the cube to be put at, for example - cube 3 == level 3
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void placeCube(int level) throws ConnectionLostException, InterruptedException{
		this.moveArmToPutCube(get_distance(), level * RobotSettings.cubeSize);
		this.releaseCube();
		this.bringArmUp();
	}
	
	/**
	 * gets the distance in centimeters from the first object in-front of the rover
	 * @return the distance from an object in-front of the rover 
	 * @throws ConnectionLostException 
	 * @throws InterruptedException 
	 */
	public float getDistanceCentimeters() throws InterruptedException, ConnectionLostException{
		return (float) 34.667 - (float)38.61 * this.get_distance();
	}
}
