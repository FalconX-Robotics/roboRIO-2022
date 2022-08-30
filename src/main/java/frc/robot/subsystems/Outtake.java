// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Outtake extends SubsystemBase implements AutoCloseable {
	private final CANSparkMax m_bottomMotor = new CANSparkMax(Constants.BOTTOM_PORT, MotorType.kBrushless);
	private final CANSparkMax m_topMotor = new CANSparkMax(Constants.TOP_PORT, MotorType.kBrushless);

	private final double kTopFlywheelGearing = 1.5;
	private final double kBottomFlywheelGearing = 1.5;
	private final RelativeEncoder m_topEncoder = m_topMotor.getEncoder();
	private final RelativeEncoder m_bottomEncoder = m_bottomMotor.getEncoder();

	private final NetworkTableEntry m_bottomOutputEntry = SmartDashboard.getEntry("Outtake/Bottom Output");
	private final NetworkTableEntry m_topOutputEntry = SmartDashboard.getEntry("Outtake/Top Output");
	
	public Outtake() {
		m_bottomMotor.setIdleMode(IdleMode.kCoast);
		m_topMotor.setIdleMode(IdleMode.kCoast);

		m_topEncoder.setVelocityConversionFactor(1. / kTopFlywheelGearing);
		m_bottomEncoder.setVelocityConversionFactor(1. / kBottomFlywheelGearing);

		if (RobotBase.isSimulation()) {
			REVPhysicsSim.getInstance().addSparkMax(m_bottomMotor, DCMotor.getNEO(1));
			REVPhysicsSim.getInstance().addSparkMax(m_topMotor, DCMotor.getNEO(1));
		}
	}
	
	public void runBottomMotor(double speed) {
		m_bottomMotor.set(speed);
	}
	
	public void runTopMotor(double speed) {
		m_topMotor.set(speed);
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

	public void periodic() {
		m_bottomOutputEntry.setDouble(m_bottomMotor.get());
		m_topOutputEntry.setDouble(m_topMotor.get());
	}

	@Override
	public void close() {
		m_bottomMotor.close();
		m_topMotor.close();
	}

	private final double kTopFlywheelMOI = 0.0001;
	private final double kBottomFlywheelMOI = 0.0001;

    private FlywheelSim topFlywheelSim = new FlywheelSim(DCMotor.getNEO(1), kTopFlywheelGearing, kTopFlywheelMOI);
	private FlywheelSim bottomFlywheelSim = new FlywheelSim(DCMotor.getNEO(1), kBottomFlywheelGearing, kBottomFlywheelMOI);


	@Override
	public void simulationPeriodic() {
		topFlywheelSim.setInput(m_topMotor.get() * RobotController.getBatteryVoltage());
		bottomFlywheelSim.setInput(m_bottomMotor.get() * RobotController.getBatteryVoltage());

		topFlywheelSim.update(0.02);
		bottomFlywheelSim.update(0.02);

		System.out.println("Top RPM: " + topFlywheelSim.getAngularVelocityRPM() + ", Bottom RPM: " + bottomFlywheelSim.getAngularVelocityRPM());
	}

	public double simulationGetCurrentDrawAmps() {
		return topFlywheelSim.getCurrentDrawAmps() + bottomFlywheelSim.getCurrentDrawAmps();
	}
}
