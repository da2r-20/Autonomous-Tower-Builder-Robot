package ioio.examples.hello;


import ioio.examples.hello.R;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

//holds all the buttons that needs listening to
public class MainActivity extends IOIOActivity {
	
	//arm pins
	private static final int ARM_STBY = 31;
	private static final int ARM_PWM = 28;
	

	//turn and led
	private static final int TURN_A02_PIN = 19;
	private static final int TURN_A01_PIN = 20;
	private static final int LED_B01_PIN = 21;
	private static final int LED_B02_PIN = 22;
	//sholder and elbow

	private static final int SHOLDER_A02_PIN = 26;  
	private static final int SHOLDER_A01_PIN = 25;
	private static final int ELBOW_B02_PIN = 23;
	private static final int ELBOW_B01_PIN = 24;
	
	//wrist and grasp
	private static final int WRIST_A01_PIN = 29;
	private static final int WRIST_A02_PIN = 30;  
	private static final int GRASP_B01_PIN = 32;
	private static final int GRASP_B02_PIN = 33;
	
	//front side motors on chassi
	private static final int FRONT_CHASSIS_E1_PIN = 39;
	private static final int FRONT_CHASSIS_E2_PIN = 40;    
	private static final int FRONT_CHASSIS_M1_PIN = 41;
	private static final int FRONT_CHASSIS_M2_PIN = 42;
    
	//front side motors on chassi
    private static final int BACK_CHASSIS_M1_PIN = 43;
    private static final int BACK_CHASSIS_M2_PIN = 44;
    private static final int BACK_CHASSIS_E1_PIN = 45;
    private static final int BACK_CHASSIS_E2_PIN = 46;
    
    //sides
    private static final boolean TURN_LEFT = false;
    private static final boolean TURN_RIGHT = true;
    
    //buttons pressed
    private static final int PRESSED_UP = 1;
    private static final int PRESSED_DOWN = 2;
    private static final int PRESSED_LEFT = 4;
    private static final int PRESSED_RIGHT = 8;

    //fields
    private Button chassiUpButton_;
    private Button chassiDownButton_;
	private Button chassiLeftButton_;
	private Button chassiRightButton_;
	private SeekBar chassiSpeedSeekBar_;
	private ToggleButton systemEnabled;
	private Button armTurnRight;
	private Button armTurnLeft;
	private ToggleButton ledOnOff;
	private Button armSholderUp;
	private Button armSholderDown;
	private Button armElbowDown;
	private Button armElbowUp;
	private Button armWristUp;
	private Button armWristDown;
	private Button armGrasp;
	private Button armRelease;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		systemEnabled = (ToggleButton) findViewById(R.id.panicButton);
		systemEnabled.setChecked(true);
		
		chassiUpButton_ = (Button) findViewById(R.id.up);
		chassiDownButton_ = (Button) findViewById(R.id.down);
		chassiLeftButton_ = (Button) findViewById(R.id.Left);
		chassiRightButton_ = (Button) findViewById(R.id.Right);
		chassiSpeedSeekBar_ = (SeekBar) findViewById(R.id.speedSeekBar);
		chassiSpeedSeekBar_.setProgress(0);
		
