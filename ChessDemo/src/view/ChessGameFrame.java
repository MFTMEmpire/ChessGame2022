package view;

import controller.GameController;
import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

//5.10添加
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    private static final String BLACK_TURN_INDICATOR = "Black turn";
    private static final String WHITE_TURN_INDICATOR = "White turn";

    //5.9添加
    BufferedImage image;

    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private JLabel turnLabel;
    private Chessboard chessboard;

    //5.14添加
    private JButton restartButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton undoButton;

    //5.11添加
    private ImageIcon backgroundPicture;
    private int width;
    private int height;




    public ChessGameFrame(int width, int height, int hh, int mm, int ss) {

        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;

        //5.11添加
        this.width = width;
        this.height = height;

        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addChessboard();
        addLabelBlackTurn();
//        addHelloButton();
        addLoadButton();
        addRestartButton();
        addSaveGameButton();
        addUndoButton();

        //5.9添加
        componentChangeWithWindow();

        //5.11添加
        addBackgroundPanel();


        //5.16添加
        // 尝试添加clock
        blackClock = new Clock(hh, mm, ss);
        whiteClock = new Clock(hh, mm, ss);
        gameWindow = this;
        h = hh;
        m = mm;
        s = ss;

        gameData = gameDataPanel("Black", "White", hh, mm, ss);
        gameData.setSize(200, 80);  //gameData.getPreferredSize()
        this.add(gameData);
    }


    public void updateTurnLabel() {
        turnLabel.setText(gameController.getChessboard().getCurrentColor() == ChessColor.BLACK ? BLACK_TURN_INDICATOR : WHITE_TURN_INDICATOR);
        turnLabel.repaint();
        checkmatePanel();
        checkAttackPanel();
    }
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);

        //5.11添加
        this.chessboard = chessboard;

        add(chessboard);
