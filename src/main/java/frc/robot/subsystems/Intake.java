package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final CANSparkMax m_intake_wheel_motor = new CANSparkMax(Constants.INTAKE_WHEEL_MOTOR, MotorType.kBrushless);
    private final CANSparkMax m_intake_arm_motor = new CANSparkMax(Constants.INTAKE_ARM_MOTOR, MotorType.kBrushless);
    private final double armGearRatio = (9.0/40.0) * (20.0 / 60.0) * (16.0 / 32.0);
    //9:40  20:60  16:32

    public Intake() {}

    public void runIntake() {
        runIntake(0.0);
    }

    public void runIntake(double speed) {
        m_intake_wheel_motor.set(speed);
    }

    public void disableIntake() {
        runIntake(0);
    }

    public void raiseArm(double speed){
        m_intake_arm_motor.set(speed);
    }

    public void lowerArm(double speed){
        m_intake_arm_motor.set(speed*-1);
    }

    public double getArmAngle() {
        return m_intake_arm_motor.getEncoder().getPosition() * 360. * armGearRatio;
    }

    public void resetArmAngle() {
        m_intake_arm_motor.getEncoder().setPosition(0);
    }
}
