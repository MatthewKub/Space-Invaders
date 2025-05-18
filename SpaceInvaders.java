package Src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList; 
import javax.swing.*;
// Note: DO NOT import java.util.*, as the Game Loop timer will be confused with which timer class to call

public class SpaceInvaders extends JPanel implements ActionListener
{
    class Block
    {
        int x, y, width, height;
        Image image;
        boolean alive = true; // Alive status for aliens
        boolean used = false; // Whether a bullet has been used or not

        Block(int x, int y, int width, int height, Image image)
        {
            this.x = x; 
            this.y = y; 
            this.width = width; 
            this.height = height;
            this.image = image;
        }
        
    }
    // Frame 
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int frameWidth = tileSize * columns; // 32 & 16
    int frameHeight = tileSize * rows; // 32 * 16


    // Images
    Image shipImage;
    Image alienImage;
    Image alienYellowImage;
    Image alienMagentaImage;
    ArrayList<Image>alienImageArray;

    
    int shipWidth = tileSize * 2; // 64px
    int shipHeight = tileSize; // 32px
    int shipX = tileSize*columns/2 - tileSize; // moving  tiles to the right (minus 1 tile)
    int shipY = frameHeight - tileSize*2; // Placed at bottom of frame, but slightly above (2 tiles above)

    Block ship; // possibly useless (delete)

    //Timer gameLoop;

    // Timestamp in Video: 21:05 minutes

    // Default Constructor 
    public SpaceInvaders()
    {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setBackground(Color.BLACK);

        // Pre-loading images
        shipImage = new ImageIcon(getClass().getResource("./ship.png")).getImage(); 
        alienImage = new ImageIcon(getClass().getResource("./alien.png")).getImage(); 
        alienYellowImage = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage(); 
        alienMagentaImage = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage(); 

        alienImageArray = new ArrayList<Image>();
        alienImageArray.add(shipImage);
        alienImageArray.add(alienImage);
        alienImageArray.add(alienYellowImage);
        alienImageArray.add(alienMagentaImage);

        var ship  = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);

        // Game timer 
        var gameLoop = new Timer(1000/60, this); // define delay (60 fps, )
    }

    // Paint method 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // call paint component of JPanel
        draw(g);
    }
    public void draw(Graphics g) // to draw aliens, bullets, etc. 
    {
        g.drawImage(ship.image, ship.x, ship.y, ship.width, ship.height, null); // Go to definition if required 

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

}