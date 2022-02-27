package frc.robot.subsystems;

import java.util.OptionalDouble;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    public final double kYawNotViewable = 3600;
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
    
    public Camera() { }

    public OptionalDouble targetYaw() { // in degrees
        return targetYaw(m_XEntry.getDouble(kYawNotViewable));
    }

    public OptionalDouble targetYaw(double x) { // in degrees
        if (x == kYawNotViewable || m_AEntry.getDouble(0.) == 0.) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(x);
    }

    public OptionalDouble targetDistance() {
        return targetDistance(m_YEntry.getDouble(kPitchNotViewable));
    }

    public OptionalDouble targetDistance(double y) {
        double pitch = y * (Math.PI / 160); // in radian
        if (y == kPitchNotViewable
            || (pitch + kCameraAngle) <= 0
            || m_AEntry.getDouble(0.) == 0.) return OptionalDouble.empty();
        return OptionalDouble.of((kTargetHeight - kCameraHeight) / (Math.tan(pitch + kCameraAngle)));
    }

    @Override
    public void periodic() {
        m_yawEntry.setDouble(targetYaw().orElse(kYawNotViewable));
        m_distanceEntry.setDouble(targetDistance().orElse(kDistanceNotViewable));
    }
}
