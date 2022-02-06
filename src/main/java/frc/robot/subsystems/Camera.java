package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    public final double kAngleNotViewable = 360;
    public final double kDistanceNotViewable = -1;

    // sets it to true or false to begin target detection
    private NetworkTableEntry m_captureEntry = SmartDashboard.getEntry("Camera/Capture");
    // yaw is the angle displaced from the target. 360 means target is not in sight
    private NetworkTableEntry m_yawEntry = SmartDashboard.getEntry("Camera/Yaw");
    // x distance from the target. -1 means target is not in sight
    private NetworkTableEntry m_distanceEntry = SmartDashboard.getEntry("Camera/Distance");
    
    public Camera() {
        
    }

    public void capture(boolean begin) {
        m_captureEntry.setBoolean(begin);
        if (!begin) {
            m_yawEntry.setDouble(kAngleNotViewable);
            m_distanceEntry.setDouble(kDistanceNotViewable);
        }
    }

    public double targetYaw() {
        return m_yawEntry.getDouble(kAngleNotViewable);
    }

    public double targetDistance() {
        return m_distanceEntry.getDouble(kDistanceNotViewable);
    }
}
