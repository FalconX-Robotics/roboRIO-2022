package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final CANSparkMax m_intake = new CANSparkMax(Constants.INTAKE_PORT, MotorType.kBrushed);// Hey look it works now
    private final CANSparkMax m_intakeArm = new CANSparkMax(Constants.INTAKE_ARM_MOTOR, MotorType.kBrushless);
    private final double m_motorSpeed = Math.PI; // random value; change later

    public Intake() {
        // something may go in here later (if you want)
        // I beleive this will run stuff while intake is running. chage this if im wrong
    }

    // self-explanitory, starts the intake motor
    public void runIntake() {
        m_intake.set(m_motorSpeed);
    }

    // turns off intake motor I think
    public void disableIntake() {
        m_intake.set(0);
    }

    public void lowerIntakeArm() {
        m_intakeArm.set(0.25);
    }

    public void disableIntakeArm() {
        m_intakeArm.set(0);
    }

    public void setArmIdleMode(IdleMode mode) {
        m_intakeArm.setIdleMode(mode);
    }

}
