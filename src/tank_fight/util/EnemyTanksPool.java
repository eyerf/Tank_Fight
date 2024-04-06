package tank_fight.util;

import java.util.ArrayList;
import java.util.List;

import tank_fight.tank.EnemyTank;
import tank_fight.tank.Tank;

/*
 * ̹�˵Ķ����
 */
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;

    //���ڱ������е�̹�˵�����
    private static final List<Tank> pool = new ArrayList<Tank>();

    //������ص�ʱ�򴴽�20��̹�˶�����ӵ�������
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /*
     * �ӳ����л�ȡһ��̹�˶���
     */
    public static Tank get() {
        Tank tank = null;
        //�������Ϳ���
        if (pool.isEmpty()) {
            tank = new EnemyTank();
        } else {
            //�����л��ж������ߵ�һ��λ�õ�̹�˶���
            tank = pool.remove(0);
        }
        return tank;
    }

    //̹�˱����ٵ�ʱ�򣬹黹����������
    public static void theReturn(Tank tank) {
        //�����е�̹�˵ĸ����Ѿ����������ֵ���ǾͲ��ٹ黹
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        //����黹�ӵ�
        pool.add(tank);
    }
}
