package tank_fight.tank;

import java.awt.*;
import java.util.*;
import java.util.List;

import tank_fight.map.MapTile;
import tank_fight.game.Bullet;
import tank_fight.game.Explode;
import tank_fight.game.GameFrame;
import tank_fight.util.*;

/*
 * 坦克类
 */
public abstract class Tank {
    //四个方向
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    //坦克半径
    public static final int RADIUS = 20;
    //默认速度 每帧 30ms
    public static final int DEFAULT_SPEED = 6;
    //坦克的状态
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //坦克的初始生命
    public static final int DEFAULT_HP = 100;
    public int maxHP = DEFAULT_HP;


    private int x, y;
    private int hp = DEFAULT_HP;
    private String name;
    private int atk;
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 15;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int state = STATE_STAND;
    private Color color;
    private boolean isEnemy = false;

    private BloodBar bar = new BloodBar();


    //子弹容器
    private List<Bullet> bullets = new ArrayList<Bullet>();

    //使用容器来保存当前坦克上的所有的爆炸效果
    private List<Explode> explodes = new ArrayList<Explode>();

    //用于构造坦克
    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }

    public Tank() {
        initTank();
    }

    private void initTank() {
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN, ATK_MAX);
    }

    /*
     * 绘制坦克
     * @param g
     */
    public void draw(Graphics g) {
        logic();

        drawImgTank(g);

        drawBullets(g);

        drawName(g);

        bar.draw(g);
    }

    private void drawName(Graphics g) {
        g.setColor(color);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name, x - name.length() * Constant.SMALL_FONT.getSize() / 2, y - RADIUS - 3 * bar.BAR_HEIGHT);
    }

    /*
     * 使用图片的方式去绘制坦克
     */
    public abstract void drawImgTank(Graphics g);

    /*
     * 使用系统的方式去绘制坦克
     */
    private void drawTank(Graphics g) {
        g.setColor(color);
        //绘制坦克的圆
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS << 1, RADIUS << 1);

        int endx = x;
        int endy = y;

        switch (dir) {
            case DIR_UP:
                endy = y - RADIUS * 2;
                g.drawLine(x - 1, y, endx - 1, endy);
                g.drawLine(x + 1, y, endx + 1, endy);
                break;
            case DIR_DOWN:
                endy = y + RADIUS * 2;
                g.drawLine(x - 1, y, endx - 1, endy);
                g.drawLine(x + 1, y, endx + 1, endy);
                break;
            case DIR_LEFT:
                endx = x - RADIUS * 2;
                g.drawLine(x, y - 1, endx, endy - 1);
                g.drawLine(x, y + 1, endx, endy + 1);
                break;
            case DIR_RIGHT:
                endx = x + RADIUS * 2;
                g.drawLine(x, y - 1, endx, endy - 1);
                g.drawLine(x, y + 1, endx, endy + 1);
                break;
        }

        g.drawLine(x, y, endx, endy);
    }

    //坦克的逻辑处理
    private void logic() {
        switch (state) {
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }

    private int oldX = -1, oldY = -1;

    //坦克移动的功能
    private void move() {
        oldX = x;
        oldY = y;
        switch (dir) {
            case DIR_UP:
                y = y - speed;
                if (y <= RADIUS + GameFrame.titleBarH) {
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y = y + speed;
                if (y >= Constant.FRAME_HEIGHT - RADIUS) {
                    y = Constant.FRAME_HEIGHT - RADIUS;
                }
                break;
            case DIR_LEFT:
                x = x - speed;
                if (x <= RADIUS) {
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x = x + speed;
                if (x >= Constant.FRAME_WIDTH - RADIUS) {
                    x = Constant.FRAME_WIDTH - RADIUS;
                }
                break;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAtk() {
        return atk;
    }

    public Color getColor() {
        return color;
    }

    public int getDir() {
        return dir;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getState() {
        return state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //上一次开火的时间
    private long fireTime;
    public static final int FIRE_INTERVAL = 200;

    /*
     * 坦克的功能，坦克开火的方法
     * 创建了一个子弹对象，子弹对象的属性信息通过坦克的信息获得
     * 然后将创建的子弹添加到坦克管理的容器中
     */
    public void fire() {
        if (System.currentTimeMillis() - fireTime >= FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY -= RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
                    break;
            }

            //从对象池中获取子弹对象
            Bullet bullet = BulletPool.get();
            //设置子弹的属性
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            //发射子弹之后，记录本次发射的时间
            fireTime = System.currentTimeMillis();
        }
    }

    /*
     * 将当前坦克的发射的所有的子弹绘制出来
     */
    private void drawBullets(Graphics g) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }

        //遍历所有的子弹，将不可见的子弹移除，并还原回对象池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                i--;
                BulletPool.theReturn(remove);
            }
        }
    }

    //坦克销毁的时候处理坦克的所有的子弹
    public void bulletsReturn() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //坦克和子弹碰撞的方法
    public void collideBullets(List<Bullet> bullets) {
        //遍历所有的子弹，依次和当前的坦克进行碰撞的检测
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            //子弹和坦克碰上了
            if (MyUtil.isCollide(x, y, RADIUS, bulletX, bulletY)) {
                //子弹消失
                bullet.setVisible(false);
                //坦克受到伤害
                hurt(bullet);
                //添加爆炸效果
                MusicUtil.playBomb();
                addExplode(bulletX, bulletY);
            }
        }
    }

    private void addExplode(int bulletX, int bulletY) {
        Explode explode = ExplodesPool.get();
        explode.setX(bulletX);
        explode.setY(bulletY);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }

    //坦克受到伤害
    private void hurt(Bullet bullet) {
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0) {
            hp = 0;
            die();
        }
    }

    //坦克死亡需要处理的内容
    private void die() {
        //敌人坦克被消灭
        if (isEnemy) {
            GameFrame.killEnemyCount++;
            //敌人坦克被消灭了 归还对象池
            EnemyTanksPool.theReturn(this);
            //本关是否结束了？
            if (GameFrame.isCrossLevel()) {
                //判断游戏是否通关？
                if (GameFrame.isLastLevel()) {
                    //通关了
                    GameFrame.setGameState(Constant.STATE_WIN);
                } else {
                    //TODO 进入下一关
                    GameFrame.startCrossLevel();
                }
            }
        } else {
            delaySecondsToOver(500);
        }
    }

    /*
     * 判断当前的坦克是否死亡
     */
    public boolean isDie() {
        return hp <= 0;
    }

    /*
     * 绘制当前坦克上的所有的爆炸的效果
     */
    public void drawExplodes(Graphics g) {
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }

        //将不可见的爆炸效果删除，还回对象池
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible()) {
                Explode remove = explodes.remove(i);
                i--;
                ExplodesPool.theReturn(remove);
            }
        }
    }

    //内部类，来表示坦克的血条
    class BloodBar {
        public static final int BAR_LENGTH = 2 * RADIUS;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g) {
            //填充底色
            g.setColor(Color.yellow);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
            //红色的当前血量
            g.setColor(Color.red);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, hp * BAR_LENGTH / maxHP, BAR_HEIGHT);
            //蓝色的边框
            g.setColor(Color.white);
            g.drawRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
        }
    }

    //坦克的子弹和地图所有块的碰撞
    public void bulletsCollideMapTiles(List<MapTile> tiles) {
        //for each (增强的for循环) 遍历容器中的元素，在遍历的过程中，只能使用迭代器的删除方式删除元素
        //所有的for each都要切换为基本的for循环
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).isCollideBullet(bullets)) {
                //添加爆炸效果
                MusicUtil.playBomb();
                addExplode(tiles.get(i).getX() + MapTile.radius, tiles.get(i).getY() + MapTile.radius);
                //地图水泥块没有击毁的处理
                if (tiles.get(i).getType() == MapTile.TYPE_HARD) {
                    continue;
                }
                //设置地图块销毁
                tiles.get(i).setVisible(false);
                //归还对象池
                MapTilePool.theReturn(tiles.get(i));
                //当老巢被击毁之后，一秒钟，切换到游戏结束的画面
                if (tiles.get(i).isHouse()) {
                    delaySecondsToOver(3000);
                }
            }
        }
    }

    /*
     * 延迟若干毫秒后切换到游戏结束
     */
    private void delaySecondsToOver(int millisSecond) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_LOST);
            }
        }.start();
    }

    /*
     * 一个地图块和当前的坦克碰撞的方法
     * 从tile中提取8个点，来判断8个点中是否有任何一个点和当前的坦克发生了碰撞
     * 点的顺序从左上角开始，顺时针遍历
     */
    public boolean isCollideTile(List<MapTile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            //如果是块不可见，或者是遮挡块，就不进行碰撞的检测
            if (!tile.isVisible() || tile.getType() == MapTile.TYPE_COVER) {
                continue;
            }
            //点 1 左上角点
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //如果碰上了就直接返回，否则就继续判断下一个点
            if (collide) {
                return true;
            }
            //点 2 中上点
            tileX = tile.getX() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 3 右上点
            tileX = tile.getX() + MapTile.radius * 2;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 4 右中点
            tileY = tile.getY() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 5 右下点
            tileY = tile.getY() + MapTile.radius * 2;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 6 中下点
            tileX = tile.getX() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 7 左下点
            tileX = tile.getX();
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点 8 左中点
            tileY = tile.getY() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
        }
        return false;
    }

    /*
     * 坦克回退的方法
     */
    public void back() {
        x = oldX;
        y = oldY;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
