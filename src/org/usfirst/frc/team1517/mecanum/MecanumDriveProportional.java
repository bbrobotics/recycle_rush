package org.usfirst.frc.team1517.mecanum;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class MecanumDriveProportional {

	double p, correction, xAccel, yAccel;
	
	SpeedController aF, aB, bF, bB;
	
	BuiltInAccelerometer bIAccel;
    
    public MecanumDriveProportional(SpeedController aFront, SpeedController aBack, SpeedController bFront, SpeedController bBack, 
    		double proportionalityConstant)
    {
        aF = aFront;
        aB = aBack;
        bF = bFront;
        bB = bBack;
        
        p = proportionalityConstant;
        
        bIAccel = new BuiltInAccelerometer();
    }
    
    public boolean drive(double mX, double mY, double twist)
    {   
        double x, y, magnitude, theta, highestValue, A, B, realTheta;
           
            x = mX;
            y = mY;
            
            magnitude = Math.sqrt(x * x + y * y);//Calculates the magnitude of the output vector.
            theta = Math.atan(y / x);//Calculates the direction of the output vector.
            
            if(x < 0)//Corrects the quadrent of theta for the value of X.
            {
                theta = theta + Math.PI;
            }
            
            realTheta = getAccelerationDirection();
            
            correction = p * (theta - realTheta);
            
            /*
             * The phase constants do not adjust for the interesting orientation of the x-axis of the joysticks.
             */
            A = Math.sqrt(2) * Math.cos(theta - Math.PI / 4);//Sets diagonal A to the value for theta of the mechanum equation.
            B = Math.sqrt(2) * Math.sin(theta - Math.PI / 4);//Sets diagonal B to the value for theta of the mechanum equation.
            
            if(A > 1)//Scales A to 1 if it is higher than one.
            {
                A = 1;
            }
            else if(A < -1)//Scales A to -1 if it is less than one. 
            {
                A = -1;
            }
            
            if(B > 1)//Scales B to 1 if it is higher than one. 
            {
                B = 1;
            }
            else if(B < -1)//Scales B to -1 if it is less than one. 
            {
                B = -1;
            }
            
            A = A * magnitude;//Scales A and B to their actual values.
            B = B * magnitude;
            
            //Scales the outputs by the value of the highest output, if it ls higher than 1.
            if(Math.abs(A + twist + correction) > 1 || Math.abs(A - twist - correction) > 1 || Math.abs(B + twist - correction) > 1 || Math.abs(B - twist + correction) > 1)
            {
                highestValue = Math.abs(A + twist + correction);
                
                if(Math.abs(A - twist - correction) > highestValue)
                {
                    highestValue = Math.abs(A - twist - correction);
                }
                
                if(Math.abs(B + twist - correction) > highestValue)
                {
                    highestValue = Math.abs(B + twist - correction);
                }
                
                if(Math.abs(B - twist + correction) > highestValue)
                {
                    highestValue = Math.abs(B - twist + correction);
                }
            }
            else
            {
                highestValue = 1.0;
            }
            
            aF.set((A + twist + correction) / highestValue); 
            aB.set((A - twist - correction) / highestValue); 
            bF.set((B - twist + correction) / highestValue); 
            bB.set((B + twist - correction) / highestValue); 
            
        return true;//Returns true if successful.
    }
    
    double getAccelerationDirection()
    {
    	xAccel = bIAccel.getX();
    	yAccel = bIAccel.getY();
    	
    	if(xAccel == 0)
    	{
    		xAccel += 0.000001;
    	}
    	
    	double theta = Math.atan(yAccel / xAccel);
    	
    	if(xAccel < 0)
    	{
    		theta += Math.PI;
    	}
    	
    	return theta;
    }
}
