package tank_fight.game;

import java.awt.*;

import tank_fight.tank.Tank;
import tank_fight.util.Constant;


/*
 * �ӵ���
 */

public class Bullet {
	//�ӵ���Ĭ�ϵ��ٶ�Ϊ̹�˵��ٶȵ�����
	public static final int DEFAULT_SPEED = Tank.DEFAULT_SPEED << 1;
	//�ڵ��İ뾶
	public static final int RADIUS = 4;
	
	private int x, y;
	private int speed = DEFAULT_SPEED;
	private int dir;
	private int atk;
	private Color color;
	
	//�ӵ��Ƿ�ɼ�
	private boolean visible = true;
	
	public Bullet(int x, int y, int dir, int akt, Color color) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.atk = atk;
		this.color = color;
	}
	
	//�������ʹ�õģ����е����Զ���Ĭ��ֵ
	public Bullet() {
		
	}
	
	/*
	 * �ڵ�������Ļ��Ƶķ���
	 */
	public void draw(Graphics g) {
		if(!visible) {
			return;
		}
		logic();
		g.setColor(color);
		g.fillOval(x - RADIUS, y - RADIUS, RADIUS << 1, RADIUS << 1);
	}
	
	
	/*
	 * �ӵ����߼�
	 */
	private void logic() {
		move();
	}
	
	private void move() {
		switch(dir) {
		case Tank.DIR_UP:
			y = y - speed;
			if(y <= 0) {
				visible = false;
			}
			break;
		case Tank.DIR_DOWN:
			y = y + speed;
			if(y >= Constant.FRAME_HEIGHT) {
				visible = false;
			}
			break;
		case Tank.DIR_LEFT:
			x = x - speed;
			if(x <= 0) {
				visible = false;
			}
			break;
		case Tank.DIR_RIGHT:
			x = x + speed;
			if(x >= Constant.FRAME_WIDTH) {
				visible = false;
			}
			break;
		}
	}
	
	public int getAtk() {
		return atk;
	}
	
	public int getDir() {
		return dir;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setAtk(int atk) {
		this.atk = atk;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
}
