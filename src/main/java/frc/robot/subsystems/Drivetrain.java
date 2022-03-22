// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.DriveMod;

public class Drivetrain extends SubsystemBase {

	// Gyro
	private WPI_PigeonIMU pigeon = new WPI_PigeonIMU(Constants.PIGEON_PORT);
	private final NetworkTableEntry m_drivetrainGyroEntry = SmartDashboard.getEntry("Drivetrain/Gyro");
	private final NetworkTableEntry m_turnAngleGyroEntry = SmartDashboard.getEntry("TurnAngle/Gyro");
	private final NetworkTableEntry m_cameraGyroEntry = SmartDashboard.getEntry("Camera/Gyro");
	private final NetworkTableEntry m_encoderEntry = SmartDashboard.getEntry("Drivetrain/Encoder");

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

	private final NetworkTableEntry m_leftSideOutputEntry = SmartDashboard.getEntry("Drivetrain/Left Side Output");
	private final NetworkTableEntry m_rightSideOutputEntry = SmartDashboard.getEntry("Drivetrain/Right Side Output");

	private final double m_maxWheelSpeed = 1;

	private final SendableChooser<DriveMod.Mod> m_modChooser = new SendableChooser<DriveMod.Mod>();
	private final DriveMod m_driveMod = new DriveMod(m_modChooser);

	// Encoder stuff
	private final double WHEEL_CIRCUMFERENCE = 6. * Math.PI; 
	private final double kEncoderConversionRatio = (WHEEL_CIRCUMFERENCE * (12. / 50.) * (24. / 50.)) / 39.37; // in meters
	
	private final RelativeEncoder m_leftEncoder = m_leftFrontMotor.getEncoder();
	private final RelativeEncoder m_rightEncoder = m_rightFrontMotor.getEncoder();

	/** Creates a new Drivetrain */
	public Drivetrain() {
		m_drivetrain.setSafetyEnabled(true);

		m_rightSide.setInverted(true);
		m_rightBackMotor.setIdleMode(IdleMode.kBrake);
		m_rightFrontMotor.setIdleMode(IdleMode.kBrake);
		m_leftBackMotor.setIdleMode(IdleMode.kBrake);
		m_leftFrontMotor.setIdleMode(IdleMode.kBrake);
		m_leftEncoder.setPositionConversionFactor(kEncoderConversionRatio);
		m_rightEncoder.setPositionConversionFactor(kEncoderConversionRatio);
		m_leftFrontMotor.setOpenLoopRampRate(0.3);
        m_rightFrontMotor.setOpenLoopRampRate(0.3);
        m_leftBackMotor.setOpenLoopRampRate(0.3);
        m_rightBackMotor.setOpenLoopRampRate(0.3);

		SmartDashboard.putData("Drivetrain/Mod", m_modChooser);
	}

	// Creates tankDrive
	public void tankDrive(double leftSpeed, double rightSpeed) {
		m_drivetrain.tankDrive(leftSpeed, rightSpeed);
	}

	public void tankDriveVolts(double leftVolt, double rightVolt) {
		m_leftSide.setVoltage(leftVolt);
		m_rightSide.setVoltage(rightVolt);
		m_drivetrain.feed();
	}

	// Creates arcadeDrive
	public void arcadeDrive(double xSpeed, double zRotation, boolean modded) {
		xSpeed = inputToSpeed(xSpeed);
		zRotation = inputToSpeed(-zRotation);

		if (modded) {
			xSpeed = m_driveMod.getSpeed(xSpeed, zRotation);
			zRotation = m_driveMod.getRotation(xSpeed, zRotation);
		}

		m_drivetrain.arcadeDrive(xSpeed, zRotation);
	}

	public void arcadeDrive(double xSpeed, double zRotation) {
		arcadeDrive(xSpeed, zRotation, false);
	}

	// turns controller input into motor speed
	public double inputToSpeed(double input) {
		return MathUtil.clamp(-input, -m_maxWheelSpeed, m_maxWheelSpeed);
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

	public double truncateDegree(double degree) {
		degree %= 360;
		if (degree < 0) degree += 360;
		return degree <= 180 ? degree : -360 + degree;
	}

	public void resetGyroYaw() {
		pigeon.setYaw(0);
	}

	public double leftEncoderDistance() { // in meters 
		return m_leftEncoder.getPosition(); 
	}

	public double rightEncoderDistance() { // in meters
		return -m_rightEncoder.getPosition(); 
	}

	public double averageEncoderDistance() {
		return (leftEncoderDistance() + rightEncoderDistance()) / 2; // implement
	}
	
	public void resetEncoder() {
		m_leftEncoder.setPosition(0.);
		m_rightEncoder.setPosition(0.);
	}

	@Override
	public void periodic() {
		m_drivetrainGyroEntry.setDouble(gyroYaw());
		m_cameraGyroEntry.setDouble(gyroYaw());
		m_turnAngleGyroEntry.setDouble(gyroYaw());

		m_encoderEntry.setDouble(averageEncoderDistance());

		m_leftSideOutputEntry.setDouble(m_leftSide.get());
		m_rightSideOutputEntry.setDouble(m_rightSide.get());
	}
}
