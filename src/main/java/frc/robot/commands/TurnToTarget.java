package frc.robot.commands;

import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends TurnAngle {

    private Drivetrain drivetrain;
    private Camera camera;

    public TurnToTarget(Drivetrain drivetrain, Camera camera, double maxSpeed) {
        super(camera::targetYaw, drivetrain, maxSpeed);
        this.drivetrain = drivetrain;
        this.camera = camera;
        addRequirements(camera);
    }
    
    protected double setpointSource() {        // latency could be possible issue
        if (camera.targetYaw() == camera.kAngleNotViewable) {
            return 10; // keep spinning until target is viewable
        }
        return drivetrain.gyroYaw() + camera.targetYaw();
    }

    @Override
    public void initialize() {
        super.initialize();
        m_setpoint = this::setpointSource;
    }
}
