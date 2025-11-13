import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create the main application window
        JFrame frame = new JFrame("Pathfinding Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create the main panel for the grid
        GridPanel gridPanel = new GridPanel();

        // --- Create Buttons ---
        JButton startButton = new JButton("Start");
        JButton wallButton = new JButton("Draw Walls");
        JButton startNodeButton = new JButton("Set Start");
        JButton endNodeButton = new JButton("Set End");

        // --- Button Actions ---
        startButton.addActionListener(e -> gridPanel.startPathfinding());
        wallButton.addActionListener(e -> gridPanel.setBrushToWalls());
        startNodeButton.addActionListener(e -> gridPanel.setBrushToStart());
        endNodeButton.addActionListener(e -> gridPanel.setBrushToEnd());

        // --- Layout ---
        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(wallButton);
        buttonPanel.add(startNodeButton);
        buttonPanel.add(endNodeButton);
        buttonPanel.add(startButton);

        // Add panels to the frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Finalize and show the window
        frame.pack(); // Sizes the window to fit the preferred size of its components
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }
}
