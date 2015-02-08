package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Talon;

/**
 * A class representing the tote and bin in-take system of the robot.
 * @author Zoraver
 *
 */
public class RotWheels {
	
	double wheelSpeed = 0.7;
	
	Talon leftWheel, rightWheel;
	
	public RotWheels() {
		leftWheel = new Talon(2);
		rightWheel = new Talon(3);
	}
	
	public void pullIn() {
		leftWheel.set(wheelSpeed);
		rightWheel.set(wheelSpeed);
	}
	
	public void pushOut() {
		leftWheel.set(-1 * wheelSpeed);
		rightWheel .set(-1 * wheelSpeed);
	}
	
	public void stopWheels() {
		leftWheel.set(0);
		rightWheel.set(0);
	}
}
