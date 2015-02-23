package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team1517.io.XboxController;
import org.usfirst.frc.team1517.mecanum.MecanumDriveProportionalGyro;
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
	Gyro dGyro;
	Encoder dEncoderLeft, dEncoderRight;
	
	MecanumDriveProportionalGyro drive;
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
    	
    	aF.enableBrakeMode(false);
    	aB.enableBrakeMode(false);
    	bF.enableBrakeMode(false);
    	bB.enableBrakeMode(false);
    	
    	bIAccelerometer = new BuiltInAccelerometer();
    	
    	dEncoderLeft = new Encoder(0, 1);
    	dEncoderRight = new Encoder(2, 3);
    	
    	
    	drive = new MecanumDriveProportionalGyro(aF, aB, bF, bB, 0.02);
    	
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
    	
    	//aF.setPosition(0);
    	
    	switch(autoMode)
    	{
    		case(BASIC_AUTO):
    			Timer failSafe = new Timer();
    			dEncoderRight.reset();
    			
    			failSafe.start();
    			
    			while(dEncoderRight.get() < 215 && failSafe.get() < 3.5)
    			{
    				drive.drive(0, -0.5, 0);
    			}
    			drive.drive(0, 0, 0);
    			failSafe.stop();
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

    public void teleopInit()
    {
    	aF.enableBrakeMode(false);
    	aB.enableBrakeMode(false);
    	bF.enableBrakeMode(false);
    	bB.enableBrakeMode(false);
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	if(controller.getRightTriggerButton() && JoystickUtils.scaledStick(controller.getLeftJoystickX()) == 0 && JoystickUtils.scaledStick(controller.getLeftJoystickY()) == 0)
		{
    		drive.slide(0.5 * JoystickUtils.scaledStick(controller.getAnalogTriggers()));
		}
    	else if (JoystickUtils.scaledStick(controller.getLeftJoystickX()) == 0 && JoystickUtils.scaledStick(controller.getLeftJoystickY()) == 0)
		{
    		drive.slide(JoystickUtils.scaledStick(controller.getAnalogTriggers()));
		}
    	else if(controller.getRightTriggerButton()){
    		drive.drive(0.5 * JoystickUtils.scaledStick(controller.getAnalogTriggers()),
    				0.5 * JoystickUtils.scaledStick(controller.getLeftJoystickY()) + 0.000000001, //Such a hack.
    				0.5 * JoystickUtils.scaledStick(controller.getLeftJoystickX()));
    		drive.gyro.reset();
    	}
    	else
    	{
    		drive.drive(JoystickUtils.scaledStick(controller.getAnalogTriggers()),
    				JoystickUtils.scaledStick(controller.getLeftJoystickY()) + 0.000000001, //Such a hack.
    				JoystickUtils.scaledStick(controller.getLeftJoystickX()));
    		drive.gyro.reset();
    	}
    	
    	
       //System.out.println("Accel x: " + String.valueOf(bIAccelerometer.getX()) 
       // 					+ ", y: " + String.valueOf(bIAccelerometer.getY()) 
       // 					+ ", z: " + String.valueOf(bIAccelerometer.getZ()));
    	
       //System.out.println("Left pos: " + indexer.getLeftEnc() + " right pos: " + indexer.getRightEnc());
       //System.out.println("Left switch: " + indexer.leftCali.get() + " right switch: " + indexer.rightCali.get());
       //System.out.println("Left pos: " + dEncoderLeft.get() + " right pos: " + dEncoderRight.get());
    	
       if(jdController.getRawButton(1) && !indexer.calibrating)
       {
    	   indexer.indexThreaded();
       }
       else if(jdController.getRawButton(2) && !indexer.isIndexing())
       {
    	   //indexer.indexThreaded();
    	   indexer.calibrateThreaded();
       }
       else if(jdController.getRawButton(3) && !indexer.isIndexing() && !indexer.calibrating)
       {
    	   indexer.manualControl(1, 1);
       }
       else if(jdController.getRawButton(5) && !indexer.isIndexing() && !indexer.calibrating && !indexer.getLeftCali() && !indexer.getRightCali())
       {
    	   indexer.manualControl(-1, -1);
       }
       else if(!indexer.calibrating && !indexer.indexing)
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
