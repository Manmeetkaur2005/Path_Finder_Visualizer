import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.*;

public class Pathfinder implements ActionListener {

    private GridPanel gridPanel;
    private Node[][] grid;
    private Node startNode, endNode;
    
    // This is the key DSA! A Priority Queue for the "Open List".
    // It's sorted by the node's fCost.
    private PriorityQueue<Node> openList;
    
    // A HashSet for the "Closed List" (nodes we've already checked)
    // A HashSet is very fast for "contains" checks (O(1) average).
    private Set<Node> closedList;
    
    // The final path
    private ArrayList<Node> path;
    
    // The Swing Timer for animation
    private Timer timer;

    public Pathfinder(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
        this.timer = new Timer(50, this); // 50ms delay between steps
    }

    public void start(Node startNode, Node endNode) {
        // Reset everything for a new run
        this.startNode = startNode;
        this.endNode = endNode;
        
        // Initialize the Open List using a Comparator
        openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.fCost));
        
        closedList = new HashSet<>();
        path = new ArrayList<>();

        // Reset all nodes in the grid
        grid = gridPanel.getGrid();
        for (Node[] row : grid) {
            for (Node node : row) {
                node.reset();
            }
        }

        // Setup the start node
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startNode);
        startNode.fCost = startNode.hCost;
        
        openList.add(startNode);
        
        // Start the animation timer
        timer.start();
    }

    // This method is called by the Timer every 50ms
    @Override
    public void actionPerformed(ActionEvent e) {
        if (openList.isEmpty()) {
            // No path found
            timer.stop();
            JOptionPane.showMessageDialog(gridPanel, "No path found!");
            return;
        }

        // --- This is ONE step of the A* Algorithm ---

        // 1. Get the best node (lowest fCost) from the open list
        Node currentNode = openList.poll(); // .poll() retrieves and removes the head

        // 2. Add it to the closed list
        closedList.add(currentNode);

        // 3. Check for the end
        if (currentNode == endNode) {
            timer.stop();
            retracePath();
            gridPanel.repaint();
            return;
        }

        // 4. Process all neighbors
        for (Node neighbor : getNeighbors(currentNode)) {
            if (neighbor.isWall || closedList.contains(neighbor)) {
                continue; // Skip wall or already checked node
            }

            // Calculate new gCost for the neighbor
            int newGCost = currentNode.gCost + 1; // 1 is the cost to move to a neighbor

            if (newGCost < neighbor.gCost) {
                // This is a better path to this neighbor
                neighbor.parent = currentNode;
                neighbor.gCost = newGCost;
                neighbor.hCost = calculateHeuristic(neighbor);
                neighbor.fCost = neighbor.gCost + neighbor.hCost;

                if (!openList.contains(neighbor)) {
                    openList.add(neighbor);
                }
            }
        }
        
        // Tell the GridPanel to redraw to show the progress
        gridPanel.repaint();
    }

    // Heuristic: Manhattan Distance (good for grids)
    private int calculateHeuristic(Node node) {
        int dx = Math.abs(node.col - endNode.col);
        int dy = Math.abs(node.row - endNode.row);
        return dx + dy; // Manhattan distance
    }

    // Helper to get all valid neighbors (up, down, left, right)
    private ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        int r = node.row;
        int c = node.col;
        int gridSize = GridPanel.GRID_SIZE;

        if (r > 0) neighbors.add(grid[r - 1][c]); // Up
        if (r < gridSize - 1) neighbors.add(grid[r + 1][c]); // Down
        if (c > 0) neighbors.add(grid[r][c - 1]); // Left
        if (c < gridSize - 1) neighbors.add(grid[r][c + 1]); // Right

        return neighbors;
    }

    // Helper to build the final path by following the 'parent' links
    private void retracePath() {
        Node current = endNode;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path); // Reverse it to be from start to end
    }

    // The Pathfinder's own draw method
    public void draw(Graphics g, int nodeSize) {
        // Draw Open List (light blue)
        g.setColor(new Color(173, 216, 230, 150)); // Light blue with transparency
        for (Node node : openList) {
            g.fillRect(node.col * nodeSize, node.row * nodeSize, nodeSize, nodeSize);
        }
        
        // Draw Closed List (light red)
        g.setColor(new Color(250, 128, 114, 150)); // Light red with transparency
        for (Node node : closedList) {
            g.fillRect(node.col * nodeSize, node.row * nodeSize, nodeSize, nodeSize);
        }
        
        // Draw Final Path (purple)
        g.setColor(new Color(128, 0, 128, 200)); // Purple
        for (Node node : path) {
            g.fillRect(node.col * nodeSize, node.row * nodeSize, nodeSize, nodeSize);
        }
    }
}