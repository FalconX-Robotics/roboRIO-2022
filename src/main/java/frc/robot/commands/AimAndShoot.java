// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Connection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Outtake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class AimAndShoot extends SequentialCommandGroup {

  // create variables for drivetrain, outtake, and camera
  private final Drivetrain m_drivetrain;
  private final Connection m_connection;
  private final Outtake m_outtake;
  private final Camera m_camera;
  
  /** Creates a new AimAndShoot. */
  public AimAndShoot(Drivetrain drivetrain, Connection connection, Outtake outtake, Camera camera) {
    m_drivetrain = drivetrain;
    m_connection = connection;
    m_outtake = outtake;
    m_camera = camera;

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    // Add TurnToTarget then AutoShoot
    addCommands(
      new TurnToTarget(m_drivetrain, m_camera), 
      new ParallelDeadlineGroup(new AutoShoot(m_outtake, m_camera), new RunConveyor(m_connection)));
    
  }
}