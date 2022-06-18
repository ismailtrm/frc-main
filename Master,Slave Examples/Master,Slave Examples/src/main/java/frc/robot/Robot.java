package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Encoder.IndexingType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {
  private VictorSPX rightSlave = new VictorSPX(0);
  private TalonSRX rightMaster = new TalonSRX(1);
  private TalonSRX leftMaster = new TalonSRX(2);
  private VictorSPX leftSlave = new VictorSPX(3);

  private Joystick driver_Joystick = new Joystick(0);
  private final double kDriveTick2Feet = 1.0 / 4096 * 6 * Math.PI / 12;
  private DifferentialDrive drive = new DifferentialDrive(leftMaster, rightMaster);

  @Override
  public void robotInit() {
    rightMaster.setInverted(true);
    leftMaster.setInverted(true);

    rightSlave.follow(rightMaster);
    leftSlave.follow(leftMaster);

    leftSlave.setInverted(InvertType.FollowMaster);
    rightSlave.setInverted(InvertType.FollowMaster);

    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    leftMaster.setSensorPhase(false);
    rightMaster.setSensorPhase(true);

    leftMaster.setSelectedSensorPosition(0, 0, 10);
    rightMaster.setSelectedSensorPosition(0, 0, 10);

    drive.setDeadband(0.05);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Side Encoder Value", rightMaster.getSelectedSensorPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Right Side Encoder Value", leftMaster.getSelectedSensorPosition() * kDriveTick2Feet);
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    double power = -driver_Joystick.getRawAxis(1);
    double turn = driver_Joystick.getRawAxis(4);
    drive.arcadeDrive(power * 0.6, turn * 0.3);
    
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
