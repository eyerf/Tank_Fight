package tank_fight.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * 游戏相关的信息的类
 */
public class GameInfo {
    //从配置文件中获取
    //关卡数量
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
