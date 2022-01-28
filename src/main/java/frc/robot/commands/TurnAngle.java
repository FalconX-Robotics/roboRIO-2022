package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnAngle extends PIDCommand {
    protected double m_P = 0, m_I = 0, m_D = 0;
    protected double m_tolerance = 2;
    protected double m_maxSpeed;
    
    public TurnAngle(DoubleSupplier setpointSource, Drivetrain drivetrain, double maxSpeed) {
        super(new PIDController(0, 0, 0),
            drivetrain::gyroYaw,
            setpointSource,
            output -> drivetrain.arcadeDrive(0, MathUtil.clamp(output, -maxSpeed, maxSpeed)),
            drivetrain);
        m_maxSpeed = maxSpeed;
        drivetrain.resetGyroYaw();
        m_controller.setPID(m_P, m_I, m_D);
        m_controller.enableContinuousInput(-180, 180);
        m_controller.setTolerance(m_tolerance);
        m_controller.setIntegratorRange(-0.3, 0.3);
    }

    public TurnAngle(double setpointSource, Drivetrain drivetrain, double maxSpeed) {
        this(() -> setpointSource, drivetrain, maxSpeed);
    }

    public void setPID(double P, double I, double D) {
        m_controller.setPID(P, I, D);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}
