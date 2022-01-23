package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnAngle extends PIDCommand {
    protected double kP = 0, kI = 0, kD = 0;
    protected double kTolerance = 2;
    protected double maxSpeed;
    
    public TurnAngle(DoubleSupplier setpointSource, Drivetrain drivetrain, double maxSpeed) {
        super(new PIDController(0, 0, 0),
            drivetrain::gyroYaw,
            setpointSource,
            output -> drivetrain.arcadeDrive(0, MathUtil.clamp(output, -maxSpeed, maxSpeed)),
            drivetrain);
        this.maxSpeed = maxSpeed;

        drivetrain.resetGyroYaw();
        m_controller.setPID(kP, kI, kD);
        m_controller.enableContinuousInput(-180, 180);
        m_controller.setTolerance(kTolerance);
        m_controller.setIntegratorRange(-0.3, 0.3);
    }

    public TurnAngle(double setpointSource, Drivetrain drivetrain, double maxSpeed) {
        this(() -> setpointSource, drivetrain, maxSpeed);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}
