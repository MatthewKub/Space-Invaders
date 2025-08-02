package Src;
import javax.swing.*;

public class Main
{
    public static void main(String[]args) throws Exception
    { 
        /*
        Documentation Index:
            F - Functionallity Explanation
            N - Personal Note
        */

        // JFrame Varriables 
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int frameWidth = tileSize * columns; // N: 32 * 16 = 512px
        int frameHeight = tileSize * rows;// N: 32*16 = 512px

        // JFrame Establishment 
        JFrame frame = new JFrame("Space Invaders");
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // F: Prevents resizing to ensure the maintanence of grid integrity
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JPanel
        var spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack(); // F: Automatically sizes a new window to fit its components
        spaceInvaders.requestFocus(); // F: Ensures the JPanel gains input priority (e.g. Mouse & Key Intputs) over other components
        spaceInvaders.setVisible(true);
        
    }
}
