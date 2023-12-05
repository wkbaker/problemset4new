import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;

    public int ticks;
    public int yMotion;
    public int score;
    public ArrayList<Rectangle> columns;
    public boolean gameOver;
    public boolean started;
    public Random rand;


    public FlappyBird() {
        //create a new JFrame
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);


        renderer = new Renderer();
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


    @Override
    public void actionPerformed(ActionEvent e){
        int speed =10;
        ticks++;
        if (started) {



       for(int i=0; i<columns.size(); i++) {
           Rectangle column = columns.get(i);
           column.x-=speed;

       }
        if (ticks%2==0 && yMotion<15) {
            yMotion+=2;
        }
        for(int i=0; i<columns.size(); i++) {
            Rectangle column = columns.get(i);
            if (column.x + column.width < 0) {
                columns.remove(column);
                //if it is the top column, add another column, so it will be infinite
                if(column.y==0) {
                    addColumn(false);

                }

                }

            }
        }

        //moving the bird vertically
            bird.y+= yMotion;
        for(Rectangle column: columns){
            //if statement shows that the bird is in the center of the column and the score updates by 1
            if(column.y==0&& bird.x+bird.width/2>column.x+column.width/2-10&& bird.x+ bird.width/2<column.x+column.width/2+10) {
                score++;
            }
            if (column.intersects(bird)){

                gameOver=true;
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

        if(bird.y> HEIGHT -120 || bird.y<0) {

            gameOver=true;
        }
        if(bird.y+yMotion>=HEIGHT-120) {
            bird.y = HEIGHT-120 - bird.height;
        }
        renderer.repaint();
    }


    public void repaint(Graphics g) {
        //this is used to set the background color for the game
        g.setColor(Color.cyan);
        g.fillRect(0,0, WIDTH, HEIGHT);

        //this sets the color to orange and creates the ground (bottom of the screen)
        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);

        //creates grass on top of the ground which is orange
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 150, WIDTH, 20);

        //this creates a red square in the center of the game
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column: columns) {
            paintColumn(g, column);

        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));
        if(!started) {
            g.drawString("Click to start!", 75,HEIGHT/2-50);

        }
        if (gameOver) {
            g.drawString("Game Over!", 100,HEIGHT/2-50);

        }
        //if statement calculates the score as you play
        if(!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH/2-25,100);
        }

    }

    //create a main method
    public static void main(String[] args) {
        flappyBird = new FlappyBird();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();

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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() ==KeyEvent.VK_SPACE) {
            jump();
        }

    }
}

