import view.ChessGameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {
            //5.16尝试修改
            ChessGameFrame mainFrame = new ChessGameFrame(1000, 760, 0, 0, 30);

            //5.20添加
            mainFrame.getTimer().stop();

            JFrame welcomeFrame = new JFrame("Welcome");

            welcomeFrame.setSize(1000,760);
            welcomeFrame.setLocationRelativeTo(null); // Center the window.
            welcomeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
            welcomeFrame.setLayout(null);
            //Stupid code written by the servant of MFTM
            try {
                welcomeFrame.setIconImage(ImageIO.read(new File("D:/Project文档/resource/icon.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            welcomeFrame.setVisible(true);
            welcomeFrame.setAlwaysOnTop(true);

            JButton button = new JButton("Play the game!");
            welcomeFrame.add(button);
            button.setSize(1000,760);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    welcomeFrame.setVisible(false);
                    mainFrame.setVisible(true);

                    //5.20添加
                    mainFrame.getTimer().start();
                }
            });
        });
    }
}
