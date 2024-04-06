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
 * ̹����
 */
public abstract class Tank {
    //�ĸ�����
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    //̹�˰뾶
    public static final int RADIUS = 20;
    //Ĭ���ٶ� ÿ֡ 30ms
    public static final int DEFAULT_SPEED = 6;
    //̹�˵�״̬
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //̹�˵ĳ�ʼ����
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


    //�ӵ�����
    private List<Bullet> bullets = new ArrayList<Bullet>();

    //ʹ�����������浱ǰ̹���ϵ����еı�ըЧ��
    private List<Explode> explodes = new ArrayList<Explode>();

    //���ڹ���̹��
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
     * ����̹��
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
     * ʹ��ͼƬ�ķ�ʽȥ����̹��
     */
    public abstract void drawImgTank(Graphics g);

    /*
     * ʹ��ϵͳ�ķ�ʽȥ����̹��
     */
    private void drawTank(Graphics g) {
        g.setColor(color);
        //����̹�˵�Բ
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

    //̹�˵��߼�����
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

    //̹���ƶ��Ĺ���
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

    //��һ�ο����ʱ��
    private long fireTime;
    public static final int FIRE_INTERVAL = 200;

    /*
     * ̹�˵Ĺ��ܣ�̹�˿���ķ���
     * ������һ���ӵ������ӵ������������Ϣͨ��̹�˵���Ϣ���
     * Ȼ�󽫴������ӵ���ӵ�̹�˹����������
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

            //�Ӷ�����л�ȡ�ӵ�����
            Bullet bullet = BulletPool.get();
            //�����ӵ�������
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            //�����ӵ�֮�󣬼�¼���η����ʱ��
            fireTime = System.currentTimeMillis();
        }
    }

    /*
     * ����ǰ̹�˵ķ�������е��ӵ����Ƴ���
     */
    private void drawBullets(Graphics g) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }

        //�������е��ӵ��������ɼ����ӵ��Ƴ�������ԭ�ض����
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                i--;
                BulletPool.theReturn(remove);
            }
        }
    }

    //̹�����ٵ�ʱ����̹�˵����е��ӵ�
    public void bulletsReturn() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //̹�˺��ӵ���ײ�ķ���
    public void collideBullets(List<Bullet> bullets) {
        //�������е��ӵ������κ͵�ǰ��̹�˽�����ײ�ļ��
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            //�ӵ���̹��������
            if (MyUtil.isCollide(x, y, RADIUS, bulletX, bulletY)) {
                //�ӵ���ʧ
                bullet.setVisible(false);
                //̹���ܵ��˺�
                hurt(bullet);
                //��ӱ�ըЧ��
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

    //̹���ܵ��˺�
    private void hurt(Bullet bullet) {
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0) {
            hp = 0;
            die();
        }
    }

    //̹��������Ҫ���������
    private void die() {
        //����̹�˱�����
        if (isEnemy) {
            GameFrame.killEnemyCount++;
            //����̹�˱������� �黹�����
            EnemyTanksPool.theReturn(this);
            //�����Ƿ�����ˣ�
            if (GameFrame.isCrossLevel()) {
                //�ж���Ϸ�Ƿ�ͨ�أ�
                if (GameFrame.isLastLevel()) {
                    //ͨ����
                    GameFrame.setGameState(Constant.STATE_WIN);
                } else {
                    //TODO ������һ��
                    GameFrame.startCrossLevel();
                }
            }
        } else {
            delaySecondsToOver(500);
        }
    }

    /*
     * �жϵ�ǰ��̹���Ƿ�����
     */
    public boolean isDie() {
        return hp <= 0;
    }

    /*
     * ���Ƶ�ǰ̹���ϵ����еı�ը��Ч��
     */
    public void drawExplodes(Graphics g) {
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }

        //�����ɼ��ı�ըЧ��ɾ�������ض����
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible()) {
                Explode remove = explodes.remove(i);
                i--;
                ExplodesPool.theReturn(remove);
            }
        }
    }

    //�ڲ��࣬����ʾ̹�˵�Ѫ��
    class BloodBar {
        public static final int BAR_LENGTH = 2 * RADIUS;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g) {
            //����ɫ
            g.setColor(Color.yellow);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
            //��ɫ�ĵ�ǰѪ��
            g.setColor(Color.red);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, hp * BAR_LENGTH / maxHP, BAR_HEIGHT);
            //��ɫ�ı߿�
            g.setColor(Color.white);
            g.drawRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
        }
    }

    //̹�˵��ӵ��͵�ͼ���п����ײ
    public void bulletsCollideMapTiles(List<MapTile> tiles) {
        //for each (��ǿ��forѭ��) ���������е�Ԫ�أ��ڱ����Ĺ����У�ֻ��ʹ�õ�������ɾ����ʽɾ��Ԫ��
        //���е�for each��Ҫ�л�Ϊ������forѭ��
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).isCollideBullet(bullets)) {
                //��ӱ�ըЧ��
                MusicUtil.playBomb();
                addExplode(tiles.get(i).getX() + MapTile.radius, tiles.get(i).getY() + MapTile.radius);
                //��ͼˮ���û�л��ٵĴ���
                if (tiles.get(i).getType() == MapTile.TYPE_HARD) {
                    continue;
                }
                //���õ�ͼ������
                tiles.get(i).setVisible(false);
                //�黹�����
                MapTilePool.theReturn(tiles.get(i));
                //���ϳ�������֮��һ���ӣ��л�����Ϸ�����Ļ���
                if (tiles.get(i).isHouse()) {
                    delaySecondsToOver(3000);
                }
            }
        }
    }

    /*
     * �ӳ����ɺ�����л�����Ϸ����
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
     * һ����ͼ��͵�ǰ��̹����ײ�ķ���
     * ��tile����ȡ8���㣬���ж�8�������Ƿ����κ�һ����͵�ǰ��̹�˷�������ײ
     * ���˳������Ͻǿ�ʼ��˳ʱ�����
     */
    public boolean isCollideTile(List<MapTile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            //����ǿ鲻�ɼ����������ڵ��飬�Ͳ�������ײ�ļ��
            if (!tile.isVisible() || tile.getType() == MapTile.TYPE_COVER) {
                continue;
            }
            //�� 1 ���Ͻǵ�
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //��������˾�ֱ�ӷ��أ�����ͼ����ж���һ����
            if (collide) {
                return true;
            }
            //�� 2 ���ϵ�
            tileX = tile.getX() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 3 ���ϵ�
            tileX = tile.getX() + MapTile.radius * 2;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 4 ���е�
            tileY = tile.getY() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 5 ���µ�
            tileY = tile.getY() + MapTile.radius * 2;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 6 ���µ�
            tileX = tile.getX() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 7 ���µ�
            tileX = tile.getX();
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //�� 8 ���е�
            tileY = tile.getY() + MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
        }
        return false;
    }

    /*
     * ̹�˻��˵ķ���
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
