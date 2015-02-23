package org.usfirst.frc.team1517.utils;

public class JoystickUtils {

	static final double zeroThreshold = 0.1;
	
	public static double scaledStick(double input)
	{
		if(Math.abs(input) < zeroThreshold) return 0;
		else return Math.pow(input, 3);
	}
	
}
