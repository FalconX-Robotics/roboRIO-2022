// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.TankDrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Outtake;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
	// Defines drivetrain and the xbox controller
	private final XboxController m_driver = new XboxController(Constants.CONTROLLER_PORT);
	private final Drivetrain m_drivetrain = new Drivetrain();
	// defines arcade drive and tank drive
	private final ArcadeDrive m_arcadeDrive = new ArcadeDrive(m_drivetrain, m_driver);
	// @SuppressWarnings("unused") // I don't know what this does so let's just
	// comment it out.
	private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, m_driver);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(m_arcadeDrive);
		// Configure the button bindings
		configureButtonBindings();
	}

	// Defines outtake subsystem
	private final Outtake m_outtake = new Outtake();
	private final Intake m_intake = new Intake();

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by
	 * instantiating a {@link GenericHID} or one of its subclasses ({@link
	 * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
	 * it to a {@link
	 * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */

	private void configureButtonBindings() {
		new JoystickButton(m_driver, XboxController.Button.kA.value)
				.toggleWhenPressed(new OuttakeCommand(m_outtake));
		new JoystickButton(m_driver, XboxController.Button.kB.value)
				.toggleWhenPressed(new IntakeCommand(m_intake));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// An ExampleCommand will run in autonomous
		return null;
	}
}
