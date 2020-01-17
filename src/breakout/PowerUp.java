package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView {

    private double x_Position;
    private double y_Position;
    private double speed;
    private String power;

    public PowerUp(Image image) {
        super(image);
        setXPos(0);
        setYPos(200);
        setX(0);
        setY(200);
        setSpeed(0);
        power = "NONE";
    }

    public PowerUp(double xPos, double yPos, double speed, Image image, String pow) {
        super(image);
        setX(xPos);
        setY(yPos);
        setXPos(xPos);
        setYPos(yPos);
        setSpeed(speed);
        setPower(pow);
    }

    public void setXPos(double X) {
        x_Position = X;
        setX(X);
    }

    public void setYPos(double Y) {
        y_Position = Y;
        setY(Y);
    }

    public void setSpeed(double X) {
        speed = X;
    }


    public double getSpeed() {
        return speed;
    }

    public void setPower(String pow) {
        power = pow;
    }

    public String getPower() {
        return power;
    }

    public double getXPos() {
        return x_Position;
    }

    public double getYPos() {
        return y_Position;
    }
}
