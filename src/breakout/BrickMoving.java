package breakout;

import javafx.scene.shape.Rectangle;

public class BrickMoving extends Rectangle {
    private int durability;
    private String power;
    private double xSpeed;
    //private
    public BrickMoving(int x, int y, double width, double height, int toughness, String power, double speed) {
        super(x, y, width, height);
        setDurability(toughness);
        setPower(power);
        setXSpeed(speed);
    }

    private void setXSpeed(double speed) {
        xSpeed = speed;
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