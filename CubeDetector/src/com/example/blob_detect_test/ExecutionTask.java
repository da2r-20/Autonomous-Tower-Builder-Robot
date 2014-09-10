package com.example.blob_detect_test;

import ioio.examples.hello.MovmentSystem;
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
	private int center = 30;
	
	//Stopping "distance" from the cube (roughly corresponds to cm, but not really)
	private int distance = 15;
	
	//Robot speed values
	private double moveSpeed = 0.4;
	private double turnSpeed = 0.7;
	
	//True if we are going to the base cube. False otherwise. 
	private boolean gotoBase;
	
	//Values to pass to the robotMove method
	private final static int MOVE = 1;
	private final static int RIGHT = 2;
	private final static int LEFT = 3;
	private final static int STOP = 0;
	
	private final static int SEARCH = 1;
	private final static int CENTER_LEFT = 2;
	private final static int CENTER_RIGHT = 3;
	private final static int GOTO = 4;
	private final static int DONE = 5;
	
	private int state = SEARCH;
	
	
	ExecutionTask(AsyncResponse delegate, MovmentSystem movmentSystem, Color[] colorArr){
		this.delegate = delegate;
		_movmentSystem = movmentSystem;
		this.colorArr = colorArr;
		this.currColor = 1;
		this.gotoBase = false;
	}

	@Override
	protected Long doInBackground(URL... params) {
		this.currColor = 1;
		this.gotoBase = false;
		try {
			_movmentSystem.setRoverSpeed((float)0.5);
			//_movmentSystem.turnLeft();
			//_movmentSystem.moveForward(30);
			
			Log.i("", "Algorithm started");
		} catch (ConnectionLostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//double horizLoc, distance;
		Log.i("", "Starting main loop");
		
		this.currColor = 1;
		while (this.currColor < this.colorArr.length){
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				this.magicalAlgorithm();
			} catch (ConnectionLostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			/*
			try {
				this.searchForCube();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				this.goToCube();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		//while(true){
			
			
			
			/*
			try {
				this.searchForCube(Color.GREEN);
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				this.goToCube();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			/*
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			distance = CubeInfo.getInstance().getDistance();
			//colorIndex = CubeInfo.getInstance().getColorIndex();
			if (horizLoc < -30){
				//TODO turn right
				//Log.i("","TURN RIGHT");
				if (currentAction != 2){
					currentAction = 2;
					publishProgress(2);	
				}
					
			} else if (horizLoc > 30){
				//TODO turn left
				//Log.i("","TURN LEFT");
				if (currentAction != -2){
					currentAction = -2;
					publishProgress(-2);	
				}
			} else if (distance > 10){
				//TODO go
				//Log.i("","Go!");
				if (currentAction != 1){
					currentAction = 1;
					publishProgress(1);	
				}
			} else if (distance < 10){
				//Log.i("","STOP");
				
				if (currentAction != 0){
					currentAction = 0;
					if (CubeInfo.getInstance().getColor() == Color.GREEN){
						CubeInfo.getInstance().setColor(Color.BLUE);
					} else {
						CubeInfo.getInstance().setColor(Color.GREEN);
					}
					
					publishProgress(0);	
				}
			}
			*/
			
		//}
		return null;
	}
	
	
	protected void searchForCube() throws ConnectionLostException, InterruptedException{
		Log.i("", "Searching for cube");
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (!CubeInfo.getInstance().getFound()){
			if (isCancelled()){
				this.robotMove(STOP);
				break;
			}
			//Log.i("", "Found? " + CubeInfo.getInstance().getFound());
			this.robotMove(RIGHT);
			Thread.sleep(100);
			this.robotMove(STOP);
			Thread.sleep(100);
			
		}
		this.robotMove(STOP);
		 horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (horizLoc < -this.center || horizLoc > this.center){
			Log.i("", "Horizontal location: " + String.valueOf(horizLoc));
			if (isCancelled()){
				this.robotMove(STOP);
				break;
			}
			if (horizLoc < -this.center){
				//Log.i("", "Turning left");
				this.robotMove(LEFT);		
			} else {
				//Log.i("", "Turning right");
				this.robotMove(RIGHT);
			}
			Thread.sleep(100);
			this.robotMove(STOP);
			Thread.sleep(100);
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		}
		this.robotMove(STOP);
		//Log.i("", "STOP!");
		//Log.i("", "Horizontal location at stop: " + String.valueOf(horizLoc));
	}
	
	protected void goToCube() throws ConnectionLostException, InterruptedException{
		this.robotMove(STOP);
		Log.i("", "Going to cube");
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		
		while (CubeInfo.getInstance().getDistance() > this.distance || !CubeInfo.getInstance().getFound()){
			if (isCancelled()){
				this.robotMove(STOP);
				break;
			}
			//Log.i("", "goToCube distance: " +  String.valueOf(CubeInfo.getInstance().getDistance()));
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			//Log.i("", "goToCube horizontal location" +  String.valueOf(horizLoc));
			if (CubeInfo.getInstance().getFound() && horizLoc > -this.center && horizLoc < this.center){
				//Log.i("", "MOVE!");
				//this._movmentSystem.moveForwardCont();
				this.robotMove(MOVE);
				//Thread.sleep(20);
				//this.robotMove(STOP);
				//Thread.sleep(20);
				//Log.i("", "Going to cube, horizontal location: " +  String.valueOf(horizLoc));
			}
			else {
				this.robotMove(STOP);
				Log.i("", "Correcting orientation");
				this.searchForCube();
			}
		}	
		//Log.i("", "STOP!");	
		this.robotMove(STOP);
		Thread.sleep(3000);
	}
	
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
			case(MOVE):
				Log.i("", "Issued move command");
				_movmentSystem.setRoverSpeed((float)moveSpeed);
				this._movmentSystem.moveForwardCont();
				break;
			case(RIGHT):				
				Log.i("", "Issued turn right command");	
				this._movmentSystem.turnRight();
				Thread.sleep(100);
				this.robotMove(STOP);
				Thread.sleep(100);
				break;
			case(LEFT):
				Log.i("", "Issued turn left command");	
				this._movmentSystem.turnLeft();
				Thread.sleep(100);
				this.robotMove(STOP);
				Thread.sleep(100);
				break;
			}
		}	
	}

	
	private void magicalAlgorithm() throws ConnectionLostException, InterruptedException{
		this.state = SEARCH;
		while (this.state != DONE){
			this.updateState();
			switch (this.state){
				case(SEARCH):
					this.robotMove(RIGHT);
					break;
				case(CENTER_LEFT):
					this.robotMove(LEFT);
					break;
				case(CENTER_RIGHT):
					this.robotMove(RIGHT);
					break;
				case(GOTO):
					this.robotMove(MOVE);
					break;
			}
		}
	}
	
	private void updateState() throws ConnectionLostException, InterruptedException{
		double currHorizLoc = CubeInfo.getInstance().getHorizontalLocation();
		double currDist = CubeInfo.getInstance().getDistance();
		if (!CubeInfo.getInstance().getFound()){
			this.setState(SEARCH);
		} else if (currHorizLoc < -this.center){
			this.setState(CENTER_LEFT);
		} else if (currHorizLoc > this.center){
			this.setState(CENTER_RIGHT);
		} else if (currDist > this.distance){
			this.setState(GOTO);
		} else {
			this.setState(DONE);
		}
	}
	
	
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
