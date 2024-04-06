package tank_fight.game;

import java.awt.*;

import tank_fight.util.MyUtil;

/*
 * 用来控制爆炸效果的类
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 9;
    //导入资源
    private static Image[] img;
    //爆炸效果图片的宽度和高度
    private static int explodeWidth;
    private static int explodeHight;

    static {
        img = new Image[EXPLODE_FRAME_COUNT / 3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.creatImage("res/bomb_" + i + ".png");
        }
    }

    //爆炸效果的属性
    private int x, y;
    //当前播放的帧的下标[0 - 8];
    private int index;
    //爆炸图片是否可见
    private boolean visible = true;

    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }

    public void draw(Graphics g) {
        //对爆炸效果图片的宽高的确定
        if (explodeHight <= 0) {
            explodeWidth = img[0].getWidth(null);
            explodeHight = img[0].getHeight(null);
        }
        if (!visible) {
            return;
        }
        int explodeX = x - explodeWidth / 2;
        int explodeY = y - explodeHight / 2;
        g.drawImage(img[index / 3], explodeX, explodeY, null);
        index++;
        //播放完最后一帧，设置为不可见
        if (index >= EXPLODE_FRAME_COUNT) {
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
