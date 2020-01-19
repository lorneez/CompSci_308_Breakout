package breakout;

import javafx.scene.shape.Rectangle;

public class BrickHeavy extends Rectangle {
    private int durability;
    private String power;
    private boolean Move;
    private double xSpeed;
    public BrickHeavy(int x, int y, double width, double height, int toughness, String power, boolean moving, double speed) {
        super(x, y, width, height);
        setDurability(toughness);
        setPower(power);
        setXSpeed(speed);
        Move = moving;
    }

    public void setXSpeed(double speed) {
        xSpeed = speed;
    }
    public double getXSpeed() {
        return xSpeed;
    }
    public boolean getMoving(){
        return Move;
    }

    public void setDurability(int toughness) {
        durability = toughness;
    }
    public int getDurability() {
        return durability;
    }
    public void setPower(String given) {
        power = given;
    }
    public String getPower() {
        return power;
    }

}