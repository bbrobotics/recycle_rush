package org.usfirst.frc.team1517.recyclerush.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Indexer {

	final double liftSpeed = 0.7; //Placeholder speed.  Will be tweaked with testing.
	final double indexPosition = 15; //Placeholders.  Will be tweaked with testing.
	final double indexTolerance = 1;
	final double distancePerPulse = 0.7;
	final double timeoutPerPulse = 0.7;
	
	boolean indexing;
	
	Talon leftLift, rightLift;
	DigitalInput leftCali, rightCali;
	Encoder leftEnc, rightEnc;
	
	public Indexer()
	{
		leftLift = new Talon(0);//Port numbers are placeholders.
		rightLift = new Talon(1);
		
		leftCali = new DigitalInput(0);
		rightCali = new DigitalInput(1);
		
		leftEnc = new Encoder(2, 3);//Each Encoder requires two digital inputs, as they are quadrature.
		rightEnc = new Encoder(4, 5);//One input for channel a and one for channel b.
		
		leftEnc.setDistancePerPulse(distancePerPulse);//placeholder
		rightEnc.setDistancePerPulse(distancePerPulse);
	}
	
	/**
	 * Uses the limit switches to set the encoders to a known zero position.
	 * This should be run at the beginning of use of this class.
	 */
	public void calibrate()
	{
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
	}
	
	public void goToTicks(int ticks, int tolerance)
	{
		if(Math.abs(leftEnc.get() - ticks) > Math.abs(tolerance) && Math.abs(rightEnc.get() - ticks) > Math.abs(tolerance))
		{
			if(leftEnc.get() < ticks && rightEnc.get() < ticks)//If below desired position...
			{
				leftLift.set(liftSpeed);
				rightLift.set(liftSpeed);
			}
			else if(leftEnc.get() > ticks && rightEnc.get() > ticks)//If above desired position...
			{
				leftLift.set(-1 * liftSpeed);
				rightLift.set(-1 * liftSpeed);
			}
			
			while(Math.abs(leftEnc.get() - ticks) > Math.abs(tolerance) && Math.abs(rightEnc.get() - ticks) > Math.abs(tolerance))
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
		}
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
	
	public void index()
	{
		indexing = true;
		calibrate();
		goToPosition(indexPosition, indexTolerance);
		indexing = false;
	}
	
	public boolean indexThreaded()
	{
		if(!indexing)
		{
			new Thread(new Runnable{
				public void run()
				{
					index();
				}
			}).start();
		}
		else return false;
	}
	
	private double getTimeout(double distance)
	{
		return timeoutPerPulse * distance / distancePerPulse;
	}
}
