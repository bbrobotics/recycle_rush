package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Indexer {

	final double liftSpeed = 1;
	final int indexPosition = 1800;
	final int coopPosition = 1000;
	final int indexTolerance = 10;
	final double distancePerPulse = 0.7;
	final double timeoutPerPulse = 0.0017;
	
	boolean indexing = false;
	boolean calibrating = false;
	boolean moving = false;
	
	//Victor leftLift, rightLift;
	CANTalon leftLift, rightLift;
	DigitalInput leftCali, rightCali;
	Encoder leftEnc, rightEnc;
	
	public Indexer()
	{
		//leftLift = new Victor(6);
		leftLift = new CANTalon(5);
		//rightLift = new Victor(9);
		rightLift = new CANTalon(6);
		
		leftLift.enableBrakeMode(true);
		rightLift.enableBrakeMode(true);
		
		leftCali = new DigitalInput(9);
		rightCali = new DigitalInput(8);
		
		leftEnc = new Encoder(4, 5);//Each Encoder requires two digital inputs, as they are quadrature.
		rightEnc = new Encoder(7, 6);//One input for channel a and one for channel b.
		
		leftEnc.setDistancePerPulse(distancePerPulse);//placeholder
		rightEnc.setDistancePerPulse(distancePerPulse);
	}
	
	/**
	 * Uses the limit switches to set the encoders to a known zero position.
	 * This should be run at the beginning of use of this class.
	 */
	public void calibrate()
	{
		System.out.println("Entering calibrate");
		
		leftLift.set(-1 * liftSpeed);
		rightLift.set(-1 * liftSpeed);
		
		while(!leftCali.get() || !rightCali.get())
		{
			if(leftCali.get())
			{
				leftLift.set(0);
			}
			
			if(rightCali.get())
			{
				rightLift.set(0);
			}
		}
		
		leftLift.set(0);//Just to be safe.
		rightLift.set(0);
		
		leftEnc.reset();
		rightEnc.reset();
		
		System.out.println("Exiting calibrate");
	}
	
	public boolean calibrateThreaded()
	{
		if(!calibrating)
		{
			calibrating = true;
			new Thread(new Runnable(){
				public void run()
				{
					System.out.println("Runnable starting");
					calibrate();
					calibrating = false;
				}
			}).start();
			return true;
		}
		else return false;
	}
	
	public void goToTicks(int ticks, int tolerance)
	{
		moving = true;
		int start = leftEnc.get();
		if(Math.abs(leftEnc.get() - ticks) > Math.abs(tolerance) || Math.abs(rightEnc.get() - ticks) > Math.abs(tolerance))
		{
			Timer timeOut = new Timer();
			
			if(leftEnc.get() < ticks && rightEnc.get() < ticks)//If below desired position...
			{
				leftLift.set(liftSpeed);
				rightLift.set(0.875 * liftSpeed);
			}
			else if(leftEnc.get() > ticks && rightEnc.get() > ticks)//If above desired position...
			{
				leftLift.set(-1 * liftSpeed);
				rightLift.set(-0.875 * liftSpeed);
			}
			
			timeOut.start();
			
			while((leftLift.get() != 0 || rightLift.get() != 0) && getTimeoutForTicks(Math.abs(start - ticks)) > timeOut.get())
			{
				if(Math.abs(leftEnc.get() - ticks) <= Math.abs(tolerance))
				{
					leftLift.set(0);
				}
				
				if(Math.abs(rightEnc.get() - ticks) <= Math.abs(tolerance))
				{
					rightLift.set(0);
				}
			}
			
			leftLift.set(0);
			rightLift.set(0);
			
			timeOut.stop();
		}
		moving = false;
		System.out.println("Indexer position within tolerance.");
	}
	
	public void goToPosition(double position, double tolerance)
	{	
		if(Math.abs(leftEnc.getDistance() - position) > Math.abs(tolerance) && Math.abs(rightEnc.getDistance() - position) > Math.abs(tolerance))
		{
			if(leftEnc.getDistance() < position && rightEnc.getDistance() < position)//If below desired position...
			{
				leftLift.set(liftSpeed);
				rightLift.set(liftSpeed);
			}
			else if(leftEnc.getDistance() > position && rightEnc.getDistance() > position)//If above desired position...
			{
				leftLift.set(-1 * liftSpeed);
				rightLift.set(-1 * liftSpeed);
			}
			
			while(Math.abs(leftEnc.getDistance() - position) > Math.abs(tolerance) && Math.abs(rightEnc.getDistance() - position) > Math.abs(tolerance))
			{
				if(Math.abs(leftEnc.getDistance() - position) <= Math.abs(tolerance))
				{
					leftLift.set(0);
				}
				
				if(Math.abs(rightEnc.getDistance() - position) <= Math.abs(tolerance))
				{
					rightLift.set(0);
				}
			}
			
			leftLift.set(0);
			rightLift.set(0);		
		}
		System.out.println("Indexer position within tolerance.");
	}
	
	public void moveToHigh()
	{
		
	}
	
	public void index()
	{
		indexing = true;
		calibrate();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//goToPosition(indexPosition, indexTolerance);
		goToTicks(indexPosition, indexTolerance);
		indexing = false;
	}
	
	public boolean indexThreaded()
	{
		if(!indexing)
		{
			new Thread(new Runnable(){
				public void run()
				{
					index();
				}
			}).start();
			return true;
		}
		else return false;
	}
	
	public boolean isIndexing()
	{
		return indexing;
	}
	
	public boolean goToTicksThreaded(final int ticks)
	{
		if(!moving)
		{
			new Thread(new Runnable(){
				public void run()
				{
					goToTicks(ticks, indexTolerance);
				}
			}).start();
			return true;
		}
		else return false;
	}
	
	public boolean manualControl(double speedL, double speedR)
	{
		if(!indexing)
		{
			leftLift.set(speedL);
			rightLift.set(speedR * 0.875);
			return true;
		}
		else return false;
	}
	
	public boolean getLeftCali()
	{
		return leftCali.get();
	}
	
	public boolean getRightCali()
	{
		return rightCali.get();
	}
	
	public int getLeftEnc()
	{
		return leftEnc.get();
	}
	
	public int getRightEnc()
	{
		return rightEnc.get();
	}
	
	private double getTimeoutForTicks(int ticks)
	{
		return timeoutPerPulse * ticks;
	}
	
	private double getTimeoutForDistance(double distance)
	{
		return timeoutPerPulse * distance / distancePerPulse;
	}
}
