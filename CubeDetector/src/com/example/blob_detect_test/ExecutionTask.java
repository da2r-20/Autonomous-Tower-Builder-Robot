package com.example.blob_detect_test;

import ioio.examples.hello.MovmentSystem;
import ioio.lib.api.exception.ConnectionLostException;

import java.net.URL;

import android.os.AsyncTask;
import android.renderscript.Type.CubemapFace;
import android.util.Log;

public class ExecutionTask extends  AsyncTask<URL, Integer, Long>{
	
	public AsyncResponse delegate = null;
	private int currentAction = 100;
	private int currState;
	private MovmentSystem _movmentSystem;
	private boolean isMoving = false;
	
	
	ExecutionTask(AsyncResponse delegate, MovmentSystem movmentSystem){
		this.delegate = delegate;
		_movmentSystem = movmentSystem;
	}

	@Override
	protected Long doInBackground(URL... params) {
		double horizLoc;
		double distance;
		int colorIndex;
		

//		try {
//			_movmentSystem.releaseCube();
//		} catch (ConnectionLostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		while(true){
			if (isCancelled()){
				break;
			}
			
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
				Log.i("","TURN LEFT");
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
	
	
	protected void searchForCube(Color color) throws ConnectionLostException{
		CubeInfo.getInstance().setColor(color);	
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (!CubeInfo.getInstance().getFound()){
			//TODO robot turn right/left
			if (!this.isMoving){
				this._movmentSystem.turnRight();
				this.isMoving = true;
			}
		}
		this._movmentSystem.stop();
		this.isMoving = false;
		
		while (horizLoc < -30){
			if (!this.isMoving){
				this._movmentSystem.turnRight();
				this.isMoving = true;
			}
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		}
		this._movmentSystem.stop();
		this.isMoving = false;
		
		while (horizLoc > 30){
			if (!this.isMoving){
				this._movmentSystem.turnLeft();
				this.isMoving = true;
			}
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		}
		this._movmentSystem.stop();
		this.isMoving = false;
		/*
		while (horizLoc < -30 || horizLoc > 30){
			if (horizLoc < -30){
				if (!this.isMoving){
					this._movmentSystem.turnRight();
					this.isMoving = true;
				}
			} else {
				if (!this.isMoving){
					this._movmentSystem.turnLeft();
					this.isMoving = true;
				}
			}
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		}
		*/
	}
	
	protected void goToCube() throws ConnectionLostException{
		double horizLoc = CubeInfo.getInstance().getHorizontalLocation();
		while (CubeInfo.getInstance().getDistance() > 10){
			if (horizLoc > -30 || horizLoc < 30){
				if (!this.isMoving){
					this._movmentSystem.moveForwardCont();
					this.isMoving = true;
				}
				horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			}
			else {
				this._movmentSystem.stop();
				this.isMoving = false;
				this.searchForCube(CubeInfo.getInstance().getColor());
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

}
