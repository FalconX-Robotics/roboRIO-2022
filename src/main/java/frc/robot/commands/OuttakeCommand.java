// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Outtake;

public class OuttakeCommand extends CommandBase {

  private final Outtake m_outtake;
  private final double m_motorSpeed;

  public OuttakeCommand(Outtake outtake, double motorSpeed) {
    m_outtake = outtake;
    m_motorSpeed = motorSpeed;
    addRequirements(m_outtake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_outtake.runMotors(m_motorSpeed * 0.75, m_motorSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_outtake.disableMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
