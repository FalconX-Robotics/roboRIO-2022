package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final CANSparkMax m_intake = new CANSparkMax(Constants.INTAKE_PORT, MotorType.kBrushless);// Hey look it works now
    private final CANSparkMax m_intakeArm = new CANSparkMax(Constants.INTAKE_ARM_MOTOR, MotorType.kBrushed);
    private final double m_intakeSpeed = Math.PI; // random value; change later
    private final double m_intakeArmSpeed = Math.E / 11;

    public Intake() {
        // something may go in here later (if you want)
    }

    // self-explanitory, starts the intake motor
    public void runIntake() {
        m_intake.set(m_intakeSpeed);
    }

    // turns off intake motor I think
    public void disableIntake() {
        m_intake.set(0);
    }

    public void lowerIntakeArm() {
        m_intakeArm.set(m_intakeArmSpeed);
    }

    public void disableIntakeArm() {
        m_intakeArm.set(0);
    }

    public void setArmIdleMode(IdleMode mode) {
        m_intakeArm.setIdleMode(mode);
    }

}
