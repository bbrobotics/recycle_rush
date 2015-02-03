package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

import org.usfirst.frc.team1517.io.XboxController;
import org.usfirst.frc.team1517.mecanum.MecanumDriveGeneral;
import org.usfirst.frc.team1517.utils.JoystickUtils;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    
	CANTalon aF, aB, bF, bB;
	
	BuiltInAccelerometer bIAccelerometer;
	
	MecanumDriveGeneral drive;
	
	XboxController controller;
	
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	aF = new CANTalon(1);
    	aB = new CANTalon(2);
    	bF = new CANTalon(3);
    	bB = new CANTalon(4);
    	
    	bIAccelerometer = new BuiltInAccelerometer();
    	
    	drive = new MecanumDriveGeneral(aF, aB, bF, bB);
    	
    	controller = new XboxController(0); //The controllers are now zero indexed.
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	if(controller.getRightTriggerButton()){
    		drive.drive(0.5 * JoystickUtils.scaledStick(controller.getAnalogTriggers()), 
            		0.5 * JoystickUtils.scaledStick(controller.getLeftJoystickY()) + 0.000000001, //Such a hack.
            		0.5 * JoystickUtils.scaledStick(controller.getLeftJoystickX()));
    	}
    	else
    	{
    		drive.drive(JoystickUtils.scaledStick(controller.getAnalogTriggers()), 
            		JoystickUtils.scaledStick(controller.getLeftJoystickY()) + 0.000000001, //Such a hack.
            		JoystickUtils.scaledStick(controller.getLeftJoystickX()));
    	}
        
        System.out.println("Accel x: " + String.valueOf(bIAccelerometer.getX()) 
        					+ ", y: " + String.valueOf(bIAccelerometer.getY()) 
        					+ ", z: " + String.valueOf(bIAccelerometer.getZ()));
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
