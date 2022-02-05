// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drivetrain extends SubsystemBase {
	// Define motor

	// define left MotorControl group
	private final CANSparkMax m_leftFrontMotor = new CANSparkMax(Constants.LEFT_FRONT_MOTOR_PORT, MotorType.kBrushless);
	private final CANSparkMax m_leftBackMotor = new CANSparkMax(Constants.LEFT_BACK_MOTOR_PORT, MotorType.kBrushless);
	private final MotorControllerGroup m_leftSide = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);

	// defines right MotorControl group
	private final CANSparkMax m_rightFrontMotor = new CANSparkMax(Constants.RIGHT_FRONT_MOTOR_PORT,
			MotorType.kBrushless);
	private final CANSparkMax m_rightBackMotor = new CANSparkMax(Constants.RIGHT_BACK_MOTOR_PORT, MotorType.kBrushless);
	private final MotorControllerGroup m_rightSide = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);

	// defines m_drivetrain
	private final DifferentialDrive m_drivetrain = new DifferentialDrive(m_leftSide, m_rightSide);

	// defines m_maxWheelSpeed self explanatory
	private final double m_maxWheelSpeed = 1;

	/** Creates a new Drivetrain */
	public Drivetrain() {
		m_drivetrain.setDeadband(0.05);
		m_drivetrain.setSafetyEnabled(true);

		m_rightSide.setInverted(true);
	}

	// Creates tankDrive
	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed = inputToSpeed(leftSpeed);
		rightSpeed = inputToSpeed(rightSpeed);
		m_drivetrain.tankDrive(leftSpeed, rightSpeed);
	}

	// Creates arcadeDrive
	public void arcadeDrive(double xSpeed, double zRotation) {
		xSpeed = inputToSpeed(xSpeed);
		zRotation = inputToSpeed(-zRotation);
		m_drivetrain.arcadeDrive(xSpeed, zRotation);
	}

	// turns controller input into motor speed
	public double inputToSpeed(double input) {
		return MathUtil.clamp(-input, -m_maxWheelSpeed, m_maxWheelSpeed);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation
	}
}
