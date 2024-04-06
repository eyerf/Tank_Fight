package tank_fight.util;

import java.awt.*;

/*
 * ������
 */
public class MyUtil {
	private MyUtil() {}
	
	
	/*
	 * �õ�ָ������������
	 * @param min ������Сֵ������
	 * @param max �������ֵ��������
	 * @return �����
	 */
	public static final int getRandomNumber(int min, int max) {
		return (int)(Math.random() * (max - min) + min);
	}
	
	/*
	 * �õ��������ɫ
	 */
	public static final Color getRandomColor() {
		int red = getRandomNumber(0, 256);
		int blue = getRandomNumber(0, 256);
		int green = getRandomNumber(0, 256);
		Color c = new Color(red, green, blue);
		return c;
	}
	
	/*
	 * �ж�һ�����Ƿ���ĳһ�������ε��ڲ�
	 * @param rectX �����ε����ĵ��X����
	 * @param rectY �����ε����ĵ��Y����
	 * @param radius �����εı߳���һ��
	 * @param pointX ���X����
	 * @param pointY ���Y����
	 * @return ������������ε��ڲ�������true�����򷵻�false
	 */
	public static final boolean isCollide(int rectX, int rectY, int radius, int pointX, int pointY) {
		//���������ĵ�͵��x,y��ľ���
		int disX = Math.abs(rectX - pointX);
		int disY = Math.abs(rectY - pointY);
		if(disX < radius && disY < radius) {
			return true;
		}
		return false;
	}
	
	/*
	 * ����ͼƬ����Դ·����������ͼƬ����
	 * @param path ͼƬ��Դ��·��
	 * @return
	 */
	public static final Image creatImage(String path) {
		return Toolkit.getDefaultToolkit().createImage(path);
	}
	
	private static final String[] NAMES = {
		"����","�ϻ�","Ϭţ","����","����","��ȸ","����","����"
	};
	
	private static final String[] MODIFIY = {
		"�ɰ�","ɵɵ","����","����","����","����","����","����"
	};
	
	public static final String getRandomName() {
		return MODIFIY[getRandomNumber(0, MODIFIY.length)] + "��" + NAMES[getRandomNumber(0, NAMES.length)];
	}
}
