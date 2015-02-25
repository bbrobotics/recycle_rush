package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Talon;

public class Rollers {

	Talon rollerMotor;
	
	public Rollers()
	{
		rollerMotor = new Talon(1);
	}
	
	public void pullIn()
	{
		rollerMotor.set(-1);
	}
	
	public void pushOut()
	{
		rollerMotor.set(1);
	}
	
	public void stop()
	{
		rollerMotor.set(0);
	}
	
}
