package tank_fight.game;

import java.awt.*;

import tank_fight.util.MyUtil;

/*
 * �������Ʊ�ըЧ������
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 9;
    //������Դ
    private static Image[] img;
    //��ըЧ��ͼƬ�Ŀ�Ⱥ͸߶�
    private static int explodeWidth;
    private static int explodeHight;

    static {
        img = new Image[EXPLODE_FRAME_COUNT / 3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.creatImage("res/bomb_" + i + ".png");
        }
    }

    //��ըЧ��������
    private int x, y;
    //��ǰ���ŵ�֡���±�[0 - 8];
    private int index;
    //��ըͼƬ�Ƿ�ɼ�
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
        //�Ա�ըЧ��ͼƬ�Ŀ�ߵ�ȷ��
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
        //���������һ֡������Ϊ���ɼ�
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
