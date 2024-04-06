package tank_fight.tank;

import java.awt.Graphics;
import java.awt.Image;

import tank_fight.util.MyUtil;

/*
 * �Լ���̹����
 */
public class MyTank extends Tank {
    //̹�˵�ͼƬ����
    private static Image[] tankImg;

    //��̬������ж������г�ʼ��
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
