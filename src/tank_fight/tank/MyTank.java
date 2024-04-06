package tank_fight.tank;

import java.awt.Graphics;
import java.awt.Image;

import tank_fight.util.MyUtil;

/*
 * 自己的坦克类
 */
public class MyTank extends Tank {
    //坦克的图片数组
    private static Image[] tankImg;

    //静态代码块中对它进行初始化
    static {
        tankImg = new Image[4];
        tankImg[0] = MyUtil.creatImage("res/player1/p1tankU.png");
        tankImg[1] = MyUtil.creatImage("res/player1/p1tankD.png");
        tankImg[2] = MyUtil.creatImage("res/player1/p1tankL.png");
        tankImg[3] = MyUtil.creatImage("res/player1/p1tankR.png");
    }

    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }
}
