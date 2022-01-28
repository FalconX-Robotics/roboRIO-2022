// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Drivetrain extends SubsystemBase {
	private PigeonIMU pigeon = new PigeonIMU(Constants.pigeonPort);

// Define motor
	// left MotorControl group
	CANSparkMax leftFrontMotor = new CANSparkMax(Constants.leftFrontMotorPort, MotorType.kBrushless);
	CANSparkMax leftBackMotor = new CANSparkMax(Constants.leftBackMotorPort, MotorType.kBrushless);
	private MotorControllerGroup leftSide = new MotorControllerGroup(leftFrontMotor, leftBackMotor);
	// right MotorControl group
	CANSparkMax rightFrontMotor = new CANSparkMax(Constants.rightFrontMotorPort, MotorType.kBrushless);
	CANSparkMax rightBackMotor = new CANSparkMax(Constants.rightBackMotorPort, MotorType.kBrushless);
	private MotorControllerGroup rightSide = new MotorControllerGroup(rightFrontMotor, rightBackMotor);

	private DifferentialDrive drivetrain = new DifferentialDrive(leftSide, rightSide);

	private double maxSpeed = 1;

	private final ShuffleboardTab m_visionTab = Shuffleboard.getTab("vision");
	private final NetworkTableEntry m_visionGyroEntry = m_visionTab.add("Gyro", 0.).getEntry();
	
	/** Creates a new ExampleSubsystem. */
	public Drivetrain() {
		drivetrain.setDeadband(0.05);
		drivetrain.setSafetyEnabled(true);

		// leftBackMotor.restoreFactoryDefaults();
		// leftFrontMotor.restoreFactoryDefaults();
		// rightBackMotor.restoreFactoryDefaults();
		// rightFrontMotor.restoreFactoryDefaults();

		rightSide.setInverted(true);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed = inputToSpeed(leftSpeed);
		rightSpeed = inputToSpeed(rightSpeed);
		drivetrain.tankDrive(leftSpeed, rightSpeed);
	}

	public void arcadeDrive(double xSpeed, double zRotation) {
		xSpeed = inputToSpeed(xSpeed);
		zRotation = inputToSpeed(-zRotation);
		drivetrain.arcadeDrive(xSpeed, zRotation);
	}

	public double inputToSpeed(double input) {
		return MathUtil.clamp(-input, -maxSpeed, maxSpeed);
	}

	public double gyroYawRaw() {
		double[] ypr_deg = new double[3];
		pigeon.getYawPitchRoll(ypr_deg);
		return ypr_deg[0];
	}

	/**
	 * @return gyro yaw between [-180, 180]
	 */
	public double gyroYaw() {
		double yaw = gyroYawRaw();
		yaw %= 360;
		if (yaw < 0) yaw += 360;
		return yaw <= 180 ? yaw : -360 + yaw;
	}

	public void resetGyroYaw() {
		pigeon.setYaw(0);
	}
	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		m_visionGyroEntry.setDouble(gyroYaw());
	}
	
	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation
	}
}
