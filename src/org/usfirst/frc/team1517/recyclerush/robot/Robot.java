package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Joystick;

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
	Indexer indexer;
	RotWheels rWheels;
	
	XboxController controller;
	Joystick jdController;
	
	final int BASIC_AUTO = 8999;
	final int INDEXER_AUTO = 9000;
	final int LANDFILL_AUTO = 9001;
	
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
    	
    	indexer = new Indexer();
    	
    	rWheels = new RotWheels();
    	
    	controller = new XboxController(0); //The controllers are now zero indexed.
    	jdController = new Joystick(1);
    	
    }
    
    /**
     * This function is called when the autonomous period begins.
     */
    public void autonomousInit() {
    	int autoMode = BASIC_AUTO;
    	
    	aF.setPosition(0);
    	
    	switch(autoMode)
    	{
    		case(BASIC_AUTO):
    			drive.drive(0, -1, 0);
    			while(aF.getEncPosition() < 637)
    			{
    				
    			}
    			drive.drive(0, 0, 0);
    			break;
    		
    		case(INDEXER_AUTO):
    			int numberOfTotes = 1; //replace with toggle system
    			
    			rWheels.pullIn();
    			
    			drive.drive(0, -0.7, 0);
    			
    			//indexing has to be added.
    			
    			break;
    	}
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
        
       //System.out.println("Accel x: " + String.valueOf(bIAccelerometer.getX()) 
       // 					+ ", y: " + String.valueOf(bIAccelerometer.getY()) 
       // 					+ ", z: " + String.valueOf(bIAccelerometer.getZ()));
        
       if(jdController.getRawButton(2))
       {
    	   //indexer.indexThreaded();
    	   indexer.calibrateThreaded();
       }
       else if(jdController.getRawButton(3) && !indexer.isIndexing())
       {
    	   indexer.manualControl(1, 1);
       }
       else if(jdController.getRawButton(5) && !indexer.isIndexing())
       {
    	   indexer.manualControl(-1, -1);
       }
       else if(!indexer.calibrating)
       {
    	   indexer.manualControl(0, 0);
       }
       
       if(jdController.getRawButton(4))
       {
    	   rWheels.pullIn();
       }
       else if(jdController.getRawButton(6))
       {
    	   rWheels.pushOut();
       }
       else rWheels.stopWheels();
       
       rWheels.setArms(jdController.getY());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	 
    }
    
}
