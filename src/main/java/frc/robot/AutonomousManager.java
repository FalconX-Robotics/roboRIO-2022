package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.commands.AimAndShoot;
import frc.robot.commands.DriveForward;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurnAngle;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Connection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
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

    // field data
    private final double FIELD_WIDTH = 8.23;
    private final double FIELD_LENGTH = 16.46;
    public final Translation2d CENTER_FIELD = new Translation2d(FIELD_WIDTH/2, FIELD_LENGTH/2);

    public final InitialPose DEFAULT_INIT_POSE = InitialPose.INVALID;
    public final Path DEFAULT_PATH = Path.NONE;

    private final Command m_turnAndShoot;
    @SuppressWarnings("unused") private final Command m_turnBack;
    private final Command m_faceInward;
    private final Command m_faceOutward;
    @SuppressWarnings("unused") private final Command m_pause;

    private final double TAXI_DRIVE_DISTANCE = 2.15;
    private final double SHOOT_DISTANCE_FROM_CENTER = 3.2;

    private Path m_path;
    private Pose2d m_initPose;
    private ArrayList<Ball> m_ballList;
    private Drivetrain m_drivetrain;
    private Intake m_intake;
    private Connection m_connection;
    private Outtake m_outtake;
    private Camera m_camera;
    private XboxController m_controller;

    public AutonomousManager(Path path, Pose2d initPose, ArrayList<Ball> ballList, Drivetrain drivetrain, Intake intake, Connection connection, Outtake outtake, Camera camera, XboxController controller) {
        m_path = path;
        m_initPose = initPose;
        m_ballList = ballList;
        m_drivetrain = drivetrain;
        m_intake = intake;
        m_connection = connection;
        m_outtake = outtake;
        m_camera = camera;
        m_controller = controller;

        m_turnAndShoot = new AimAndShoot(m_drivetrain, m_connection, m_outtake, m_camera);
        m_turnBack = new TurnAngle(() -> -m_drivetrain.getRotation().getDegrees(), m_drivetrain);
        m_faceInward = TurnAngle.toRotation(() -> getAngleToCenter(), m_drivetrain);
        m_faceOutward = TurnAngle.toRotation(() -> 180 - getAngleToCenter(), m_drivetrain);
        m_pause = new WaitUntilCommand(() -> m_controller.getStartButton());
    }

    public AutonomousManager(Path path, InitialPose initPose, ArrayList<Ball> ballList, Drivetrain drivetrain, Intake intake, Connection connection, Outtake outtake, Camera camera, XboxController controller) {
        this(path, initPose.pose, ballList, drivetrain, intake, connection, outtake, camera, controller);
    }

    public AutonomousManager(Drivetrain drivetrain, Intake intake, Connection connection, Outtake outtake, Camera camera, XboxController controller) {
        this(Path.NONE, InitialPose.INVALID, new ArrayList<Ball>(), drivetrain, intake, connection, outtake, camera, controller);
    }

    public Ball getClosestBall() {
        Alliance alliance = DriverStation.getAlliance(); // works in comp?
        return Ball.getClosestBallTo(m_ballList, alliance, alliance == Alliance.Blue, m_initPose.getTranslation());
    }

    public double getAngleToBall() {
        Ball ball = getClosestBall();
        Translation2d v = ball.m_position.plus(m_drivetrain.getPose().getTranslation().unaryMinus());
        return Math.atan2(v.getY(), v.getX())*(160./Math.PI);
    }

    public double getDistanceToBall() {
        Ball ball = getClosestBall();
        return ball.m_position.getDistance(m_drivetrain.getPose().getTranslation());
    }

    public double getAngleToCenter() {
        Pose2d pose = m_drivetrain.getPose();
        Translation2d v = pose.getTranslation().plus(CENTER_FIELD.unaryMinus());
        double a = Math.atan2(v.getY(), v.getX()) * (160 / Math.PI);
        return a + 180; // + 180 to face inward instead of outward
    }

    public double getDistanceFromCenter(double d) {
        Pose2d pose = m_drivetrain.getPose();
        return CENTER_FIELD.getDistance(pose.getTranslation()) - d;
    }

    private Command getCommand(Path path) {
        switch (path) {
            case TAXI: // assumes to be facing outwards
                return new SequentialCommandGroup(m_faceOutward, new DriveForward(TAXI_DRIVE_DISTANCE, m_drivetrain));
            case ONE_BALL: // assumes to be facing towards center
                return new SequentialCommandGroup(m_faceInward, m_turnAndShoot);
            case ONE_BALL_TAXI: // assumes to be facing towards center
                return new SequentialCommandGroup(getCommand(Path.ONE_BALL), getCommand(Path.TAXI));
            case TWO_BALL_TAXI: // assumes to be facing same as ONE_BALL_TAXI
                return new SequentialCommandGroup(
                    getCommand(Path.ONE_BALL_TAXI),
                    TurnAngle.toRotation(this::getAngleToBall, m_drivetrain),
                    new ParallelDeadlineGroup(new DriveForward(() -> getDistanceToBall() + 0.3, m_drivetrain), new IntakeCommand(m_intake)),
                    m_faceInward,
                    new DriveForward(() -> getDistanceFromCenter(SHOOT_DISTANCE_FROM_CENTER), m_drivetrain),
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

    public void addBalls(Ball... balls) {
        for (Ball ball : balls) {
            m_ballList.add(ball);
        }
    }

    public void resetSensors() {
        m_drivetrain.resetEncoder();
        m_drivetrain.resetGyroYaw();
        m_drivetrain.setOdometry(m_initPose, m_drivetrain.getRotation());
    }
}
