package frc.robot.commands;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Outtake;


public class AutoShoot extends WaitCommand {
    private final static double k_shootTime = 2;
    private final double k_minDistance = 0;

    private MotorSpeed[] motorSpeeds = new MotorSpeed[] {
        new MotorSpeed(0, 0, 0),
        new MotorSpeed(5, 0.3, 0.25),
        new MotorSpeed(10, 0.6, 0.5),
        new MotorSpeed(20, 0.9, 0.75)
    };

    private double m_targetDistance = 0;
    private MotorSpeed m_motorSpeed;
    private Outtake m_outtake;
    private Camera m_camera;
    
    public AutoShoot(Outtake outtake, Camera camera) {
        super(k_shootTime);
        m_outtake = outtake;
        m_camera = camera;
        m_targetDistance = camera.kDistanceNotViewable;
    }

    private class MotorSpeed {
        public final double distance, bottom, top;
        public MotorSpeed(double distance, double bottom, double top) {
            this.distance = distance;
            this.bottom = bottom;
            this.top = top;
        }
    }
    
    private double approxSpeed(double x, double x0, double y0, double x1, double y1) {
        double slope = (y1 - y0) / (x1 - x0);
        return y0 + slope * (x - x0);
    }

    private Optional<MotorSpeed> getMotorSpeed(double distance) {
        if (distance <= k_minDistance) return Optional.empty();
        for (int i = 0; i < motorSpeeds.length-1; i++) {
            MotorSpeed lowerSpeed = motorSpeeds[i];
            if (lowerSpeed.distance <= distance) {
                MotorSpeed upperSpeed = motorSpeeds[i+1];
                double bottom = approxSpeed(distance, lowerSpeed.distance, lowerSpeed.bottom, upperSpeed.distance, upperSpeed.bottom);
                double top = approxSpeed(distance, lowerSpeed.distance, lowerSpeed.top, upperSpeed.distance, upperSpeed.top);

                return Optional.of(new MotorSpeed(distance, bottom, top));
            }
        }
        return Optional.empty();
    }

    @Override
    public void initialize() {
        super.initialize();
        m_targetDistance = m_camera.targetDistance();
        Optional<MotorSpeed> maybeSpeed = getMotorSpeed(m_targetDistance);
        if (maybeSpeed.isPresent()) {
            m_motorSpeed = maybeSpeed.get();
        } else {
            this.cancel();
        }
    }

    @Override
    public void execute() {
        super.execute();
        m_outtake.runMotors(m_motorSpeed.bottom, m_motorSpeed.top);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        m_outtake.disableMotors();
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }

}
