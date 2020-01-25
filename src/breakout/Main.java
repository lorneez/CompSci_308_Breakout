package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Breakout Game.
 *
 * @author Lorne Zhang
 */
public class Main extends Application {
    public static final int SIZE = 600;
    public static final int MENU_SIZE = 200;
    public static final int FRAMES_PER_SECOND = 100;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND_LEVEL_1 = Color.LIGHTYELLOW;
    public static final Paint BACKGROUND_LEVEL_2 = Color.LIGHTBLUE;
    public static final Paint BACKGROUND_LEVEL_3 = Color.LIGHTCORAL;
    public static final Paint BACKGROUND_SPLASH = Color.WHITESMOKE;
    public static final Paint BLOCK_COLOR_1 = Color.YELLOW;
    public static final Paint BLOCK_COLOR_2 = Color.ORANGE;
    public static final Paint BLOCK_COLOR_3 = Color.ORANGERED;
    public static final Paint BLOCK_COLOR_4 = Color.BLUE;
    public static final Paint BLOCK_COLOR_5 = Color.PURPLE;
    public static final Paint BLOCK_COLOR_6 = Color.GREEN;
    public static final Paint BLOCK_COLOR_7 = Color.PINK;
    public static final Paint BLOCK_COLOR_8 = Color.LAVENDER;
    public static final Paint BLOCK_COLOR_9 = Color.FUCHSIA;
    public static final Paint POWER_COLOR_1 = Color.LIGHTGRAY;
    public static final Paint POWER_COLOR_2 = Color.LIGHTSEAGREEN;
    public static final Paint POWER_COLOR_3 = Color.LIGHTBLUE;
    public static final int BLOCK_SIZE = 50;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String POWER_BALL_IMAGE = "extraballpower.gif";
    public static final String POWER_DAMAGE_IMAGE = "laserpower.gif";
    public static final String POWER_LIFE_IMAGE = "pointspower.gif";
    private int timer = 0;
    private int blink;
    private int BALL_X_SPEED = 200;
    private int BALL_Y_SPEED = 200;
    private int totalBlocks = 0;
    private int POWERUP_SPEED = 200;
    private int PADDLE_SPEED = 20;
    private Integer currentLevel = 1;
    private int BREAK_STRENGTH, lives;
    private Ball ball, newBall;
    private Stage window;
    private Scene homeScreen, splash;
    private BrickHeavy block;
    private Rectangle paddleLeft, paddleMiddle, paddleRight;
    private ArrayList<Rectangle> levelpaddles;
    private ArrayList<Paint> levelbackgrounds, brickColor, powerColor;
    private ArrayList<BrickHeavy> levelblocks;
    private ArrayList<PowerUp> levelpowerups;
    private ArrayList<Ball> levelballs;
    private Group root;
    private Text score, desc, livesTag, win, deadText;
    private Timeline animation;
    private Button resetButton, nextLevel, play;

