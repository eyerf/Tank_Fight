package tank_fight.util;

import java.awt.*;

/*
 * 工具类
 */
public class MyUtil {
	private MyUtil() {}
	
	
	/*
	 * 得到指定区间的随机数
	 * @param min 区间最小值，包含
	 * @param max 区间最大值，不包含
	 * @return 随机数
	 */
	public static final int getRandomNumber(int min, int max) {
		return (int)(Math.random() * (max - min) + min);
	}
	
	/*
	 * 得到随机的颜色
	 */
	public static final Color getRandomColor() {
		int red = getRandomNumber(0, 256);
		int blue = getRandomNumber(0, 256);
		int green = getRandomNumber(0, 256);
		Color c = new Color(red, green, blue);
		return c;
	}
	
	/*
	 * 判断一个点是否在某一个正方形的内部
	 * @param rectX 正方形的中心点的X坐标
	 * @param rectY 正方形的中心点的Y坐标
	 * @param radius 正方形的边长的一半
	 * @param pointX 点的X坐标
	 * @param pointY 点的Y坐标
	 * @return 如果点在正方形的内部，返回true，否则返回false
	 */
	public static final boolean isCollide(int rectX, int rectY, int radius, int pointX, int pointY) {
		//正方形中心点和点的x,y轴的距离
		int disX = Math.abs(rectX - pointX);
		int disY = Math.abs(rectY - pointY);
		if(disX < radius && disY < radius) {
			return true;
		}
		return false;
	}
	
	/*
	 * 根据图片的资源路径创建加载图片对象
	 * @param path 图片资源的路径
	 * @return
	 */
	public static final Image creatImage(String path) {
		return Toolkit.getDefaultToolkit().createImage(path);
	}
	
	private static final String[] NAMES = {
		"兔子","老虎","犀牛","蟒蛇","海豹","孔雀","鳄鱼","猴子"
	};
	
	private static final String[] MODIFIY = {
		"可爱","傻傻","萌萌","羞羞","笨笨","呆呆","美丽","聪明"
	};
	
	public static final String getRandomName() {
		return MODIFIY[getRandomNumber(0, MODIFIY.length)] + "的" + NAMES[getRandomNumber(0, NAMES.length)];
	}
}
