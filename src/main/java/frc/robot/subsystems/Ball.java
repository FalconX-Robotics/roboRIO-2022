package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Ball extends SubsystemBase {    
    public final Alliance m_alliance;
    public final Translation2d m_position;

    public Ball(Alliance alliance, Translation2d translation) {
        m_alliance = alliance;
        m_position = translation;
    }

    public boolean isBall() {
        return true;
    }

    public static Ball getClosestBallTo(ArrayList<? extends Ball> balls, Alliance alliance, Translation2d position) {
        Ball closestBall = null;
        double closestDistance = Double.MAX_VALUE;
        for (Ball ball : balls) {
            if (ball.m_alliance != alliance) continue;
            double distance = ball.m_position.getDistance(position);
            if (distance < closestDistance) {
                closestBall = ball;
                closestDistance = distance;
            }
        }
        return closestDistance == Double.MAX_VALUE ? null : closestBall;
    }
}
