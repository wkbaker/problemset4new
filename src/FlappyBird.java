import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
/****************************************************************
 * @file FlappyBird.java
 * @brief write a java program that creates the game Flappy Bird and allows the user to click using their mouse
 * to play the game and move the bird in between the columns. End the game when the bird hits the column and keep
 * the users score of the number of times they successfully navigate the bird through the columns.
 * @author Willa Kate Baker, Emily Toro
 * @date: December 8th, 2023
 * @acknowledgement: Willa Kate Baker and Emily Toro
 ***********************************************************************/
//implements the java class ActionListener, MouseListener, KeyListener
public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;

    public int ticks;
    public int yMotion;
    public int score;
    //this arraylist creates a list of columns so that there is an infinite amount
    public ArrayList<Rectangle> columns;
    public boolean gameOver;
    public boolean started;
    public Random rand;


    public FlappyBird() {
        //create a new JFrame
        JFrame jframe = new JFrame();
        //creates a new timer
        Timer timer = new Timer(20, this);

        //create a new Renderer
        renderer = new Renderer();
        //create a new Random
        rand = new Random();

        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        //this creates a title for the game
        jframe.setTitle("Flappy Bird");
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

    }
    //this method creates the columns and spaces between the columns
    public void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        //this if/else statement deals with the adding of columns to the screen and places them at a specific distance from one another
        if(start){
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }

    }

    //this method sets the color of the columns
    public void paintColumn(Graphics g, Rectangle column){

        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }
    //this method sets the default score to zero, sets yMotion to zero since yMotion = 0 represents the top of the screen,
    //and if the spacebar is pressed, and the game has started and has not yet ended, it allows for the bird to move
    public void jump() {
        if(gameOver) {
            bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
          columns.clear();
          yMotion = 0;
          score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver= false;

        }
        if(!started){
            started = true;


        } else if(!gameOver) {
            if (yMotion>0) {
                yMotion = 0;
            }
            yMotion -=10;

        }
    }

    //create method actionPerformed comes from implementing ActionListener interface
    //this method handles the movement of the columns, the vertical motion of the bird and keeps adding columns to the screen
    //until the game has reached gameover
    @Override
    public void actionPerformed(ActionEvent e){
        int speed =10;
        ticks++;
        if (started) {


        //this for loop retrieves a column from the array list and moves the column horizontally across the screen
       for(int i=0; i<columns.size(); i++) {
           Rectangle column = columns.get(i);
           column.x-=speed;

       }
        if (ticks%2==0 && yMotion<15) {
            yMotion+=2;
        }
        for(int i=0; i<columns.size(); i++) {
            Rectangle column = columns.get(i);
            //this if method checks if any of the columns are not shown on the screen and then removes those columns
            if (column.x + column.width < 0) {
                columns.remove(column);
                //if it is the top column, add another column, so it will be infinite
                //this if statement adds a new column so that there is always a continual and infinite amount of columns
                if(column.y==0) {
                    addColumn(false);

                }

                }

            }
        }

        //moving the bird vertically
            bird.y+= yMotion;
        for(Rectangle column: columns){
            //if statement shows that the bird is in the center of the column and the score updates by 1 if the bird passes through the columns without a collision
            if(column.y==0&& bird.x+bird.width/2>column.x+column.width/2-10&& bird.x+ bird.width/2<column.x+column.width/2+10) {
                score++;
            }
            //this if statement checks to see if the bird collided with the column and if it did, it ends the game
            if (column.intersects(bird)){

                gameOver=true;
                //the following if/else statements deal with the positioning of the birds coordinates in relation to the columns coordinates
                if(bird.x<=column.x) {
                    bird.x=column.x-bird.width;

                } else {
                    if(column.y!=0) {
                        bird.y=column.y-bird.height;

                    } else if (bird.y<column.height) {
                        bird.y=column.height;
                    }
                }

            }
        }
        //this if statement looks at the position of the bird and if the bird is too close to the top of the screen or too close to the bottom of the screen it ends the game
        if(bird.y> HEIGHT -120 || bird.y<0) {

            gameOver=true;
        }
        if(bird.y+yMotion>=HEIGHT-120) {
            bird.y = HEIGHT-120 - bird.height;
        }
        renderer.repaint();
    }

    //create method repaint that recieves Graphics g as a parameter
    //this method sets the color of the various rectangles that make up the screen and makes the columns
    public void repaint(Graphics g) {
        //this is used to set the background color for the game
        g.setColor(Color.cyan);
        g.fillRect(0,0, WIDTH, HEIGHT);

        //this sets the color to orange and creates the ground (bottom of the screen)
        g.setColor(Color.ORANGE);
        //sets the dimensions of the rectangle and fills the rectangle with the color set above (orange)
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);

        //creates grass on top of the ground which is orange
        g.setColor(Color.GREEN);
        //sets the dimensions of the rectangle and fills the rectangle with the color set above (green)
        g.fillRect(0, HEIGHT - 150, WIDTH, 20);

        //this creates a red square in the center of the game
        g.setColor(Color.RED);
        //creates the rectangle that represents the bird and sets the color of the bird to red
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column: columns) {
            paintColumn(g, column);

        }
        //sets the color of the text that will appear on the screen
        g.setColor(Color.white);
        //sets the font and style of the text that will appear on the screen
        g.setFont(new Font("Arial", 1, 100));
        //this if statement allows the user to click with the mouse to start the game
        if(!started) {
            g.drawString("Click to start!", 75,HEIGHT/2-50);

        }
        //if the game is over, this if statement allows for "Game Over!" to be displayed on the screen
        if (gameOver) {
            g.drawString("Game Over!", 100,HEIGHT/2-50);

        }
        //if statement calculates the score as you play
        if(!gameOver && started) {
            //allows the value of the score to be shown on the screen
            g.drawString(String.valueOf(score), WIDTH/2-25,100);
        }

    }

    //create a main method
    public static void main(String[] args) {
        flappyBird = new FlappyBird();

    }
    //method mouseClicked comes from implementing MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        //calls the method jump()
        jump();

    }
    //method mousePressed comes from implementing MouseListener
    @Override
    public void mousePressed(MouseEvent e) {

    }
    //method mouseReleased comes from implementing MouseListener
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    //method mouseEntered comes from implementing MouseListener
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    //method mouseExited comes from implementing MouseListener
    @Override
    public void mouseExited(MouseEvent e) {

    }
    //method keyTyped comes from implementing KeyListener
    @Override
    public void keyTyped(KeyEvent e) {

    }
    //method keyPressed comes from implementing KeyListener
    @Override
    public void keyPressed(KeyEvent e) {

    }
    //method keyReleased comes from implementing KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
        //this if statement is showing that if the spacebar is pressed, then the bird moves/jumps
        if(e.getKeyCode() ==KeyEvent.VK_SPACE) {
            //calls the method jump()
            jump();
        }

    }
}

