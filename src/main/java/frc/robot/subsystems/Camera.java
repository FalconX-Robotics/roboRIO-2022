package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    private ShuffleboardTab visionTab = Shuffleboard.getTab("Vision");
    public final double kAngleNotViewable = 360;
    public final double kDistanceNotViewable = -1;

    // sets it to true or false to begin target detection
    private NetworkTableEntry captureEntry = visionTab.add("Capture", false).getEntry();
    // yaw is the angle displaced from the target. 360 means target is not in sight
    private NetworkTableEntry yawEntry = visionTab.add("Yaw", kAngleNotViewable).getEntry();
    // x distance from the target. -1 means target is not in sight
    private NetworkTableEntry distanceEntry = visionTab.add("Distance", kDistanceNotViewable).getEntry();
    

    public Camera() {
        
    }

    public void capture(boolean begin) {
        captureEntry.setBoolean(begin);
        if (!begin) {
            yawEntry.setDouble(kAngleNotViewable);
            distanceEntry.setDouble(kDistanceNotViewable);
        }
    }

    public double targetYaw() {
        return yawEntry.getDouble(kAngleNotViewable);
    }

    public double targetDistance() {
        return distanceEntry.getDouble(kDistanceNotViewable);
    }
}