		armTurnRight = (Button) findViewById(R.id.TurnRight);
		armTurnLeft = (Button) findViewById(R.id.TurnLeft);
		ledOnOff = (ToggleButton) findViewById(R.id.LedOnOff);
		armSholderUp = (Button) findViewById(R.id.SholderUp);
		armSholderDown = (Button) findViewById(R.id.SholderDown);
		armElbowUp = (Button) findViewById(R.id.Elbow_up);
		armElbowDown = (Button) findViewById(R.id.Elbow_Down);
		armWristUp = (Button) findViewById(R.id.Wrist_Up);
		armWristDown = (Button) findViewById(R.id.Wrist_Down);
		armGrasp = (Button) findViewById(R.id.arm_Grasp);
		armRelease = (Button) findViewById(R.id.arm_Release);
	}

	// holds all the components connected to the IOIO and responsible for their update
	class Looper extends BaseIOIOLooper {
		private BigMotorDriver chassiFront;
		private BigMotorDriver chassiBack;
		private SmallMotorDriver turn_and_led;
		private SmallMotorDriver sholder_and_elbow;
		private SmallMotorDriver wrist_and_grasp;
		private RoboticArmEdge arm;


		protected void setup() throws ConnectionLostException {
			chassiFront = new BigMotorDriver(ioio_, FRONT_CHASSIS_M1_PIN, FRONT_CHASSIS_E1_PIN, FRONT_CHASSIS_M2_PIN, FRONT_CHASSIS_E2_PIN);
			chassiBack = new BigMotorDriver(ioio_, BACK_CHASSIS_M1_PIN, BACK_CHASSIS_E1_PIN, BACK_CHASSIS_M2_PIN, BACK_CHASSIS_E2_PIN);

			turn_and_led = new SmallMotorDriver(ioio_, TURN_A01_PIN, TURN_A02_PIN, LED_B01_PIN, LED_B02_PIN);
			sholder_and_elbow = new SmallMotorDriver(ioio_, SHOLDER_A01_PIN, SHOLDER_A02_PIN, ELBOW_B01_PIN, ELBOW_B02_PIN);
			wrist_and_grasp = new SmallMotorDriver(ioio_, WRIST_A01_PIN, WRIST_A02_PIN, GRASP_B01_PIN, GRASP_B02_PIN);
			ioio_.openDigitalOutput(ARM_STBY, true);
			
			ioio_.openPwmOutput(ARM_PWM, 100).setDutyCycle((float)100);
		}
		
		public void loop() throws ConnectionLostException {

			//ensures that loop runs only if systemEnabled button is pressed
			if (!systemEnabled.isChecked()) {
				chassiBack.stop();
				chassiFront.stop();
				turn_and_led.stop();
				try {
					Thread.sleep(10);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}

			turn_and_led.turnMotorA(armTurnLeft.isPressed(), armTurnRight.isPressed());
			turn_and_led.turnMotorB(ledOnOff.isChecked(), false);
			sholder_and_elbow.turnMotorA(armSholderUp.isPressed(), armSholderDown.isPressed());
			sholder_and_elbow.turnMotorB(armElbowUp.isPressed(), armElbowDown.isPressed());
			wrist_and_grasp.turnMotorA(armWristUp.isPressed(), armWristDown.isPressed());
			wrist_and_grasp.turnMotorB(armRelease.isPressed(), armGrasp.isPressed());
			
			//Chassis part
			int buttonState =  (chassiUpButton_.isPressed()? 0x1:0x0) + (chassiDownButton_.isPressed()? 0x2:0x0) + (chassiLeftButton_.isPressed()? 0x4:0x0) + (chassiRightButton_.isPressed()? 0x8:0x0);
			if (buttonState == PRESSED_UP){
				chassiFront.turnBothMotors(TURN_LEFT);
				chassiBack.turnBothMotors(TURN_RIGHT);
			}
			else if((buttonState == PRESSED_DOWN)){
				chassiFront.turnBothMotors(TURN_RIGHT);
				chassiBack.turnBothMotors(TURN_LEFT);
			}
			else if(buttonState == PRESSED_LEFT){
				chassiFront.turnBothMotorsOposite(TURN_RIGHT);
				chassiBack.turnBothMotorsOposite(TURN_LEFT);
				
			}
			else if(buttonState == PRESSED_RIGHT){
				chassiFront.turnBothMotorsOposite(TURN_LEFT);
				chassiBack.turnBothMotorsOposite(TURN_RIGHT);
			}
			else{}
			
			//Chassis speed
			float newSpeed = ((buttonState & 15)!=0? 1:0) * ((float) chassiSpeedSeekBar_.getProgress()/100);
			chassiFront.setMotorA_speed(newSpeed);
			chassiBack.setMotorA_speed(newSpeed);
			chassiFront.setMotorB_speed(newSpeed);
			chassiBack.setMotorB_speed(newSpeed);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
}