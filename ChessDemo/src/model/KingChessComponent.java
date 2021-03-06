package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示国际象棋里面的车
 */
public class KingChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KING_WHITE;
    private static Image KING_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image kingImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    @Override
    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("D:/Project文档/images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("D:/Project文档/images/king-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKingImage(color);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        /*
        判断目的处是否由地方控制
         */
        boolean Controled = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (!(chessComponents[x][y] instanceof KingChessComponent)&!(chessComponents[x][y].getChessColor().equals(this.getChessColor()))) {
                    if ((chessComponents[x][y].canMoveTo(chessComponents, new ChessboardPoint(destination.getX(), destination.getY())))&!(chessComponents[x][y] instanceof PawnChessComponent)) {
                        Controled = true;
                    }

                    else if (chessComponents[x][y] instanceof PawnChessComponent) {
                        if (chessComponents[x][y].getChessColor() != this.getChessColor()) {
                            if (this.getChessColor() == ChessColor.BLACK) {
                                if ((x - 1 == destination.getX() & y + 1 == destination.getY()) | (x - 1 == destination.getX() & y - 1 == destination.getY())) {
                                    Controled = true;
                                }
                            }
                            else if (this.getChessColor() == ChessColor.WHITE) {
                                if ((x + 1 == destination.getX() & y + 1 == destination.getY()) | (x + 1 == destination.getX() & y - 1 == destination.getY())) {
                                    Controled = true;
                                }
                            }
                        }
                    }
                }
                else if(chessComponents[x][y] instanceof KingChessComponent&!(chessComponents[x][y].getChessColor().equals(this.getChessColor()))){
                    if ((Math.abs(destination.getX() - x) + Math.abs(destination.getY() - y) == 1 || (Math.abs(destination.getX() - x) + Math.abs(destination.getY() - y)) == 2)){
                        if ((Math.abs(destination.getY() -y) != 2) & (Math.abs(destination.getX() - x) != 2)) {
                            Controled=true;
                        }
                    }
                }
            }
        }



        if (!Controled) {

        /*
        当不为敌方控制时
        首先判断destination是否在King的可走区间内
         */
            if ((Math.abs(destination.getX() - source.getX()) + Math.abs(destination.getY() - source.getY())) == 1 || (Math.abs(destination.getX() - source.getX()) + Math.abs(destination.getY() - source.getY())) == 2) {
                if ((Math.abs(destination.getY() - source.getY()) != 2) & (Math.abs(destination.getX() - source.getX()) != 2)) {
        /*
        判断该点处是否为己方
         */
                    if (!(chessComponents[destination.getX()][destination.getY()].getChessColor().equals(this.getChessColor()))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
        mousePassedPaint(g);
        paintCanMoveTo(g);
    }


    @Override
    public String getChessComponentName() {
        if (this.getChessColor().equals(ChessColor.WHITE)){
            return "k";
        }
        else return "K";
    }

    @Override
    public String toString(){
        if (this.getChessColor().equals(ChessColor.BLACK)) return "K";
        return "k";
    }
}

