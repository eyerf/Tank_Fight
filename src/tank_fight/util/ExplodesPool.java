package tank_fight.util;

import java.util.ArrayList;
import java.util.List;

import tank_fight.game.Explode;

/*
 * 爆炸效果的对象池
 */
public class ExplodesPool {
    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int POOL_MAX_SIZE = 20;

    //用于保存所有的爆炸效果的容器
    private static List<Explode> pool = new ArrayList<Explode>();

    //
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /*
     * 从池塘中获取一个爆炸效果对象
     */
    public static Explode get() {
        Explode explode = null;
        //池塘被掏空了
        if (pool.size() == 0) {
            explode = new Explode();
        } else {
            //池塘中还有对象，拿走第一个位置的子弹对象
            explode = pool.remove(0);
        }
        return explode;
    }

    //爆炸消失的时候，归还到池塘中来
    public static void theReturn(Explode explode) {
        //池塘中的爆炸效果的个数已经到达了最大值，那就不在归还
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //否则归还爆炸效果对象
        pool.add(explode);
    }
}
