package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase{
    CANSparkMax m_intake = new CANSparkMax(Constants.INPUT_PORT, MotorType.kBrushless);//Hey look it works now
    double m_motorSpeed = Math.PI; // random value; change later
    public Intake() {
        //something may go in here later (if you want)
        //I beleive this will run stuff while intake is running. chage this if im wrong
    }
    
    //self-explanitory, starts the intake motor
    public void runIntake() {
        m_intake.set(m_motorSpeed);
    }
    
    //turns off intake motor I think
    public void disableIntake() {
        m_intake.set(0);
    }

}
