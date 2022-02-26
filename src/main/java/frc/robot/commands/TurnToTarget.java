package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends TurnAngle {

    private final double kNotViewableMaxSpeed = 0.2;
    private final double kNotViewableSetpoint = 10;
    private Drivetrain m_drivetrain;
    private Camera m_camera;
    private double m_maxSpeedCopy;

    protected NetworkTableEntry m_setpointField = SmartDashboard.getEntry("Camera/Setpoint");
    protected NetworkTableEntry m_processedField = SmartDashboard.getEntry("Camera/Processed");
    protected NetworkTableEntry m_errorField = SmartDashboard.getEntry("Camera/Error");

    public TurnToTarget(Drivetrain drivetrain, Camera camera, double maxSpeed) {
        super(camera.targetYaw(), drivetrain);
        m_drivetrain = drivetrain;
        m_camera = camera;
        m_maxSpeedCopy = maxSpeed;
        
        addRequirements(drivetrain, camera);
    }
    
    // deprecated way. which uses camera.getYaw() as a setpoint source
    protected double setpointSource() {        // latency could be possible issue
        if (m_camera.targetYaw() == m_camera.kAngleNotViewable) {
            m_maxSpeed = kNotViewableMaxSpeed;
            return kNotViewableSetpoint; // keep spinning until target is viewable
        }
        m_maxSpeed = m_maxSpeedCopy;
        return m_drivetrain.gyroYaw() + m_camera.targetYaw();
    }

    @Override
    public void initialize() {
        super.initialize();
        // m_setpoint = this::setpointSource;
    }

    @Override 
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
