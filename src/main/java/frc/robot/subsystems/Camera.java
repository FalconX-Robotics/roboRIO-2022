package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    public final double kAngleNotViewable = 3600;
    public final double kPitchNotViewable = -3600;
    public final double kDistanceNotViewable = -1000;

    private final double kTargetHeight = 100 * 2.54;
    private final double kCameraHeight = 61;
    private final double kCameraAngle = (90-65) * (Math.PI / 160);

    private NetworkTable m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry m_XEntry = m_limelightTable.getEntry("tx");
    private NetworkTableEntry m_YEntry = m_limelightTable.getEntry("ty");
    private NetworkTableEntry m_AEntry = m_limelightTable.getEntry("ta");

    private NetworkTableEntry m_yawEntry = SmartDashboard.getEntry("Camera/Pitch");
    private NetworkTableEntry m_distanceEntry = SmartDashboard.getEntry("Camera/Distance");
    

    public Camera() {
    }

    public double targetYaw() { // in degrees
        return targetYaw(m_XEntry.getDouble(kAngleNotViewable));
    }

    public double targetYaw(double x) { // in degrees
        // double nx = (1./160) * (px - 159.5);
        // double vpw = 2.0*Math.tan(kHorizontalFov/2);
        // return Math.atan2(1., nx * vpw/2);
        if (m_AEntry.getDouble(0.) == 0.) {
            return kAngleNotViewable;
        }
        return x;
    }

    public double targetDistance() {
        return targetDistance(m_YEntry.getDouble(kPitchNotViewable));
    }

    public double targetDistance(double y) {
        // double ny = (1./120) * (119.5 - py);
        // double vph = 2.0*Math.tan(kVerticalFov/2);
        // double y = vph/2 * ny;
        // double pitch = Math.atan2(1, y);
        if ((y + kCameraAngle) < 0 || m_AEntry.getDouble(0.) == 0.) return kDistanceNotViewable;
        double pitch = y * (Math.PI / 160); // in radian
        return (kTargetHeight - kCameraHeight) / (Math.tan(pitch + kCameraAngle));
    }

    @Override
    public void periodic() {
        m_yawEntry.setDouble(targetYaw());
        m_distanceEntry.setDouble(targetDistance());
    }
}
