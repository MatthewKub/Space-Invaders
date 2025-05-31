package Src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
// Note: DO NOT import java.util.*, as the Game Loop timer will be confused with which timer class to call

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener
{
    // Frame 
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int frameWidth = tileSize * columns; // 32 & 16
    int frameHeight = tileSize * rows; // 32 * 16


    // Images
    Image shipImage;
    Image alienWhiteImage;
    Image alienYellowImage;
    Image alienMagentaImage;
    ArrayList<Image>alienImageArray;

    // Aliens 
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize; // alien x-coordinate
    int alienY = tileSize; // alien y-coordinate

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; // number of aliens required to be killed, to be updated in method "createAliens".

    class Block
    {
        int x, y, width, height;
        Image image;
        boolean alive = true; // Alive status for aliens
        boolean used = false; // Whether a bullet has been used or not
        boolean AP_used = false; // Whether an AP bullet has been used or not (AP -- Armor Piercing) 

        Block(int x, int y, int width, int height, Image image)
    {           
            this.x = x; 
            this.y = y; 
            this.width = width; 
            this.height = height;
            this.image = image;
        }
        
    }

    // Ship Mechanics
    int shipWidth = tileSize * 2; // 64px
    int shipHeight = tileSize; // 32px
    int shipX = tileSize*columns/2 - tileSize; // moving  tiles to the right (minus 1 tile)
    int shipY = frameHeight - tileSize*2; // Placed at bottom of frame, but slightly above (2 tiles above)
    int shipVelocityX = tileSize;


    Block ship; // possibly useless (delete)

    //Timer gameLoop;

    // Default Constructor 
    public SpaceInvaders()
    {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Pre-loading images
        shipImage = new ImageIcon(getClass().getResource("./ship.png")).getImage(); 
        alienWhiteImage = new ImageIcon(getClass().getResource("./alien-white.png")).getImage(); 
        alienYellowImage = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage(); 
        alienMagentaImage = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage(); 

        alienImageArray = new ArrayList<Image>();
        alienImageArray.add(alienWhiteImage);
        alienImageArray.add(alienYellowImage);
        alienImageArray.add(alienMagentaImage);

        ship  = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
        alienArray = new ArrayList<Block>(); //Note: alienArray must be an ArrayList, and not a 2D array as the number of aliens will be changing every round 
        createAliens();

        // Game timer 
        var gameLoop = new Timer(1000/60, this); // define delay (60 fps, )
        //createAliens();
        gameLoop.start();
    }

    // Paint method 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // call paint component of JPanel
        draw(g);
    }
    public void draw(Graphics g) // to draw aliens, bullets, etc. 
    {
        // Drawing ship 
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height, null); // Go to definition if required 

        // Drawing aliens 
        for(int i = 0; i < alienArray.size(); i++)
        {
            Block alien = alienArray.get(i);
            if(alien.alive == true)
            {
                g.drawImage(alien.image, alien.x, alien.y, alien.width, alien.height, null);
            }
        }
    }

    public void createAliens()
    {
        Random random = new Random();
        for(int r = 0; r < alienRows; r++)
        {
            for(int c = 0; c < alienColumns; c++)
            {
                //int randomImageIndex = (int) ((Math.random())*alienImageArray.size()); // Upper bound: Array Size ; Lower bound: 0
                int randomImageIndex = random.nextInt(alienImageArray.size());
                int Xcorr = alienX + c*alienWidth; // move j*alienWidth lengths to the right 
                int Ycorr = alienY + r*alienHeight; // move i*alienHeight lengths to the bottom

                Block alien = new Block(Xcorr, Ycorr, alienWidth, alienHeight, alienImageArray.get(randomImageIndex));
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size(); 
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {


    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
    

    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        /* 
            TODO: Implement conditional check to determine whether a left or right movement of the player
            will cause the player to go out of the bounds of the JFrame, if yes, prevent movement, otherwise no
            restrictions required 

         */
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            ship.x -= shipVelocityX; // ship.x = ship.x - shipVelocityX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            ship.x += shipVelocityX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_A)
        {
            ship.x -= shipVelocityX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_D)
        {
            ship.x += shipVelocityX;
        }


    }

}