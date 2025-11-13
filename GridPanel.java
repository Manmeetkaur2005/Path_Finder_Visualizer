import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GridPanel extends JPanel {
    
    // Grid settings
    public static final int GRID_SIZE = 25; // 25x25 grid
    public static final int NODE_SIZE = 20; // 20 pixels per node

    private Node[][] grid;
    private Node startNode;
    private Node endNode;
    
    private Pathfinder pathfinder; // This is our algorithm class (Step 4)
    
    // For selecting what to draw
    private boolean settingWalls = true; 
    private boolean settingStart = false;
    private boolean settingEnd = false;

    public GridPanel() {
        this.setPreferredSize(new Dimension(GRID_SIZE * NODE_SIZE, GRID_SIZE * NODE_SIZE));
        this.grid = new Node[GRID_SIZE][GRID_SIZE];
        this.pathfinder = new Pathfinder(this); // Give the pathfinder access to this panel

        // Initialize the grid with Node objects
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                grid[r][c] = new Node(r, c);
            }
        }
        
        // Add mouse controls
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }
        });
    }

    // Public method for the Pathfinder to get the grid
    public Node[][] getGrid() {
        return grid;
    }
    
    // Public methods for the Pathfinder to get start/end
    public Node getStartNode() { return startNode; }
    public Node getEndNode() { return endNode; }

    // --- Mouse Handling Methods ---

    private void handleMouseClick(MouseEvent e) {
        int row = e.getY() / NODE_SIZE;
        int col = e.getX() / NODE_SIZE;

        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return;

        Node node = grid[row][col];

        if (settingStart) {
            if(startNode != null) startNode.isStart = false; // Remove old start
            startNode = node;
            startNode.isStart = true;
            startNode.isWall = false; // Can't be a wall
        } else if (settingEnd) {
            if(endNode != null) endNode.isEnd = false; // Remove old end
            endNode = node;
            endNode.isEnd = true;
            endNode.isWall = false; // Can't be a wall
        } else { // settingWalls
            node.isWall = !node.isWall; // Toggle wall
        }
        repaint();
    }
    
    private void handleMouseDrag(MouseEvent e) {
        int row = e.getY() / NODE_SIZE;
        int col = e.getX() / NODE_SIZE;

        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return;
        
        // Only allow dragging to create walls
        if (settingWalls) {
            Node node = grid[row][col];
            if (!node.isStart && !node.isEnd) {
                node.isWall = true;
            }
            repaint();
        }
    }

    // --- Public Methods to Change Brush ---
    public void setBrushToWalls() {
        settingWalls = true;
        settingStart = false;
        settingEnd = false;
    }
    public void setBrushToStart() {
        settingWalls = false;
        settingStart = true;
        settingEnd = false;
    }
    public void setBrushToEnd() {
        settingWalls = false;
        settingStart = false;
        settingEnd = true;
    }
    
    // Public method to start the pathfinding
    public void startPathfinding() {
        if (startNode != null && endNode != null) {
            pathfinder.start(startNode, endNode);
        } else {
            JOptionPane.showMessageDialog(this, "Please set a Start and End node.");
        }
    }

    // --- Drawing Method ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Node node = grid[r][c];

                // Determine color
                if (node.isStart) {
                    g.setColor(Color.GREEN);
                } else if (node.isEnd) {
                    g.setColor(Color.RED);
                } else if (node.isWall) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }

                // Draw the node
                g.fillRect(c * NODE_SIZE, r * NODE_SIZE, NODE_SIZE, NODE_SIZE);

                // Draw outline
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(c * NODE_SIZE, r * NODE_SIZE, NODE_SIZE, NODE_SIZE);
            }
        }
        
        // Let the pathfinder draw its progress (open/closed lists, path)
        if (pathfinder != null) {
            pathfinder.draw(g, NODE_SIZE);
        }
    }
}
