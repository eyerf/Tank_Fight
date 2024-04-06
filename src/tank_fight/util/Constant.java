package tank_fight.util;

import java.awt.Font;
import java.awt.Toolkit;

/*
 * ��Ϸ�еĳ������ڸ�����ά����������ڵĹ���
 */
public class Constant {
    /***************��Ϸ�������**********************/
    public static final String GAME_TITLE = "̹�˴�ս";

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;

    //��̬�Ļ��ϵͳ��Ļ�Ŀ��
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int FRAME_X = (SCREEN_W - FRAME_WIDTH) >> 1;
    public static final int FRAME_Y = (SCREEN_H - FRAME_HEIGHT) >> 1;

    /********************��Ϸ�˵����************************/
    public static final int STATE_MENUE = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_LOST = 4;
    public static final int STATE_WIN = 5;
    public static final int STATE_CROSS = 6;

    public static final String[] MENUS = {
            "��ʼ��Ϸ",
            "������Ϸ",
            "��Ϸ����",
            "��Ϸ����",
            "�˳���Ϸ",
    };

    public static final String OVER_STR0 = "ESC���˳���Ϸ";
    public static final String OVER_STR1 = "ENTER�������˵�";

    //��������
    public static final Font SMALL_FONT = new Font("����", Font.BOLD, 12);
    public static final Font GAME_FONT = new Font("����", Font.BOLD, 24);

    public static final int REPAINT_INTERVAL = 30;

    //����������
    public static final int ENEMY_MAX_COUNT = 5;
    public static final int ENEMY_BORN_INTERVAL = 10000;

    public static final int ENEMY_AI_INTERVAL = 750;
    public static final double ENEMY_FIRE_PERCENT = 0.05;

}

