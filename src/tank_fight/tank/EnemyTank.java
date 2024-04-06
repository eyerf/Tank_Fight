package tank_fight.tank;

import java.awt.Graphics;
import java.awt.Image;

import tank_fight.game.GameFrame;
import tank_fight.game.LevelInfo;
import tank_fight.util.Constant;
import tank_fight.util.EnemyTanksPool;
import tank_fight.util.MyUtil;

/*
 * ����̹����
 */
public class EnemyTank extends Tank {
    public static final int TYPE_GREEN = 0;
    public static final int TYPE_WHITE = 1;
    private int type = TYPE_GREEN;
    //����̹�˵�ͼƬ����
    private static final Image[] greenImg;
    private static final Image[] whiteImg;
    //��¼5�뿪ʼ��ʱ��
    private long aiTime;

    static {
        greenImg = new Image[4];
        greenImg[0] = MyUtil.creatImage("res/player2/p2tankU.png");
        greenImg[1] = MyUtil.creatImage("res/player2/p2tankD.png");
        greenImg[2] = MyUtil.creatImage("res/player2/p2tankL.png");
        greenImg[3] = MyUtil.creatImage("res/player2/p2tankR.png");
        whiteImg = new Image[4];
        whiteImg[0] = MyUtil.creatImage("res/enemy/enemy1U.png");
        whiteImg[1] = MyUtil.creatImage("res/enemy/enemy1D.png");
        whiteImg[2] = MyUtil.creatImage("res/enemy/enemy1L.png");
        whiteImg[3] = MyUtil.creatImage("res/enemy/enemy1R.png");
    }

    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //����һ�������Ϳ�ʼ��ʱ
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0, 2);
    }

    public EnemyTank() {
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0, 2);
    }

    public void drawImgTank(Graphics g) {

        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] : whiteImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
        ai();
    }

    //���ڴ���һ�����˵�̹��
    public static Tank creatEnemy() {
        int x = MyUtil.getRandomNumber(0, 2) == 0 ? RADIUS : Constant.FRAME_WIDTH - RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank) EnemyTanksPool.get();
        enemy.setEnemy(true);
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setState(STATE_MOVE);
        //������Ϸ���Ѷȣ����õ��˵�Ѫ��
        int maxHP = DEFAULT_HP * LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHP);
        enemy.setMaxHP(maxHP);
        //ͨ�عؿ���Ϣ�еĵ������ͣ������õ�ǰ�����ĵ��˵�����
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }

    /*
     * ���˵�AI
     */
    private void ai() {
        if (System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL) {
            //���5�����һ��״̬
            setDir(MyUtil.getRandomNumber(DIR_UP, DIR_RIGHT + 1));
            setState(MyUtil.getRandomNumber(0, 2) == 0 ? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //�����С�ڿ�����ʣ�����̹�˿���
        if (Math.random() < Constant.ENEMY_FIRE_PERCENT) {
            fire();
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
