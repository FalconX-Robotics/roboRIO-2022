package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveForward;
import frc.robot.commands.TurnAngle;
import frc.robot.commands.TurnToTarget;
import frc.robot.subsystems.Ball;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Outtake;

public class AutonomousManager {
    
    public enum Path {
        NONE, TAXI, ONE_BALL, ONE_BALL_TAXI, TWO_BALL_TAXI;
    }
    
    public enum InitialPose {
        INVALID(new Pose2d(0, 0, Rotation2d.fromDegrees(0))),
        A(new Pose2d(0, 0, Rotation2d.fromDegrees(0))),
        B(new Pose2d(0, 0, Rotation2d.fromDegrees(0))),
        C(new Pose2d(0, 0, Rotation2d.fromDegrees(0))),
        D(new Pose2d(0, 0, Rotation2d.fromDegrees(0)));
    
        public final Pose2d pose;
    
        private InitialPose(Pose2d pose) {
            this.pose = pose;
        }
    }

    public final InitialPose DEFAULT_INIT_POSE = InitialPose.INVALID;
    public final Path DEFAULT_PATH = Path.NONE;

    private final SequentialCommandGroup m_turnAndShoot;
    private final Command m_turnBack;

    private final double TAXI_DRIVE_DISTANCE = 1;

    private Path m_path;
    private Pose2d m_initPose;
    private Drivetrain m_drivetrain;
    private Outtake m_outtake;
    private Camera m_camera;

    public AutonomousManager(Path path, Pose2d initPose, Drivetrain drivetrain, Outtake outtake, Camera camera) {
        m_path = path;
        m_initPose = initPose;
        m_drivetrain = drivetrain;
        m_outtake = outtake;
        m_camera = camera;

        m_turnAndShoot = new SequentialCommandGroup(new TurnToTarget(m_drivetrain, m_camera), new AutoShoot(m_outtake, m_camera));
        m_turnBack = new TurnAngle(() -> -m_drivetrain.getPose().getRotation().getDegrees(), m_drivetrain);
    }

    public AutonomousManager(Path path, InitialPose initPose, Drivetrain drivetrain, Outtake outtake, Camera camera) {
        this(path, initPose.pose, drivetrain, outtake, camera);
    }

    public AutonomousManager(Drivetrain drivetrain, Outtake outtake, Camera camera) {
        this(Path.NONE, InitialPose.INVALID, drivetrain, outtake, camera);
    }

    public Ball getClosestBall() {
        return Ball.getClosestBallTo(DriverStation.getAlliance(), m_initPose.getTranslation());
    }

    public double getAngleToBall() {
        Ball ball = getClosestBall();
        return 0; // fix 
    }

    public double getDistanceToBall() {
        Ball ball = getClosestBall();
        return ball.m_position.getDistance(m_drivetrain.getPose().getTranslation()); // fix
    }

    private Command getCommand(Path path) {
        switch (path) {
            case TAXI:
                return new DriveForward(TAXI_DRIVE_DISTANCE, m_drivetrain);
            case ONE_BALL:
                return new SequentialCommandGroup(m_turnAndShoot);
            case ONE_BALL_TAXI:
                return new SequentialCommandGroup(getCommand(Path.ONE_BALL), m_turnBack, getCommand(Path.TAXI));
            case TWO_BALL_TAXI:
                return new SequentialCommandGroup(getCommand(Path.ONE_BALL),
                    new TurnAngle(() -> getAngleToBall(), m_drivetrain),
                    new DriveForward(() -> getDistanceToBall(), m_drivetrain),
                    m_turnAndShoot);
            default:
                return new InstantCommand();
        }
    }

    public Command getCommand() {
        if (m_initPose.equals(InitialPose.INVALID.pose)) return new InstantCommand();
        return getCommand(m_path);
    }

    public void setPath(Path path) {
        m_path = path;
    }

    public void setInitPose(Pose2d pose) {
        m_initPose = pose;
    }

    public void setInitPose(InitialPose pose) {
        setInitPose(pose.pose);
    }

    public void resetOdometry() {
        m_drivetrain.setOdometry(m_initPose, m_drivetrain.gyroRotation());
    }
}
