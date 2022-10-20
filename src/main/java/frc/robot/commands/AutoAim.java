package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class AutoAim extends CommandBase{
    private double angleToTarget = 0;
    private NetworkTable m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry m_XEntry = m_limelightTable.getEntry("tx");
    private Drivetrain m_drivetrain;

    public AutoAim (Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        m_XEntry = m_limelightTable.getEntry("tx");
        angleToTarget = m_XEntry.getDouble(0);
        //if the target is within 5 degrees then stop moving
        if (angleToTarget > 5.) {
            m_drivetrain.arcadeDrive(0, 10);
        } else if (angleToTarget < -5.)  {
            m_drivetrain.arcadeDrive(0, -10);
        }
    }

    @Override
    public void initialize() {}
    
    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() { return false; }
}
