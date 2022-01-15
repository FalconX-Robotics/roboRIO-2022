package frc.robot.commands;

import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends TurnAngle {

    private final double kNotViewableMaxSpeed = 0.2;
    private final double kNotViewableSetpoint = 10;
    private Drivetrain drivetrain;
    private Camera camera;
    private double maxSpeedCopy;

    public TurnToTarget(Drivetrain drivetrain, Camera camera, double maxSpeed) {
        super(camera::targetYaw, drivetrain, maxSpeed);
        this.drivetrain = drivetrain;
        this.camera = camera;
        maxSpeedCopy = maxSpeed;
        addRequirements(camera);
    }
    
    protected double setpointSource() {        // latency could be possible issue
        if (camera.targetYaw() == camera.kAngleNotViewable) {
            super.maxSpeed = kNotViewableMaxSpeed;
            return kNotViewableSetpoint; // keep spinning until target is viewable
        }
        super.maxSpeed = maxSpeedCopy;
        return drivetrain.gyroYaw() + camera.targetYaw();
    }

    @Override
    public void initialize() {
        super.initialize();
        m_setpoint = this::setpointSource;
        camera.capture(true);
    }

    @Override 
    public void end(boolean interrupted) {
        super.end(interrupted);
        camera.capture(false);
    }
}
