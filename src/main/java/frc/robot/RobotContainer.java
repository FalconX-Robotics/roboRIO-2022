// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AimAndShoot;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.DriveForward;
import frc.robot.commands.ManualShoot;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.RunConveyor;
import frc.robot.commands.TankDrive;
import frc.robot.commands.TurnAngle;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Connection;
import frc.robot.subsystems.Drivetrain;
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
	private final Camera m_camera = new Camera();
	// private final Led m_led = new Led();
	private final Drivetrain m_drivetrain = new Drivetrain();
	private final Outtake m_outtake = new Outtake();
	private final Connection m_connection = new Connection();
	// private final Intake m_intake = new Intake();

	private final AutonomousManager m_autonomousManager = new AutonomousManager(m_drivetrain, m_connection, m_outtake, m_camera, m_driver);
	private final SendableChooser<AutonomousManager.Path> m_pathChooser = new SendableChooser<AutonomousManager.Path>();

	private final ArcadeDrive m_arcadeDrive = new ArcadeDrive(m_drivetrain, m_driver);
	@SuppressWarnings("unused")
	private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, m_driver);

	private final NetworkTableEntry m_autoTurnPField = SmartDashboard.getEntry("AutoTurn/P Field");
	private final NetworkTableEntry m_autoTurnIField = SmartDashboard.getEntry("AutoTurn/I Field");
	private final NetworkTableEntry m_autoTurnDField = SmartDashboard.getEntry("AutoTurn/D Field");
	private final NetworkTableEntry m_autoTurnFField = SmartDashboard.getEntry("AutoTurn/F Field");
	private final NetworkTableEntry m_autoTurnSetpointField = SmartDashboard.getEntry("AutoTurn/Setpoint Field");

	private final NetworkTableEntry m_driveForwardPField = SmartDashboard.getEntry("DriveForward/P Field");
	private final NetworkTableEntry m_driveForwardIField = SmartDashboard.getEntry("DriveForward/I Field");
	private final NetworkTableEntry m_driveForwardDField = SmartDashboard.getEntry("DriveForward/D Field");
	private final NetworkTableEntry m_driveForwardFField = SmartDashboard.getEntry("DriveForward/F Field");
	private final NetworkTableEntry m_driveForwardSetpointField = SmartDashboard.getEntry("DriveForward/Setpoint Field");

	/** The container for the robot. Contains subsystems, OI devices, and commands. */
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(m_arcadeDrive);

		// auto manager
		for (AutonomousManager.Path path : AutonomousManager.Path.values()) {
			m_pathChooser.addOption(path.name(), path);
		}

		m_pathChooser.setDefaultOption(m_autonomousManager.DEFAULT_PATH.name(), m_autonomousManager.DEFAULT_PATH);
		SmartDashboard.putData("Main/Path", m_pathChooser);

		// turn angle
		m_autoTurnPField.setDouble(0);
		m_autoTurnIField.setDouble(0);
		m_autoTurnDField.setDouble(0);
		m_autoTurnFField.setDouble(0);
		m_autoTurnSetpointField.setDouble(90);
		SmartDashboard.putData("AutoTurn/TurnAngle", new TurnAngle(90, m_drivetrain) {
			@Override
			public void initialize() {
				setPIDF(m_autoTurnPField.getDouble(0), m_autoTurnIField.getDouble(0), m_autoTurnDField.getDouble(0), m_autoTurnFField.getDouble(0));
				setSetpoint(m_autoTurnSetpointField.getDouble(0));
				super.initialize();
			}
		}.withName("TurnAngle Modified"));

		// drive forward
		m_driveForwardPField.setDouble(0);
		m_driveForwardIField.setDouble(0);
		m_driveForwardDField.setDouble(0);
		m_driveForwardFField.setDouble(0);
		m_driveForwardSetpointField.setDouble(5);
		SmartDashboard.putData("DriveForward/DriveForward", new DriveForward(5, m_drivetrain) {
			@Override
			public void initialize() {
				setPIDF(m_driveForwardPField.getDouble(0), m_driveForwardIField.getDouble(0), m_driveForwardDField.getDouble(0), m_driveForwardFField.getDouble(0));
				setSetpoint(m_driveForwardSetpointField.getDouble(0));
				super.initialize();
			}
		}.withName("DriveForward Modified"));

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
		// new JoystickButton(m_driver, XboxController.Button.kA.value)
		// 		.toggleWhenPressed(new OuttakeCommand(m_outtake, 1));
		// new JoystickButton(m_driver, XboxController.Button.kB.value)
		// 		.toggleWhenPressed(new IntakeCommand(m_intake));

		// new JoystickButton(m_driver, XboxController.Button.kX.value)
		// 	.whenPressed(new LowerArm(m_intake));

		new JoystickButton(m_driver, XboxController.Button.kX.value)
			.whenHeld(new OuttakeCommand(m_outtake, 1));
			
		new JoystickButton(m_driver, XboxController.Button.kA.value)
			.whenHeld(new RunConveyor(m_connection));
		
		// bind AimAndShoot to the right bumper
		new JoystickButton(m_driver, XboxController.Button.kRightBumper.value)
			.whenPressed(new AimAndShoot(m_drivetrain, m_connection, m_outtake, m_camera));
		
		new JoystickButton(m_driver, XboxController.Button.kY.value)
			.whenPressed(new ManualShoot(m_outtake, m_connection));
	}
	
	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		m_autonomousManager.setPath(m_pathChooser.getSelected());
		return m_autonomousManager.getCommand();
	}
}
