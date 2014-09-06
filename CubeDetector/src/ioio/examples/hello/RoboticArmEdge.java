package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * this class handles all the robotic arm API functionality 
 * @author Doron
 */
public class RoboticArmEdge implements Stoppable{
	private SmallMotorDriver _wrist_and_grasp;
	private SmallMotorDriver _sholder_and_elbow;
	private SmallMotorDriver _turn_and_led;
	private DigitalOutput _stby;
	private PwmOutput _pwm;

	/**
	 * this constructor takes the instances of the SmallMotorDriver controlling the arms wrist, grasp, sholder, elbow, arm turn and led.
	 * @param ioio a IOIO instance provided by the IOIO activity class
	 * @param wrist_and_grasp this is the instance controlling the wrist and the grasp
	 * @param sholder_and_elbow this is the instance controlling the sholder and elbow
	 * @param turn_and_led this is the instance controlling the turn and led
	 * @param stby standby pin number
	 * @param pwm pwm pin number for controlling arm joints speed
	 */
	public RoboticArmEdge(IOIO ioio,SmallMotorDriver wrist_and_grasp, SmallMotorDriver sholder_and_elbow, SmallMotorDriver turn_and_led, int stby, int pwm) {
		this._sholder_and_elbow = sholder_and_elbow;
		this._wrist_and_grasp = wrist_and_grasp;
		this._turn_and_led = turn_and_led;
		try {
			_stby = ioio.openDigitalOutput(stby,true);
			_pwm  = ioio.openPwmOutput(pwm, 100);
			_pwm.setDutyCycle((float)100);
			
		} catch (ConnectionLostException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * turning the shoulder in a certain direction
	 * @param a0 
	 * @param a1
	 * @throws ConnectionLostException
	 */
	public void turnSholder(boolean a0,boolean a1) throws ConnectionLostException{
		_sholder_and_elbow.turnMotorA(a0,a1);
	}

	/**
	 * turning the Elbow in a certain direction
	 * @param a0 
	 * @param a1
	 * @throws ConnectionLostException
	 */
	public void turnElbow(boolean a0,boolean a1) throws ConnectionLostException{
		_sholder_and_elbow.turnMotorB(a0,a1);
	}
	
	
	/**
	 * turning the Wrist in a certain direction
	 * @param a0 
	 * @param a1
	 * @throws ConnectionLostException
	 */
	private void turnWrist(boolean a0,boolean a1) throws ConnectionLostException{
		_wrist_and_grasp.turnMotorA(a0,a1);
	}
	
	/**
	 * turning the Grasp in a certain direction
	 * @param a0 
	 * @param a1
	 * @throws ConnectionLostException
	 */
	private void turnGrasp(boolean a0,boolean a1) throws ConnectionLostException{
		_wrist_and_grasp.turnMotorB(a0,a1);
	}
	
	
	/**
	 * turning the Arm turning in a certain direction
	 * @param a0 
	 * @param a1
	 * @throws ConnectionLostException
	 */
	private void turnTurning(boolean a0,boolean a1) throws ConnectionLostException{
		_turn_and_led.turnMotorA(a0,a1);
	}

	
	/**
	 * turn the arm Left
	 * @throws ConnectionLostException
	 */
	public void TurnLeft() throws ConnectionLostException{
		this.turnTurning(true, false);
	}
	
	/**
	 * turn the arm right
	 * @throws ConnectionLostException
	 */
	public void TurnRight() throws ConnectionLostException{
		this.turnTurning(false, true);
	}

	/**
	 * move shoulder up
	 * @throws ConnectionLostException
	 */
	public void sholderUp() throws ConnectionLostException{
		this.turnSholder(true, false);
	}

	
	/**
	 * move shoulder down
	 * @throws ConnectionLostException
	 */
	public void sholderDown() throws ConnectionLostException{
		this.turnSholder(false, true);
	}

	/**
	 * move elbow up
	 * @throws ConnectionLostException
	 */
	public void elbowUp() throws ConnectionLostException{
		this.turnElbow(true, false);
	}
	/**
	 * move elbow down
	 * @throws ConnectionLostException
	 */
	public void elbowDown() throws ConnectionLostException{
		this.turnElbow(false, true);
	}

	/**
	 * move wrist up
	 * @throws ConnectionLostException
	 */
	public void wristUp() throws ConnectionLostException{
		this.turnWrist(true, false);
	}

	/**
	 * move wrist down
	 * @throws ConnectionLostException
	 */
	public void wristDown() throws ConnectionLostException{
		this.turnWrist(false, true);
	}

	/**
	 * closes the arm's hand
	 * @throws ConnectionLostException
	 */
	public void openHand() throws ConnectionLostException{
		this.turnGrasp(false, true);
	}

	/**
	 * opens the arm's hand
	 * @throws ConnectionLostException
	 */
	public void closeHand() throws ConnectionLostException{
		this.turnGrasp(true, false);
	}
	
	/**
	 * turning the arm's led on
	 * @throws ConnectionLostException
	 */
	public void turnOnLed() throws ConnectionLostException{
		_turn_and_led.turnMotorB(true, false);
	}

	/**
	 * turning the arm's led off
	 * @throws ConnectionLostException
	 */
	public void turnOffLed() throws ConnectionLostException{
		_turn_and_led.turnMotorB(false, false);
	}

	@Override
	public void stop() throws ConnectionLostException{
		_turn_and_led.stop();
		_wrist_and_grasp.stop();
		_sholder_and_elbow.stop();
	}
	
	/**
	 * closes all pins
	 */
	public void close(){
		_sholder_and_elbow.close();
		_turn_and_led.close();
		_wrist_and_grasp.close();
		_pwm.close();
		_stby.close();
	}
}
