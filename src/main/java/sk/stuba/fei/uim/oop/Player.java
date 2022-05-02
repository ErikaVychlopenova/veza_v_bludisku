package sk.stuba.fei.uim.oop;


public class Player {

    private int x;
    private int y;

    private boolean isClicked = false;

    public Player(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveX(int movement) {
        this.x += movement;
    }

    public void moveY(int movement) {
        this.y += movement;
    }
}
