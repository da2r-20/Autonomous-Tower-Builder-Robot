package com.example.blob_detect_test;

import ioio.examples.hello.MovmentSystem;
import ioio.lib.api.exception.ConnectionLostException;
import java.net.URL;
import android.os.AsyncTask;


public class ExecutionTask extends  AsyncTask<URL, Integer, Long>{
	
	public AsyncResponse delegate = null;
	private int currentAction = 100;
	private MovmentSystem _movmentSystem;
	private boolean isMoving = false;
	private Color[] colorArr;
	private int currColor;
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
		try {
			_movmentSystem.setRoverSpeed(100);
			_movmentSystem.turnAround(90);
		} catch (ConnectionLostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double horizLoc, distance;
		
		while(true){
			if (isCancelled()){
				break;
			}
			
			while (this.colorArr.length - this.currColor > 0){
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
				}
				try {
					this.goToCube();
				} catch (ConnectionLostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
			
		}
		return null;
	}
	
	
	protected void searchForCube() throws ConnectionLostException{
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (!CubeInfo.getInstance().getFound()){
			//TODO robot turn right/left
			this.robotMove(RIGHT);
		}
		this.robotMove(STOP);
		
		while (horizLoc < -30 || horizLoc > 30){
			if (horizLoc < -30){
				this.robotMove(RIGHT);
			} else {
				this.robotMove(LEFT);
			}
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		}
		this.robotMove(STOP);
	}
	
	protected void goToCube() throws ConnectionLostException{
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (CubeInfo.getInstance().getDistance() > 10){
			if (horizLoc > -30 || horizLoc < 30){
				this.robotMove(MOVE);
				horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			}
			else {
				this.robotMove(STOP);
				this.searchForCube(CubeInfo.getInstance().getColor());
			}
		}	
		this.robotMove(STOP);
	}
	
	private void robotMove(int movement) throws ConnectionLostException{
		if (!this.isMoving){
			this.isMoving = true;
			switch (movement){
			case(MOVE):
				this._movmentSystem.moveForwardCont();
				break;
			case(RIGHT):
				this._movmentSystem.turnRight();
				break;
			case(LEFT):
				this._movmentSystem.turnLeft();
				break;
			case(STOP):
				this._movmentSystem.stop();
				this.isMoving = false;
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