//        chessboard.bindToFrame(this);
    }
    private void addLabelBlackTurn() {
        turnLabel = new JLabel("Black turn");
        turnLabel.setLocation(HEIGHT, HEIGHT / 10);
        turnLabel.setSize(200, 60);
        turnLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
        this.add(turnLabel);
    }
    private void addLabelWhiteTurn() {
        turnLabel = new JLabel("White turn");
        turnLabel.setLocation(HEIGHT, HEIGHT / 10);
        turnLabel.setSize(200, 60);
        turnLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        this.add(turnLabel);
    }
    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */
    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGHT, HEIGHT / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
    private void addLoadButton() {
        this.loadButton = new JButton("Load");
        loadButton.setLocation(getHeight(), getHeight() / 10 + 2*getHeight()/760*120);
        loadButton.setSize(200, 60);
        loadButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(loadButton);

        loadButton.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this, "Input Path here");
            String returnmessage=gameController.loadGameFromFile(path);
            JOptionPane.showConfirmDialog(this,returnmessage);
        });

        updateTurnLabel();
    }


    private void addUndoButton() {
        this.undoButton= new JButton("Undo");
        undoButton.setLocation(getHeight(), getHeight() / 10 + 1*getHeight()/760*120);
        undoButton.setSize(200, 60);
        undoButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(undoButton);

        undoButton.addActionListener(e -> {
            System.out.println("Click UndoButton");
            String number = JOptionPane.showInputDialog(this, "输入悔棋步数,正确的整数");



            String path=gameController.Undo(Integer.parseInt(number));
            if (!path.equals("false")){
                gameController.loadGameFromFile(path);
                JOptionPane.showConfirmDialog(this,"Undo success");
            }
            else
                JOptionPane.showConfirmDialog(this,"Undo false,You have not played a chess");
        });
        updateTurnLabel();
    }
    /**
     * 添加RestartButton: 清空当前棋盘，重新回到初始状态
     */
    private void addRestartButton() {
        this.restartButton = new JButton("Restart");
        restartButton.setLocation(getHeight(), getHeight()/10 + 3*getHeight()/760*120);
        restartButton.setSize(200, 60);
        restartButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(restartButton);

        restartButton.addActionListener(e -> {
            gameController.restart();
            updateTurnLabel();

            //5.17添加
            restartClock();
        });
    }


    /**
     * 添加SaveGameButton: 保存当前游戏
     */
    private void addSaveGameButton() {
        this.saveButton = new JButton("Save");


        saveButton.addActionListener((e) -> {
            String name=JOptionPane.showInputDialog(this, "Save the game?", "请输入想设置的文件名");
            try {
                if (gameController.saveButtonWork(name)){
                    JOptionPane.showConfirmDialog(this,"Save successfully");
                }
                else {
                    JOptionPane.showConfirmDialog(this,"Save failed,please deleate false archive");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        saveButton.setLocation(getHeight(), getHeight()/10 + 4*getHeight()/760*120);
        saveButton.setSize(200, 60);
        saveButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(saveButton);
    }


    private void checkmatePanel() {
        if (gameController.checkmate()){
            System.out.println("can not flee by itself");


            if (!gameController.checkmatePlus()){
                JOptionPane.showConfirmDialog(this,String.format("%s%s%s","Checkmate!The ",chessboard.getCurrentColor().getName()," is lose"));
                gameController.restart();
                updateTurnLabel();
            }
            else{
                System.out.println("can flee by others");
            }

        }
    }


    private void checkAttackPanel() {
        if (gameController.checkAttack()){
            JOptionPane.showConfirmDialog(this,String.format("%s%s%s","The  ",chessboard.getCurrentColor().getName()," King is attacked"));
        }
    }


    /**
     * 用于更新ChessGameFrame中的特定组件（可继续添加）
     */
    public void update() {
        updateTurnLabel();

        //5.16添加
        updateClock();

    }

    //5.10添加
    public void componentChangeWithWindow() {
        getContentPane().setLayout(null);
        addComponentListener(new ComponentAdapter() {//拖动窗口监听
            public void componentResized(ComponentEvent e) {
                width = getWidth();//获取窗口宽度
                height = getHeight();//获取窗口高度  你也可以设置高度居中
                int chessboardSize = Math.min(width*4/5, height*4/5);

                chessboard.changeSizeWithWindow(width, height, chessboardSize);
                turnLabel.setLocation(getHeight(), getHeight()/10);
                loadButton.setLocation(getHeight(), getHeight() / 10 + 3*getHeight()/760*120);
                restartButton.setLocation(getHeight(), getHeight()/10 + 4*getHeight()/760*120);
                saveButton.setLocation(getHeight(), getHeight()/10 + 5*getHeight()/760*120);
                undoButton.setLocation(getHeight(), getHeight()/10 + 2*getHeight()/760*120);

                //将lable放在 窗口左边的1/3处
//                label.setBounds(3*width/4, height/5, 61, 16);//(起始点x，起始点y，宽地w，高h)  标签设置宽高不明显
//                //将lable放在 窗口左边的1/2处
//                button.setBounds(3*width/4, 2*height/5, 61, 16);//(起始点x，起始点y，宽地w，高h)
//                //宽度始终是窗口的1/2
//                textField.setBounds(3*width/4, 3*height/5, 61, 26);//(起始点x，起始点y，宽地w，高h)
            }

        });
    }
    //5.11添加
    public void loadResource() throws IOException {
        backgroundPicture = new ImageIcon("D:/Project文档/resource/f2026ae9d93b41e1b1d0530ba209be05.jpg");
    }

    //5.14添加
    public void addBackgroundPanel(){
        try {
            loadResource();
            JLabel backgroundLabel = new JLabel(backgroundPicture);  //把背景图片添加到标签里
            backgroundLabel.setLocation(0, 0);
            backgroundPicture.setImage(backgroundPicture.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT));  //改变图片大小

            addComponentListener(new ComponentAdapter() {//拖动窗口监听
                public void componentResized(ComponentEvent e) {

                    backgroundLabel.setSize(width, height);	//把标签设置为和图片等高等宽
                }

            });


            this.add(backgroundLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    //5.16添加
    private JFrame gameWindow;
    public Clock blackClock;
    public Clock whiteClock;
    private Timer timer;
    private JPanel gameData;
    int h, m, s;

    public Timer getTimer() {
        return timer;
    }

    private JPanel gameDataPanel(final String bn, final String wn, final int hh, final int mm, final int ss) {

        gameData = new JPanel();
        gameData.setLayout(new GridLayout(3,2,20,10));

        // PLAYER NAMES

        JLabel w = new JLabel(wn);
        JLabel b = new JLabel(bn);

        w.setHorizontalAlignment(JLabel.CENTER);
        w.setVerticalAlignment(JLabel.CENTER);
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setVerticalAlignment(JLabel.CENTER);

        w.setSize(w.getMinimumSize());
        b.setSize(b.getMinimumSize());

        gameData.add(w);
        gameData.add(b);


        // CLOCKS
        final JLabel bTime = new JLabel(blackClock.getTime());
        final JLabel wTime = new JLabel(whiteClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        if (!(hh == 0 && mm == 0 && ss == 0)) {
            timer = new Timer(1000, null);
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean turn = chessboard.getCurrentColor()==ChessColor.WHITE;

                    if (turn) {
                        whiteClock.decr();
                        wTime.setText(whiteClock.getTime());

                        if (whiteClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    gameWindow,
                                    wn + "'s time is up!\n", "time is up!",
                                    JOptionPane.YES_OPTION);

                            chessboard.swapColor();

                        }
                    } else {
                        blackClock.decr();
                        bTime.setText(blackClock.getTime());

                        if (blackClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    gameWindow,
                                    bn + "'s time is up!\n", "time is up!",
                                    JOptionPane.YES_OPTION);

                            chessboard.swapColor();

                        }
                    }
                }
            });

            timer.start();

        } else {
            wTime.setText("Untimed game");
            bTime.setText("Untimed game");
        }

        gameData.add(wTime);
        gameData.add(bTime);

        gameData.setPreferredSize(gameData.getMinimumSize());

        return gameData;
    }

    private void restartClock(){
        blackClock = new Clock(h, m, s);
        whiteClock = new Clock(h, m, s);

        try {
            timer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        remove(gameData);
        gameData = gameDataPanel("Black", "White", h, m, s);
        gameData.setSize(200,80);
        this.add(gameData);
    }

    private void updateClock(){
        remove(gameData);
        blackClock = new Clock(h, m, s);
        whiteClock = new Clock(h, m, s);

        timer.stop();
        gameData = gameDataPanel("Black", "White", h, m, s);
        gameData.setSize(200, 80);
        this.add(gameData);
    }




}
