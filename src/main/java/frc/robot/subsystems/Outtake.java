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
  private final CANSparkMax m_topMotor = new CANSparkMax(Constants.TOP_PORT, MotorType.kBrushless);
  private final CANSparkMax m_bottomMotor = new CANSparkMax(Constants.BOTTOM_PORT, MotorType.kBrushless);

  public final double m_motorSpeed = Math.PI; // random value; change later

  // starts outtake motor, top goes backwards, bottom goes forwards
  public void runOuttake(double motorSpeed) {
    m_topMotor.set(motorSpeed);
    m_bottomMotor.set(motorSpeed);
  }

  // stops outtake motor
  public void disableOuttake() {
    m_topMotor.set(0);
    m_bottomMotor.set(0);
  }

}
