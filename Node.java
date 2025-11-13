import java.awt.Color;
import java.awt.Graphics;

public class Node {
    //--- Core Properties ---
    public int row, col;
    public boolean isWall;
    public boolean isStart, isEnd;

    //--- A* Algorithm Properties ---
    public int gCost; // Cost from the start node
    public int hCost; // Heuristic (estimated) cost to the end node
    public int fCost; // The total cost (gCost + hCost)
    public Node parent; // The node we came from

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        
        // Default values
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        
        // A* costs
        this.gCost = Integer.MAX_VALUE;
        this.hCost = 0;
        this.fCost = Integer.MAX_VALUE;
        this.parent = null;
    }

    // A helper method to reset the node for a new pathfind
    public void reset() {
        this.gCost = Integer.MAX_VALUE;
        this.hCost = 0;
        this.fCost = Integer.MAX_VALUE;
        this.parent = null;
    }
}