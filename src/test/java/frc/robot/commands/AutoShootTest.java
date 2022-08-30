package frc.robot.commands;

import static org.junit.Assert.*;

import java.util.OptionalDouble;
import java.util.Random;

import com.revrobotics.REVPhysicsSim;

import org.junit.*;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Outtake;

public class AutoShootTest {
    private class MockCamera extends Camera {
        private double distance = 0.0;
        private double yaw = 0.0;

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public void setYaw(double yaw) {
            this.yaw = yaw;
        }

        @Override
        public OptionalDouble targetDistance() {
            return OptionalDouble.of(distance);
        }

        @Override
        public OptionalDouble targetDistance(double y) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public OptionalDouble targetYaw() {
            return OptionalDouble.of(yaw);
        }

        @Override 
        public OptionalDouble targetYaw(double y) {
            throw new UnsupportedOperationException("unimplemented");

        }
    }

    private MockCamera camera;
    private Outtake outtake;
    private CommandScheduler scheduler;

    @Before
    public void setup() {
        assert HAL.initialize(500, 0);
        camera = new MockCamera(); // Mock camera
        outtake = new Outtake();

        scheduler = CommandScheduler.getInstance();
        DriverStationSim.setEnabled(true);
        DriverStationSim.setTest(true);
    }
    
    @After
    public void shutdown() throws Exception {
        outtake.close();
        camera.close();
    }

    Random randomGenerator = new Random(6662);

    private void runSimulationForTime(double seconds) {
        double elapsed = 0;
        while (elapsed < seconds) {
            scheduler.run();

            RoboRioSim.setVInVoltage(
                BatterySim.calculateDefaultBatteryLoadedVoltage(
                    outtake.simulationGetCurrentDrawAmps() +
                    randomGenerator.nextGaussian() * 5 + 20.
                )
            );
		    REVPhysicsSim.getInstance().run();

            elapsed += 0.020;
        }
    }

    @Test
    public void testAutoShootRunsMotors() {
        camera.setDistance(20.);
        AutoShoot autoShootCommand = new AutoShoot(outtake, camera);
        scheduler.schedule(autoShootCommand);
        runSimulationForTime(1);
        
    }
}