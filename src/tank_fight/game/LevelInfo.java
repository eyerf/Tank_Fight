package tank_fight.game;

import tank_fight.util.MyUtil;

/**
 * ��������ǰ�ؿ���Ϣ�ģ�������
 * ��������ǰ�ؿ�����Ϣ
 * �������ģʽ�����һ����ֻ��Ҫ�������Ψһ��ʵ������ô����ʹ�õ������ģʽ����Ƹ���
 */
public class LevelInfo {
    //���췽��˽�л�
    private LevelInfo() {
    }


    //���徲̬�ı������͵ı�������ָ��Ψһ��ʵ��
    private static LevelInfo instance;

    //����ģʽ�ĵ���  ��һ��ʹ�ø�ʵ����ʱ�򴴽�Ψһ��ʵ��
    //���еķ��ʸ����Ψһʵ��������ͨ���÷���
    //�÷������а�ȫ���������̵߳�����¿��ܻᴴ�����ʵ��
    public static LevelInfo getInstance() {
        if (instance == null) {
            //������Ψһ��ʵ��
            instance = new LevelInfo();
        }
        return instance;
    }

    //�ؿ��ı��
    private int level;
    //�ؿ��ĵ��˵�����
    private int enemyCount;
    //ͨ�ص�Ҫ���ʱ��, -1 ��ζ�Ų���ʱ
    private int crossTime = -1;
    //���˵�����
    private int[] enemyType;
    //��Ϸ���Ѷ� һ�����ڵ���1��ֵ
    private int levelType;

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public int getLevelType() {
        return levelType <= 0 ? 1 : levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    //��õ������������е������һ��Ԫ��
    //���һ������ĵ�������
    public int getRandomEnemyType() {
        int index = MyUtil.getRandomNumber(0, enemyType.length);
        return enemyType[index];
    }
}
