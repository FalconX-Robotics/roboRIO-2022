package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final CANSparkMax m_intake_motor_one = new CANSparkMax(Constants.INTAKE_MOTOR_ONE, MotorType.kBrushless);
    private final CANSparkMax m_intake_motor_two = new CANSparkMax(Constants.INTAKE_MOTOR_TWO, MotorType.kBrushless);

    public Intake() {}

    public void runIntake() {
        
    }

    public void disableIntake() {
        
    }

    
}
