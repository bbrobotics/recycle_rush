package org.usfirst.frc.team1517.utils;

public class JoystickUtils {

	public static double scaledStick(double input)
	{
		return Math.pow(input, 3);
	}
}
