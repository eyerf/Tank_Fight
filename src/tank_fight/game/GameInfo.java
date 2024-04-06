package tank_fight.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * ��Ϸ��ص���Ϣ����
 */
public class GameInfo {
    //�������ļ��л�ȡ
    //�ؿ�����
    private static final int levelCount;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameInfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }
}
