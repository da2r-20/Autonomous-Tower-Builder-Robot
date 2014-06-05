package ioio.examples.hello;

import ioio.lib.api.exception.ConnectionLostException;

public interface MotorController {

	public void turnMotorA(boolean direction) throws ConnectionLostException;
	public void turnMotorB(boolean direction) throws ConnectionLostException;
	public void setMotorA_speed(float speed) throws ConnectionLostException;
	public void setMotorB_speed(float speed) throws ConnectionLostException;
	public void stop() throws ConnectionLostException;
	public void close();
}
