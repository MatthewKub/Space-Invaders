package Src;
import javax.swing.*;

public class Main
{
    public static void main(String[]args) throws Exception
    {
        // JFrame Varriables 
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int frameWidth = tileSize * columns; // 32 * 16 = 516px
        int frameHeight = tileSize * rows;// 32*16 = 512px

        // JFrame Establishment 
        JFrame frame = new JFrame("Space Invaders");
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JPanel
        var spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);


    }
}
