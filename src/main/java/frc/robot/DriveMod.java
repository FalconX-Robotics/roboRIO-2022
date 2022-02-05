package frc.robot;

import java.util.function.BiFunction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class DriveMod {
    private SendableChooser<Mod> m_chooser;
    private final Mod DEFAULT_MOD = Mod.NONE;
    private static double[] tMap = new double[50];
    private static final double TOLERANCE = 0.2;

    public DriveMod(SendableChooser<Mod> chooser) {
        m_chooser = chooser;
        m_chooser.setDefaultOption(DEFAULT_MOD.name, DEFAULT_MOD);
        for (Mod mod : Mod.values()) {
            m_chooser.addOption(mod.name, mod);
        }
    }

    public double getSpeed(double x, double y) {
        return m_chooser.getSelected().speedSupplier.apply(x, y);
    }

    public double getRotation(double x, double y) {
        return m_chooser.getSelected().rotationSupplier.apply(x, y);
    }

    private static boolean inTolerance(double x) {
        return x <= TOLERANCE;
    }

    public static enum Mod {
        NONE("NONE", (s, r) -> s, (s, r) -> r),
        STATIONARY("STATIONARY", (s, r) -> 0., (s, r) -> 0.),
        SLOW("SLOW", (s, r) -> s/4., (s, r) -> r/4.),
        FAST_ONLY("FAST_ONLY", (s, r) -> inTolerance(s) ? 0. : Math.signum(s), (s, r) -> inTolerance(r) ? 0. : Math.signum(r)),
        SQUARED("SQUARED", (s, r) -> s*s * Math.signum(s), (s, r) -> r*r * Math.signum(r)),
        SIN("SIN",
            (s, r) -> !inTolerance(s) ? Math.cos(tMap[0])/5 : 0.,
            (s, r) -> !inTolerance(s) && inTolerance(r) ? Math.sin((tMap[0]+=0.02) - 0.02)/5 : r
        ),
        SPIN("SPIN", (s, r) -> s, (s, r) -> inTolerance(s) ? 0.4 : 0.),
        RANDOM("RANDOM", (s, r) -> (Math.random()-0.5)/5, (s, r) -> (Math.random()-0.5)/5);

        public final String name;
        public final BiFunction<Double, Double, Double> speedSupplier, rotationSupplier;
        
        private Mod(String name, BiFunction<Double, Double, Double> speedSupplier, BiFunction<Double, Double, Double> rotationSupplier) {
            this.name = name;
            this.speedSupplier = speedSupplier;
            this.rotationSupplier = rotationSupplier;
        }
    }
}
