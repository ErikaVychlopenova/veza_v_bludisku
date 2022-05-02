package sk.stuba.fei.uim.oop;

import javax.swing.*;
import java.awt.*;

public class Maze extends JPanel {

    private final int CELL_SIZE = 35;
    private final int OFFSETX = 80;
    private final int OFFSETY = 20;
    private Cell[][] cells;

    public Maze(Cell[][] cells) {
        this.cells = cells;
    }

    public void update(Cell[][] cells) {
        this.cells = cells;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x].isOccupied()) {
                    g.setColor(Color.blue);
                } else if (cells[y][x] instanceof Finish) {
                    g.setColor(Color.red);
                } else
                    g.setColor(Color.orange);
                g.fillRect(x * CELL_SIZE + OFFSETX, y * CELL_SIZE + OFFSETY, CELL_SIZE, CELL_SIZE);
                renderCell(g, cells[y][x], y, x);
            }
        }
    }

    public void renderCell(Graphics g, Cell cell, int y, int x) {
        Graphics2D g2D = (Graphics2D) g;
        boolean[] walls = cell.getWalls();
        for (int i = 0; i < walls.length; i++) {
            int beginPosX = x * CELL_SIZE + OFFSETX;
            int beginPosY = y * CELL_SIZE + OFFSETY;
            int endPosX = x * CELL_SIZE + OFFSETX;
            int endPosY = y * CELL_SIZE + OFFSETY;
            if (walls[i]) {
                g.setColor(Color.darkGray);
                if (i == 0) {
                    endPosX += CELL_SIZE;
                } else if (i == 1) {
                    beginPosX += CELL_SIZE;
                    endPosX += CELL_SIZE;
                    endPosY += CELL_SIZE;
                } else if (i == 2) {
                    beginPosY += CELL_SIZE;
                    endPosX += CELL_SIZE;
                    endPosY += CELL_SIZE;
                } else if (i == 3) {
                    beginPosY += CELL_SIZE;
                }
                g2D.setStroke(new BasicStroke(3));
                g2D.drawLine(beginPosX, beginPosY, endPosX, endPosY);
            }
        }
    }
}
