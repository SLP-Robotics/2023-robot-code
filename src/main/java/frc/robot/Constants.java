// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static final double periodicUpdateRateSec = 0.02;//How often 'periodic' runs IN SECONDS

  public static final double normalSpeedMultiplier = 0.6;//The multiplier for how fast the robot normally runs at max speed
  
  public static final double armExtensionRotRate = 1.0;//How fast the arm extension motor turns PER SECOND
  public static final double armExtensionRotRatio = 1.0;//How far the arm extends PER ROTATION of the extension gear
  
  public static final double armDeflectionRotRate = 1000;//How fast the arm extension motor turns PER SECOND
  public static final double armDeflectionRotRatio = 1.0;//How far the arm extends PER ROTATION of the extension gear

  public static final double armZRotRate = 0.1;//The maximum rate at which the arm rotates when the motor is engaged
  public static final double armZRotRatio = 1.0;//How much the arm rotates PER ROTATION of the arm rotation gear
  public static final double wheelRadius = 3.03125;//This is in INCHES
  public static final double autonomousDist = 300.0;//This is how far the robot will travel in INCHES
  public static final double autonomousSpeed = 0.25;//This is the speed at which the robot travels in autonomous
  public static final double maxPlatformAngle = 15.0;
  public static final double[][] operativeRange = {
    {1/4*Math.PI, 3/4*Math.PI},
    {5/4*Math.PI, 7/4*Math.PI}
  };//Operative rotation range in RADIANS
  //THESE NUMBERS WILL NEED TO BE CHANGED ONCE WE DETERMINE THE CORRECT OPERATIVE RANGE
}
