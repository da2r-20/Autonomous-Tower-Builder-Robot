package com.example.blob_detect_test;

import ioio.examples.hello.BigMotorDriver;
import ioio.examples.hello.ChassisFrame;
import ioio.examples.hello.MovmentSystem;
import ioio.examples.hello.RobotSettings;
import ioio.examples.hello.RoboticArmEdge;
import ioio.examples.hello.SmallMotorDriver;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import java.util.ArrayList;
import object_detector.ImgController;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.blob_detect_test.Adapter.SeekBarListener;

public class MainActivity extends IOIOActivity   implements OnNavigationListener, CvCameraViewListener2, AsyncResponse {
	
	//which image to display
	private static final int HSV = 1;
	private static final int RGB = 2;

	//image related variables
	private CameraBridgeViewBase mOpenCvCameraView;
	private ImgController imgController;
	private TextView robotDirections;

	//GUI fields
//	private ToggleButton _startStop;

	//robot modules
	private ChassisFrame _chasiss;
	private RoboticArmEdge _arm;
	private MovmentSystem _movmentModule;
	
	//The displayed image type
	private int imgType = RGB;

	
	//main execution AsyncTask
	private ExecutionTask _execution;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				//Log.i("OpenCV loaded successfully");
				imgController = new ImgController();
				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.camera);
//		_startStop = (ToggleButton) findViewById(R.id.toggleButton1);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_main_view);
		//mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		
		Color[] colorArr = {Color.GREEN, Color.BLUE};
		
		this._execution = (ExecutionTask) new ExecutionTask(this, _movmentModule, colorArr);
		//init cube info
		CubeInfo.getInstance().setColor(Color.GREEN);
		
		//init execution task
		//this.execution = new ExecutionTask(this);
		
		//init reference to buttom TextView
		robotDirections = (TextView)findViewById(R.id.RobotDirection);
		
		//setContentView(R.layout.activity_main);
		ActionBar bar = getActionBar();
		ArrayList<String> list = new ArrayList<String>();
		list.add("HMin ");
		list.add("SMin");
		list.add("VMin ");
		list.add("HMax");
		list.add("SMax ");
		list.add("VMax");
		Adapter adapter = new Adapter();

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setListNavigationCallbacks(adapter.getAdapter(this, list, "Controls"), this);

		adapter.setSeekBarListener( new SeekBarListener(){

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser, int positionInList) {
				//Log.i("", "onProgressChanged " + progress + " position in list" + positionInList);
				ImgController.setThresh(positionInList, progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar, int positionInList) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar, int positionInList) {
				// TODO Auto-generated method stub
			}

		});
	}

	/**
	 * this method defines the action to be taken for pressing the toggle button
	 * @param view 
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void onToggleClicked1(View view) throws ConnectionLostException, InterruptedException{
		boolean on = ((ToggleButton) view).isChecked();
		
		if (on){
			
			Log.i("", "Algorithm started");
			/*
			Handler handler = new Handler();
			Runnable r=new Runnable()
			{
			    public void run() 
			    {
			        try {
			        	System.out.println(_movmentModule);
			        	_movmentModule.grabCube();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			    }
			};
			handler.postAtFrontOfQueue(r);
			*/
				
			_execution.execute();
			
			//execution.execute();
			//_movmentModule.setRoverSpeed(100);
			//_movmentModule.moveArm(15);
		} else{
			Log.i("", "Algorithm stopped");
			_execution.cancel(true);
			//_movmentModule.stop();
		}
	}
	
	public void onToggleClicked2(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	        this.imgType = HSV;
	    } else {
	    	this.imgType = RGB;
	        // Disable vibrate
	    }
	}


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.rgba();
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2BGR);
		
		//imgController.detectObjects(frame);
		imgController.processFrame(frame);
		if (imgType == RGB){
			frame = imgController.getRGB();
			Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2BGR);
		} else if (imgType == HSV){
			frame = imgController.getThreshed();
		}
		//robotDirections = (TextView)findViewById(R.id.RobotDirection);
		return frame;
	}


	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallback);
	}
	
	class Looper extends BaseIOIOLooper {
		protected void setup() throws ConnectionLostException {
			BigMotorDriver chassiFront = new BigMotorDriver(ioio_, RobotSettings.FRONT_CHASSIS_M1_PIN, RobotSettings.FRONT_CHASSIS_E1_PIN, RobotSettings.FRONT_CHASSIS_M2_PIN, RobotSettings.FRONT_CHASSIS_E2_PIN);
			BigMotorDriver chassiBack = new BigMotorDriver(ioio_, RobotSettings.BACK_CHASSIS_M1_PIN, RobotSettings.BACK_CHASSIS_E1_PIN, RobotSettings.BACK_CHASSIS_M2_PIN, RobotSettings.BACK_CHASSIS_E2_PIN);
			_chasiss = new ChassisFrame(chassiFront, chassiBack);
			
			SmallMotorDriver turn_and_led = new SmallMotorDriver(ioio_, RobotSettings.TURN_A01_PIN, RobotSettings.TURN_A02_PIN, RobotSettings.LED_B01_PIN, RobotSettings.LED_B02_PIN);
			SmallMotorDriver sholder_and_elbow = new SmallMotorDriver(ioio_, RobotSettings.SHOLDER_A01_PIN, RobotSettings.SHOLDER_A02_PIN, RobotSettings.ELBOW_B01_PIN, RobotSettings.ELBOW_B02_PIN);
			SmallMotorDriver wrist_and_grasp = new SmallMotorDriver(ioio_, RobotSettings.WRIST_A01_PIN, RobotSettings.WRIST_A02_PIN, RobotSettings.GRASP_B01_PIN, RobotSettings.GRASP_B02_PIN);
			_arm = new RoboticArmEdge(ioio_, wrist_and_grasp, sholder_and_elbow, turn_and_led, RobotSettings.ARM_STBY, RobotSettings.ARM_PWM);
			_movmentModule = new MovmentSystem(ioio_, _chasiss, _arm, RobotSettings.WRIST_POT_PIN, RobotSettings.SHOLDER_POT_PIN, RobotSettings.ELBOW_POT_PIN, RobotSettings.DISTANCE_PIN);
			_execution.set_movmentSystem(_movmentModule);
			
		}
		
		public void loop() throws ConnectionLostException {
//			try {
//				System.out.println(_movmentModule.getDistanceCentimeters());
//				System.out.println(_movmentModule.get_distance());
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
	}//BaseIOIOLooper

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}


	@Override
	public void processFinish(String output) {
		this.robotDirections.setText(output);
		
	}
}