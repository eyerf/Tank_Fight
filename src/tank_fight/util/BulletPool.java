package tank_fight.util;

import java.util.*;

import tank_fight.game.Bullet;

/*
 * �ӵ��������
 */
public class BulletPool {
    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;

    //���ڱ������е��ӵ�������
    private static final List<Bullet> pool = new ArrayList<Bullet>();

    //������ص�ʱ�򴴽�200���ӵ�������ӵ�������
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /*
     * �ӳ����л�ȡһ���ӵ�����
     */
    public static Bullet get() {
        Bullet bullet = null;
        //�������Ϳ���
        if (pool.isEmpty()) {
            bullet = new Bullet();
        } else {
            //�����л��ж������ߵ�һ��λ�õ��ӵ�����
            bullet = pool.remove(0);
        }
        return bullet;
    }

    //�ӵ������ٵ�ʱ�򣬹黹����������
    public static void theReturn(Bullet bullet) {
        //�����е��ӵ��ĸ����Ѿ����������ֵ���ǾͲ��ڹ黹
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //����黹�ӵ�
        pool.add(bullet);
    }
}
