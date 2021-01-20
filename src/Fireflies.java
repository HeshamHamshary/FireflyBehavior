import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Fireflies {
    private static void createWindow(){
        // Create and set up the window
        JFrame frame = new JFrame("Fireflies");
        frame.setSize(new Dimension(700, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JButton saveToFile = new JButton("Save to File");
        JButton loadFromFile = new JButton("Load from File");
        JLabel choose = new JLabel("Choose:");
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(10, 1));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(choose);
        buttonPanel.add(saveToFile);
        buttonPanel.add(loadFromFile);

        MainComponent mainComp = new MainComponent();
        panel.add(mainComp, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        // Save to file
        saveToFile.addActionListener(e -> {
            try {
                mainComp.saveToFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Load from file
        loadFromFile.addActionListener(e -> {
            try {
                mainComp.loadFromFile();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });


        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        createWindow();
    }
}
