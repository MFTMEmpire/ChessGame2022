package view;

import model.*;

import controller.ClickController;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {


    private static final int CHESSBOARD_SIZE = 8;
    public final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.BLACK;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private int CHESS_SIZE;


    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = height / 8;
//        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();
        initiateAllChessPieces();


        try {
            saveNumberController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    将棋盘的打开过的次数进行保存，并根据此数据进行新建AutoSave存档;
    同时进行一次保存操作；
    */
    public void saveNumberController() throws IOException {
        try {
            /*
            保存文件创建，注意路径名
             */

            File parent = new File("D:/Project文档/resource");
            File file = new File(parent, "SaveNumber.txt");


            /*
            根据相应文件有无创建文件
             */
            if (!(parent.exists() | parent.isDirectory())) {
                parent.mkdirs();
                file.createNewFile();
            }
            if (!file.exists()){
                file.createNewFile();
            }

            List<String> number = Files.readAllLines(Path.of(file.getAbsolutePath()));

            /*
            根据所需内容进行对文件的写入
             */

            Writer writer = new FileWriter(file,true);
            writer.write(String.valueOf(number.size()+1));
            writer.write("\n");
            writer.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            saveGameToFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }
    public ChessColor getCurrentColor() {
        return currentColor;
    }
    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();
        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }
    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            //移除了chess2，相当于吃子
            remove(chess2);
            //新建了一个空白的chess2，用来进行下面的交换操作
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        //注意此时chess1和chess2的ChessboardPoint已经交换过了
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        chess1.repaint();
        chess2.repaint();
    }
    public void swapColor() {

        //5.17添加
        clickController.setFirst(null);

        for (int i = 0; i <8 ; i++) {
            for (int j = 0; j <8 ; j++) {
                this.getChessComponents()[i][j].setCanMoveTo(false);
                this.getChessComponents()[i][j].repaint();
            }
        }

        //以下两行代码会导致错误
//        clickController.getFirst().setSelected(false);
//        clickController.getFirst().repaint();

        for (int i = 0; i <8 ; i++) {
            for (int j = 0; j <8 ; j++) {
                chessComponents[i][j].setSelected(false);
                chessComponents[i][j].repaint();
            }
        }
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        update();

        try {
            saveGameToFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update(){
        //You can only perform this type cast
        ((ChessGameFrame)getParent().getParent().getParent().getParent()).update();
    }
    public void restart(){
        clickController.setFirst(null);

        currentColor = ChessColor.BLACK;
        initiateEmptyChessboard();
        initiateAllChessPieces();
        try {
            saveNumberController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void  initiateAllChessPieces() {
        // FIXME: Initialize chessboard for testing only.
        //初始化车（demo自带）
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);

        //初始化马
        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);

        //初始化象
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);

        //初始化后
        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);

        //初始化王
        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);

        //初始化兵
        initPawnOnBoard(1, 0, ChessColor.BLACK);
        initPawnOnBoard(1, 1, ChessColor.BLACK);
        initPawnOnBoard(1, 2, ChessColor.BLACK);
        initPawnOnBoard(1, 3, ChessColor.BLACK);
        initPawnOnBoard(1, 4, ChessColor.BLACK);
        initPawnOnBoard(1, 5, ChessColor.BLACK);
        initPawnOnBoard(1, 6, ChessColor.BLACK);
        initPawnOnBoard(1, 7, ChessColor.BLACK);

        initPawnOnBoard(CHESSBOARD_SIZE - 2, 0, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 1, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 2, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 3, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 4, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 5, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 6, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 7, ChessColor.WHITE);
    }
    public void  initiateEmptyChessboard() {
        for (int x = 2; x < 6; x++) {
            for (int y = 0; y < 8; y++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(x, y), calculatePoint(x,y), clickController, CHESS_SIZE));
            }
        }
    }
    public void  initiateEmptyChessboard(int row,int col) {
        ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(row, col), calculatePoint(row, col), clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    public char[][] getChessboardGraph() {
        char[][] needToReturn=new char[9][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                needToReturn[i][j]=chessComponents[i][j].toString().charAt(0);
            }
        }
        if (this.currentColor.equals(ChessColor.BLACK))  needToReturn[8][0]='b';
        else needToReturn[8][0]='w';

        return needToReturn;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        for (int row = chessData.size()-9; row <chessData.size()-1; row++) {
            for (int column = 0; column < 8; column++) {

                char input=chessData.get(row).charAt(column);
                int rowPlus=row-chessData.size()+9;



                switch (input)  {
                    case 'k':
                    {
                        initKingOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'K':
                    {
                        initKingOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    case 'q':
                    {
                        initQueenOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'Q':
                    {
                        initQueenOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    case 'b':
                    {
                        initBishopOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'B':
                    {
                        initBishopOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    case 'n':
                    {
                        initKnightOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'N':
                    {
                        initKnightOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    case 'r':
                    {
                        initRookOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'R':
                    {
                        initRookOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    case 'p':
                    {
                        initPawnOnBoard(rowPlus,column,ChessColor.WHITE);
                        break;
                    }
                    case 'P':
                    {
                        initPawnOnBoard(rowPlus,column,ChessColor.BLACK);
                        break;
                    }
                    default:
                    {
                        initiateEmptyChessboard(rowPlus,column);
                        break;
                    }
                }
            }
        }



        if (chessData.get(chessData.size()-1).charAt(0)=='w'){
            currentColor=ChessColor.WHITE;
        }

        else currentColor=ChessColor.BLACK;


        try {
            saveNumberController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.repaint();

    }




    /*
     实现根据输入名称|输入chessboard来储存文件的功能

    */
    public void saveGameToFile(Chessboard chessboard) throws IOException {
        try {
            /*
            保存文件创建，注意路径名
             */
                File parent = new File("D:/Project文档/resource");
                List<String> chessData = Files.readAllLines(Path.of("D:/Project文档/resource/SaveNumber.txt"));
                String all = "autoSave"+String.valueOf(chessData.get(chessData.size()-1)) + ".txt";
                File file = new File(parent, all);



                if (!(parent.exists() | parent.isDirectory())) {
                    parent.mkdirs();
                    file.createNewFile();
                }


                if (!file.exists()){
                    file.createNewFile();
                }


            /*
            根据所需内容进行对文件的写入
             */
                Writer writer = new FileWriter(file,true);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        writer.write(chessboard.getChessboardGraph()[i][j]);
                    }
                    writer.write("\n");
                }
                writer.write(chessboard.getChessboardGraph()[8][0]);
                writer.write("\n");


                writer.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //5.11添加
    public void changeSizeWithWindow(int width, int height, int chessboardSize){
        this.setLocation(width / 10, height / 10);
        this.setSize(chessboardSize, chessboardSize);
        this.changeComponentSizeWithChessBoard(chessboardSize);
    }

    public void changeComponentSizeWithChessBoard(int chessBoardSize){
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                chessComponents[i][j].setSize(chessBoardSize/8, chessBoardSize/8);
                chessComponents[j][i].setLocation(i * chessBoardSize/8, j * chessBoardSize/8);
            }
        }
    }

}
