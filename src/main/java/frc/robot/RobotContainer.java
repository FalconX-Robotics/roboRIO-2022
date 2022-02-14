// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.LowerArm;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.TankDrive;
import frc.robot.commands.TurnAngle;
import frc.robot.commands.runConveyor;
import frc.robot.subsystems.Connection;
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
	// Defines controller and subsystems
	private final XboxController m_driver = new XboxController(Constants.CONTROLLER_PORT);
	private final Drivetrain m_drivetrain = new Drivetrain();
	private final Outtake m_outtake = new Outtake();
	private final Intake m_intake = new Intake();
	private final Connection m_connection = new Connection();
	
	private final ArcadeDrive m_arcadeDrive = new ArcadeDrive(m_drivetrain, m_driver);
	@SuppressWarnings("unused")
	private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, m_driver);

	private final NetworkTableEntry m_visionPField = SmartDashboard.getEntry("Drivetrain/P");
	private final NetworkTableEntry m_visionIField = SmartDashboard.getEntry("Drivetrain/I");
	private final NetworkTableEntry m_visionDField = SmartDashboard.getEntry("Drivetrain/D");
	private final NetworkTableEntry m_visionFField = SmartDashboard.getEntry("Drivetrain/F");

	/** The container for the robot. Contains subsystems, OI devices, and commands. */
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(m_arcadeDrive);

		SmartDashboard.putData("Drivetrain/TurnAngle", new TurnAngle(90, m_drivetrain, 0.5) {
			@Override
			public void initialize() {
				setPIDF(m_visionPField.getDouble(0), m_visionIField.getDouble(0), m_visionDField.getDouble(0), m_visionFField.getDouble(0));
				super.initialize();
			}
		});

		// Configure the button bindings
		configureButtonBindings();
	}

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
				.toggleWhenPressed(new OuttakeCommand(m_outtake, m_outtake.m_motorSpeed));
		new JoystickButton(m_driver, XboxController.Button.kB.value)
				.toggleWhenPressed(new IntakeCommand(m_intake));

		// new JoystickButton(m_driver, XboxController.Button.kRightBumper.value)
		// .toggleWhenPressed(new IntakeCommand(m_armIntake));//set later

		new JoystickButton(m_driver, XboxController.Button.kX.value)
			.whenPressed(new LowerArm(m_intake));
			
		new JoystickButton(m_driver, XboxController.Button.kY.value)
			.toggleWhenPressed(new runConveyor(m_connection));
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
