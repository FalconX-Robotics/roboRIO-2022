// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
	/** Creates a new ExampleSubsystem. */
	public Drivetrain() {
		
	}

	public void arcadeDrive(double xSpeed, double zRotation) {
		// to be implemented
	}

	public void resetGyro() {

	}

	public double gyroYaw() {
		return 0; // to be implemented
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
