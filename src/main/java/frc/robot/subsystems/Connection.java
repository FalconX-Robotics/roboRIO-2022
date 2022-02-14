// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Connection extends SubsystemBase {
 
  private final CANSparkMax m_connectionMotor = new CANSparkMax(Constants.CONNECTION_PORT , MotorType.kBrushless);

  public Connection() {}

  // run motor method
  public void runConnection(){
    m_connectionMotor.set(0.5);
  }
  
  // stop motor method
  public void disableConnection() {
    m_connectionMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
