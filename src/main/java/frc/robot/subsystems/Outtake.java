// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Outtake extends SubsystemBase {
  /** Creates a new Outtake. */
  CANSparkMax topMotor = new CANSparkMax(Constants.TOP_PORT, MotorType.kBrushless);
  CANSparkMax bottomMotor = new CANSparkMax(Constants.BOTTOM_PORT, MotorType.kBrushless);

  double m_motorSpeed = Math.PI; // random value; change later
  
  //starts outtake motor, top goes backwards, bottom goes forwards
  public void runOuttake() {
    topMotor.set(-m_motorSpeed);
    bottomMotor.set(m_motorSpeed);
  }

  //stops outtake motor
  public void disableOuttake() {
    topMotor.set(0);
    bottomMotor.set(0);
  }

}
