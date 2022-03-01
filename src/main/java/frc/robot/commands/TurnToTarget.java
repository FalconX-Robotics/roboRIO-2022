package frc.robot.commands;

import java.util.OptionalDouble;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;

public class TurnToTarget extends TurnAngle {
    private Drivetrain m_drivetrain;
    private Camera m_camera;

    //Access Smart Dashboard(?)
    protected NetworkTableEntry m_setpointField = SmartDashboard.getEntry("Camera/Setpoint");
    protected NetworkTableEntry m_processedField = SmartDashboard.getEntry("Camera/Processed");
    protected NetworkTableEntry m_errorField = SmartDashboard.getEntry("Camera/Error");
    
    public TurnToTarget(Drivetrain drivetrain, Camera camera) {
        super(0., drivetrain);
        m_drivetrain = drivetrain;
        m_camera = camera;
        
        addRequirements(m_drivetrain, camera);
    }

    @Override
    public void initialize() {
        OptionalDouble distance = m_camera.targetDistance();
        if (distance.isPresent()) {
            setSetpoint(distance.getAsDouble());
        } else {
            this.cancel();
        }
        super.initialize();
    }

    @Override 
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
