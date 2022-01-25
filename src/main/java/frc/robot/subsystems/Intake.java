package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase{
    CANSparkMax m_intake = new CANSparkMax(Constants.INPUT_PORT, MotorType.kBrushless);//Hey look it works now

    double motorSpeed = 0.314159265358979;//Hey this is important

    public Intake() {
    }//something may go in here later (if you want)
    
    //self-explanitory
    public void runIntake() {
        m_intake.set(motorSpeed);
    }
    public void disableIntake() {
        m_intake.set(0);
    }

}
