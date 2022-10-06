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

    private final double kTargetHeight = 104;
    private final double kCameraHeight = 23;
    private final double kCameraAngle = 40 * (Math.PI / 180);

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
        return OptionalDouble.of(getTargetDistance(m_YEntry.getDouble(kPitchNotViewable)));
    }

    
    // public OptionalDouble getTargetDistance(double a2) {
    //     //a2 is the angle from the target to the limelight
    //     double pitch = a2 * (Math.PI / 180); // convert to radians
    //     if (a2 == kPitchNotViewable
    //         || (pitch + kCameraAngle) <= 0
    //         || m_AEntry.getDouble(0.) == 0.) return OptionalDouble.empty();

    //     double horizontalDistance = (kTargetHeight - kCameraHeight) / (Math.tan(pitch + kCameraAngle));
    //     // double diagonalDistance = Math.sqrt((horizontalDistance*horizontalDistance) + ((kTargetHeight - kCameraHeight)*(kTargetHeight - kCameraHeight)));
    //     return OptionalDouble.of(horizontalDistance);
    // }

    public double getTargetDistance(double a2) {
        //a2 is the angle from the target to the limelight
        double pitch = a2 * (Math.PI / 180); // convert to radians
        

        double horizontalDistance = (kTargetHeight - kCameraHeight) / (Math.tan(pitch + kCameraAngle));
        // double diagonalDistance = Math.sqrt((horizontalDistance*horizontalDistance) + ((kTargetHeight - kCameraHeight)*(kTargetHeight - kCameraHeight)));
        return horizontalDistance;
    }

    @Override
    public void periodic() {
        m_yawEntry.setDouble(targetYaw().orElse(kYawNotViewable));
        m_distanceEntry.setDouble(targetDistance().orElse(kDistanceNotViewable));

        SmartDashboard.putNumber("distance", getTargetDistance(25));
    }
}
