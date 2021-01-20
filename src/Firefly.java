import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

class Firefly implements Serializable {
    double currentPhase;
    private final double minPhase = 0;
    private final double maxPhase = 2 * Math.PI;
    private final double flashingFreq = 0.785;
    final double K = 0.1;
    int x;
    int y;
    boolean isFlashing;
    transient BufferedImage currentPhaseImage = null;

    private double randBetween(double min, double max){
        return (Math.random()*(max-min))+min;
    }

    private int randBetween(int min, int max){ return (int)(Math.random()*(max-min)+min); }

    private static BufferedImage loadPhaseImage(boolean flashing){
        // Load firefly image based on phase
        try {
            if(flashing) {
                return ImageIO.read(new File("fireflyOn.png"));
            } else {
                return ImageIO.read(new File("fireflyOff.png"));
            }
        } catch (IOException e) {
        }
        return null;
    }

    Firefly() {
        this.currentPhase = randBetween(minPhase, maxPhase);
        this.isFlashing = false;
        this.currentPhaseImage = loadPhaseImage(false);
        this.x = randBetween(20, 520);
        this.y = randBetween(20, 520);
    }

    private double getCurrentPhase() {
        return currentPhase;
    }

    private void setCurrentPhase(double currentPhase) {
        this.currentPhase = currentPhase;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    boolean isFlashing() {
        return isFlashing;
    }

    private void setFlashing(boolean flashing) {
        isFlashing = flashing;
    }

    BufferedImage getCurrentPhaseImage() {
        return currentPhaseImage;
    }

    private void setCurrentPhaseImage(BufferedImage currentPhaseImage) {
        this.currentPhaseImage = currentPhaseImage;
    }

    void startle(int numOfFlies){
        this.currentPhase += flashingFreq + K * numOfFlies * Math.sin(maxPhase - this.currentPhase);
    }

    void incrementPhase(){
        this.currentPhase += 0.785;
    }

    private void flash(){
        this.setCurrentPhaseImage(loadPhaseImage(true));
        this.setFlashing(true);
        this.setCurrentPhase(0);
    }

    private void dim(){
        this.currentPhaseImage = loadPhaseImage(false);
        this.setFlashing(false);
    }

    void updateFly(){
        if (this.getCurrentPhase() >= maxPhase) {
            this.flash();
        } else {
            this.dim();
        }
    }

}
