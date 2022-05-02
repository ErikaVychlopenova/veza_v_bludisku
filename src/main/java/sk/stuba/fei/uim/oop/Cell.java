package sk.stuba.fei.uim.oop;

public class Cell {
    private boolean visited = false;
    private boolean[] walls = {true, true, true, true};
    private boolean isOccupied = false;
    private final int x;
    private final int y;

    public Cell(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void destroyWall(int index) {        // true -> wall on index
        this.walls[index] = false;              //  false -> no wall on index
    }

    public boolean[] getWalls() {
        return this.walls;
    }

    public boolean getOneWall(int index){
        return walls[index];
    }

    public void setWalls(boolean[] walls) {
        this.walls = walls;
    }

    public void visit() {
        this.isOccupied = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void unVisit() {
        this.isOccupied = false;
    }

    public boolean isOccupied() {
        return this.isOccupied;
    }   // if player is standing on this cell
}
