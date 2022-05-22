package model;

import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;
import java.io.IOException;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size);
    }

    public EmptySlotComponent(ChessboardPoint chessboardPoint) {
        super(chessboardPoint,ChessColor.NONE);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource() throws IOException {
        //No resource!
    }

    public String getChessComponentName() {

        return "_";
    }


    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override

    //5.10修改
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        mousePassedPaint(g);
        paintCanMoveTo(g);
    }

    @Override
    public String toString(){
        return "_";
    }
}
