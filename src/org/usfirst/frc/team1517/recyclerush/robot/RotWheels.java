package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Talon;

/**
 * A class representing the tote and bin in-take system of the robot.
 * @author Zoraver
 *
 */
public class RotWheels {
	
	double wheelSpeed = 0.7;
	
	Talon leftWheel, rightWheel, gripper;
	
	public RotWheels() {
		leftWheel = new Talon(0);
		rightWheel = new Talon(1);
		gripper = new Talon(2);
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
		rightWheel.set(speed);
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
