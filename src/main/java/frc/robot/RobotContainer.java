// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.TankDrive;
import frc.robot.commands.TurnAngle;
import frc.robot.subsystems.Drivetrain;

/**
* This class is where the bulk of the robot should be declared. Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
* subsystems, commands, and button mappings) should be declared here.
*/
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final XboxController m_driver = new XboxController(Constants.CONTROLLER_PORT);
	private final Drivetrain m_drivetrain = new Drivetrain();
	
	@SuppressWarnings("unused")
	private final ArcadeDrive m_arcadeDrive = new ArcadeDrive(m_drivetrain, m_driver);
	@SuppressWarnings("unused")
	private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, m_driver);

	private final TurnAngle m_turnAngle = new TurnAngle(90, m_drivetrain, 0.5);
	private final ShuffleboardTab m_visionTab = Shuffleboard.getTab("vision");
	private final NetworkTableEntry m_visionPField = m_visionTab.addPersistent("P", 0.).getEntry();
	private final NetworkTableEntry m_visionIField = m_visionTab.addPersistent("I", 0.).getEntry();
	private final NetworkTableEntry m_visionDField = m_visionTab.addPersistent("D", 0.).getEntry();

	/** The container for the robot. Contains subsystems, OI devices, and commands. */
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(m_arcadeDrive);

		m_visionTab.add("TurnAngle", new InstantCommand(() -> {
			m_turnAngle.setPID(m_visionPField.getDouble(0), m_visionIField.getDouble(0), m_visionDField.getDouble(0));
			m_turnAngle.schedule();
		}).withName("TurnAngle"));
		m_visionTab.add("End", new InstantCommand(() -> {
			m_turnAngle.cancel();
		}).withName("End TurnAngle"));

		// Configure the button bindings
		configureButtonBindings();
	}
	
	/**
	* Use this method to define your button->command mappings. Buttons can be created by
	* instantiating a {@link GenericHID} or one of its subclasses ({@link
	* edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
	* edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	*/
	private void configureButtonBindings() {
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
		