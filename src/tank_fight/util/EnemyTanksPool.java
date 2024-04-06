package tank_fight.util;

import java.util.ArrayList;
import java.util.List;

import tank_fight.tank.EnemyTank;
import tank_fight.tank.Tank;

/*
 * 坦克的对象池
 */
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;

    //用于保存所有的坦克的容器
    private static final List<Tank> pool = new ArrayList<Tank>();

    //在类加载的时候创建20个坦克对象添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /*
     * 从池塘中获取一个坦克对象
     */
    public static Tank get() {
        Tank tank = null;
        //池塘被掏空了
        if (pool.isEmpty()) {
            tank = new EnemyTank();
        } else {
            //池塘中还有对象，拿走第一个位置的坦克对象
            tank = pool.remove(0);
        }
        return tank;
    }

    //坦克被销毁的时候，归还到池塘中来
    public static void theReturn(Tank tank) {
        //池塘中的坦克的个数已经到达了最大值，那就不再归还
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //否则归还子弹
        pool.add(tank);
    }
}
