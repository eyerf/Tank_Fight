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
    private static int levelCount;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameInfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }
}
