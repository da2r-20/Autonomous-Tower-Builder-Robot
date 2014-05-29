package ioio.examples.hello;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.view.Display;


/*
 * this class represents all sensors used by the robot
 * whether they are android integrated sensors or are IOIO connected sensors
 */
public class SensorySystem {
	private SensorManager _mSensorManager;
	private Sensor _mSensor;
	private Display _d;
	
	
	public SensorySystem(Display d) {
		_d=d;
	}
	
	
	public int getAzimut() {
		return _d.getRotation();
	}
	
}