package tank_fight.map;

import java.awt.*;
import java.util.List;

import tank_fight.game.Bullet;
import tank_fight.util.BulletPool;
import tank_fight.util.MyUtil;

/*
 * 地图元素块
 */
public class MapTile {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;
    public static int tileW = 40;
    public static int tileH = 40;
    public static int radius = tileW / 2;
    private int type = TYPE_NORMAL;

    private static Image[] tileImg;

    static {
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.creatImage("res/tile.png");
        tileImg[TYPE_HOUSE] = MyUtil.creatImage("res/star.gif");
        tileImg[TYPE_COVER] = MyUtil.creatImage("res/grass.png");
        tileImg[TYPE_HARD] = MyUtil.creatImage("res/steels.png");
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
            tileH = tileW;
        }
    }

    //图片资源的左上角的坐标
    private int x, y;
    private boolean visible = true;


    public MapTile() {
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
            tileH = tileW;
        }
    }

    public void draw(Graphics g) {
        if (!visible) {
            return;
        }
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
            tileH = tileW;
        }
        g.drawImage(tileImg[type], x, y, null);

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

    //地图块和若干个子弹是否有碰撞
    public boolean isCollideBullet(List<Bullet> bullets) {
        if (!visible || type == TYPE_COVER) {
            return false;
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if (collide) {
                //子弹的销毁
                bullet.setVisible(false);
                BulletPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }

    //判断当前的地图块是否是老巢
    public boolean isHouse() {
        return type == TYPE_HOUSE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
