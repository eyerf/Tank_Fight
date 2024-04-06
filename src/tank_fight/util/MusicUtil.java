package tank_fight.util;

import java.applet.*;
import java.io.*;
import java.net.MalformedURLException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;

    //װ��������Դ
    static {
        try {
            //����������������ļ������ڵľ���·��
            //���뿪ʼ����
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
            //���뱬ը����
            bomb = Applet.newAudioClip(new File("music/blast.wav").toURL());
        } catch (MalformedURLException e) {
            // TODO �Զ����ɵ� catch ��
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
