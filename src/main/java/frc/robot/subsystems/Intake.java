package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final CANSparkMax m_intake_wheel_motor = new CANSparkMax(Constants.INTAKE_WHEEL_MOTOR, MotorType.kBrushless);
    private final CANSparkMax m_intake_arm_motor = new CANSparkMax(Constants.INTAKE_ARM_MOTOR, MotorType.kBrushless);

    public Intake() {}

    public void runIntake() {
        
    }

    public void runIntake(double speed) {
        
    }

    public void disableIntake() {
        runIntake(0);
    }

    public void raiseArm(double speed){
        m_intake_arm_motor.set(speed);
    }

    public void lowerArm(double speed){
        m_intake_wheel_motor.set(speed);
    }

    public double armAngle() {

    }
}
