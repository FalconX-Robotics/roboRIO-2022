package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnAngle extends PIDCommand {
    final static double kP = 0, kI = 0, kD = 0;
    final static double kTolerance = 2;
    
    public TurnAngle(DoubleSupplier setpointSource, Drivetrain drivetrain) {
        super(new PIDController(kP, kI, kD),
            drivetrain::gyroYaw,
            setpointSource,
            output -> drivetrain.arcadeDrive(0, output),
            drivetrain);

        drivetrain.resetGyro();
        getController().enableContinuousInput(-180, 180);
        getController().setTolerance(kTolerance);
    }

    public TurnAngle(double setpointSource, Drivetrain drivetrain) {
        this(() -> setpointSource, drivetrain);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}
