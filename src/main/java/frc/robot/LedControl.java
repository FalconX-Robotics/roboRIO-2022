package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class LedControl {
    static PWMSparkMax ledController = new PWMSparkMax(Constants.LED_CONTROLLER_PORT);

    public enum Pattern {
        kNone(0.0), kViolet(0.91), kRed(0.61), kBreathBlue(-0.15), kRainbow(-0.99), kRainbowParty(-0.97),
        kRainbowGlitter(-0.89), kConfetti(-0.87), kSinelonRainbow(-0.79), kBPMRainbow(-0.69), kFireLarge(-0.57),
        kTwinklesRainbow(-0.55), kColorWavesRainbow(-0.45), kLarsonScanner(-0.35), kLightChase(-0.29), 
        kShot(0.13), kBlack(0.99);

        private Double speed;

        private Pattern(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return this.speed;
        }
    }

    /**
     * Sets the led pattern according to values from:
     * <p>
     * www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
     * 
     * @param pattern the pattern to set the led to
     */
    public static void setLed(Pattern pattern) {
        ledController.set(pattern.getSpeed());
    }
}
