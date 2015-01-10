/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team1517.mecanum;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 *
 * @author Zoraver
 */
public class MecanumDrive {
    
    CANJaguar aF, aB, bF, bB;
    final double mP = 1, mI = 0, mD = 0;//All final variables will have to be tweaked through testing.
    final int MAX_RPM = 100;
    
    public MecanumDrive(CANJaguar aFront, CANJaguar aBack, CANJaguar bFront, CANJaguar bBack)
    {
        aF = aFront;
        aB = aBack;
        bF = bFront;
        bB = bBack;
    }
    
    public boolean drive(double mX, double mY, double twist)
    {   
        double x, y, magnitude, theta, highestValue, A, B, T;

        //Sets the control mode of the CANJaguars to percent mode.
        if(aF.getControlMode() != CANJaguar.ControlMode.PercentVbus)
        {
            aF.setPercentMode();
            aB.setPercentMode();
            bF.setPercentMode();
            bB.setPercentMode();
            
            aF.enableControl();
            aB.enableControl();
            bF.enableControl();
            bB.enableControl();
        }
        
        x = mX;
        y = mY;
        T = twist;
        
        magnitude = Math.sqrt(x * x + y * y);//Calculates the magnitude of the output vector.
        theta = Math.atan(y / x);//Calculates the direction of the output vector.
        
        if(x < 0)//Corrects the quadrent of theta for the value of X.
        {
            theta = theta + Math.PI;
        }
        
        A = Math.sqrt(2) * Math.sin(theta - 3 * Math.PI / 4);//Sets diagonal A to the value for theta of mechanum graph.
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
    
    public boolean pidDrive(double mX, double mY, double twist)
    {
        if(aF.getControlMode() != CANJaguar.ControlMode.Speed)
        {
        	//Sets the CANJaguars to closed loop speed control mode with a 100 tick quadrature encoder.
        	//mP, mI, and mD are the p, i, and d coefficients, respectively.
        	aF.setSpeedMode(CANJaguar.kQuadEncoder, 100, mP, mI, mD);
            aB.setSpeedMode(CANJaguar.kQuadEncoder, 100, mP, mI, mD);
            bF.setSpeedMode(CANJaguar.kQuadEncoder, 100, mP, mI, mD);
            bB.setSpeedMode(CANJaguar.kQuadEncoder, 100, mP, mI, mD);
            
            aF.enableControl();
            aB.enableControl();
            bF.enableControl();
            bB.enableControl();
        }
        
        double x, y, magnitude, theta, highestValue, A, B, T;
          
        x = mX;
        y = mY;
        T = twist;
            
        magnitude = Math.sqrt(x * x + y * y);
        theta = Math.atan(y / x);
            
        if(x < 0)
        {
            theta = theta + Math.PI;
        }
            
        A = Math.sqrt(2) * Math.sin(theta - 3 * Math.PI / 4);
        B = Math.sqrt(2) * Math.cos(theta + Math.PI / 4);
        
        if(A > 1)
        {
            A = 1;
        }
        else if(A < -1) 
        {
            A = -1;
        }
        
        if(B > 1) 
        {
            B = 1;
        }
        else if(B < -1) 
        {
            B = -1;
        }
            
        A = A * magnitude;
        B = B * magnitude;
            
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
        
        aF.set(MAX_RPM * ((A + T) / highestValue)); 
        aB.set(MAX_RPM * ((A - T) / highestValue)); 
        bF.set(MAX_RPM * ((B - T) / highestValue)); 
        bB.set(MAX_RPM * ((B + T) / highestValue));
  
        return true;
    }
}
