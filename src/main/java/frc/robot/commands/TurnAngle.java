package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnAngle extends PIDCommand {
    private Drivetrain m_drivetrain;
    protected double m_P = 0, m_I = 0, m_D = 0;
    protected double m_tolerance = 2;
    protected double m_maxSpeed = 0.5;

    private final ShuffleboardTab m_visionTab = Shuffleboard.getTab("vision");
	private final NetworkTableEntry m_errorField = m_visionTab.add("Error", 0.).getEntry();
    
    public TurnAngle(DoubleSupplier setpointSource, Drivetrain drivetrain, double maxSpeed) {
        super(new PIDController(0, 0, 0),
            drivetrain::gyroYaw,
            setpointSource,
            output -> drivetrain.arcadeDrive(0, MathUtil.clamp(output, -maxSpeed, maxSpeed)),
            drivetrain);
        m_maxSpeed = maxSpeed;
        m_drivetrain = drivetrain;
        m_controller.setPID(m_P, m_I, m_D);
        m_controller.enableContinuousInput(-180, 180);
        m_controller.setTolerance(m_tolerance);
        m_controller.setIntegratorRange(-0.3, 0.3);
    }

    public TurnAngle(double setpointSource, Drivetrain drivetrain, double maxSpeed) {
        this(() -> setpointSource, drivetrain, maxSpeed);
    }

    public void setPID(double P, double I, double D) {
        m_controller.setPID(P, I, D);
        System.out.println("P: " + P);
    }

    @Override
    public void initialize() {
        System.out.println("TurnAngle initalized");
        m_useOutput = output -> m_drivetrain.arcadeDrive(0, MathUtil.clamp(output, -m_maxSpeed, m_maxSpeed));
        m_drivetrain.resetGyroYaw();
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("TurnAngle (Gyro & Setpoint) " + m_drivetrain.gyroYaw() + " " + m_controller.getSetpoint());
        m_errorField.setDouble(m_controller.getPositionError());
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("TurnAngle ended (interrupted)" + interrupted);
        m_errorField.setDouble(360);
    }
}

// c:\Users\Public\wpilib\2022\jdk\bin\java -jar c:\Users\Public\wpilib\2022\tools\shuffleboard.jar