import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
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
    @Override
    public void actionPerformed(ActionEvent e){
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
    }

    //create a main method
    public static void main(String[] args) {
        flappyBird = new FlappyBird();

    }
}

