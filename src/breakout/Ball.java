package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView {

    private double x_Position;
    private double y_Position;
    private double x_Speed;
    private double y_Speed;


    public Ball(Image image) {
        super(image);
        setXPos(0);
        setYPos(200);
        setX(0);
        setY(200);
        setXSpeed(0);
        setYSpeed(100);
    }

    public Ball( double xPos, double yPos, double xSpeed, double ySpeed, Image image) {
        super(image);
        setX(xPos);
        setY(yPos);
        setXPos(xPos);
        setYPos(yPos);
        setXSpeed(xSpeed);
        setYSpeed(ySpeed);
    }

    public void setXPos( double X ) {
        x_Position = X;
        setX(X);
    }

    public void setYPos( double Y ) {
        y_Position = Y;
        setY(Y);
    }

    public void setXSpeed( double X ) {
        x_Speed = X;
    }

    public void setYSpeed( double Y ) {
        y_Speed = Y;
    }

    public double getXSpeed( ) {
        return x_Speed;
    }

    public double getYSpeed( ) {
        return y_Speed;
    }
    public double getXPos() {
        return x_Position;
    }

    public double getYPos() {
        return y_Position;
    }
}