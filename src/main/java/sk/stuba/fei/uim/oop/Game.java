package sk.stuba.fei.uim.oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class Game extends JFrame implements KeyListener, MouseListener, MouseMotionListener {

    private final int CELL_SIZE = 35;
    private final JPanel buttons = new JPanel(new GridBagLayout());
    private final JPanel window = new JPanel();
    private final JPanel labyrinth = new JPanel();
    private final JButton reset = new JButton("Reset");
    private final JButton up = new JButton("Up / W");
    private final JButton right = new JButton("Right / D");
    private final JButton down = new JButton("Down / S");
    private final JButton left = new JButton("Left / A");
    private final JLabel counterLabel = new JLabel();
    private Maze maze;
    private Cell[][] cells;
    private Player player;
    private int counter;


    public Game(int width, int height) {
        this.counter = 0;
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Veža v bludisku");
        addKeyListener(this);
        labyrinth.addMouseListener(this);
        setFocusable(true);
        addButtons();
        init();
        newGame();
        render();
        this.setVisible(true);
    }

    private void newGame() {
        MazeGeneration mazeGeneration = new MazeGeneration();
        this.cells = mazeGeneration.getCells();
        this.player = mazeGeneration.getPlayer();
    }

    private void init() {
        window.setLayout(null);
        labyrinth.setBounds(5, 5, this.getWidth() - 26, this.getHeight() - buttons.getHeight() - 138);
        labyrinth.setLayout(new GridLayout(15, 15, 0, 0));
        labyrinth.setSize(1000, 8000);
        window.add(labyrinth);
        this.add(window);
    }

    private void addButtons() {
        JPanel menu = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(150, 50);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttons.add(up, gbc);
        up.addActionListener(e -> moveUp());
        up.setFocusable(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        buttons.add(down, gbc);
        down.addActionListener(e -> moveDown());
        down.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttons.add(left, gbc);
        left.addActionListener(e -> moveLeft());
        left.setFocusable(false);
        gbc.gridx = 2;
        gbc.gridy = 1;
        buttons.add(right, gbc);
        right.addActionListener(e -> moveRight());
        right.setFocusable(false);
        buttonPanel.add(buttons);
        panelsTogether(menu, buttonPanel);
    }

    private void panelsTogether(JPanel menu, JPanel buttonPanel) {
        GridBagConstraints gbc1 = new GridBagConstraints();
        JPanel resetPanel = new JPanel();
        JPanel counterPanel = new JPanel();
        counterPanel.setLayout(new GridBagLayout());

        resetPanel.setLayout(new GridBagLayout());
        resetPanel.add(reset, gbc1);
        reset.addActionListener(e -> resetGame());
        reset.setFocusable(false);
        counterPanel.add(counterLabel, gbc1);
        updateCounter();
        menu.add(resetPanel, BorderLayout.EAST);
        menu.add(counterPanel, BorderLayout.WEST);
        menu.add(buttonPanel, BorderLayout.CENTER);
        add(menu, BorderLayout.SOUTH);
    }

    public void update() {
        this.cells[player.getY()][player.getX()].visit();
        if (isWon()) {
            this.maze.update(cells);
            counter++;
            updateCounter();
            newGame();
            repaint();
        }
        repaint();
        this.maze.update(cells);
    }

    public boolean isWon() {
        return this.cells[player.getY()][player.getX()] instanceof Finish;
    }

    public void updateCounter() {
        this.counterLabel.setText("Wins: " + this.counter);
    }

    public void resetGame() {
        this.counter = 0;
        updateCounter();
        newGame();
        repaint();
        this.maze.update(cells);
    }

    public void render() {
        this.maze = new Maze(cells);
        labyrinth.add(maze);
    }

    private boolean isClear(int dir, boolean xAxis) {
        if (!cells[player.getY()][player.getX()].getWalls()[dirToWall(dir, xAxis)]) {
            this.cells[player.getY()][player.getX()].unVisit();
            return true;
        }
        return false;
    }

    private int dirToWall(int dir, boolean xAxis) {
        if (dir == -1) {
            if (xAxis) {
                return 3;
            }
            return 0;
        } else {
            if (xAxis) {
                return 1;
            }
            return 2;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            moveLeft();
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            moveRight();
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            moveUp();
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            moveDown();
        }
    }

    private void moveDown() {
        if (player.getY() < cells.length - 1 && isClear(1, false)) {
            player.moveY(1);
            update();
        }
    }

    private void moveUp() {
        if (player.getY() > 0 && isClear(-1, false)) {
            player.moveY(-1);
            update();
        }
    }

    private void moveRight() {
        if (player.getX() < cells.length - 1 && isClear(1, true)) {
            player.moveX(1);
            update();
        }
    }

    private void moveLeft() {
        if (player.getX() > 0 && isClear(-1, true)) {
            player.moveX(-1);
            update();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private int mouseXcoord(MouseEvent e) {
        return (e.getX() / CELL_SIZE - 2);
    }

    private int mouseYcoord(MouseEvent e) {
        return (e.getY() / CELL_SIZE - 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LinkedList<Cell> possibleCells = getPossibleMovement();
        int x = mouseXcoord(e);
        int y = mouseYcoord(e);
        if (x == player.getX()) {
            if (y == player.getY()) {
                player.setClicked(true);
            }
        }
        try{
            if (possibleCells.contains(cells[y][x]) && player.isClicked()) {
                moveTo(x, y);
                player.setClicked(false); }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    private void moveTo(int x, int y) {
        cells[player.getY()][player.getX()].unVisit();
        player.moveX(x - player.getX());
        player.moveY(y - player.getY());
        update();
    }

    private LinkedList<Cell> getPossibleMovement() {
        LinkedList<Cell> list = new LinkedList<>();
        int x = player.getX();
        int y = player.getY();
        while (x > -1 && !cells[y][x].getOneWall(1)) {
            x++;
            list.add(cells[y][x]);
        }
        x = player.getX();
        y = player.getY();
        while (x < cells.length && !cells[y][x].getOneWall(3)) {
            x--;
            list.add(cells[y][x]);
        }
        x = player.getX();
        y = player.getY();
        while (y < cells.length && !cells[y][x].getOneWall(0)) {
            y--;
            list.add(cells[y][x]);
        }
        x = player.getX();
        y = player.getY();
        while (y > -1 && !cells[y][x].getOneWall(2)) {
            y++;
            list.add(cells[y][x]);
        }
        return list;
    }


    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
