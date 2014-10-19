package magical.robot.ioio;

import ioio.lib.api.exception.ConnectionLostException;
/**
 * this class handles all API functionality of the chassis movement
 * @author гешеп
 */
public class ChassisFrame  implements Stoppable{
	BigMotorDriver _front;
	BigMotorDriver _back;
    //sides
    private static final boolean TURN_LEFT = false;
    private static final boolean TURN_RIGHT = true;
    
    
    /**
     * this contractor takes two instances of a BigMotorDriver and assignes them to the fields 
     * @param front the front BigMotorDriver instance
     * @param back the back BigMotorDriver instance
     */
	public ChassisFrame(BigMotorDriver front, BigMotorDriver back) {
		_front = front;
		_back = back;
	}
	
	/**
	 * making the robot start driving forwards
	 * @throws ConnectionLostException
	 */
	public void driveForward() throws ConnectionLostException{
		_front.turnBothMotors(TURN_LEFT);
		_back.turnBothMotors(TURN_RIGHT);
	}

	/**
	 * making the robot start driving backwards
	 * @throws ConnectionLostException
	 */
	public void driveBackwards() throws ConnectionLostException{
		_front.turnBothMotors(TURN_RIGHT);
		_back.turnBothMotors(TURN_LEFT);
	}
	
	/**
	 * making the robot start turning left
	 * @throws ConnectionLostException
	 */
	public void turnLeft() throws ConnectionLostException{
		_front.turnBothMotorsOposite(TURN_RIGHT);
		_back.turnBothMotorsOposite(TURN_LEFT);
	}
	
	/**
	 * making the robot start turning right
	 * @throws ConnectionLostException
	 */
	public void turnRight() throws ConnectionLostException{
		_front.turnBothMotorsOposite(TURN_LEFT);
		_back.turnBothMotorsOposite(TURN_RIGHT);
	}
	
	/**
	 * setting the rover speed
	 * @param speed the speed to be assigned
	 * @throws ConnectionLostException
	 */
	public void setSpeed(float speed) throws ConnectionLostException{
		_front.setMotorA_speed(speed);
		_back.setMotorA_speed(speed);
		_front.setMotorB_speed(speed);
		_back.setMotorB_speed(speed);
	}
	
	@Override
	public void stop() throws ConnectionLostException {
		_front.stop();
		_back.stop();
	}
	
	/**
	 * closes the digital connections
	 */
	public void close(){
		_back.close();
		_front.close();
	}
	
}