    /**
     * Start method that begins the game
     * Stage stage is the stage being displayed
     */
    @Override
    public void start (Stage stage) {
        setUpArrays();
        splash = setUpSplash(stage);
        stage.setScene(splash);
        stage.show();
    }
    /**
     * Initializes the arrays being used to hold color information
     */
    private void setUpArrays() {
        levelbackgrounds = new ArrayList<Paint>();
        levelbackgrounds.add(BACKGROUND_LEVEL_1);
        levelbackgrounds.add(BACKGROUND_LEVEL_2);
        levelbackgrounds.add(BACKGROUND_LEVEL_3);
        brickColor = new ArrayList<Paint>();
        brickColor.add(BLOCK_COLOR_1);
        brickColor.add(BLOCK_COLOR_2);
        brickColor.add(BLOCK_COLOR_3);
        brickColor.add(BLOCK_COLOR_4);
        brickColor.add(BLOCK_COLOR_5);
        brickColor.add(BLOCK_COLOR_6);
        brickColor.add(BLOCK_COLOR_7);
        brickColor.add(BLOCK_COLOR_8);
        brickColor.add(BLOCK_COLOR_9);
        powerColor = new ArrayList<Paint>();
        powerColor.add(POWER_COLOR_1);
        powerColor.add(POWER_COLOR_2);
        powerColor.add(POWER_COLOR_3);
    }
    /**
     * Sets up splash screen
     * Stage stage is the stage being displayed
     */
    private Scene setUpSplash(Stage stage){
        stage.setTitle("Breakout created by Lorne Zhang, Duke CompSci 308 Spring 2020");
        Group splashBlocks = new Group();
        play = createButton(stage, "Enter level 1!", 150, 300);
        Text welcome = createText("Welcome to Breakout!", 150, 100);
        Text instructions = createText("Instructions:\n1. Drag the game screen to the top-left corner of your computer\nscreen for best performance.\n2. Move your mouse to control the paddle and keep the balls alive\n3. Win the game by breaking all the blocks on the screen before\nyou lose all three lives.\n4. Different color blocks have different durabilities. Some blocks\nwill move and some levels will have blinking blocks.\n5. Have fun and good luck!", 150, 130);
        splashBlocks.getChildren().add(play);
        splashBlocks.getChildren().add(welcome);
        splashBlocks.getChildren().add(instructions);
        Scene splash = new Scene(splashBlocks, Main.SIZE + MENU_SIZE, Main.SIZE, BACKGROUND_SPLASH);
        return splash;
    }
    /**
     * A genaric method to create text
     * Sting s is the text information
     * double i is the x position
     * double i1 is the y position
     */
    private Text createText(String s, double i, double i1) {
        Text text = new Text(s);
        text.setLayoutX(i);
        text.setLayoutY(i1);
        return text;
    }
    /**
     * A genaric method to create buttons
     * Stage stage is the stage being displayed
     * String text is the text on the button
     * double x is the x position
     * double y is the y position
     */
    private Button createButton(Stage stage, String text, double x, double y) {
        Button button = new Button(text);
        button.setOnAction(e -> startGame(stage));
        button.setLayoutX(x);
        button.setLayoutY(y);
        return button;
    }
    /**
     * Starts the game using the stage
     * Stage stage is the stage being displayed
     */
    void startGame(Stage stage) {
        lives = 3;
        BREAK_STRENGTH=1;
        homeScreen = setupLevel(levelbackgrounds.get(currentLevel-1), currentLevel);
        homeScreen.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        resetButton = createButton(window, "Play Again!", SIZE + 30, 180);
        winButton();
        nextLevelButton();
        window = stage;
        window.setTitle("Breakout: Level " + Integer.toString(currentLevel));
        window.setScene(homeScreen);
        window.show();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }
    /**
     * Creates the win button
     */
    private void winButton() {
        win = new Text("CONGRATULATIONS!!!\nYou Win");
        win.setX(SIZE + 30);
        win.setY(100);
    }
    /**
     * Creates the next level button
     */
    private void nextLevelButton() {
        nextLevel = new Button("Enter Next Level");
        nextLevel.setOnAction(e -> {
            currentLevel = currentLevel + 1;
            restart(window);
        });
        nextLevel.setLayoutX(SIZE + 30);
        nextLevel.setLayoutY(180);
    }
    /**
     * sets up the root and paddles of the current level along with initializing the arraylists that contain all the level elements
     * Paint background is the background color of the current level
     * int level is the current level
     */
    private Scene setupLevel(Paint background, int level) {
        root = new Group();
        levelblocks = new ArrayList<BrickHeavy>();
        levelpowerups = new ArrayList<PowerUp>();
        levelballs = new ArrayList<Ball>();
        levelpaddles = new ArrayList<Rectangle>();
        Image imageBouncer = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE)));
        ball = new Ball(280, 570, BALL_X_SPEED, -BALL_Y_SPEED, imageBouncer);
        levelballs.add(ball);
        root.getChildren().add(ball);
        paddleLeft = new Rectangle( 250, 585, 30, 10);
        paddleMiddle = new Rectangle( 280, 585, 30, 10);
        paddleRight = new Rectangle( 310, 585, 30, 10);
        levelpaddles.add(paddleLeft);
        levelpaddles.add(paddleRight);
        levelpaddles.add(paddleMiddle);
        Rectangle Bar = new Rectangle( SIZE + 10, 0, 10, SIZE);
        root.getChildren().add(Bar);
        root.getChildren().add(paddleLeft);
        root.getChildren().add(paddleMiddle);
        root.getChildren().add(paddleRight);
        setUpText(currentLevel);
        levelblocks = generateBlocks2(currentLevel, SIZE, SIZE);
        root.getChildren().addAll(levelblocks);
        return new Scene(root, Main.SIZE + MENU_SIZE, Main.SIZE, background);
    }
    /**
     * initializes the text of the current level
     * int level is the current level
     */
    private void setUpText(int level) {
        score = new Text("Welcome to Level " + Integer.toString(level));
        score.setX(SIZE + 30);
        score.setY(100);
        desc = new Text("Welcome to Level" + Integer.toString(level));
        desc.setX(SIZE + 30);
        desc.setY(80);
        deadText = new Text("Good try!\nYou are out of lives.");
        deadText.setX(SIZE + 30);
        deadText.setY(100);
        livesTag = new Text("Lives: " + Integer.toString(lives));
        livesTag.setLayoutX(SIZE + 30);
        livesTag.setLayoutY(140);
        root.getChildren().add(score);
        root.getChildren().add(desc);
        root.getChildren().add(livesTag);
    }
    /**
     * The main step function that loops through all aspects of the game to update it
     * double elapsedTime is the amount of time that has occurred
     */
    private void step (double elapsedTime) {
        timer = timer + 1;
        Point p = MouseInfo.getPointerInfo().getLocation();
        if(p.x > 5 && p.x < SIZE-45){
            paddleLeft.setX(p.x - 30);
            paddleMiddle.setX(p.x);
            paddleRight.setX(p.x + 30);
        }
        List<Ball> toRemove = new ArrayList<Ball>();
        List<PowerUp> toRemovePower = new ArrayList<PowerUp>();
        boolean addball = false;
        ArrayList<BrickHeavy> toRemoveBrick = new ArrayList<BrickHeavy>();
        stepMovingBrick(elapsedTime);
        for (PowerUp checkPower : levelpowerups){
            if(stepPower(elapsedTime, checkPower)){
                if(checkPower.getPower().equals("DoubleDamage")){
                    System.out.println("### Strength");
                    BREAK_STRENGTH = BREAK_STRENGTH + 1;
                }
                else if(checkPower.getPower().equals("AddBall")){
                    System.out.println("### ADDED Ball");
                    addBall();
                }
                else if(checkPower.getPower().equals("AddLife")){
                    System.out.println("### ADDED Life");
                    lives = lives + 1;
                    livesTag.setText("Lives: "+Integer.toString((lives)));
                }
                toRemovePower.add(checkPower);
            }
        }
        for (Ball check : levelballs) {
            addball = stepBrick(check);
            if (stepBall(elapsedTime, check)) {
                toRemove.add(check);
            }
        }
        if(addball){
            addBall();
        }
        levelballs.removeAll(toRemove);
        root.getChildren().removeAll(toRemove);
        levelpowerups.removeAll(toRemovePower);
        root.getChildren().removeAll(toRemovePower);

        if (levelballs.size() == 0){
            root.getChildren().add(newBall);
            levelballs.add(newBall);
        }
        score.setText("Score: " + Integer.toString((totalBlocks - levelblocks.size()) * 100));
        if(levelblocks.size() == 0){
            winLevel();
        }
        if(lives == 0){
            loseLevel();
        }
    }
    /**
     * A sub step function that updates the position of moving blocks
     * double elapsedTime is the amount of time that has occurred
     */
    private void stepMovingBrick(double elapsedTime) {
        for(BrickHeavy brick : levelblocks){
            if(brick.getMoving()){
                brick.setX(brick.getX() + brick.getXSpeed()*elapsedTime);
            }
            if (brick.getX() >= SIZE-25 || brick.getX() <= 0){
                brick.setXSpeed(brick.getXSpeed() * (-1));
            }
        }
    }
    /**
     * A sub step function that updates the position of dropping powers
     * double elapsedTime is the amount of time that has occurred
     * Powerup checkPower is the powerup block that is being checked
     */
    private boolean stepPower(double elapsedTime, PowerUp checkPower) {
        if(paddleMiddle.intersects(checkPower.getX(), checkPower.getY(), 20,5)){
            return true;
        }
        else if(paddleLeft.intersects(checkPower.getX(), checkPower.getY(), 20,5)){
            return true;
        }
        else if(paddleRight.intersects(checkPower.getX(), checkPower.getY(), 20,5)){
            return true;
        }
        checkPower.setYPos(checkPower.getYPos() + elapsedTime * POWERUP_SPEED);
        return false;
    }
    /**
     * A sub step function that checks if the ball intersects any bricks
     * Ball check is the ball being checked
     */
    private boolean stepBrick(Ball check){

        double Bounce_X = check.getX();
        double Bounce_Y = check.getY();
        ArrayList<BrickHeavy> toRemoveBrick = new ArrayList<BrickHeavy>();
        Boolean addball = false;
        for(BrickHeavy checkBrick : levelblocks){
            if(checkBrick.intersects(Bounce_X, Bounce_Y, 10, 10)){
                if((checkBrick.getX() + BLOCK_SIZE/2) < Bounce_X || (checkBrick.getX() - BLOCK_SIZE/2) > Bounce_X){
                    check.setXSpeed(check.getXSpeed() * (-1));
                }
                else{
                    check.setYSpeed(check.getYSpeed() * (-1));
                }
                if(checkBrick.getDurability() - BREAK_STRENGTH <= 0){
                    System.out.println("<<< Broke Block >>>");
                    if(checkBrick.getPower().equals("DoubleDamage")){
                        createPower(checkBrick, POWER_DAMAGE_IMAGE, "DoubleDamage");
                    }
                    else if(checkBrick.getPower().equals("AddBall")){
                        createPower(checkBrick, POWER_BALL_IMAGE, "AddBall");
                    }
                    else if(checkBrick.getPower().equals("AddLife")){
                        createPower(checkBrick, POWER_LIFE_IMAGE, "AddLife");
                    }
                    toRemoveBrick.add(checkBrick);
                }
                else{
                    checkBrick.setDurability(checkBrick.getDurability() - BREAK_STRENGTH);
                    checkBrick.setFill(brickColor.get(checkBrick.getDurability()-1));
                }

            }
            if(blink == 1){
                if(timer % 500 > 250){
                    checkBrick.setFill(Color.TRANSPARENT);
                }
                else{
                    checkBrick.setFill(brickColor.get(checkBrick.getDurability()-1));
                }    /**
                 * A sub step function that updates the position of moving blocks
                 * double elapsedTime is the amount of time that has occurred
                 */
            }
            if(timer == 500){
                timer = timer - 500;
            }

        }
        levelblocks.removeAll(toRemoveBrick);
        root.getChildren().removeAll(toRemoveBrick);
        return addball;
    }
    /**
     * Creates the power drops
     * BrickHeavy checkBrick is the brick being checked
     * String powerDamageImage is image of the power
     * String power is the power
     */
    private void createPower(BrickHeavy checkBrick, String powerDamageImage, String power) {
        Image imageDamage = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(powerDamageImage)));
        PowerUp newPower = new PowerUp(checkBrick.getX(), checkBrick.getY(), POWERUP_SPEED, imageDamage, power);
        levelpowerups.add(newPower);
        root.getChildren().add(newPower);
    }
    /**
     *
     * A sub step function that analyzes if the ball intersects the paddle or hits the edge of the game screen
     * double elapsedTime is the amount of time that has occurred
     * Ball check is the ball being checked
     */
    private boolean stepBall(double elapsedTime, Ball check) {
        double Bounce_X = check.getXPos();
        double Bounce_Y = check.getYPos();
        if (Bounce_X >= SIZE || Bounce_X <= 0){
            check.setXSpeed(check.getXSpeed() * (-1));
        }
        if (Bounce_Y >= SIZE) {
            check.setYSpeed(check.getYSpeed() * (-1));
            loseLife();
            System.out.println("<<< Lost Ball >>>");
            return true;
        }
        else if (Bounce_Y <= 0) {
            check.setYSpeed(check.getYSpeed() * (-1));
        }
        if(paddleLeft.intersects(Bounce_X, Bounce_Y, 20,5)){
            if(check.getYSpeed() > 201){
                check.setYSpeed(-200);
            }
            else{
                check.setYSpeed(check.getYSpeed() * (-1));
            }
            paddleSpeedChange(check.getXSpeed() > 0, check);
        }
        else if(paddleMiddle.intersects(Bounce_X, Bounce_Y, 20,5)){
            check.setYSpeed(check.getYSpeed() * (-1));
            if(check.getXSpeed() != BALL_X_SPEED/2){
                check.setXSpeed(BALL_X_SPEED/2);
                check.setYSpeed(-265);
            }
        }
        else if(paddleRight.intersects(Bounce_X, Bounce_Y, 20,5)){
            if(check.getYSpeed() > 201){
                check.setYSpeed(-200);
            }
            else{
                check.setYSpeed(check.getYSpeed() * (-1));
            }
            paddleSpeedChange(check.getXSpeed() < 0, check);
        }
        check.setXPos(check.getXPos() + check.getXSpeed() * elapsedTime);
        check.setYPos(check.getYPos() + check.getYSpeed() * elapsedTime);
        return false;
    }
    /**
     * Determines how the ball reacts to hitting the paddle
     * boolean b is whether the ball needs to change direction in the x direction
     * Ball hit is the ball that intersects with the paddle
     */
    private void paddleSpeedChange(boolean b, Ball hit) {
        if (b) {
            hit.setXSpeed(hit.getXSpeed() * (-1));
        }
        if (hit.getXSpeed() == BALL_X_SPEED / 2) {
            hit.setXSpeed(BALL_X_SPEED);
        }
        if (hit.getXSpeed() == -BALL_X_SPEED / 2) {
            hit.setXSpeed(-BALL_X_SPEED);
        }
    }
    /**
     * Updates the screen information when the player loses a level
     */
    private void loseLevel() {
        score.setText("You LOSE!");
        root.getChildren().add(deadText);
        cleanLevel();
        root.getChildren().add(resetButton);
        animation.stop();
    }
    /**
     * Updates the screen information when the player beats a level
     */
    private void winLevel() {
        score.setText("You WIN!");
        root.getChildren().add(win);
        cleanLevel();
        if(currentLevel != 3){
            root.getChildren().add(nextLevel);
        }
        else{
            end();
        }
        animation.stop();
    }
    /**
     * Ends the game when the player wins the game
     */
    private void end() {
        Group endGroup = new Group();
        Button Play = new Button("Play Again!");
        Play.setOnAction(e -> start(window));
        Text thanks = new Text();
        thanks.setText("Thanks for playing! You win!!!");
        thanks.setLayoutX(300);
        thanks.setLayoutY(100);
        currentLevel = 1;
        Play.setLayoutX(300);
        Play.setLayoutY(220);
        endGroup.getChildren().add(Play);
        endGroup.getChildren().add(thanks);
        Scene end = new Scene(endGroup, Main.SIZE + MENU_SIZE, Main.SIZE, Color.WHITESMOKE);
        window.setScene(end);
    }
    /**
     * Cleans the level by removing elements from root
     */
    private void cleanLevel() {
        root.getChildren().remove(ball);
        for(Ball ball : levelballs){
            root.getChildren().remove(ball);
        }
        root.getChildren().removeAll(levelpowerups);
        root.getChildren().remove(paddleLeft);
        root.getChildren().remove(paddleMiddle);
        root.getChildren().remove(score);
        root.getChildren().remove(paddleRight);
    }
    /**
     * Generates the level's bricks
     * int currentLevel is the level being generated
     * int width and int height are the height and width of the screen
     */
    private ArrayList<BrickHeavy> generateBlocks2 (int currentLevel, int width, int height) {
        ArrayList<BrickHeavy> blocks = new ArrayList<>();
        try {
            String i;
            BufferedReader r=new BufferedReader(new FileReader("src/breakout/level"+Integer.toString(currentLevel)+".txt"));
            blink = Integer.parseInt(r.readLine());
            int rows = Integer.parseInt(r.readLine());
            int columns = Integer.parseInt(r.readLine());
            totalBlocks = Integer.parseInt(r.readLine());
            int rowsadded = 0;
            int colsadded = 0;
            while(!((i=r.readLine()).equals("STOP"))) {
                for(int f=0; f<i.length(); f++){
                    String x = i.substring(f,f+1);
                    BrickHeavy blockToBeAdded;
                    BrickMoving movingBlockToBeAdded;
                    int blockNumber;
                    if(x.equals("d")){
                        blockNumber = 1;
                        blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "DoubleDamage", false, 0);
                        colsadded = colsadded + 1;
                    }
                    else if(x.equals("a")){
                        blockNumber = 2;
                        blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "AddBall", false, 0);
                        colsadded = colsadded + 1;
                    }
                    else if(x.equals("m")){
                        blockNumber = 1;
                        blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "NONE", true, 50);
                        colsadded = colsadded + 1;
                    }
                    else if(x.equals("n")){
                        blockNumber = 2;
                        blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "NONE", true, 100);
                        colsadded = colsadded + 1;
                    }
                    else if(x.equals("i")){
                        blockNumber = 3;
                        blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "AddLife", false, 0);
                        colsadded = colsadded + 1;
                    }
                    else{
                        blockNumber = Integer.parseInt(x);
                        if(blockNumber == 0){
                            colsadded = colsadded + 1;
                            blockToBeAdded = new BrickHeavy( colsadded*50 + 20, rowsadded*30 + 10, BLOCK_SIZE/1.5, BLOCK_SIZE/3, 1, "NONE", false, 0);
                        }
                        else{
                            blockToBeAdded = createHeavyBrick(rowsadded, colsadded, blockNumber, "NONE", false, 0);
                            colsadded = colsadded + 1;
                        }
                    }
                    if(colsadded == columns){
                        colsadded = 0;
                        rowsadded = rowsadded + 1;
                    }
                    if(blockNumber != 0){
                        blocks.add(blockToBeAdded);
                    }
                }
            }
            r.close();
        } catch(IOException ie) {
            ie.printStackTrace();
        }
        return blocks;
    }
    /**
     * Create a heavy brick
     * int rowsadded is the current row number
     * int colsadded is the current column number
     * int blockNumber is the block durability
     * String power is the power attached to the block
     * boolean moving determines if the block is moving
     * double speed is the block's speed
     */
    private BrickHeavy createHeavyBrick(int rowsadded, int colsadded, int blockNumber, String power, boolean moving, double speed) {
        BrickHeavy blockToBeAdded;
        if(power.equals("NONE")){
            blockToBeAdded = new BrickHeavy(colsadded * 50 + 20, rowsadded * 30 + 10, BLOCK_SIZE / 1.5, BLOCK_SIZE / 3, blockNumber, power, moving, speed);
            blockToBeAdded.setFill(brickColor.get(blockNumber -1));

        }
        else {
            blockToBeAdded = new BrickHeavy(colsadded * 50 + 20, rowsadded * 30 + 10, BLOCK_SIZE / 1.5, BLOCK_SIZE / 3, 1, power, moving, speed);
            blockToBeAdded.setFill(powerColor.get(blockNumber -1)); 
        }
        blockToBeAdded.setArcWidth(5);
        blockToBeAdded.setArcHeight(5);
        return blockToBeAdded;
    }
    /**
     * Handle keyboard inputs
     * Keycode code is the keyboard input
     */
    private void handleKeyInput (KeyCode code) {
        if (code.getChar().equals("W")) {
            winLevel();
        } else if (code.getChar().equals("Q")) {
            loseLevel();
        } else if (code.getChar().equals("B")) {
            addBall();
        } else if (code.getChar().equals("L")) {
            lives = lives + 1;
            livesTag.setText("Lives: "+Integer.toString((lives)));
        } else if (code.getChar().equals("S")) {
            BREAK_STRENGTH = BREAK_STRENGTH + 1;
        } else if (code.getChar().equals("1")) {
            setLevel(1);
        } else if (code.getChar().equals("2")) {
            setLevel(2);
        } else if (code.getChar().equals("3")) {
            setLevel(3);
        }
    }
    /**
     * Set level to int i
     * Begin restarting the game by clearing the level and stopping the animation
     */
    private void setLevel(int i) {
        currentLevel = i;
        cleanLevel();
        animation.stop();
        startGame(window);
    }
    /**
     * Add a ball to the game
     */
    private void addBall() {
        Image imageBouncer = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE)));
        Ball add = new Ball(paddleMiddle.getX(), paddleMiddle.getY()-10, BALL_X_SPEED, -BALL_Y_SPEED, imageBouncer);
        levelballs.add(add);
        root.getChildren().add(add);
    }
    /**
     * Lose a life
     */
    private void loseLife () {
        if(levelballs.size() > 1){
            return;
        }
        else{
            lives = lives - 1;
            livesTag.setText("Lives: " + Integer.toString(lives));
            resetBallPaddle();
        }
    }
    /**
     * Reset paddle and ball to initial position after losing a life
     */
    void resetBallPaddle(){
        Image imageBouncer = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE)));
        newBall = new Ball(280, 570, BALL_X_SPEED, -BALL_Y_SPEED, imageBouncer);
        if(newBall.getYSpeed() > 0){
            newBall.setYSpeed(ball.getYSpeed() * (-1));
        }
        updatePaddleY(585, 585, 585);
    }
    /**
     * Update paddle position
     * double i is the updated y coordinate for the left paddle
     * double i2 is the updated y coordinate for the right paddle
     * double i3 is the updated y coordinate for the middle paddle
     */
    private void updatePaddleY(double i, double i2, double i3) {
        paddleLeft.setY(i);
        paddleRight.setY(i2);
        paddleMiddle.setY(i3);
    }
    /**
     * Restart the game
     */
    void restart(Stage stage) {
        startGame(stage);
    }
    /**
     * Start the game
     */
    public static void main (String[] args) {
        launch(args);
    }
}








