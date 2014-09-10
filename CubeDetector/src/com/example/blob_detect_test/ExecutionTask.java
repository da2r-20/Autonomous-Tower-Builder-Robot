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
	private Color[] colorArr;
	private int currColor;
	private int center = 30;
	private int distance = 15;
	private boolean gotoBase;
	private final static int MOVE = 1;
	private final static int RIGHT = 2;
	private final static int LEFT = 3;
	private final static int STOP = 0;
	
	
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
		double horizLoc, distance;
		Log.i("", "Starting main loop");
		
		while (this.colorArr.length - this.currColor > 0){
			if (isCancelled()){
				Log.i("", "Task stopped");
				try {
					robotMove(STOP);
				} catch (ConnectionLostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			if (this.gotoBase){
				CubeInfo.getInstance().setColor(this.colorArr[0]);
				this.gotoBase = false;
			} else {
				CubeInfo.getInstance().setColor(this.colorArr[currColor]);
				this.gotoBase = true;
				currColor++;
			}
			
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
			//TODO robot turn right/left
			this.robotMove(RIGHT);
			Thread.sleep(100);
			this.robotMove(STOP);
			Thread.sleep(100);
			
		}
		this.robotMove(STOP);
		
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
		
		while (CubeInfo.getInstance().getDistance() > this.distance){
			if (isCancelled()){
				this.robotMove(STOP);
				break;
			}
			//Log.i("", "goToCube distance: " +  String.valueOf(CubeInfo.getInstance().getDistance()));
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			//Log.i("", "goToCube horizontal location" +  String.valueOf(horizLoc));
			if (horizLoc > -this.center && horizLoc < this.center){
				//Log.i("", "MOVE!");
				//this._movmentSystem.moveForwardCont();
				this.robotMove(MOVE);
				//Thread.sleep(100);
				//this.robotMove(STOP);
				//Thread.sleep(100);
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
		Thread.sleep(1000);
	}
	
	private void robotMove(int movement) throws ConnectionLostException{
		if (this.isMoving){
			if (movement == STOP){
				this.isMoving = false;	
				Log.i("", "Stop command issued");
				this._movmentSystem.stop();			
			}	
		} else if (movement > STOP) {
			this.isMoving = true;
			_movmentSystem.setRoverSpeed((float)0.7);
			switch (movement){
			case(MOVE):
				Log.i("", "Issued move command");
				_movmentSystem.setRoverSpeed((float)0.4);
				this._movmentSystem.moveForwardCont();
				break;
			case(RIGHT):
				Log.i("", "Issued turn right command");	
				this._movmentSystem.turnRight();
				break;
			case(LEFT):
				Log.i("", "Issued turn left command");	
				this._movmentSystem.turnLeft();
				break;
			}
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
