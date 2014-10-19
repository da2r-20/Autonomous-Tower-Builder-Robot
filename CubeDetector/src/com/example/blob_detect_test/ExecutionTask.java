package com.example.blob_detect_test;

import ioio.examples.hello.MovmentSystem;
import ioio.examples.hello.RobotSettings;
import ioio.lib.api.exception.ConnectionLostException;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;


public class ExecutionTask extends  AsyncTask<URL, Integer, Long>{
	
	public AsyncResponse delegate = null;
	private MovmentSystem _movmentSystem;
	private boolean isMoving = false;
	
	//The array of colors that we are searching for, in the order of their appearance in the tower.
	//First member is the base cube.
	private Color[] colorArr;
	
	//The index (in colorArr) of the current color we are looking for
	private int currColor;
	
	//Horizontal center threshold (from -center to +center)
	private int centerLimit = 50; //Should be 30
	private int center =  230;//should be 230; //210;
	
	//Stopping "distance" from the cube (roughly corresponds to cm, but not really)
	private double distance = 0;
	private double sensorDistance = 0.7;
	
	//Robot speed values
	private double moveSpeed = 1;
	private double turnSpeed = 1;
	
	//True if we are going to the base cube. False otherwise. 
	private boolean gotoBase;
	
	private boolean follow;
	
	//Values to pass to the robotMove method
	private final static int STOP = 0;
	private final static int MOVE_SLOW = 7;
	private final static int MOVE_NORMAL = 1;
	private final static int MOVE_FAST = 10;
	private final static int RIGHT_VERY_SLOW = 8;
	private final static int RIGHT_SLOW = 3;
	private final static int RIGHT_FAST = 2;
	private final static int LEFT_SLOW = 4;
	private final static int BACK = 5;
	private final static int TAKE = 6;
	private final static int PUT = 9;
	
	private final static int SEARCH = 1;
	private final static int CENTER_LEFT = 2;
	private final static int CENTER_RIGHT = 3;
	private final static int GOTO_BY_CAMERA_NORMAL = 4;
	private final static int GOTO_BY_CAMERA_FAST = 10;
	private final static int GOTO_BY_SENSOR_FORW = 5;
	private final static int DONE = 6;
	private final static int CENTER_RIGHT_SLOW = 7;
	private final static int NOTHING = 8;
	private final static int GOTO_BY_SENSOR_BACK = 9;
	
	private boolean cubeIsCentered = false;
	private boolean cubeIsCentered2 = false;
	
	private int state = SEARCH;
	
	ExecutionTask(AsyncResponse delegate, MovmentSystem movmentSystem, Color[] colorArr, boolean follow){
		this.delegate = delegate;
		_movmentSystem = movmentSystem;
		this.colorArr = colorArr;
		this.currColor = 1;
		this.gotoBase = false;
		this.follow = follow;
		if (follow){
			this.moveSpeed = 0.8;
			this.distance = 0;
			this.centerLimit = 50;
		} else {
			this.moveSpeed = 0.6;
			this.distance = 9;
			this.centerLimit = 30;
		}
	}

