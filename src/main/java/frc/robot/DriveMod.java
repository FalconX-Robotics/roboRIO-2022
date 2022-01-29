package frc.robot;

import java.util.function.BiFunction;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class DriveMod {
    private SendableChooser<Mod> m_chooser;
    private final Mod DEFAULT_MOD = Mod.NONE;
    private static double[] t_map = new double[50];

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
 
    public static enum Mod {
        NONE("NONE", (s, r) -> s, (s, r) -> r),
        STATIONARY("STATIONARY", (s, r) -> 0., (s, r) -> 0.),
        SLOW("SLOW", (s, r) -> MathUtil.clamp(s, -0.1, 0.1), (s, r) -> MathUtil.clamp(r, -0.1, 0.1)),
        FAST_ONLY("FAST ONLY", (s, r) -> Math.signum(s), (s, r) -> Math.signum(r)),
        SQUARED("SQUARED", (s, r) -> s*s, (s, r) -> r*r),
        SIN("SIN", (s, r) -> s, (s, r) -> {
            t_map[0] += 0.02;
            return MathUtil.clamp(Math.sin(t_map[0]), -0.5, 0.5);
        }),
        SPIN("SPIN", (s, r) -> s, (s, r) -> Math.abs(s) <= 0.2 ? 0.2 : 0.),
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
