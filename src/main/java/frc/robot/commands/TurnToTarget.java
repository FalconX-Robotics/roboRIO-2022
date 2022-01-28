package frc.robot.commands;

import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends TurnAngle {

    private final double kNotViewableMaxSpeed = 0.2;
    private final double kNotViewableSetpoint = 10;
    private Drivetrain m_drivetrain;
    private Camera m_camera;
    private double m_maxSpeedCopy;

    public TurnToTarget(Drivetrain drivetrain, Camera camera, double maxSpeed) {
        super(camera::targetYaw, drivetrain, maxSpeed);
        m_drivetrain = drivetrain;
        m_camera = camera;
        m_maxSpeedCopy = maxSpeed;
        addRequirements(camera);
    }
    
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
        m_setpoint = this::setpointSource;
        m_camera.capture(true);
    }

    @Override 
    public void end(boolean interrupted) {
        super.end(interrupted);
        m_camera.capture(false);
    }
}
