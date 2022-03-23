package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Connection;
import frc.robot.subsystems.Outtake;

public class ManualShoot extends ParallelDeadlineGroup {

    public ManualShoot(Outtake m_outtake, Connection m_connection) {
        super(new WaitCommand(2), new ParallelCommandGroup(
            new OuttakeCommand(m_outtake, 1),
            new WaitCommand(0.5).andThen(new RunConveyor(m_connection))
        ));

        addRequirements(m_outtake, m_connection);
    }
}
