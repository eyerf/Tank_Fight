package tank_fight.util;

import java.applet.*;
import java.io.*;
import java.net.MalformedURLException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;

    //装载音乐资源
    static {
        try {
            //括号里面的是音乐文件的所在的绝对路径
            //导入开始音乐
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
            //导入爆炸音乐
            bomb = Applet.newAudioClip(new File("music/blast.wav").toURL());
        } catch (MalformedURLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    public static void playStart() {
        start.play();
    }

    public static void playBomb() {
        bomb.play();
    }
}
