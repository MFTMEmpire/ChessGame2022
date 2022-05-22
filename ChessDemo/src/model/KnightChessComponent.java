package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class KnightChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image knightImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    @Override
    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("D:/Project文档/images/knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("D:/Project文档/images/knight-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage=KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage =KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color);
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
        ChessboardPoint source= this.getChessboardPoint();

        /*
        黑棋方行棋
         */
        if(this.getChessColor().equals(ChessColor.BLACK)){
            if (!(chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.BLACK))){
                if ((destination.getX()==source.getX()+2|destination.getX()==source.getX()-2)&&(destination.getY()==source.getY()+1|destination.getY()==source.getY()-1)){
                    return true;
                }
                else return (destination.getX() == source.getX() + 1 | destination.getX() == source.getX() - 1) && (destination.getY() == source.getY() + 2 | destination.getY() == source.getY() - 2);
            }
            return false;
        }
        /*
        白棋方行棋
         */
        if(this.getChessColor().equals(ChessColor.WHITE)){
            if (!(chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.WHITE))){
                if ((destination.getX()==source.getX()+2|destination.getX()==source.getX()-2)&&(destination.getY()==source.getY()+1|destination.getY()==source.getY()-1)){
                    return true;
                }
                else return (destination.getX() == source.getX() + 1 | destination.getX() == source.getX() - 1) && (destination.getY() == source.getY() + 2 | destination.getY() == source.getY() - 2);
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
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
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
            return "n";
        }
        else return "N";
    }



    @Override
    public String toString(){
        if (this.getChessColor().equals(ChessColor.BLACK)) return "N";
        return "n";
    }
}

