package tank_fight.util;

import java.util.*;

import tank_fight.game.Bullet;

/*
 * 子弹对象池类
 */
public class BulletPool {
    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;

    //用于保存所有的子弹的容器
    private static final List<Bullet> pool = new ArrayList<Bullet>();

    //在类加载的时候创建200个子弹对象添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /*
     * 从池塘中获取一个子弹对象
     */
    public static Bullet get() {
        Bullet bullet = null;
        //池塘被掏空了
        if (pool.isEmpty()) {
            bullet = new Bullet();
        } else {
            //池塘中还有对象，拿走第一个位置的子弹对象
            bullet = pool.remove(0);
        }
        return bullet;
    }

    //子弹被销毁的时候，归还到池塘中来
    public static void theReturn(Bullet bullet) {
        //池塘中的子弹的个数已经到达了最大值，那就不在归还
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //否则归还子弹
        pool.add(bullet);
    }
}
