// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class MoveIntakeArm extends CommandBase {
  /** Creates a new MoveIntakeArm. */
  private Intake m_intake;
  private double m_angle;
  private PIDController m_controller = new PIDController(1, 0, 0);

  public MoveIntakeArm(double angle, Intake intake) {
    m_intake = intake;
    m_angle = angle;

    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controller.setSetpoint(m_angle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = m_controller.calculate(m_intake.getArmAngle());
    m_intake.lowerArm(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
