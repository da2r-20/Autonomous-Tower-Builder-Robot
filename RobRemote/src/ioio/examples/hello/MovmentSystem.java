package ioio.examples.hello;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class MovmentSystem {
	private ChassisFrame _chassis;
	private RoboticArmEdge _arm;
	private AnalogInput _wristPosition;
	private AnalogInput _sholderPosition;
	private AnalogInput _elbowPosition;
	private static final float ELBOW_MAX_UP = 0;
	private static final float ELBOW_MAX_DOWN = 0;
	private static final float WRIST_MAX_UP = 0;
	private static final float WRIST_MAX_DOWN = 0;
	private static final float SHOLDER_MAX_UP = 0;
	private static final float SHOLDER_MAX_DOWN = 0;
	
	public MovmentSystem(IOIO ioio, ChassisFrame chassis, RoboticArmEdge arm, int wristP_num, int elbowP_num, int sholderP_num) throws ConnectionLostException {
		_chassis = chassis;
		_arm = arm;
		_elbowPosition = ioio.openAnalogInput(elbowP_num);
		_sholderPosition = ioio.openAnalogInput(sholderP_num);
		_wristPosition= ioio.openAnalogInput(wristP_num);
		
	}
	
	
	public RoboticArmEdge get_arm() {
		return _arm;
	}
	
	public ChassisFrame get_chassis() {
		return _chassis;
	}
	
	
	public float get_elbowPosition() throws InterruptedException, ConnectionLostException {
		return _elbowPosition.read();
	}
	
	public float get_sholderPosition() throws InterruptedException, ConnectionLostException {
		return _sholderPosition.read();
	}
	
	public float get_wristPosition() throws InterruptedException, ConnectionLostException {
		return _wristPosition.read();
	}
	
	
	public void close() {
		_elbowPosition.close();
		_sholderPosition.close();
		_sholderPosition.close();
		
	}
	
	
}
