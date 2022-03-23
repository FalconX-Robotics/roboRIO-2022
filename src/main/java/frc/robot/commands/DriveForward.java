package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class DriveForward extends PIDCommand {
    private Drivetrain m_drivetrain;
    protected double m_P = 0, m_I = 0, m_D = 0, m_F = 0;
    protected double m_positionTolerance = 0, m_velocityTolerance = 0;
    protected double m_maxSpeed = 1;

	// protected NetworkTableEntry m_errorField = SmartDashboard.getEntry("DriveForward/Error");
    // protected NetworkTableEntry m_velocityField = SmartDashboard.getEntry("DriveForward/Velocity");
    
    /**
     * @param setpointSource is only called on initialize
     */
    public DriveForward(DoubleSupplier setpointSource, Drivetrain drivetrain) {
        super(new PIDController(0, 0, 0),
            () -> 0.,
            setpointSource,
            output -> {},
            drivetrain);
        m_drivetrain = drivetrain;
        m_controller.setPID(m_P, m_I, m_D);
        m_controller.setTolerance(m_positionTolerance, m_velocityTolerance);

        addRequirements(m_drivetrain);
    }

    public DriveForward(double setpointSource, Drivetrain drivetrain) {
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
        m_setpoint = () -> setpoint;
    }

    @Override
    public void initialize() {
        m_useOutput = output -> m_drivetrain.arcadeDrive(-m_F*Math.signum(output) - MathUtil.clamp(output, -m_maxSpeed, m_maxSpeed), 0);

        double m_initEncoderDistance = m_drivetrain.averageEncoderDistance();
        m_measurement = () -> m_drivetrain.averageEncoderDistance() - m_initEncoderDistance;

        m_drivetrain.resetEncoder();
    }

    @Override
    public void execute() {
        super.execute();
        // m_errorField.setDouble(m_controller.getPositionError());
        // m_velocityField.setDouble(m_controller.getVelocityError());
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        // m_errorField.setDouble(100);
    }
}