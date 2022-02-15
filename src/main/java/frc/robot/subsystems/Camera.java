package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    public final double kAngleNotViewable = 360;
    public final double kDistanceNotViewable = -1;

    private final double kHorizontalFov = 0;
    private final double kVerticalFov = 0;

    private final double kTargetHeight = 0;
    private final double kCameraHeight = 0;
    private final double kCameraAngle = 0;

    private NetworkTable m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry m_XEntry = m_limelightTable.getEntry("tx");
    private NetworkTableEntry m_YEntry = m_limelightTable.getEntry("ty");

    private final double[] m_processedTargetYaw = new double[321];
    private final double[] m_processedTargetDistance = new double[241];
    

    public Camera() {
        for (int i = 0; i < m_processedTargetYaw.length; i++) {
            m_processedTargetYaw[i] = targetYaw(i);
        }

        for (int i = 0; i < m_processedTargetDistance.length; i++) {
            m_processedTargetDistance[i] = targetDistance(i);
        }
    }

    public double fastTargetYaw() {
        int px = (int) Math.round(m_XEntry.getDouble(kAngleNotViewable));
        return m_processedTargetYaw[px];
    }

    public double fastTargetDistance() {
        int py = (int) Math.round(m_YEntry.getDouble(kDistanceNotViewable));
        return m_processedTargetYaw[py];
    }

    public double targetYaw() {
        double px = m_XEntry.getDouble(kAngleNotViewable);
        return targetYaw(px);
    }

    public double targetYaw(double px) {
        double nx = (1./160) * (px - 159.5);
        double vpw = 2.0*Math.tan(kHorizontalFov/2);
        return Math.atan2(1., nx * vpw/2);
    }

    public double targetDistance() {
        double py = m_YEntry.getDouble(kDistanceNotViewable);
        return targetDistance(py);
    }

    public double targetDistance(double py) {
        double ny = (1./120) * (119.5 - py);
        double vph = 2.0*Math.tan(kVerticalFov/2);
        double y = vph/2 * ny;
        double pitch = Math.atan2(1, y);
        return (kTargetHeight - kCameraHeight) / (Math.tan(pitch + kCameraAngle));
    }
}
