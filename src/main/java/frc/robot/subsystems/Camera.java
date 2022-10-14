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
<<<<<<< HEAD
    private NetworkTableEntry m_AEntry = m_limelightTable.getEntry("ta"); 
=======
    private NetworkTableEntry m_AEntry = m_limelightTable.getEntry("ta");
>>>>>>> e2ebfe4 (Camera Distance and Shot Plausibility without the)

    // private NetworkTableEntry m_yawEntry = SmartDashboard.getEntry("Camera/Pitch"); 
    // private NetworkTableEntry m_distanceEntry = SmartDashboard.getEntry("Camera/Distance");
    
    
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
        // m_yawEntry.setDouble(targetYaw().orElse(kYawNotViewable));
        // m_distanceEntry.setDouble(targetDistance().orElse(kDistanceNotViewable));


        //double VOffset = m_YEntry.getNumber(kCameraAngle*-1).doubleValue();
        //SmartDashboard.putNumber("distance", getTargetDistance(VOffset));

        // double VOffset = m_YEntry.getNumber(kCameraAngle*-1).doubleValue();
        // SmartDashboard.putNumber("distance", getTargetDistance(m_limelightTable.getEntry("ty").getDouble(0)));

NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
NetworkTableEntry ty = table.getEntry("ty");
double targetOffsetAngle_Vertical = ty.getDouble(0.0);

// how many degrees back is your limelight rotated from perfectly vertical?
double limelightMountAngleDegrees = 25.0;

// distance from the center of the Limelight lens to the floor
double limelightLensHeightInches = 20.0;

// distance from the target to the floor
double goalHeightInches = 60.0;

double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

// calculate distance
double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches)/Math.tan(angleToGoalRadians);
SmartDashboard.putNumber("Distance", distanceFromLimelightToGoalInches);
// System.out.println(distanceFromLimelightToGoalInches);
boolean shotPlausible = (distanceFromLimelightToGoalInches <= 50) ? (true) : (false);
SmartDashboard.putBoolean("Shot Plausible", shotPlausible);

    }
}
