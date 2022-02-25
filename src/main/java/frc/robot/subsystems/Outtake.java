// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Outtake extends SubsystemBase {
	private final CANSparkMax m_bottomMotor = new CANSparkMax(Constants.BOTTOM_PORT, MotorType.kBrushless);
	private final CANSparkMax m_topMotor = new CANSparkMax(Constants.TOP_PORT, MotorType.kBrushless);
	
	public Outtake() {
		m_bottomMotor.setIdleMode(IdleMode.kCoast);
		m_topMotor.setIdleMode(IdleMode.kCoast);
	}
	
	public void runBottomMotor(double speed) {
		m_bottomMotor.set(-speed);
	}
	
	public void runTopMotor(double speed) {
		m_topMotor.set(-speed);
	}
	
	public void runMotors(double bottomSpeed, double topSpeed) {
		runBottomMotor(bottomSpeed);
		runTopMotor(topSpeed);
	}
	
	public void runMotors(double speed) {
		runMotors(speed, speed);
	}
	
	// stops outtake motor
	public void disableMotors() {
		runMotors(0);
	}
	
}
