package org.usfirst.frc.team1517.mecanum;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * This class is what MecanumDrive.java should have been.  
 * It uses the SpeedController interface for the motor controller, so any motor controllers can be used.
 * All other mecanum drive classes should be child classes of this class.
 * @author Zoraver
 *
 */
public class MecanumDriveGeneral
{
	SpeedController aF, aB, bF, bB;
    
    public MecanumDriveGeneral(SpeedController aFront, SpeedController aBack, SpeedController bFront, SpeedController bBack)
    {
        aF = aFront;
        aB = aBack;
        bF = bFront;
        bB = bBack;
    }
    
    public boolean drive(double mX, double mY, double twist)
    {   
        double x, y, magnitude, theta, highestValue, A, B, T;
            
            x = mX;
            y = mY;
            T = twist;
            
            magnitude = Math.sqrt(x * x + y * y);//Calculates the magnitude of the output vector.
            theta = Math.atan(y / x);//Calculates the direction of the output vector.
            
            if(x < 0)//Corrects the quadrent of theta for the value of X.
            {
                theta = theta + Math.PI;
            }
            
            /*
             * The phase constants are interesting because a forward movement of the joystick on the y-axis in real life
             * returns a negative value in the code, and vice-versa.  Thus, a position of theta = pi / 2 in real life is
             * corresponds to a position of theta = 3 pi / 2 in the code.
             * If you graph the equations for A and B, this becomes more clear.
             */
            A = Math.sqrt(2) * Math.sin(theta - 3 * Math.PI / 4);//Sets diagonal A to the value for theta of the mechanum equation.
            B = Math.sqrt(2) * Math.cos(theta + Math.PI / 4);//Sets diagonal B to the value for theta of the mechanum equation.
            
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
            if(Math.abs(A + T) > 1 || Math.abs(A - T) > 1 || Math.abs(B + T) > 1 || Math.abs(B - T) > 1)
            {
                highestValue = Math.abs(A + T);
                
                if(Math.abs(A - T) > highestValue)
                {
                    highestValue = Math.abs(A - T);
                }
                
                if(Math.abs(B + T) > highestValue)
                {
                    highestValue = Math.abs(B + T);
                }
                
                if(Math.abs(B - T) > highestValue)
                {
                    highestValue = Math.abs(B - T);
                }
            }
            else
            {
                highestValue = 1.0;
            }
            
            aF.set((A + T) / highestValue); 
            aB.set((A - T) / highestValue); 
            bF.set((B - T) / highestValue); 
            bB.set((B + T) / highestValue); 
            
        return true;//Returns true if successful.
    }
}