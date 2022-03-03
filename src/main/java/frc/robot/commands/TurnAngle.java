package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnAngle extends PIDCommand {
    private Drivetrain m_drivetrain;
    protected double m_P = 0.7, m_I = 0, m_D = 0.03, m_F = 0.192;
    protected double m_positionTolerance = 2, m_velocityTolerance = 40;
    protected double m_maxSpeed = 1;
    
    protected DoubleSupplier m_setpointSupplier;
	protected NetworkTableEntry m_errorField = SmartDashboard.getEntry("TurnAngle/Error");
    protected NetworkTableEntry m_velocityField = SmartDashboard.getEntry("TurnAngle/Velocity");
    
    /**
     * @param setpointSupplier is only called on initialize
     */
    public TurnAngle(DoubleSupplier setpointSupplier, Drivetrain drivetrain) {
        super(new PIDController(0, 0, 0),
            () -> 0.,
            0.,
            output -> {},
            drivetrain);
        m_drivetrain = drivetrain;
        m_setpointSupplier = setpointSupplier;
        m_controller.setPID(m_P, m_I, m_D);
        m_controller.enableContinuousInput(-180, 180);
        m_controller.setTolerance(m_positionTolerance, m_velocityTolerance);
    }

    public TurnAngle(double setpointSource, Drivetrain drivetrain) {
        this(() -> setpointSource, drivetrain);
    }

    public void setPIDF(double P, double I, double D, double F) {
        m_P = P;
        m_I = I;
        m_D = D;
        m_F = F;
        m_controller.setPID(P, I, D);
    }

    public void setSetpoint(double setpoint) {
        this.m_setpoint = () -> m_drivetrain.truncateDegree(setpoint);
    }

    @Override
    public void initialize() {
        double setpoint = m_setpointSupplier.getAsDouble();
        m_useOutput = output -> m_drivetrain.arcadeDrive(0, -m_F*Math.signum(output) - MathUtil.clamp(output, -m_maxSpeed, m_maxSpeed));
        m_setpoint = () -> m_drivetrain.truncateDegree(setpoint);
        
        double initGyro = m_drivetrain.gyroYawRaw();
        m_measurement = () -> {
            double yaw = m_drivetrain.gyroYawRaw() - initGyro;
            yaw %= 360;
            if (yaw < 0) yaw += 360;
            return yaw <= 180 ? yaw : -360 + yaw;
        };
    }

    @Override
    public void execute() {
        super.execute();
        m_errorField.setDouble(m_controller.getPositionError());
        m_velocityField.setDouble(m_controller.getVelocityError());
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        m_errorField.setDouble(360);
    }
}