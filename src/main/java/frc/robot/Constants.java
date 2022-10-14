// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    // piegon ports
    public final static int PIGEON_PORT = 42;

    // left ports
    public final static int LEFT_FRONT_MOTOR_PORT = 2;
    public final static int LEFT_BACK_MOTOR_PORT = 7;

    // right ports
    public final static int RIGHT_FRONT_MOTOR_PORT = 1;
    public final static int RIGHT_BACK_MOTOR_PORT = 4;

    // controller port
    public final static int CONTROLLER_PORT = 0;

    // INTAKE_PORT
    public final static int INTAKE_WHEEL_MOTOR = 3; // random value; change later
    public final static int INTAKE_ARM_MOTOR = 5; //again, random value 

    // top and bottom ports
    public final static int TOP_PORT = 9; // random value; change later
    public final static int BOTTOM_PORT = 8; // random value; change later

    // conveyor motor port
    public final static int CONNECTION_PORT = 6;

    // led controller port
    public final static int LED_CONTROLLER_PORT = 9;

    public static final double DRIVETRAIN_S = 0;

    public static final double DRIVETRAIN_V = 0;

    public static final double DRIVETRAIN_A = 0;

    public static final DifferentialDriveKinematics DRIVE_KINEMATICS = null;

}
