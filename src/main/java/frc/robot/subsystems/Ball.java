package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Ball extends SubsystemBase {
    
    private double m_radius;

    public Ball(double radius) {
        m_radius = radius;
    }

    public boolean isBall() {
        return m_radius > 0;
    }
}
