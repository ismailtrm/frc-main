package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Robot extends TimedRobot {
  

  private VictorSP leftMotor1 = new VictorSP(Constans.leftMotorOne);
  private VictorSP leftMotor2 = new VictorSP(Constans.leftMotorTwo);

  private VictorSP rightMotor1 = new VictorSP(Constans.rightMotorOne);
  private VictorSP rightMotor2 = new VictorSP(Constans.rightMotorTwo);

  private VictorSP intakeMotor = new VictorSP(Constans.intakeMotor); //intake motor tanımlandı

  private PowerDistribution masterPD = new PowerDistribution(0, ModuleType.kCTRE);

  private CANSparkMax dropperMotor = new CANSparkMax(Constans.dropperMotor, MotorType.kBrushless); //sparkmax tanımmlandı?
  
  //dropperMotor.restoreFactoryDefaults();

  private Joystick joystick = new Joystick(0);

  @Override
  public void robotInit() {
    if(dropperMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }

    /**
     * Similarly, parameters will have a Get method which allows you to retrieve their values
     * from the controller
     */
    if(dropperMotor.getIdleMode() == IdleMode.kCoast) {
      SmartDashboard.putString("Idle Mode", "Coast");
    } else {
      SmartDashboard.putString("Idle Mode", "Brake");
    }

    // Set ramp rate to 0
    if(dropperMotor.setOpenLoopRampRate(0) != REVLibError.kOk) {
      SmartDashboard.putString("Ramp Rate", "Error");
    }

    // read back ramp rate value
    SmartDashboard.putNumber("Ramp Rate", dropperMotor.getOpenLoopRampRate());
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    
  }

  @Override
  public void teleopPeriodic() {

    double speedCore = -joystick.getRawAxis(Constans.joystick_reverse) + joystick.getRawAxis(Constans.joystick_forward);
    double turn = joystick.getRawAxis(Constans.joystick_turn) * 0.3;
    double speed = speedCore * 0.6;

    boolean butonMode = joystick.getRawButton(3);
    boolean buttonDropper = joystick.getRawButton(2); //xbox buton id gerekli
    boolean buttonIntake = joystick.getRawButton(1);

    double left = speed + turn;
    double right = speed - turn;
  

    if (Math.abs(speed) < 0.1) {
      speed = 0;
    }

    if (Math.abs(turn) < 0.1) {
     turn = 0;
    }
    
    if (joystick.isConnected()){

      leftMotor1.set(left);
      leftMotor2.set(left);
      
      rightMotor1.set(-right);
      rightMotor2.set(-right);
  
      //intake
      if (buttonIntake == true) {
        intakeMotor.set(-0.4);
      }
      else{
        intakeMotor.set(0);
      }
      
      //dropper
      if (buttonDropper == true){  
        dropperMotor.set(1);
      }else{
        dropperMotor.set(0);
      }

      //cift
      if(butonMode == true){
        dropperMotor.set(1);
        intakeMotor.set(-0.4);
      }
      else{
        dropperMotor.set(0);
        intakeMotor.set(0);
      }
      SmartDashboard.putNumber("Voltage", dropperMotor.getBusVoltage());
      SmartDashboard.putNumber("Temperature", dropperMotor.getMotorTemperature());
      SmartDashboard.putNumber("Output", dropperMotor.getAppliedOutput());
      
     
    }
  

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {

    intakeMotor.set(-0.3);
    dropperMotor.set(1);
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
