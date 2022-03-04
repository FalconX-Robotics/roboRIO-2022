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
        m_chooser.setDefaultOption(DEFAULT_MOD.name(), DEFAULT_MOD);
        for (Mod mod : Mod.values()) {
            m_chooser.addOption(mod.name(), mod);
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
        NONE((s, r) -> s, (s, r) -> r),
        STATIONARY((s, r) -> 0., (s, r) -> 0.),
        SLOW((s, r) -> s/3., (s, r) -> r/3.),
        FAST_ONLY((s, r) -> inTolerance(s) ? 0. : Math.signum(s), (s, r) -> inTolerance(r) ? 0. : Math.signum(r)),
        SIN((s, r) -> !inTolerance(s) && !inTolerance(r) ? Math.cos(tMap[0])/2. : 0.,
            (s, r) -> !inTolerance(s) && !inTolerance(r) ? Math.sin((tMap[0]+=0.02) - 0.02)/2. : r
        ),
        SPIN((s, r) -> s, (s, r) -> inTolerance(s) ? 0.4 : 0.);

        public final BiFunction<Double, Double, Double> speedSupplier, rotationSupplier;
        
        private Mod(BiFunction<Double, Double, Double> speedSupplier, BiFunction<Double, Double, Double> rotationSupplier) {
            this.speedSupplier = speedSupplier;
            this.rotationSupplier = rotationSupplier;
        }
    }
}
