package sk.stuba.fei.uim.oop;


import java.util.LinkedList;
import java.util.Stack;

public class MazeGeneration {
    private final int sizeX = 12;
    private final int sizeY = 12;
    private Cell[][] cells;
    private Player player;

    public MazeGeneration() {
        setPlayer();
        init();
        renderPath();
    }

    private void init() {
        cells = new Cell[sizeY][sizeX];
        Cell finish = setFinish();
        for (int y = 0; y < sizeY; y++) { //
            for (int x = 0; x < sizeX; x++) {
                if (finish.getX() == x && finish.getY() == y)
                    cells[y][x] = finish;
                else
                    cells[y][x] = new Cell(y, x);
            }
        }
        cells[player.getY()][player.getX()].visit();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer() {
        int x = (int) Math.floor(Math.random() * 2) == 1 ? sizeX - 1 : 0;
        int y = (int) Math.floor(Math.random() * 2) == 1 ? sizeY - 1 : 0;
        this.player = new Player(y, x);
    }

    private Finish setFinish() {
        int y = player.getY() == sizeY - 1 ? 0 : sizeY - 1;
        int x = player.getX() == sizeX - 1 ? 0 : sizeX - 1;
        return new Finish(y, x);
    }

    private void renderPath() {      //random DFS
        Cell current;
        Cell nextCell;
        int indexMirrored = 0;
        int index;
        Stack<Cell> stack = new Stack<>();
        LinkedList<Cell> neighbours;
        int r = (int) Math.floor(Math.random() * (sizeY - 1));
        int c = (int) Math.floor(Math.random() * (sizeX - 1));
        current = cells[r][c];  // random start cell
        stack.push(current);
        while (current != null) {
            current.setVisited(true);
            neighbours = getNeighbours(current);

            boolean wereVisited = true;
            for (Cell neighbour : neighbours) {         //check if all neighbours are visited
                if (neighbour != null && !neighbour.isVisited()) {
                    wereVisited = false;
                }
            }
            if (wereVisited) {      // if all neighbours are visited, backtrack to last cell and repeat
                if (stack.size() == 0) {
                    break;
                }
                current = stack.pop();
                neighbours = getNeighbours(current);
            }
            index = (int) Math.floor(Math.random() * neighbours.size());
            nextCell = neighbours.get(index);

            switch (index) {
                case 0:  indexMirrored = 2; break;
                case 1:  indexMirrored = 3; break;
                case 2:  indexMirrored = 0; break;
                default: indexMirrored = 1; break;
            }
            if (nextCell != null && !nextCell.isVisited()) {
                current.destroyWall(index);
                nextCell.destroyWall(indexMirrored);
                current = nextCell;
                stack.push(current);
            }
        }
    }


    private LinkedList<Cell> getNeighbours(Cell current) {
        LinkedList<Cell> list = new LinkedList<>();
        //left cell
        if (current.getY() > 0)
            list.add(cells[current.getY() - 1][current.getX()]);
        else
            list.add(null);
        //bottom cell
        if (current.getX() < cells.length - 1)
            list.add(cells[current.getY()][current.getX() + 1]);
        else
            list.add(null);
        //right cell
        if (current.getY() < cells.length - 1)
            list.add(cells[current.getY() + 1][current.getX()]);
        else
            list.add(null);
        //top cell
        if (current.getX() > 0)
            list.add(cells[current.getY()][current.getX() - 1]);
        else
            list.add(null);
        return list;
    }
}
