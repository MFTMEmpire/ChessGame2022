package controller;

import model.ChessColor;
import model.ChessComponent;
import model.EmptySlotComponent;
import model.KingChessComponent;
import org.apache.commons.beanutils.BeanUtils;
import view.Chessboard;
import view.ChessboardPoint;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class GameController {
    private Chessboard chessboard;


    public Chessboard getChessboard() {
        return chessboard;
    }

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }


    /**
     * 读取保存的游戏
     *
     * @param path
     * @return
     */
    public String loadGameFromFile(String path) {
        /*
            判断文件格式是否正确
             */
        int length = path.length();

        if (!path.substring(length - 3, length).equals("txt")) {
            return "104";
        }
        /*
        检验棋盘是否8x8
         */

        try {
            List<String> chessData = Files.readAllLines(Path.of(path));

            /*
            读取长度并非规定长度
             */
            if (chessData.get(chessData.size() - 1).length() == 0) {
                return "Not required storage format";
            }
            System.out.println(chessData.size());



//            if (chessData.size() % 9 != 0) {
//                return "104 Not required storage format";
//            }



            for (int i = 0; i < chessData.size(); i++) {
                if ((i + 1) % 9 != 0) {
                    if (chessData.get(i).length() != 8) {
                        return "101";
                    }
                }
                else if (chessData.get(i).charAt(0) != 'w' & chessData.get(i).charAt(0) != 'b') {
                    return "103";
                }
            }


            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    char test = chessData.get(row).charAt(col);
                    if (!(test == 'K' | test == 'k' | test == 'Q' | test == 'q' | test == 'R' | test == 'r' | test == 'N' | test == 'n' | test == 'P' | test == 'p' | test == '_' | test == 'B' | test == 'b')) {
                        return "102";
                    }
                }
            }



            if (chessData.get(8).charAt(0) != 'b' & chessData.get(8).charAt(0) != 'w') {
                System.out.println(chessData.get(8).charAt(0));
                return "103";
            }


            chessboard.loadGame(chessData);

            return "Load success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Load error";
        }


    }

    /*
    实现根据输入名称来储存文件的功能,并会根据是否可以储存返回相应boolean值
     */
    public boolean saveButtonWork(String fileName) throws IOException {
        try {
                /*
                构造需要读取的自动保存File的文件
                 */
            File parent = new File("D:/Project文档/resource");
            List<String> chessData = Files.readAllLines(Path.of("D:/Project文档/resource/SaveNumber.txt"));
            String all = "autoSave" + String.valueOf(chessData.get(chessData.size() - 1)) + ".txt";
            File file = new File(parent, all);


                /*
                构造目标文件，并返回相应的成功与否
                 */

            String desfile = fileName + ".txt";
            File desFile = new File(parent, desfile);
            if (desFile.exists()) {
                System.out.println("desFile.exists");
                return false;
            } else {
                desFile.createNewFile();
            }


                /*
                对指定路径名进行copy
                 */
            // 使用字符流进行文件复制，注意：字符流只能复制只含有汉字的文件
            FileReader fr = new FileReader(file);
            FileWriter fw = new FileWriter(desFile);
            Integer by = 0;
            while ((by = fr.read()) != -1) {
                fw.write(by);
            }
            fr.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 重新开始游戏（所有棋子清空复原）
     * 注意：还未添加数组复原！
     */
    public void restart() {
        chessboard.restart();
        chessboard.repaint();
    }


    public boolean checkmate() {
        int x=0;
        int y=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (chessboard.getChessComponents()[i][j]instanceof KingChessComponent){
                    if (chessboard.getCurrentColor().equals(ChessColor.BLACK)){
                        if (chessboard.getChessComponents()[i][j].toString().equals("K")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                    else {
                        if (chessboard.getChessComponents()[i][j].toString().equals("k")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                }

            }
        }

        ArrayList<ChessboardPoint>  controlledPoints=new ArrayList<>();
        ArrayList<ChessboardPoint>  canFleePoints=new ArrayList<>();

        /*
        通过循环将controlledpoints和CanfleePoints找出
         */

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {


              if (!(chessboard.getChessComponents()[i][j].getChessColor().equals(chessboard.getCurrentColor())|chessboard.getChessComponents()[i][j].getChessColor().equals(ChessColor.NONE))){
                  for (int k = 0; k < 8; k++) {
                      for (int l = 0; l < 8; l++) {
                          if (chessboard.getChessComponents()[i][j].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(k,l))){
                              controlledPoints.add(new ChessboardPoint(k,l));
                          }
                      }
                  }
              }

              if (chessboard.getChessComponents()[x][y].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(i,j))){
                  canFleePoints.add(new ChessboardPoint(i,j));
              }
            }
        }


//        System.out.println("Controlled points");
//        for (ChessboardPoint controlled: controlledPoints){
//            System.out.println(controlled.toString());
//        }
//        System.out.println("Canflee points");
//        for (ChessboardPoint canflee: canFleePoints){
//            System.out.println(canflee.toString());
//        }
        /*
        判断control点中是否有回合王
         */
        if (controlledPoints.contains(new ChessboardPoint(x,y))){
            boolean canFlee=false;


            /*
            回合王的可逃路线进行遍历
             */
            for (ChessboardPoint flee:canFleePoints){
                /*
                若回合王的该可逃路线中有空白棋子
                 */
                if (chessboard.getChessComponents()[flee.getX()][flee.getY()] instanceof EmptySlotComponent){
                    /*
                    若确定该点可逃，则判断为true
                     */
                    if (!controlledPoints.contains(chessboard.getChessComponents()[flee.getX()][flee.getY()])){
                        canFlee=true;
                    }
                }
                /*
                若回合王的该可逃路线为敌方棋子
                将chessComponents[flee.getX()][flee.getY()]替换为空白棋子
                 */
                else{
                    /*
                    将目前棋盘copy，并将目标棋子更改为特殊的空白棋子
                     */
                    ChessComponent copy=this.chessboard.getChessComponents()[flee.getX()][flee.getY()];
                    this.chessboard.getChessComponents()[flee.getX()][flee.getY()]=new EmptySlotComponent(new ChessboardPoint(flee.getX(),flee.getY()));
                    /*
                    判断更改后的棋盘中，该空白区域是否被控制，若被控制，则将EmptyControlled更改为true
                     */

                    boolean EmptyControlled=false;

                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            /*
                            若为敌方棋子，则进行判断
                             */
                            if (!(this.chessboard.getChessComponents()[i][j].getChessColor().equals(this.chessboard.getCurrentColor())|this.chessboard.getChessComponents()[i][j].getChessColor().equals(ChessColor.NONE))|this.chessboard.getChessComponents()[i][j].getChessColor().equals(null)){
                                if(this.chessboard.getChessComponents()[i][j].canMoveTo(this.chessboard.getChessComponents(),new ChessboardPoint(flee.getX(),flee.getY()))){
                                    EmptyControlled=true;
                                }
                            }
                        }
                    }
                    this.chessboard.getChessComponents()[flee.getX()][flee.getY()]=copy;

                    if (!EmptyControlled){
                        canFlee=true;
                    }
                }
            }

            return !canFlee;
        }


        /*
        无回合王，直接return false
         */
        return false;

    }

    public boolean checkAttack(){
        int x=0;
        int y=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (chessboard.getChessComponents()[i][j]instanceof KingChessComponent){
                    if (chessboard.getCurrentColor().equals(ChessColor.BLACK)){
                        if (chessboard.getChessComponents()[i][j].toString().equals("K")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                    else {
                        if (chessboard.getChessComponents()[i][j].toString().equals("k")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                }

            }
        }

        ArrayList<ChessboardPoint>  controlledPoints=new ArrayList<>();
        ArrayList<ChessboardPoint>  canFleePoints=new ArrayList<>();

        /*
        通过循环将controlledpoints和CanfleePoints找出
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {


                if (!(chessboard.getChessComponents()[i][j].getChessColor().equals(chessboard.getCurrentColor())|chessboard.getChessComponents()[i][j].getChessColor().equals(ChessColor.NONE))){
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            if (chessboard.getChessComponents()[i][j].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(k,l))){
                                controlledPoints.add(new ChessboardPoint(k,l));
                            }
                        }
                    }
                }

                if (chessboard.getChessComponents()[x][y].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(i,j))){
                    canFleePoints.add(new ChessboardPoint(i,j));
                }
            }
        }

        if (controlledPoints.contains(new ChessboardPoint(x,y))){
            return true;
        }
        return false;
    }

    public boolean checkmatePlus(){
        /*
        重新得出回合王的位置
         */
        int x=0;
        int y=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (chessboard.getChessComponents()[i][j]instanceof KingChessComponent){
                    if (chessboard.getCurrentColor().equals(ChessColor.BLACK)){
                        if (chessboard.getChessComponents()[i][j].toString().equals("K")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                    else {
                        if (chessboard.getChessComponents()[i][j].toString().equals("k")){
                            x=i;
                            y=j;
                            System.out.println("回合王位置("+  x + "，"+y+")");
                        }
                    }
                }

            }
        }


        boolean canReleaseFlee =false;

        /*
        定义一个遍历数组，统计可走的可能
         */

        int[][] boardList=new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardList[i][j]=1;
            }
        }


        /*
        屏蔽回合王走的可能性
         */
        boardList[x][y]=0;














        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                /*
                首先判断该个棋子是否已经走过，若未走过，则走
                 */
                if(boardList[i][j]==1){
                    /*
                    判断该棋子是否未可行棋子，若可行，则进行随机下棋
                     */

                    if (chessboard.getChessComponents()[i][j].getChessColor().equals(this.chessboard.getCurrentColor())){
                        ArrayList<ChessboardPoint> TestPoints=new ArrayList<>();


                        /*
                        对该可行棋子统计可行棋子
                         */
                        for (int k = 0; k < 8; k++) {
                            for (int l = 0; l < 8; l++) {
                                if (chessboard.getChessComponents()[i][j].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(k,l))){
                                    TestPoints.add(new ChessboardPoint(k,l));
                                }
                            }
                        }


                        /*
                        对该可行棋子的可行棋子进行行走
                        该可行棋子为chessboard.getChessComponents()[i][j]
                         */




                        while (TestPoints.size()!=0){

                            ChessComponent movedChess=chessboard.getChessComponents()[i][j];

                            ChessComponent eatednChess=chessboard.getChessComponents()[TestPoints.get(0).getX()][TestPoints.get(0).getY()];


                            /*
                            提前将行棋棋子与被行棋子备份好
                             */
                            chessboard.chessComponents[i][j]=new EmptySlotComponent(new ChessboardPoint(i,j));

                            chessboard.chessComponents[TestPoints.get(0).getX()][TestPoints.get(0).getY()]=movedChess;


                            /*
                            再次进行是否将军的判断操作
                             */


                            ArrayList<ChessboardPoint>  controlledPoints=new ArrayList<>();

                            /*
                            通过循环将controlledpoints  现在已经是新棋盘了
                            */

                            for (int p = 0; p < 8; p++) {
                                for (int q = 0; q < 8; q++) {


                                    if (!(chessboard.getChessComponents()[p][q].getChessColor().equals(chessboard.getCurrentColor())|chessboard.getChessComponents()[p][q].getChessColor().equals(ChessColor.NONE))){

                                        for (int k = 0; k < 8; k++) {
                                            for (int l = 0; l < 8; l++) {
                                                if (chessboard.getChessComponents()[p][q].canMoveTo(chessboard.getChessComponents(),new ChessboardPoint(k,l))){
                                                    controlledPoints.add(new ChessboardPoint(k,l));
                                                }
                                            }
                                        }

                                    }


                                }
                            }





                            if(!controlledPoints.contains(new ChessboardPoint(x,y))){
                                canReleaseFlee=true;
                                System.out.println();
                            }
                            /*
                            恢复棋盘，并将第一个棋子删去
                            */
                            chessboard.chessComponents[i][j]=movedChess;
                            chessboard.chessComponents[TestPoints.get(0).getX()][TestPoints.get(0).getY()]=eatednChess;

                            TestPoints.remove(0);
                        }
                    }
                }
            }
        }

        return canReleaseFlee;
    }

    public String Undo(int number){

        try {
            List<String> chessData = Files.readAllLines(Path.of("D:/Project文档/resource/SaveNumber.txt"));
            /*
            获得需要更改的存档的编号
             */
            int needToUndoTxt=0;
            needToUndoTxt=Integer.parseInt(chessData.get(chessData.size()-1));
            /*
            获取存档数据
             */
            List<String>  archive= Files.readAllLines(Path.of("D:/Project文档/resource/autoSave"+needToUndoTxt+".txt"));
            /*
            将所需删除的文档删去
             */
            if (archive.size()%9==0&archive.size()!=9&(archive.size()-number*9)>=9){
                int archicveSize=archive.size();
                for (int i = 0; i <(number*9); i++) {
                    archive.remove(archicveSize-1-i);
                }

                /*
                将改动后的存档文件封装
                 */

                File parent = new File("D:/Project文档/resource");
                String all = "HadMovedSave" + String.valueOf(chessData.get(chessData.size() - 1)) + ".txt";
                File file = new File(parent, all);
                /*
                构造目标文件，并返回相应的成功与否
                 */

                if (file.exists()) {
                    System.out.println("fiel.exists");
                } else {
                    file.createNewFile();
                    System.out.println("file.created");
                }

                FileWriter fw = new FileWriter(file);
                for (int i = 0; i < archive.size(); i++) {
                    fw.write(archive.get(i));
                    fw.write("\n");
                }

                fw.close();

                System.out.println("D:/Project文档/resource/"+all);
                return "D:/Project文档/resource/"+all;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }
}
