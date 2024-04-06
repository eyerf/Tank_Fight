package tank_fight.game;

import tank_fight.util.MyUtil;

/**
 * 用来管理当前关卡信息的：单例类
 * 用来管理当前关卡的信息
 * 单例设计模式：如果一个类只需要该类具有唯一的实例，那么可以使用单例设计模式来设计该类
 */
public class LevelInfo {
    //构造方法私有化
    private LevelInfo() {
    }


    //定义静态的本类类型的变量，来指向唯一的实例
    private static LevelInfo instance;

    //懒汉模式的单例  第一次使用该实例的时候创建唯一的实例
    //所有的访问该类的唯一实例，都是通过该方法
    //该方法具有安全隐患，多线程的情况下可能会创建多个实例
    public static LevelInfo getInstance() {
        if (instance == null) {
            //创建了唯一的实例
            instance = new LevelInfo();
        }
        return instance;
    }

    //关卡的编号
    private int level;
    //关卡的敌人的数量
    private int enemyCount;
    //通关的要求的时长, -1 意味着不限时
    private int crossTime = -1;
    //敌人的类型
    private int[] enemyType;
    //游戏的难度 一个大于等于1的值
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

    //获得敌人类型数组中的随机的一个元素
    //获得一个随机的敌人类型
    public int getRandomEnemyType() {
        int index = MyUtil.getRandomNumber(0, enemyType.length);
        return enemyType[index];
    }
}
