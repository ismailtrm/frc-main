package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends TimedRobot {

    private VictorSP leftMotor1 = new VictorSP(Constants.LEFT_MOTOR_ONE);
    private VictorSP leftMotor2 = new VictorSP(Constants.LEFT_MOTOR_TWO);
    private VictorSP rightMotor1 = new VictorSP(Constants.RIGHT_MOTOR_ONE);
    private VictorSP rightMotor2 = new VictorSP(Constants.RIGHT_MOTOR_TWO);
    private VictorSP intakeMotor = new VictorSP(Constants.INTAKE_MOTOR);
    private PowerDistribution masterPD = new PowerDistribution(0, ModuleType.kCTRE);
    private CANSparkMax dropperMotor = new CANSparkMax(Constants.DROPPER_MOTOR, MotorType.kBrushless);
    private Joystick joystick = new Joystick(0);

    @Override
    public void robotInit() {
        configureDropperMotor();
    }

    @Override
    public void teleopPeriodic() {
        double speedCore = -joystick.getRawAxis(Constants.JOYSTICK_REVERSE) + joystick.getRawAxis(Constants.JOYSTICK_FORWARD);
        double turn = joystick.getRawAxis(Constants.JOYSTICK_TURN) * 0.3;
        double speed = speedCore * 0.6;

        boolean buttonMode = joystick.getRawButton(3);
        boolean buttonDropper = joystick.getRawButton(2);
        boolean buttonIntake = joystick.getRawButton(1);

        double left = speed + turn;
        double right = speed - turn;

        if (joystick.isConnected()) {
            setMotorSpeeds(left, right);
            controlIntake(buttonIntake);
            controlDropper(buttonDropper);
            controlBoth(buttonMode);
            updateSmartDashboard();
        }
    }

    private void configureDropperMotor() {
        if (dropperMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk) {
            SmartDashboard.putString("Idle Mode", "Error");
        }
        SmartDashboard.putString("Idle Mode", dropperMotor.getIdleMode() == IdleMode.kCoast ? "Coast" : "Brake");

        if (dropperMotor.setOpenLoopRampRate(0) != REVLibError.kOk) {
            SmartDashboard.putString("Ramp Rate", "Error");
        }
        SmartDashboard.putNumber("Ramp Rate", dropperMotor.getOpenLoopRampRate());
    }

    private void setMotorSpeeds(double left, double right) {
        leftMotor1.set(left);
        leftMotor2.set(left);
        rightMotor1.set(-right);
        rightMotor2.set(-right);
    }

    private void controlIntake(boolean buttonIntake) {
        intakeMotor.set(buttonIntake ? -0.4 : 0);
    }

    private void controlDropper(boolean buttonDropper) {
        dropperMotor.set(buttonDropper ? 1 : 0);
    }

    private void controlBoth(boolean buttonMode) {
        if (buttonMode) {
            dropperMotor.set(1);
            intakeMotor.set(-0.4);
        } else {
            dropperMotor.set(0);
            intakeMotor.set(0);
        }
    }

    private void updateSmartDashboard() {
        SmartDashboard.putNumber("Voltage", dropperMotor.getBusVoltage());
        SmartDashboard.putNumber("Temperature", dropperMotor.getMotorTemperature());
        SmartDashboard.putNumber("Output", dropperMotor.getAppliedOutput());
    }
}