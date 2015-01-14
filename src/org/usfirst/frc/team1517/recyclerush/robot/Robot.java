package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANJaguar;

import org.usfirst.frc.team1517.io.XboxController;
import org.usfirst.frc.team1517.mecanum.MecanumDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    
	CANJaguar aF, aB, bF, bB;
	
	MecanumDrive drive;
	
	XboxController controller;
	
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	aF = new CANJaguar(1);
    	aB = new CANJaguar(2);
    	bF = new CANJaguar(3);
    	bB = new CANJaguar(4);
    	
    	drive = new MecanumDrive(aF, aB, bF, bB);
    	
    	controller = new XboxController(1);
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
        drive.drive(controller.getAnalogTriggers(), controller.getLeftJoystickY(), controller.getLeftJoystickX());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
