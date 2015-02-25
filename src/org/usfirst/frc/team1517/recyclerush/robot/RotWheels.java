package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

/**
 * A class representing the tote and bin in-take system of the robot.
 * @author Zoraver
 *
 */
public class RotWheels {
	
	double wheelSpeed = -0.7;
	
	Victor gripper;
	Talon leftWheel, rightWheel;
	
	public RotWheels() {
		//leftWheel = new Victor(8);
		gripper = new Victor(2);
		leftWheel = new Talon(0);
		//rightWheel = new Talon(1);
		//gripper = new Talon(2);
	}
	
	public void pullIn() {
		setWheels(wheelSpeed);
	}
	
	public void pushOut() {
		setWheels(-1* wheelSpeed);
	}
	
	public void stopWheels() {
		setWheels(0);
	}
	
	public void setWheels(double speed)
	{
		leftWheel.set(speed);
		//rightWheel.set(speed);
	}
	
	public void open()
	{
		//wip
	}
	
	public void close()
	{
		//wip
	}
	
	public void setArms(double speed)
	{
		gripper.set(speed);
	}
}
