// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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
	//
	private final ArcadeDrive m_arcadeDrive = new ArcadeDrive(m_drivetrain, m_driver);
	private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, m_driver);

	private final TurnAngle m_turnAngle = new TurnAngle(0, m_drivetrain, 0.5);
	private final ShuffleboardTab visionTab = Shuffleboard.getTab("vision");
	private final NetworkTableEntry visionPField = visionTab.add("P", 0.).getEntry();
	private final NetworkTableEntry visionIField = visionTab.add("I", 0.).getEntry();
	private final NetworkTableEntry visionDField = visionTab.add("D", 0.).getEntry();

	/** The container for the robot. Contains subsystems, OI devices, and commands. */
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(m_arcadeDrive);
		for (NetworkTableEntry entry : new NetworkTableEntry[] {visionPField, visionIField, visionDField}) {
			visionPField.addListener((notification) -> {
				m_turnAngle.setPID(visionPField.getDouble(0), visionIField.getDouble(0), visionDField.getDouble(0));
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		Shuffleboard.getTab("vision").add(m_turnAngle);
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
		