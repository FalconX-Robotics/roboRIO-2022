package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Ball {    
    public final Alliance m_alliance;
    public final Translation2d m_position;
    public final boolean m_blueSide;

    public Ball(Alliance alliance, Translation2d translation, boolean blueSide) {
        m_alliance = alliance;
        m_position = translation;
        m_blueSide = blueSide;
    }

    public boolean isBall() {
        return true;
    }

    public static Ball getClosestBallTo(ArrayList<? extends Ball> balls, Alliance alliance, boolean blueSide, Translation2d position) {
        Ball closestBall = null;
        double closestDistance = Double.MAX_VALUE;
        for (Ball ball : balls) {
            if (ball.m_alliance != alliance || ball.m_blueSide != blueSide) continue;
            double distance = ball.m_position.getDistance(position);
            if (distance < closestDistance) {
                closestBall = ball;
                closestDistance = distance;
            }
        }
        return closestDistance == Double.MAX_VALUE ? null : closestBall;
    }
}
