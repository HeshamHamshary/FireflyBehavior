import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainComponent extends JComponent implements Serializable {
    private int numOfFlies = 100;
    private final int timestep = 125;
    private Lock lock;
    private ArrayList<Firefly> fireflies;
    Timer timer = null;

    public MainComponent(){
        initializeFlies();
    }

    public void initializeFlies(){
        lock = new ReentrantLock();
        fireflies = new ArrayList<>();
        for(int i = 0; i< numOfFlies; i++){
            fireflies.add(new Firefly());
        }
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Flash and startle flies based on other methods
                lock.lock();
                try{
                    startleFlies();
                    for (int i = 0; i < numOfFlies; i++) {
                        Firefly currentFly = fireflies.get(i);
                        currentFly.incrementPhase();
                        currentFly.updateFly();
                        repaint();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }, 0,timestep);
    }

    public double getDistance(int x1, int x2, int y1, int y2){
        // Calculate distance via distance formula
        double xDistance = Math.pow((x1 - x2), 2);
        double yDistance = Math.pow((y1 - y2), 2);
        return Math.sqrt(xDistance + yDistance);
    }

    public void startleFlies(){
        // Startle flies based on neighbor flies
        for(int i = 0; i < numOfFlies; i++){
            Firefly currentFly = fireflies.get(i);
            int N = 0;
            for(int j = 0; j < numOfFlies; j++){
                if(fireflies.get(j) != currentFly) {
                    Firefly nextFly = fireflies.get(j);

                    double distance = getDistance(currentFly.getX(), nextFly.getX(), currentFly.getY(), nextFly.getY());
                    if (distance <= 100 && nextFly.isFlashing()){
                        N++;
                    }
                }
            }
            currentFly.startle(N);
        }
    }

    public void saveToFile() throws IOException {
        JFileChooser fileInput = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin", "dat");
        fileInput.setFileFilter(filter);
        fileInput.setApproveButtonText("Save");
        int click = fileInput.showOpenDialog(MainComponent.this);
        if(click == JFileChooser.APPROVE_OPTION){
            ObjectOutputStream toFile = new ObjectOutputStream(new FileOutputStream(fileInput.getSelectedFile()));
            for(Firefly f : fireflies){
                toFile.writeObject(f);
            }
        }
    }

    public void loadFromFile() throws IOException, ClassNotFoundException {
        JFileChooser fileInput = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin", "dat");
        fileInput.setFileFilter(filter);
        fileInput.setApproveButtonText("Load");
        int click = fileInput.showOpenDialog(MainComponent.this);
        if(click == JFileChooser.APPROVE_OPTION){
            ObjectInputStream fromFile = new ObjectInputStream(new FileInputStream(fileInput.getSelectedFile()));
            for(int i = 0; i < numOfFlies; i++){
                fireflies.set(i, (Firefly)fromFile.readObject());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < numOfFlies; i++) {
            g.drawImage(fireflies.get(i).getCurrentPhaseImage(), fireflies.get(i).getX(), fireflies.get(i).getY(), null);
        }
    }
}