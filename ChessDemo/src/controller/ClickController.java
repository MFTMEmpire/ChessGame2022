package controller;


import model.ChessComponent;
import view.Chessboard;
import view.ChessboardPoint;

import java.awt.*;
import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;


    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
        first = null;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {  //first一开始默认是空的（即没有选中任何component的状态）
            if (handleFirst(chessComponent)) {  //这里handleFirst检验了点击的棋子是否属于当前行棋方
                chessComponent.setSelected(true);

                //5.17添加
                for (int i = 0; i <8 ; i++) {
                    for (int j = 0; j <8 ; j++) {
                        if (chessComponent.canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(i,j))) {
                            chessboard.getChessComponents()[i][j].setCanMoveTo(true);
                            chessboard.getChessComponents()[i][j].repaint();
                        }
                    }
                }





                first = chessComponent;
                first.repaint();  //出现红圈
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();  //红圈消失


                for (int i = 0; i <8 ; i++) {
                    for (int j = 0; j <8 ; j++) {
                        if (chessComponent.canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(i,j))) {
//                            needToDraw.add(chessboard.getChessComponents()[i][j]);
                            chessboard.getChessComponents()[i][j].setCanMoveTo(false);
                            chessboard.getChessComponents()[i][j].repaint();
                        }
                    }
                }



            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, chessComponent);
                first.setSelected(false);
                first = null;
                chessboard.swapColor();
            }
        }
    }

    //5.9添加
    public void mouseEntered(ChessComponent chessComponent){
        chessComponent.repaint();
    }

    public void mouseExisted(ChessComponent chessComponent){
        chessComponent.repaint();
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    //5.17添加
    public void setFirst(ChessComponent first) {
        this.first = first;
    }

    public ChessComponent getFirst() {
        return first;
    }
}



