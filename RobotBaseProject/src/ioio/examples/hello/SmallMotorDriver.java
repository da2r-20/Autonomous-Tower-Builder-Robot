package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;

import ioio.lib.api.exception.ConnectionLostException;

/**
 * this class handles API for the TB6612FNG Motor controller
 * controlling the robot's robotic arm
 * notice that every motor driver should also receive a standby and two pwm signals 
 * this is made through the RoboticArmEdge class
 * @author Doron
 */

public class SmallMotorDriver implements  Stoppable{
	private DigitalOutput A01, A02, B01, B02;
	
	/**
	 * this constructor takes the assigned pins numbers and opens the the IOIO pins for writing
	 * @param ioio the IOIO occurrence received from the IOIO activity
	 * @param A01 pin number
	 * @param A02 pin number
	 * @param B01 pin number
	 * @param B02 pin number
	 * @throws ConnectionLostException
	 */
	public SmallMotorDriver(IOIO ioio, int A01, int A02, int B01, int B02) throws ConnectionLostException{
		this.A01 = ioio.openDigitalOutput(A01, false);
		this.A02 = ioio.openDigitalOutput(A02, false);
		this.B01 = ioio.openDigitalOutput(B01,false);
		this.B02 = ioio.openDigitalOutput(B02,false);
	}
	
	/**
	 * this constructor takes an already opened pin instances and assign them to the fields
	 * @param A01 digital output
	 * @param A02 digital output
	 * @param B01 digital output
	 * @param B02 digital output
	 * @throws ConnectionLostException
	 */
	public SmallMotorDriver(DigitalOutput A01, DigitalOutput A02, DigitalOutput B01, DigitalOutput B02) throws ConnectionLostException{
		this.A01 = A01;
		this.A02 = A02;
		this.B01 = B01;
		this.B02 = B02;
	}
	
	/**
	 * writes to pin A01
	 * @param b the value to be written
	 * @throws ConnectionLostException
	 */
	public void writeToA01(boolean b) throws ConnectionLostException {
		A01.write(b);
	}
	
	/**
	 * writes to pin A02
	 * @param b the value to be written
	 * @throws ConnectionLostException
	 */
	public void writeToA02(boolean b) throws ConnectionLostException {
		A02.write(b);
	}
	
	/**
	 * writes to pin B01
	 * @param b the value to be written
	 * @throws ConnectionLostException
	 */
	public void writeToB01(boolean b) throws ConnectionLostException {
		B01.write(b);
	}
	
	/**
	 * writes to pin B02
	 * @param b the value to be written
	 * @throws ConnectionLostException
	 */
	public void writeToB02(boolean b) throws ConnectionLostException {
		B02.write(b);
	}
	
	/**
	 * turn motor a in direction
	 * @param l the signal to write to the A01 pin
	 * @param r the signal to write to the A02 pin
	 * @throws ConnectionLostException
	 */
	public void turnMotorA(boolean l,boolean r) throws ConnectionLostException{
		this.A01.write(l);
		this.A02.write(r);
	}
	
	/**
	 * turn motor a in direction
	 * @param l the signal to write to the B01 pin
	 * @param r the signal to write to the B02 pin
	 * @throws ConnectionLostException
	 */
	public void turnMotorB(boolean l,boolean r) throws ConnectionLostException{
		this.B01.write(l);
		this.B02.write(r);
	}
	
	/**
	 * closes all digital outputs
	 */
	public void close(){
		this.A01.close();
		this.A02.close();
		this.B01.close();
		this.B02.close();
	}
	
	@Override
	public void stop() throws ConnectionLostException {
		this.A01.write(false);
		this.A02.write(false);
		this.B01.write(false);
		this.B02.write(false);
	}
}
