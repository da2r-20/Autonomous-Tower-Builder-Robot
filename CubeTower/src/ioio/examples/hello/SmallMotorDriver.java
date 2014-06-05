package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;

import ioio.lib.api.exception.ConnectionLostException;

public class SmallMotorDriver implements MotorController, Stoppable{
	public DigitalOutput A01, A02, B01, B02;
	
	public SmallMotorDriver(IOIO ioio, int A01, int A02, int B01, int B02) throws ConnectionLostException{
		this.A01 = ioio.openDigitalOutput(A01, false);
		this.A02 = ioio.openDigitalOutput(A02, false);
		this.B01 = ioio.openDigitalOutput(B01,false);
		this.B02 = ioio.openDigitalOutput(B02,false);
	}
	
	public SmallMotorDriver(DigitalOutput A01, DigitalOutput A02, DigitalOutput B01, DigitalOutput B02) throws ConnectionLostException{
		this.A01 = A01;
		this.A02 = A02;
		this.B01 = B01;
		this.B02 = B02;
	}
	
	public void writeToA01(boolean b) throws ConnectionLostException {
		A01.write(b);
	}
	
	public void writeToA02(boolean b) throws ConnectionLostException {
		A02.write(b);
	}
	
	public void writeToB01(boolean b) throws ConnectionLostException {
		B01.write(b);
	}
	
	public void writeToB02(boolean b) throws ConnectionLostException {
		B02.write(b);
	}
	
	public void turnMotorA(boolean direction) throws ConnectionLostException{
		this.A01.write(direction);
		this.A02.write(!direction);
	}

	public void turnMotorB(boolean direction) throws ConnectionLostException{
	}
	
	public void turnMotorA(boolean l,boolean r) throws ConnectionLostException{
		this.A01.write(l);
		this.A02.write(r);
	}
	
	public void turnMotorB(boolean l,boolean r) throws ConnectionLostException{
		this.B01.write(l);
		this.B02.write(r);
	}
	
	public void close(){
		this.A01.close();
		this.A02.close();
		this.B01.close();
		this.B02.close();
	}
	
	@Override
	public String toString() {
		return "A01 - " + A01 +
			   "\nA02 - " + A02 +
			   "\nB01 - " + B01 +
			   "\nB02 - " + B02 ;
	}

	@Override
	public void stop() throws ConnectionLostException {
		this.A01.write(false);
		this.A02.write(false);
		this.B01.write(false);
		this.B02.write(false);
	}

	@Override
	public void setMotorA_speed(float speed) throws ConnectionLostException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMotorB_speed(float speed) throws ConnectionLostException {
		// TODO Auto-generated method stub
		
	}
}
