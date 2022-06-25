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
  public void robotInit() {}

  @Override
  public void robotPeriodic() {

    SmartDashboard.putNumber("Temperature", masterPD.getTemperature());

    SmartDashboard.putNumber("Right Front Motor Current", masterPD.getCurrent(0));
    SmartDashboard.putNumber("Right Rear Motor Current", masterPD.getCurrent(1));

    SmartDashboard.putNumber("Left Front Motor Current", masterPD.getCurrent(2));
    SmartDashboard.putNumber("Left Rear Motor Current", masterPD.getCurrent(3));

  }

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

    boolean buttonMode = joystick.getRawButton(0); //xbox buton id gerekli
    
    int p = 0;
    if (buttonMode == true){  

      while(p==50){                 //50 birim olana kadar her 20 milisaniyede bir birim arttırılır-- 
        p++;                        //--işlem(charge time) 1000milisaniyede(1 saniye) tamamlanır.
        intakeMotor.set(p*0.1);
        dropperMotor.set(p*0.1);
        try {                        
          Thread.sleep(20);           
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
    }
     
    }else{
    p = 0;
    }

    double left = speed + turn;
    double right = speed - turn;
    
    if (Math.abs(speed) < 0.1) {
      speed = 0;
    }

    if (Math.abs(turn) < 0.1) {
     turn = 0;
    }
    
    leftMotor1.set(left);
    leftMotor2.set(left);
    
    rightMotor1.set(-right);
    rightMotor2.set(-right);

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {
    rightMotor1.set(-0.5);
    rightMotor2.set(-0.5);

    leftMotor1.set(0.5);
    leftMotor2.set(0.5);

    intakeMotor.set(-0.3);
    dropperMotor.set(1);
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
