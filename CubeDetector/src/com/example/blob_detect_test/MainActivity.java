package com.example.blob_detect_test;

import java.util.ArrayList;

import object_detector.ImgController;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.example.blob_detect_test.Adapter.SeekBarListener;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends Activity implements OnNavigationListener, CvCameraViewListener2 {
	
	private CameraBridgeViewBase mOpenCvCameraView;
	private ImgController imgController;
	private TextView robotDirections;
	
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
   
	mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_main_view);
    //mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
    mOpenCvCameraView.setCvCameraViewListener(this);
   
   
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
        	
        	Log.i("", "onProgressChanged " + progress + " position in list" + positionInList);
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


@Override
public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    // TODO Auto-generated method stub
    return false;
}


@Override
public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
	Mat frame = inputFrame.rgba();
	Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2BGR);
	Log.i("test", "test");
	//imgController.detectObjects(frame);
	frame = imgController.getProcessedFrame(frame);
	robotDirections = (TextView)findViewById(R.id.RobotDirection);
	/*
	Handler refresh = new Handler(Looper.getMainLooper());
	refresh.post(new Runnable() {
	    public void run()
	    {
	    	robotDirections.setText(imgController.getDirections());
	    }
	});
	*/
	
	
	runOnUiThread(new Runnable() {
	     @Override
	     public void run() {
	    	 robotDirections.setText(imgController.getDirections());
	    }
	});
	
	
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

}