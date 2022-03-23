// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Connection;

public class RunConveyor extends CommandBase {
  // create object for subsystem
  private final Connection m_connection;
  /** Creates a new runConveyor. */
  public RunConveyor(Connection connection) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_connection = connection;

    addRequirements(m_connection);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // call method to run motor
    m_connection.runConnection();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // call method to stop motor
    m_connection.disableConnection();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
