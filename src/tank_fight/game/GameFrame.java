package tank_fight.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import tank_fight.map.GameMap;
import tank_fight.tank.EnemyTank;
import tank_fight.tank.MyTank;
import tank_fight.tank.Tank;
import tank_fight.util.MusicUtil;
import tank_fight.util.MyUtil;
import tank_fight.util.Constant;


/*
 * ��Ϸ����������
 * ���е���Ϸչʾ�����ݶ�Ҫ�ڸ�����ʵ��
 */
public class GameFrame extends Frame implements Runnable {
    //��һ��ʹ�õ�ʱ����أ�����������ص�ʱ�����
    private Image overImg = null;
    //����һ�ź���Ļ��Сһ�µ�ͼƬ
    private final BufferedImage bufImg = new BufferedImage(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    //��Ϸ״̬
    private static int gameState;

    //�˵�ָ��
    private static int menuIndex;

    //�������ĸ߶�
    public static int titleBarH = 31;

    //����̹�˶���
    private static Tank myTank;

    //���˵�̹������
    private static final List<Tank> enemies = new ArrayList<Tank>();

    //ͳ�Ʊ��ؿ�һ�����ֵĵ�������
    private static int bornEnemyCount;

    public static int killEnemyCount;

    //�����ͼ��ص�����
    private static GameMap gameMap = new GameMap();

    private Image helpImg;

    private Image aboutImg;

    /*
     * �Դ��ڽ��г�ʼ��
     */
    public GameFrame() {
        initGame();
        initFrame();
        initEventListener();

        //��������ˢ�´��ڵ��߳�
        new Thread(this).start();
    }

    /*
     * ����Ϸ���г�ʼ��
     */
    private void initGame() {
        gameState = Constant.STATE_MENUE;
    }

    /*
     * ���Խ��г�ʼ��
     */
    private void initFrame() {
        //���ñ���
        setTitle(Constant.GAME_TITLE);
        //���ô��ڴ�С
        setSize(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        //���ô��ڵ����Ͻǵ�����
        setLocation(Constant.FRAME_X, Constant.FRAME_Y);
        //���ô��ڴ�С���ɸı�
        setResizable(false);
        //���ô��ڿɼ�
        setVisible(true);
    }

    /*
     * ��Frame��ķ������̳������ķ�����
     * �÷������������еĻ��Ƶ����ݣ�������Ҫ����Ļ����ʾ��
     * ���ݣ���Ҫ�ڸ÷����е��á��÷��������������á�����ͨ������
     * repaint()ȥ�ص��÷���
     * @param g1 ���ʶ���ϵͳ�ṩ,ϵͳ���г�ʼ��
     */
    public void update(Graphics g1) {
        //�õ�ͼƬ�Ļ���
        Graphics g = bufImg.getGraphics();
        //ʹ��ͼƬ���ʽ����е����ݻ��Ƶ�ͼƬ��
        g.setFont(Constant.GAME_FONT);
        switch (gameState) {
            case Constant.STATE_MENUE -> drawMenu(g);
            case Constant.STATE_HELP -> drawHelp(g);
            case Constant.STATE_ABOUT -> drawAbout(g);
            case Constant.STATE_RUN -> drawRun(g);
            case Constant.STATE_LOST -> drawLost(g, "����ʧ��");
            case Constant.STATE_WIN -> drawWin(g);
            case Constant.STATE_CROSS -> drawCross(g);
        }
        //ʹ��ϵͳ���ʣ���ͼƬ���Ƶ�frame����
        g1.drawImage(bufImg, 0, 0, null);
    }


    /*
     * ������Ϸ�����ķ���
     */
    private void drawLost(Graphics g, String str) {
        //��ֻ֤����һ��
        if (overImg == null) {
            overImg = MyUtil.creatImage("res/TankEnd.jpg");
        }
        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);
        g.drawImage(overImg, (Constant.FRAME_WIDTH - imgW) >> 1, (Constant.FRAME_HEIGHT - imgH) >> 1, null);
        //��Ӱ�������ʾ��Ϣ
        g.setColor(Color.white);
        g.drawString(Constant.OVER_STR0, 10, Constant.FRAME_HEIGHT - 30);
        g.drawString(Constant.OVER_STR1, Constant.FRAME_WIDTH - 200, Constant.FRAME_HEIGHT - 30);
        g.setColor(Color.white);
        g.drawString(str, Constant.FRAME_WIDTH / 2 - 30, 50);
    }

    /**
     * ������Ϸʤ���Ľ���
     *
     * @param g
     */
    private void drawWin(Graphics g) {
        drawLost(g, "��Ϸͨ�أ�");
    }

    //��Ϸ����״̬�Ļ�������
    private void drawRun(Graphics g) {
        //���ƺ�ɫ�ı���
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        //���Ƶ�ͼ����ײ��
        gameMap.drawBk(g);
        drawEnemies(g);
        myTank.draw(g);
        drawExplodes(g);
        //���Ƶ�ͼ���ڵ���
        gameMap.drawCover(g);
        //�ӵ���̹�˵���ײ�ķ���
        bulletCollideTank();
        //�ӵ������еĵ�ͼ�����ײ
        bulletAndTankCollideMapTile();
    }

    //�������еĵ��˵�̹��,��������Ѿ����������������Ƴ�
    private void drawEnemies(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie()) {
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    private void drawAbout(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
    }

    /*
     * ���Ʋ˵�״̬�µ�����
     * @param g ���ʶ���ϵͳ�ṩ
     */
    private void drawMenu(Graphics g) {
        //�ƻ��ɫ�ı���
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        final int STR_WIDTH = 76;
        int x = Constant.FRAME_WIDTH - STR_WIDTH >> 1;
        int y = Constant.FRAME_HEIGHT / 3;
        final int DIS = 50;
        g.setColor(Color.WHITE);
        for (int i = 0; i < Constant.MENUS.length; i++) {
            if (i == menuIndex) {//ѡ�в˵������ɫ����Ϊ��ɫ
                g.setColor(Color.red);
            } else {//������Ϊ��ɫ
                g.setColor(Color.white);
            }
            g.drawString(Constant.MENUS[i], x, y + DIS * i);
        }
    }

    /*
     * ��ʼ�����ڵ��¼�����
     */
    private void initEventListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // �����ڹر��¼����������Ǽ򵥵ص��� System.exit(0) ���˳�����
                System.exit(0);
            }
        });
        //��Ӱ��������¼�
        addKeyListener(new KeyAdapter() {
            //���������µ�ʱ�򱻻ص��ķ���
            @Override
            public void keyPressed(KeyEvent e) {
                //�����¼��ļ�ֵ
                int keyCode = e.getKeyCode();
                //��ͬ����Ϸ״̬��������ͬ�Ĵ�����
                switch (gameState) {
                    case Constant.STATE_MENUE -> keyPressedEventMenu(keyCode);
                    case Constant.STATE_HELP -> keyPressedEventHelp(keyCode);
                    case Constant.STATE_ABOUT -> keyPressedEventAbout(keyCode);
                    case Constant.STATE_RUN -> keyPressedEventRun(keyCode);
                    case Constant.STATE_LOST -> keyPressedEventLost(keyCode);
                    case Constant.STATE_WIN -> keyPressedEventWin(keyCode);
                }
            }
            //�����ɿ���ʱ��ص�������
            @Override
            public void keyReleased(KeyEvent e) {
                //�����¼��ļ�ֵ
                int keyCode = e.getKeyCode();
                //��ͬ����Ϸ״̬��������ͬ�Ĵ�����
                if (gameState == Constant.STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }

    /*
     * ��Ϸͨ�صİ�������
     */
    protected void keyPressedEventWin(int keyCode) {
        keyPressedEventLost(keyCode);
    }

    //�����ɿ���ʱ����Ϸ�еĴ�����
    protected void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                    myTank.setState(Tank.STATE_STAND);
        }
    }

    //��Ϸ�����İ�������
    private void keyPressedEventLost(int keyCode) {
        //������Ϸ
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            setGameState(Constant.STATE_MENUE);
            //�ܶ���Ϸ�Ĳ�����Ҫ�رգ���Ϸ��ĳЩ������Ҫ����
            resetGame();
        }
    }

    //������Ϸ״̬
    private void resetGame() {
        killEnemyCount = 0;
        menuIndex = 0;
        //�����Լ���̹�˵��ӵ����ض����
        myTank.bulletsReturn();
        //�����Լ���̹��
        myTank = null;
        //������е���������ӵ�
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
            enemy = null;
        }
        enemies.clear();
        //��յ�ͼ��Դ
        gameMap = null;
    }

    //��Ϸ�����еİ����Ĵ�����
    private void keyPressedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
            }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
            }
            case KeyEvent.VK_SPACE -> myTank.fire();
        }
    }

    private void keyPressedEventAbout(int keyCode) {
        setGameState(Constant.STATE_MENUE);
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(Constant.STATE_MENUE);
    }

    //�˵�״̬�µİ����Ĵ���
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                menuIndex--;
                if (menuIndex < 0) {
                    menuIndex = Constant.MENUS.length - 1;
                }
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                menuIndex++;
                if (menuIndex > Constant.MENUS.length - 1) {
                    menuIndex = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (menuIndex) {
                    //TODO
                    case 0 -> startGame(1);
                    case 1 -> {
                    }
                    //������Ϸ������ؿ�ѡ�����
                    case 2 -> setGameState(Constant.STATE_HELP);
                    case 3 -> setGameState(Constant.STATE_ABOUT);
                    case 4 -> {
                        System.exit(0);
                    }
                }
            }
        }
    }

    /*
     * ��ʼ��Ϸ
     * �����عؿ���Ϣ
     */
    private static void startGame(int level) {
        enemies.clear();
        if (gameMap == null) {
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        bornEnemyCount = 0;
        killEnemyCount = 0;
        gameState = Constant.STATE_RUN;
        //����̹�˶���
        myTank = new MyTank(Constant.FRAME_WIDTH / 3, Constant.FRAME_HEIGHT - Tank.RADIUS, Tank.DIR_UP);
        //ʹ��һ���������߳����ڿ����������˵�̹��
        new Thread() {
            @Override
            public void run() {
                do {
                    if (LevelInfo.getInstance().getEnemyCount() > bornEnemyCount && enemies.size() < Constant.ENEMY_MAX_COUNT) {
                        Tank enemy = EnemyTank.creatEnemy();
                        enemies.add(enemy);
                        bornEnemyCount++;
                    }
                    try {
                        Thread.sleep(Constant.ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //ֻ������ϷRUN״̬�²Ŵ�������
                } while (gameState == Constant.STATE_RUN);
            }
        }.start();
    }

    @Override
    public void run() {
        while (true) {
            //�ڴ˵���repaint�� �ص�update
            repaint();
            try {
                Thread.sleep(Constant.REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //���˵�̹�˵��ӵ����ҵ�̹�˵���ײ
    //�ҵ�̹�˵��ӵ������еĵ��˵���ײ
    private void bulletCollideTank() {
        //�ҵ�̹�˵��ӵ������еĵ��˵���ײ
        for (Tank enemy : enemies) {
            enemy.collideBullets(myTank.getBullets());
        }
        //���˵�̹�˵��ӵ����ҵ�̹�˵���ײ
        for (Tank enemy : enemies) {
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //���е��ӵ���̹�˺͵�ͼ�����ײ
    private void bulletAndTankCollideMapTile() {
        //̹�˵��ӵ��͵�ͼ�����ײ
        myTank.bulletsCollideMapTiles(gameMap.getTiles());
        for (Tank enemy : enemies) {
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //̹�˺͵�ͼ����ײ
        if (myTank.isCollideTile(gameMap.getTiles())) {
            myTank.back();
        }
        //���˵�̹�˺͵�ͼ����ײ
        for (Tank enemy : enemies) {
            if (enemy.isCollideTile(gameMap.getTiles())) {
                enemy.back();
            }
        }
        //�������еı����ٵĵ�ͼ��
        gameMap.clearDestoriedTile();
    }

    //���е�̹���ϵı�ըЧ��
    private void drawExplodes(Graphics g) {
        for (Tank enemy : enemies) {
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    //�����Ϸ״̬
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    //�޸���Ϸ״̬
    public static int getGameState() {
        return gameState;
    }

    //��ʼ���ض���
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = Constant.FRAME_WIDTH / RECT_WIDTH + 1;
    public static boolean isOpen = false;

    /**
     * ��Ϸ�Ƿ������һ��
     */
    public static boolean isLastLevel() {
        //��ǰ�ؿ����ܹؿ���һ��
        int currentLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currentLevel == levelCount;

    }

    /**
     * �ж��Ƿ����
     *
     * @return
     */
    public static boolean isCrossLevel() {
        if (killEnemyCount == LevelInfo.getInstance().getEnemyCount()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ������һ�صķ���
     */
    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel() + 1);
    }

    public static void startCrossLevel() {
        gameState = Constant.STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }

    //���ƹ��ض���
    public void drawCross(Graphics g) {
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);
        g.setColor(Color.black);
        //�رհ�Ҷ����Ч��
        if (!isOpen) {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Constant.FRAME_HEIGHT);
            }
            //���е�ҶƬ���ر���
            if (flashTime++ - RECT_WIDTH > 5) {
                isOpen = true;
                //��ʼ����һ����ͼ
                gameMap.initMap(LevelInfo.getInstance().getLevel() + 1);
            }
        } else {
            //����Ҷ����Ч��
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Constant.FRAME_HEIGHT);
            }
            if (flashTime-- == 0) {
                startGame(LevelInfo.getInstance().getLevel());
            }
        }
    }

}
