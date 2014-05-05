package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

public class BigMotorDriver implements MotorController {
	DigitalOutput m1,m2;
	PwmOutput e1,e2;
	
	public BigMotorDriver(IOIO ioio, int m1_pin, int e1_pin, int m2_pin, int e2_pin) throws ConnectionLostException {
		this.m1 = ioio.openDigitalOutput(m1_pin,false);
		this.e1 = ioio.openPwmOutput(e1_pin, 100);
		
		this.m2 = ioio.openDigitalOutput(m2_pin,false);
		this.e2 = ioio.openPwmOutput(e2_pin, 100);
		e1.setDutyCycle((float)0.0);
		e2.setDutyCycle((float)0.0);
	}
	
	public void writeTo_m1(boolean b) throws ConnectionLostException {
		m1.write(b);
	}
	
	public void writeTo_m2(boolean b) throws ConnectionLostException {
		m2.write(b);
	}
	

	@Override
	public void turnMotorA(boolean direction) throws ConnectionLostException {
		
		
	}


	@Override
	public void turnMotorB(boolean direction) throws ConnectionLostException {
		// TODO Auto-generated method stub
		
	}

	public void turnBothMotors(boolean direction) throws ConnectionLostException{
		m1.write(direction);
		m2.write(!direction);
	}
	
	public void turnBothMotorsOposite(boolean direction) throws ConnectionLostException{
		m1.write(direction);
		m2.write(direction);
	}
	
	
	@Override
	public void close() {
		this.e1.close();
		this.e2.close();
		this.m1.close();
		this.m2.close();
	}


	@Override
	public void setMotorA_speed(float speed) throws ConnectionLostException {
		e1.setDutyCycle(speed);
		
	}


	@Override
	public void setMotorB_speed(float speed) throws ConnectionLostException {
		e2.setDutyCycle(speed);
		
	}

	@Override
	public void stop() throws ConnectionLostException {
		this.setMotorA_speed(0);
		this.setMotorB_speed(0);
	}
}
