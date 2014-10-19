package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * this class handles API for the DFROBOT DRI10002 Motor controller
 * controlling the robot's robotic arm
 *
 * @author Doron
 */

public class BigMotorDriver implements Stoppable{
	private DigitalOutput m1,m2;
	private PwmOutput e1,e2;
	

	/**
	 * this constructor takes the assigned pins numbers and opens the the IOIO pins for writing
	 * @param ioio the IOIO occurrence received from the IOIO activity
	 * @param m1_pin motor 1 direction control signal pin number  
	 * @param e1_pin motor 1 speed control signal
	 * @param m2_pin motor 2 direction control signal pin number  
	 * @param e2_pin motor 2 speed control signal
	 * @throws ConnectionLostException
	 */
	public BigMotorDriver(IOIO ioio, int m1_pin, int e1_pin, int m2_pin, int e2_pin) throws ConnectionLostException {
		this.m1 = ioio.openDigitalOutput(m1_pin,false);
		this.e1 = ioio.openPwmOutput(e1_pin, 100);
		this.m2 = ioio.openDigitalOutput(m2_pin,false);
		this.e2 = ioio.openPwmOutput(e2_pin, 100);
		
		e1.setDutyCycle((float)0.0);
		e2.setDutyCycle((float)0.0);
	}
	
	/**
	 * this method simply writes a given boolean value to the direction digital signal
	 * @param b the boolean value to be written
	 * @throws ConnectionLostException
	 */
	public void writeTo_m1(boolean b) throws ConnectionLostException {
		m1.write(b);
	}
	
	/**
	 * this method simply writes a given boolean value to the direction digital signal
	 * @param b the boolean value to be written
	 * @throws ConnectionLostException
	 */	
	public void writeTo_m2(boolean b) throws ConnectionLostException {
		m2.write(b);
	}
	
	/**
	 * this method simply writes a given boolean value to both motors direction digital signal
	 * @param direction the boolean value to be written
	 * @throws ConnectionLostException
	 */
	public void turnBothMotors(boolean direction) throws ConnectionLostException{
		m1.write(direction);
		m2.write(!direction);
	}
	
	/**
	 * this method simply writes a given boolean value to both motors direction digital signal
	 * @param direction the boolean value to be written
	 * @throws ConnectionLostException
	 */
	public void turnBothMotorsOposite(boolean direction) throws ConnectionLostException{
		m1.write(direction);
		m2.write(direction);
	}
	
	
	/**
	 * closes all opened connections
	 */
	public void close() {
		this.e1.close();
		this.e2.close();
		this.m1.close();
		this.m2.close();
	}


	/**
	 * writes a new speed to the motor's speed control output
	 * @param speed the new speed to be updated
	 * @throws ConnectionLostException
	 */
	public void setMotorA_speed(float speed) throws ConnectionLostException {
		e1.setDutyCycle(speed);
		
	}

	/**
	 * writes a new speed to the motor's speed control output
	 * @param speed the new speed to be updated
	 * @throws ConnectionLostException
	 */
	public void setMotorB_speed(float speed) throws ConnectionLostException {
		e2.setDutyCycle(speed);
		
	}

	@Override
	public void stop() throws ConnectionLostException {
		this.setMotorA_speed(0);
		this.setMotorB_speed(0);
	}
}