	@Override
	/**
	 * Main execution loop of the thread
	 */
	protected Long doInBackground(URL... params) {
		this.currColor = 1;
		this.gotoBase = false;
		
		try {
			
			_movmentSystem.setRoverSpeed((float)moveSpeed);
			_movmentSystem.initArm();
			Thread.sleep(2000);
			//_movmentSystem.releaseCube();
			//_movmentSystem.moveSholder(45);
			//_movmentSystem.moveElbow(45);
			
			
			//grabbing first cube
			//_movmentSystem.moveArmToPutCube(13, 1, _movmentSystem.SHOLDER_FIRST);
			//while (true){
				
		//	}
//			_movmentSystem.grabCube();

//			Thread.sleep((long)(RobotSettings.clawTime * 1000));			
//			_movmentSystem.moveSholder(45);
//			_movmentSystem.moveElbow(45);
//
//			_movmentSystem.moveArmToPutCube(15,1, _movmentSystem.SHOLDER_FIRST);
//			_movmentSystem.releaseCube();
//			Thread.sleep((long)(RobotSettings.clawTime * 1000));
////			
//			_movmentSystem.moveSholder(45);
//			_movmentSystem.moveElbow(45);
//
//			_movmentSystem.moveArmToPutCube(10,1, _movmentSystem.ELBOW_FIRST);
//			_movmentSystem.grabCube();
//			Thread.sleep((long)(RobotSettings.clawTime * 1000));
////
//			_movmentSystem.moveElbow(20);
//			_movmentSystem.moveSholder(45);
//			
////
//			_movmentSystem.moveArmToPutCube(15,2, _movmentSystem.ELBOW_FIRST);
//			_movmentSystem.releaseCube();
//			Thread.sleep((long)(RobotSettings.clawTime * 1000));			
//			_movmentSystem.moveSholder(45);
//			_movmentSystem.moveElbow(45);
//			
			//			_movmentSystem.bringArmUp();
//			Log.i("", "Algorithm started");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("", "Starting main loop");
		
		if (this.follow){
			CubeInfo.getInstance().setColor(Color.LINE_COLOR);
			try {
				this.magicalAlgorithm(false);
			} catch (ConnectionLostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			this.currColor = 1;
			
			for (int i=0; i<2; i++/*this.currColor < this.colorArr.length*/){
				
				if (isCancelled()){
					Log.i("", "Task stopped");
					try {
						robotMove(STOP);
					} catch (ConnectionLostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				
				if (this.gotoBase){
					CubeInfo.getInstance().setColor(this.colorArr[0]);
					Log.i("Color change", "Color index is now: " + 0);
					this.gotoBase = false;
					currColor++;
				} else {
					CubeInfo.getInstance().setColor(this.colorArr[currColor]);
					Log.i("Color change", "Color index is now: " + String.valueOf(currColor));
					this.gotoBase = true;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated c6atch block
					e1.printStackTrace();
				}
				
				try {
					this.magicalAlgorithm(this.gotoBase);
				} catch (ConnectionLostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		
		
		return null;
	}
	
	/**
	 * Moves/turns/stops the robot. 
	 * @param movement
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	private void robotMove(int movement) throws ConnectionLostException, InterruptedException{
		if (this.isMoving){
			if (movement == STOP){
				this.isMoving = false;	
				Log.i("", "Stop command issued");
				this._movmentSystem.stop();			
			}	
		} else if (movement != STOP) {
			this.isMoving = true;
			_movmentSystem.setRoverSpeed((float)turnSpeed);
			switch (movement){
			case(MOVE_NORMAL):
				Log.i("", "Issued move command");
				_movmentSystem.setRoverSpeed((float)moveSpeed);
				
				this._movmentSystem.moveForwardCont();
				Thread.sleep(100);
				this.robotMove(STOP);
				Thread.sleep(20);
				break;
			case(MOVE_FAST):
				Log.i("", "Issued move command");
				_movmentSystem.setRoverSpeed((float)moveSpeed);
				this._movmentSystem.moveForwardCont();
				Thread.sleep(100);
				this.robotMove(STOP);
				Thread.sleep(100);
				break;
			case(MOVE_SLOW):
				Log.i("", "Issued move slow command");
				_movmentSystem.setRoverSpeed((float)moveSpeed);
				this._movmentSystem.moveForwardCont();
				Thread.sleep(50);
				this.robotMove(STOP);
				Thread.sleep(800);
				break;
			case(RIGHT_FAST):				
				Log.i("", "Issued turn right command");	
				this._movmentSystem.turnRight();
				Thread.sleep(200);
				this.robotMove(STOP);
				Thread.sleep(50);
				break;
			case(RIGHT_SLOW):				
				Log.i("", "Issued turn right command");	
				this._movmentSystem.turnRight();
				Thread.sleep(60);
				this.robotMove(STOP);
				Thread.sleep(150);
				break;
			case(RIGHT_VERY_SLOW):
				Log.i("", "Issued turn right slow command");	
				this._movmentSystem.turnRight();
				Thread.sleep(500);
				this.robotMove(STOP);
				Thread.sleep(500);
				break;
			case(LEFT_SLOW):
				Log.i("", "Issued turn left command");	
				this._movmentSystem.turnLeft();
				Thread.sleep(60);
				this.robotMove(STOP);
				Thread.sleep(150);
				break;
			case(BACK):
				Log.i("", "Issued go back command");	
				this._movmentSystem.driveBackwardsCont();
				Thread.sleep(80);
				this.robotMove(STOP);
				Thread.sleep(800);
				break;
			case(TAKE):
				this.robotMove(STOP);
				try {
					//_movmentSystem.moveArmToPutCube(13, 1, MovmentSystem.SHOLDER_FIRST);
					_movmentSystem.takeCube();
				} catch (NanExeption e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.sleep(2000);
				this.robotMove(STOP);
				break;
			case (PUT):
				this.robotMove(STOP);
				try {
					//_movmentSystem.moveArmToPutCube(13, 1, MovmentSystem.SHOLDER_FIRST);
					_movmentSystem.placeCube(2);
				} catch (NanExeption e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.sleep(5000);
				this.robotMove(STOP);
				break;
			}
		}	
	}

	/**
	 * Main movement algorithm. 
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	private void magicalAlgorithm(boolean take) throws ConnectionLostException, InterruptedException{
		this.state = SEARCH;
		while (this.state != DONE){
			if (isCancelled()){
				this.robotMove(STOP);
				break;
			}
			//CubeInfo.getInstance().updateSensorDistanceAvg(_movmentSystem.get_distance());
			this.updateState();
			switch (this.state){
				case(SEARCH):
					this.robotMove(RIGHT_FAST);
					break;
				case(CENTER_LEFT):
					this.robotMove(LEFT_SLOW);
					break;
				case(CENTER_RIGHT):
					this.robotMove(RIGHT_SLOW);
					break;
				case(GOTO_BY_CAMERA_NORMAL):
					this.robotMove(MOVE_NORMAL);
					break;
				case(GOTO_BY_CAMERA_FAST):
					this.robotMove(MOVE_FAST);
					break;
				case (CENTER_RIGHT_SLOW):
					this.robotMove(RIGHT_VERY_SLOW);
					break;
				case(GOTO_BY_SENSOR_FORW):
					this.robotMove(MOVE_SLOW);
					break;
				case(NOTHING):
					break;
				case(GOTO_BY_SENSOR_BACK):
					this.robotMove(BACK);
					break;
				case(DONE):
					//this.robotMove(STOP);
					//this.robotMove(BACK);
					if (take){
						this.robotMove(TAKE);
					} else {
						this.robotMove(PUT);
					}
					
					break;
			}
		}
	}
	
	/**
	 * Checks camera data and updates the state of the robot in relation to the cubes.
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	private void updateState() throws ConnectionLostException, InterruptedException{
		double currHorizLoc = CubeInfo.getInstance().getHorizontalLocation();
		double currDist = CubeInfo.getInstance().getDistance();
		
		if (follow || !this.cubeIsCentered){
			Log.i("IMPORTANT Camera distance","Status - Camera distance: " + String.valueOf(CubeInfo.getInstance().getDistance()));
			if (!CubeInfo.getInstance().getFound()){
				this.setState(SEARCH);
			} else if (currHorizLoc < center-centerLimit){
				this.setState(CENTER_LEFT);
			} else if (currHorizLoc > center+centerLimit){
				this.setState(CENTER_RIGHT);
			} else if (currDist > this.distance+this.distance/2){
				this.setState(GOTO_BY_CAMERA_FAST);
			} else if (currDist > this.distance) {
				this.setState(GOTO_BY_CAMERA_NORMAL);
			} else {
				this.setState(NOTHING);
				Log.i("State update", "Status - Switching to 'NOTHING' with camera distance reading of " + String.valueOf(currDist));
				this.cubeIsCentered = true;
			}
		} else { //Cube is centered by camera. Now moving using the distance sensor
			double sensDist = CubeInfo.getInstance().getSensorDistanceAvg();
			Log.i("IMPORTANT Sensor distance","Status - Sensor distance: " + String.valueOf(sensDist));
			Log.i("IMPORTANT Camera distance","Status - Camera distance: " + String.valueOf(CubeInfo.getInstance().getDistance()));
			Log.i("IMPORTANT Cube location", "Center location: " + String.valueOf(CubeInfo.getInstance().getHorizontalLocation()));
//			if (this._movmentSystem.get_distance()<0.3){
//			this.setState(CENTER_RIGHT_SLOW);	
			if (sensDist < this.sensorDistance-0.01){
				this.setState(GOTO_BY_SENSOR_FORW);
			} else if (sensDist > this.sensorDistance+0.01) {
				this.setState(GOTO_BY_SENSOR_BACK);
			} else {
				Log.i("State update", "Status - Switching to 'DONE' with sensor distance reading of " + String.valueOf(sensDist));
				this.setState(DONE);
				this.cubeIsCentered = false;
			}				
		}
	}
	
	/**
	 * Sets the state of the robot. Issuing "Stop" command between state changes.
	 * @param newState
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	private void setState(int newState) throws ConnectionLostException, InterruptedException{
		if (this.state != newState){
			robotMove(STOP);
			this.state = newState;
			
		}
	}

	protected void onProgressUpdate(Integer... progress) {
		switch (progress[0]){
			case (2): 
				delegate.processFinish("Turn right");
				break;
			case(-2):
				delegate.processFinish("Turn left");
				break;
			case(1):
				delegate.processFinish("Go!");
				break;
			case(0):
				delegate.processFinish("Stop");
				break;
			default:
				break;					
		}
		//delegate.processFinish("test");
		//(TextView)findViewById(R.id.RobotDirection)
		//setProgressPercent(progress[0]);
	}
	
	public void set_movmentSystem(MovmentSystem _movmentSystem) {
		this._movmentSystem = _movmentSystem;
	}
}
