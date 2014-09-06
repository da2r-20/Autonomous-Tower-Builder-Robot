package com.example.blob_detect_test;

import ioio.examples.hello.MovmentSystem;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class ExecutionTask extends AsyncTask<URL, Integer, Long>{
	
	public AsyncResponse delegate = null;
	private int currentAction = 100;
	private MovmentSystem _movmentSystem;
	
	
	ExecutionTask(AsyncResponse delegate, MovmentSystem movmentSystem){
		this.delegate = delegate;
		_movmentSystem = movmentSystem;
	}

	/**
	 * algo code goes here
	 */
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
			/*
			if (isCancelled()){
				break;
			}*/
			horizLoc = CubeInfo.getInstance().getHorizontalLocation();
			distance = CubeInfo.getInstance().getDistance();
			colorIndex = CubeInfo.getInstance().getColorIndex();
			if (horizLoc < -30){
				//TODO turn right
				//Log.i("","TURN RIGHT");
				if (currentAction != 2){
					currentAction = 2;
					publishProgress(2);	
				}
					
			} else if (horizLoc > 30){
				//TODO turn left
//				Log.i("","TURN LEFT");
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
					colorIndex = (colorIndex + 1) % 2 ;
					CubeInfo.getInstance().setColorIndex(colorIndex);
					publishProgress(0);	
				}
			}
		}
//		return null;
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
