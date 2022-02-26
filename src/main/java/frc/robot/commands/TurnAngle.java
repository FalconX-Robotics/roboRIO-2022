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

	protected NetworkTableEntry m_setpointField = SmartDashboard.getEntry("Drivetrain/Setpoint");
	protected NetworkTableEntry m_processedField = SmartDashboard.getEntry("Drivetrain/Processed");
	protected NetworkTableEntry m_errorField = SmartDashboard.getEntry("Drivetrain/Error");
    protected NetworkTableEntry m_velocityField = SmartDashboard.getEntry("Drivetrain/Velocity");
    
    public TurnAngle(DoubleSupplier setpointSource, Drivetrain drivetrain) {
        super(new PIDController(0, 0, 0),
            drivetrain::gyroYaw,
            setpointSource,
            output -> {},
            drivetrain);
        m_drivetrain = drivetrain;
        m_controller.setPID(m_P, m_I, m_D);
        m_controller.enableContinuousInput(-180, 180);
        m_controller.setTolerance(m_positionTolerance, m_velocityTolerance);
        m_controller.setIntegratorRange(-0.1, 0.1);

        m_setpointField.setPersistent();
        m_processedField.setPersistent();
        m_errorField.setPersistent();
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
        this.m_setpoint = () -> setpoint;
    }

    @Override
    public void initialize() {
        // System.out.println("TurnAngle initalized");
        m_useOutput = output -> m_drivetrain.arcadeDrive(0, -m_F*Math.signum(output) - MathUtil.clamp(output, -m_maxSpeed, m_maxSpeed));
        m_drivetrain.resetGyroYaw();
    }

    @Override
    public void execute() {
        super.execute();
        // System.out.println("TurnAngle (Gyro & Setpoint) " + m_drivetrain.gyroYaw() + " " + m_controller.getSetpoint());
        m_setpointField.setDouble(m_setpoint.getAsDouble());
        m_processedField.setDouble(m_drivetrain.gyroYaw());
        m_errorField.setDouble(m_controller.getPositionError());
        m_velocityField.setDouble(m_controller.getVelocityError());
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        // System.out.println("TurnAngle ended (interrupted) " + interrupted);
        m_errorField.setDouble(360);
    }
}

// c:\Users\Public\wpilib\2022\jdk\bin\java -jar c:\Users\Public\wpilib\2022\tools\shuffleboard.jar