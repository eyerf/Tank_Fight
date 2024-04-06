package tank_fight.util;

import java.util.ArrayList;
import java.util.List;

import tank_fight.game.Explode;

/*
 * ��ըЧ���Ķ����
 */
public class ExplodesPool {
    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int POOL_MAX_SIZE = 20;

    //���ڱ������еı�ըЧ��������
    private static List<Explode> pool = new ArrayList<Explode>();

    //
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /*
     * �ӳ����л�ȡһ����ըЧ������
     */
    public static Explode get() {
        Explode explode = null;
        //�������Ϳ���
        if (pool.size() == 0) {
            explode = new Explode();
        } else {
            //�����л��ж������ߵ�һ��λ�õ��ӵ�����
            explode = pool.remove(0);
        }
        return explode;
    }

    //��ը��ʧ��ʱ�򣬹黹����������
    public static void theReturn(Explode explode) {
        //�����еı�ըЧ���ĸ����Ѿ����������ֵ���ǾͲ��ڹ黹
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //����黹��ըЧ������
        pool.add(explode);
    }
}
