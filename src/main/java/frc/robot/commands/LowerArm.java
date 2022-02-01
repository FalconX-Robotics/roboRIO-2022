// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class LowerArm extends CommandBase {
  /** Creates a new IntakeCommand. */
  private final Intake m_intake;
  private final boolean m_canMove;

  public LowerArm(Intake intake, boolean canMove) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_intake = intake;
    m_canMove = canMove;
    addRequirements(m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_canMove) {
         m_intake.lowerIntakeArm();
      } else {
        m_intake.disableIntakeArm();
      }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.disableIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
