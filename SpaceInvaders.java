package Src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
// Note: DO NOT import java.util.*, as the Game Loop timer will be confused with which timer class to call

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener, MouseListener
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
    double alienVelocityX = 1; // Alien moving speed 
    double alienVelocityX_Increase = 0.25;

    // Standard Bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10;

    // Armor-Piercing Rounds
    ArrayList<Block> APbulletArray;
    int APbulletWidth = tileSize/8;
    int APbulletHeight = tileSize/1;
    int APbulletVelocityY = -20;
    int AP_CollisionCount = 0;
    boolean AP_Ready = true;
    Timer AP_CooldownTimer;


    // Round & Score 
    Timer gameLoop;
    int roundCount = 1;
    int score = 0;
    boolean gameOver = false; 



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
        addMouseListener(this);

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
        bulletArray = new ArrayList<Block>();
        APbulletArray = new ArrayList<Block>();

        // Game timer 
        gameLoop = new Timer(1000/60, this); // define delay (60 fps, )
        createAliens();
        gameLoop.start();

        // AP Cooldown Timer (Resets every n seconds, in this case n = 1s)
        AP_CooldownTimer = new Timer(1000 , new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                AP_Ready = true;
                AP_CooldownTimer.stop();
            }
        });
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

        // Drawing bullets
        g.setColor(Color.white);
        for(int i = 0; i < bulletArray.size(); i++)
        {
            Block bullet = bulletArray.get(i);
            if(bullet.used == false)
            {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // Drawing AP - bullets
        g.setColor(Color.red);
        for(int i = 0; i < APbulletArray.size(); i++)
        {
            Block APbullet = APbulletArray.get(i);
            if(APbullet.used == false)
            {
                g.fillRect(APbullet.x, APbullet.y, APbullet.width, APbullet.height);
            }
        }

        // Drawing Score 
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver == true)
        {
            g.drawString("Game Over!" , 10, 35);
            g.drawString("Score: " + String.valueOf(score), 10, 65);
            g.drawString("Round: " + String.valueOf(roundCount), 10, 95);
        }
        else
        {
            g.drawString("Score: " + String.valueOf(score), 10, 35);
            g.drawString("Round: " + String.valueOf(roundCount), 10, 65);
        }
    }

    public void move()
    {
        /* Movement Mechanics for Aliens (Left & Right, Up & Down)*/ 
        for(int i = 0; i < alienArray.size(); i++)
        {
            Block alien = alienArray.get(i);
            if(alien.alive == true)
            {
                alien.x += alienVelocityX;

                // Case handler for Border Touch (Move down & Opposite Direction)
                if(alien.x + alien.width >= frameWidth || alien.x <= 0)
                {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;

                    for(int j = 0; j < alienArray.size(); j++)
                    {
                        alienArray.get(j).y += alienHeight;
                    }
                }

                if(alien.y >= ship.y)
                {
                    gameOver = true;
                }
            }
        }

        /* Movement mechanics for bullet (Trajectory) */
        for(int i = 0; i < bulletArray.size(); i++)
        {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            for(int j = 0; j < alienArray.size(); j++)
            {
                Block alien = alienArray.get(j);
                if(bullet.used == false & alien.alive == true & detectCollision(bullet, alien) == true)
                {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        /* Movement mechanics for AP bullet (Trajectory) */
        for(int i = 0; i < APbulletArray.size(); i++)
        {
            Block APbullet = APbulletArray.get(i);
            APbullet.y += APbulletVelocityY;

            for(int j = 0; j < alienArray.size(); j++)
            {
                Block alien = alienArray.get(j);
                if(APbullet.used == false & alien.alive == true & detectCollision(APbullet, alien) == true)
                {
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                    AP_CollisionCount++;

                    if(AP_CollisionCount >= 3)
                    {
                        APbullet.used = true;
                        AP_CollisionCount = 0;
                    }
                }
            }
        }

        // Bullet Cleanup 
        while(bulletArray.size() > 0 && (bulletArray.get(0).used == true || bulletArray.get(0).y < 0))
        {
            bulletArray.remove(0);
        }
        // AP Bullet Cleanup
        while(!APbulletArray.isEmpty() && APbulletArray.get(0).y < 0)
        {
            APbulletArray.remove(0);
            AP_CollisionCount = 0;
        }

        // Level advancement
        if(alienCount <= 0)
        {
            // increase the number of aliens in columns and rows by 1
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            APbulletArray.clear();
            AP_CollisionCount = 0;
            createAliens();
            alienVelocityX += alienVelocityX_Increase;
            roundCount++;
            score += alienColumns * alienRows * 100;
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

    public boolean detectCollision(Block a, Block b)
    {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    public void boundaryException()
    {
        if(ship.x < 0) 
        {
            ship.x = shipVelocityX * -1;
        }
        else if(ship.x > frameWidth - 40) // 40 arbitrary number 
        {
            ship.x = shipVelocityX * -1;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver == true)
        {
            gameLoop.stop();
        }
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

            -- Completed 

         */

        /* Reset Game Condition */
        if(gameOver == true)
        {
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            AP_CooldownTimer.stop();
            AP_Ready = true;
            AP_CollisionCount = 0;
            score = 0;
            roundCount = 1;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            ship.x -= shipVelocityX; // ship.x = ship.x - shipVelocityX;
            boundaryException();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            ship.x += shipVelocityX;
            boundaryException();
        }
        else if(e.getKeyCode() == KeyEvent.VK_A)
        {
            ship.x -= shipVelocityX;
            boundaryException();
        }
        else if(e.getKeyCode() == KeyEvent.VK_D)
        {
            ship.x += shipVelocityX;
            boundaryException();
        }

        else if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }

        else if(e.getKeyCode() == KeyEvent.VK_ENTER) // AP - Bullets 
        {
            if(AP_Ready == true)
            {
                Block APbullet = new Block(ship.x + shipWidth*15/32, ship.y, APbulletWidth, APbulletHeight, null);
                APbulletArray.add(APbullet);
                AP_Ready = false;
                AP_CooldownTimer.start();
            }
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) // Combination of mousePressed & mouseReleased
    {
        Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null);
        bulletArray.add(bullet);
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
        
    }

}