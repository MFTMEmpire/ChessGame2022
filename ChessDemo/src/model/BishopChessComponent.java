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
public class BishopChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image bishopImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    @Override
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("D:/Project文档/images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("D:/Project文档/images/bishop-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color);
    }

    @Override
    public String getChessComponentName() {
        if (this.getChessColor().equals(ChessColor.WHITE)){
            return "b";
        }
        else return "B";
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

//        if ((destination.getX()+destination.getY()==source.getX()+source.getY())){
//
//
//            if (!(chessComponents[destination.getX()][destination.getY()].getChessColor().equals(this.getChessColor()))){
//                boolean test=true;
//                for (int x = Math.min(destination.getX(), source.getX())+1; x <Math.max(destination.getX(), source.getX()); x++) {
//                    for (int y = Math.max(destination.getY(), source.getY())-1; y >Math.min(destination.getY(),source.getY()) ; y--) {
//                        if (!(chessComponents[x][y] instanceof EmptySlotComponent)) {
//                            test = false;
//                            break;
//                        }
//                    }
//                }
//                return test;
//            }
//            return false;

        if (!(chessComponents[destination.getX()][destination.getY()].getChessColor().equals(this.getChessColor()))){


            if (source.getX()+source.getY()==destination.getY()+destination.getX()) {
                boolean test= true;
                for (int x = Math.min(destination.getX(), source.getX()) + 1,y=Math.max(destination.getY(), source.getY())-1; x < Math.max(destination.getX(), source.getX()); x++,y--) {
                    if (!(chessComponents[x][y] instanceof EmptySlotComponent)){
                        test=false;
                    }
                }
                return test;
            }



            if (source.getX()-destination.getX()==source.getY()-destination.getY()) {
                boolean test = true;

                for (int x = Math.min(source.getX(), destination.getX()) + 1,  y=Math.min(source.getY(),destination.getY())+1; x < Math.max(source.getX(), destination.getX()); x++,y++) {
                    if (!(chessComponents[x][y] instanceof EmptySlotComponent)){
                        test=false;
                    }
                }
                return test;
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
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
        mousePassedPaint(g);

        //5.17添加
        paintCanMoveTo(g);
    }

    @Override
    public String toString(){
        if (this.getChessColor().equals(ChessColor.BLACK)) return "B";
        return "b";
    }




}



