package breakout;

import javafx.scene.shape.Rectangle;

public class BrickHeavy extends Rectangle {
    private int durability;
    private String power;
    public BrickHeavy(int x, int y, double width, double height, int toughness, String power) {
        super(x, y, width, height);
        setDurability(toughness);
        setPower(power);
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