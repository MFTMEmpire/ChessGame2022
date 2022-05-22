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
public class PawnChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image Pawn_WHITE;
    private static Image Pawn_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image PawnImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    @Override
    public void loadResource() throws IOException {
        if (Pawn_WHITE == null) {
            Pawn_WHITE= ImageIO.read(new File("D:/Project文档/images/pawn-white.png"));
        }

        if (Pawn_BLACK == null) {
            Pawn_BLACK = ImageIO.read(new File("D:/Project文档/images/pawn-black.png"));
        }
    }




    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                PawnImage = Pawn_WHITE;
            } else if (color == ChessColor.BLACK) {
                PawnImage = Pawn_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("PawnChessComponent 63行代码异常");
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color);
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
        ChessboardPoint source =this.getChessboardPoint();

        /*
        黑棋行棋
         */
        if (this.getChessColor().equals(ChessColor.BLACK)) {

            /*
            下述讨论棋子为第一步行棋
             */
            if (source.getX()==1) {
                /*
                讨论当棋子仅在X轴方向移动一格或两格时（正常行棋）
                 */
                if (destination.getY()==source.getY()&destination.getX()==source.getX()+1&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                    return true;
                }
                else if (destination.getY()==source.getY()&destination.getX()==source.getX()+2&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent&chessComponents[source.getX()+1][source.getY()] instanceof EmptySlotComponent){
                    return true;
                }
                /*
                讨论棋子在对角线吃棋子时（吃子行棋）
                 */
                else if (chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.WHITE)){
                    if (destination.getY()-1==source.getY()||destination.getY()+1==source.getY()){
                        return destination.getX() - 1 == source.getX();
                    }
                }
                return false;
            }

            /*
            下述讨论棋子并非自身的第一步棋
             */
            else {
                /*
                讨论当棋子仅在X轴方向移动一格时（正常行棋）
                 */
                if (destination.getY() == source.getY()&destination.getX()==source.getX()+1&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                    return true;
                }
                /*
                讨论棋子在对角线吃棋子时（吃子行棋）
                 */
                else if (chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.WHITE)){
                    if (destination.getY()-1==source.getY()||destination.getY()+1==source.getY()){
                        return destination.getX() - 1 == source.getX();
                    }
                }
                return false;
            }
        }
        /*
        白棋行棋
         */
        if (this.getChessColor().equals(ChessColor.WHITE)) {
            /*
            下述讨论棋子为第一步行棋
             */
            if (source.getX()==6) {
                /*
                讨论当棋子仅在X轴方向移动一格或两格时（正常行棋）
                 */
                if (destination.getY()==source.getY()&destination.getX()==source.getX()-1&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                    return true;
                }
                else if (destination.getY()==source.getY()&destination.getX()==source.getX()-2&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent&chessComponents[source.getX()-1][source.getY()] instanceof EmptySlotComponent){
                    return true;
                }
                /*
                讨论棋子在对角线吃棋子时（吃子行棋）
                 */
                else if (chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.BLACK)){
                    if (destination.getY()-1==source.getY()||destination.getY()+1==source.getY()){
                        if(destination.getX()+1==source.getX()){
                            return true;
                        }
                    }
                }
                return false;
            }

            /*
            下述讨论棋子并非自身的第一步棋
             */
            else {
                /*
                讨论当棋子仅在X轴方向移动一格时（正常行棋）
                 */
                if (destination.getY() == source.getY()&destination.getX()==source.getX()-1&chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                    return true;
                }
                /*
                讨论棋子在对角线吃棋子时（吃子行棋）
                 */
                else if (chessComponents[destination.getX()][destination.getY()].getChessColor().equals(ChessColor.BLACK)){
                    if (destination.getY()-1==source.getY()||destination.getY()+1==source.getY()){
                        if(destination.getX()+1==source.getX()){
                            return true;
                        }
                    }
                }
                return false;
            }
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
        g.drawImage(PawnImage, 0, 0, getWidth() , getHeight(), this);
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
            return "p";
        }
        else return "P";
    }




    @Override
    public String toString(){
        if (this.getChessColor().equals(ChessColor.BLACK)) return "P";
        return "p";
    }
}
