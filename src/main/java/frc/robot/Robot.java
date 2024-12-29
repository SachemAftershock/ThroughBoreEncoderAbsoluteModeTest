// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private DigitalInput input = new DigitalInput(0);
  private DutyCycleEncoder encoder = new DutyCycleEncoder(input);
  private double absolutePosition = 0;
  private double absoluteDistance = 0;
  private final int printLimiterHeartbeatInterval = 50;
  private int heartbeats = 0;

  private enum whichDemoEnum { eLinearDistanceDemo, eAngularDistanceDemo };
  private final whichDemoEnum theDemo = whichDemoEnum.eAngularDistanceDemo;

  // Note: the REV-11-1271 has a measure accuract of about 1/10th of a degree. 

  // Without encoder reset upon power up, acts as an Absolute continuous encoder for the 
  // REV-11-1271 with REV-11-1817 configuration.  Encoder reset will make the reported net 
  // continous distance relative to encoder position at time of the encoder reset.
  private final Boolean demoWithReset = false;

  // A setPositionOffset can be used when configured as an absolute continous encoder, to
  // allow shifting the Distance Travelled to a more convenient zero.  Setting to the current
  // values of the getAbsolutePosition (0..1) after power up will shift getDistance to zero.
  private final Boolean demoSetPositionOffset = true;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Hex Through Bore Encoder is 4096 counts per rotation
    switch (theDemo) {

      case eLinearDistanceDemo:

        // This demonstration uses the REV-11-1271 Through Bore Encoder
        // https://www.revrobotics.com/rev-11-1271/    
        // with the JST-PH 6-pin to 4 x 3-pin 0.1" (PWM/Dupont) Cable (REV-11-1817)
        // to a DIO port on the RoboRIO.
        // Review the application examples there. 
  
        // Set each rotation to circumference of a 4" diameter wheel, in feet.
        // Assume the Hex Bore Encoder is on the wheel axil, thus the rest of 
        // drive train and gears are not needed.  C = PI*d. Ft = In./12.
        encoder.setDistancePerRotation(Math.PI*4.0/12.0); 
        break;
        
      case eAngularDistanceDemo:

        // This demonstration uses the REV-11-1271 Through Bore Encoder
        // https://www.revrobotics.com/rev-11-1271/    
        // with the JST-PH 6-pin to 4 x 3-pin 0.1" (PWM/Dupont) Cable (REV-11-1817)
        // to a DIO port on the RoboRIO.
        // Review the application examples there. 
  
        // Set each rotaton to 360 degrees, continuous (negative infinity to infinity).
        encoder.setDistancePerRotation(360.0); 
        break;
    }

    if (demoWithReset) encoder.reset();

    if (demoSetPositionOffset) encoder.setPositionOffset(0.2457);

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
    absolutePosition = encoder.getAbsolutePosition();
    absoluteDistance = encoder.getDistance();
    if ((heartbeats++ % printLimiterHeartbeatInterval) == 0) {
      switch (theDemo) {
        case eLinearDistanceDemo:
          System.out.println("Net Feet Travelled: " + String.format("%9.6f",absoluteDistance) + "  (Absolute Encoder: " + absolutePosition + ")");
          break;
        case eAngularDistanceDemo:
          System.out.println("Net Degrees Travelled: " + String.format("%9.6f",absoluteDistance) + "  (Absolute Encoder: " + absolutePosition + ")");
          break;
      }
    }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

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
