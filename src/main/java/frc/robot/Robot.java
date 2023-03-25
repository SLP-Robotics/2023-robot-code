// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.security.spec.DSAParameterSpec;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final DriveSystem Drive = new DriveSystem();
  private final ArmSystem ArmControl = new ArmSystem();
  private final PneumaticSystem SolenoidControl = new PneumaticSystem();
  private Command m_autonomousCommand;
  DigitalInput reverseExtendSwitch = new DigitalInput(0);
  DigitalInput forwardExtendSwitch = new DigitalInput(1);
  DigitalInput leftTurretSwitch = new DigitalInput(2);
  DigitalInput rightTurretSwitch = new DigitalInput(3);
  private RobotContainer m_robotContainer;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    ArmControl.initMotors();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    while(DriveSystem.getDist()<Constants.autonomousDist){
      DriveSystem.drive(Constants.autonomousSpeed, 0);
    }
    DriveSystem.drive(0,0);
    while(GyroControl.getAngle()!=0){
      Drive.drive(GyroControl.getAngle()/Constants.maxPlatformAngle*0.8,0);
    }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Tell the robot container to get the latest joystick values
    // This has to be the first thing called in this method
    m_robotContainer.readButtons();

    if (reverseExtendSwitch.get() && RobotContainer.armExtend <= 0d) {
      ArmControl.extendArm(RobotContainer.armExtend);
    } else if (forwardExtendSwitch.get() && RobotContainer.armExtend >= 0d) {
      ArmControl.extendArm(RobotContainer.armExtend);
    } else if (!forwardExtendSwitch.get() && !reverseExtendSwitch.get()) {
      ArmControl.extendArm(RobotContainer.armExtend);
    } else {
      ArmControl.extendArm(0);
    }

    if (leftTurretSwitch.get() && RobotContainer.armZRot >= 0d) {
      ArmControl.turnArm(RobotContainer.armZRot);
    } else if (rightTurretSwitch.get() && RobotContainer.armZRot <= 0d) {
      ArmControl.turnArm(RobotContainer.armZRot);
    } else if (!leftTurretSwitch.get() && !rightTurretSwitch.get()) {
      ArmControl.turnArm(RobotContainer.armZRot);
    } else {
      ArmControl.turnArm(0);
    }
    ArmControl.turnArm(RobotContainer.armZRot);
    //ArmControl.extendArm(RobotContainer.armExtend);
    if (RobotContainer.recenterArmPressed) {
      ArmControl.recenterMotorPos();
    };

    if (RobotContainer.diagPressed) {
      System.out.println("--------------------");
      System.out.println("Reverse Extension Limit Switch: " +  String.valueOf(reverseExtendSwitch.get()));
      System.out.println("Forward Extension Limit Switch: " +  String.valueOf(forwardExtendSwitch.get()));
      System.out.println("Arm Extension Rate: " + String.valueOf(RobotContainer.armExtend));
      System.out.println("PID Arm Position: " + String.valueOf(ArmControl.armDeflectionPos));
      System.out.println("--------------------");
    }
    // Call the DriveSystem.drive() method with the speed and direction joystick values
    //Drive.drive(RobotContainer.speed, RobotContainer.direction);
    // Call the ArmSystem.turnArm() method with the arm rotation value
    ArmControl.extendArm(RobotContainer.armExtend);

      if((ArmControl.getArmRot()>Constants.operativeRange[0][0]&&ArmControl.getArmRot()<Constants.operativeRange[0][1])||(ArmControl.getArmRot()>Constants.operativeRange[1][0]&&ArmControl.getArmRot()<Constants.operativeRange[1][1])){
        ArmControl.turnArm(RobotContainer.armZRot);
      }else if(!forwardExtendSwitch.get()){
        ArmControl.turnArm(RobotContainer.armZRot);
      }
    //disabled while extension is not finished
    ArmControl.armDeflection(RobotContainer.armDeflect);
    SolenoidControl.solenoidControl(RobotContainer.clawEngaged);
    if(RobotContainer.triggerLevel){
      Drive.drive(GyroControl.getAngle()/Constants.maxPlatformAngle*0.8,0);
    }else{
      Drive.drive(RobotContainer.speed, RobotContainer.direction);
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
