package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.commands.AimAndShoot;
import frc.robot.commands.DriveForward;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.RunConveyor;
import frc.robot.commands.TurnAngle;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Connection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Outtake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutonomousManager {
    
    public enum Path {
        NONE, TAXI, ONE_BALL, ONE_BALL_TAXI;
    }

    // field data
    private final double FIELD_WIDTH = 8.23 * 2;
    private final double FIELD_LENGTH = 16.46 * 2;
    public final Translation2d CENTER_FIELD = new Translation2d(FIELD_WIDTH/2, FIELD_LENGTH/2);

    public final Path DEFAULT_PATH = Path.NONE;

    private Command m_turnAround, m_shoot;
    @SuppressWarnings("unused") private final Command m_pause;

    private final double TAXI_DRIVE_DISTANCE = 2.15;

    private boolean useVision = false;
    private Path m_path;
    private Drivetrain m_drivetrain;
    private Connection m_connection;
    private Outtake m_outtake;
    private XboxController m_controller;

    public AutonomousManager(Path path, Drivetrain drivetrain, Connection connection, Outtake outtake, Camera camera, XboxController controller) {
        m_path = path;
        m_drivetrain = drivetrain;
        m_connection = connection;
        m_outtake = outtake;
        m_controller = controller;

        m_turnAround = new TurnAngle(180, drivetrain);
        m_pause = new WaitUntilCommand(() -> m_controller.getStartButton());

        if (useVision) {
            m_shoot = new AimAndShoot(drivetrain, connection, outtake, camera);
        } else {
            m_shoot = new ParallelCommandGroup(
                new OuttakeCommand(m_outtake, 1),
                new WaitCommand(0.5).andThen(new RunConveyor(m_connection))
            ).withTimeout(2);
        }
    }

    public AutonomousManager(Drivetrain drivetrain, Connection connection, Outtake outtake, Camera camera, XboxController controller) {
        this(Path.NONE, drivetrain, connection, outtake, camera, controller);
    }

    private Command getCommand(Path path) {
        switch (path) {
            case TAXI: // facing outward
                return new DriveForward(TAXI_DRIVE_DISTANCE, m_drivetrain);
            case ONE_BALL: // facing inward
                return m_shoot;
            case ONE_BALL_TAXI: // facing inward
                return new SequentialCommandGroup(
                    getCommand(Path.ONE_BALL),
                    m_turnAround,
                    getCommand(Path.TAXI));
            default:
                return new InstantCommand();
        }
    }

    public Command getCommand() {
        return getCommand(m_path);
    }

    public void setPath(Path path) {
        m_path = path;
    }
}
