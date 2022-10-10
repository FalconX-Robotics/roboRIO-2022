// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Intake;

public class TurnOnIntake extends InstantCommand {

  private final Intake m_intake;
  private final double m_motorSpeed;

  public TurnOnIntake(Intake intake, double motorSpeed) {
    m_intake = intake;
    m_motorSpeed = motorSpeed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intake.runIntake(m_motorSpeed);
  }

}
