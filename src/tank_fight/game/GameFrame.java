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
 * 游戏的主窗口类
 * 所有的游戏展示的内容都要在该类中实现
 */
public class GameFrame extends Frame implements Runnable {
    //第一次使用的时候加载，而不是类加载的时候加载
    private Image overImg = null;
    //定义一张和屏幕大小一致的图片
    private final BufferedImage bufImg = new BufferedImage(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    //游戏状态
    private static int gameState;

    //菜单指向
    private static int menuIndex;

    //标题栏的高度
    public static int titleBarH = 31;

    //定义坦克对象
    private static Tank myTank;

    //敌人的坦克容器
    private static final List<Tank> enemies = new ArrayList<Tank>();

    //统计本关卡一共出现的敌人数量
    private static int bornEnemyCount;

    public static int killEnemyCount;

    //定义地图相关的内容
    private static GameMap gameMap = new GameMap();

    private Image helpImg;

    private Image aboutImg;

    /*
     * 对窗口进行初始化
     */
    public GameFrame() {
        initGame();
        initFrame();
        initEventListener();

        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /*
     * 对游戏进行初始化
     */
    private void initGame() {
        gameState = Constant.STATE_MENUE;
    }

    /*
     * 属性进行初始化
     */
    private void initFrame() {
        //设置标题
        setTitle(Constant.GAME_TITLE);
        //设置窗口大小
        setSize(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        //设置窗口的左上角的坐标
        setLocation(Constant.FRAME_X, Constant.FRAME_Y);
        //设置窗口大小不可改变
        setResizable(false);
        //设置窗口可见
        setVisible(true);
    }

    /*
     * 是Frame类的方法，继承下来的方法，
     * 该方法负责了所有的绘制的内容，所有需要在屏幕中显示的
     * 内容，都要在该方法中调用。该方法不能主动调用。必须通过调用
     * repaint()去回调该方法
     * @param g1 画笔对象，系统提供,系统进行初始化
     */
    public void update(Graphics g1) {
        //得到图片的画笔
        Graphics g = bufImg.getGraphics();
        //使用图片画笔将所有的内容绘制到图片上
        g.setFont(Constant.GAME_FONT);
        switch (gameState) {
            case Constant.STATE_MENUE -> drawMenu(g);
            case Constant.STATE_HELP -> drawHelp(g);
            case Constant.STATE_ABOUT -> drawAbout(g);
            case Constant.STATE_RUN -> drawRun(g);
            case Constant.STATE_LOST -> drawLost(g, "过关失败");
            case Constant.STATE_WIN -> drawWin(g);
            case Constant.STATE_CROSS -> drawCross(g);
        }
        //使用系统画笔，将图片绘制到frame上来
        g1.drawImage(bufImg, 0, 0, null);
    }


    /*
     * 绘制游戏结束的方法
     */
    private void drawLost(Graphics g, String str) {
        //保证只加载一次
        if (overImg == null) {
            overImg = MyUtil.creatImage("res/TankEnd.jpg");
        }
        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);
        g.drawImage(overImg, (Constant.FRAME_WIDTH - imgW) >> 1, (Constant.FRAME_HEIGHT - imgH) >> 1, null);
        //添加按键的提示信息
        g.setColor(Color.white);
        g.drawString(Constant.OVER_STR0, 10, Constant.FRAME_HEIGHT - 30);
        g.drawString(Constant.OVER_STR1, Constant.FRAME_WIDTH - 200, Constant.FRAME_HEIGHT - 30);
        g.setColor(Color.white);
        g.drawString(str, Constant.FRAME_WIDTH / 2 - 30, 50);
    }

    /**
     * 绘制游戏胜利的界面
     *
     * @param g
     */
    private void drawWin(Graphics g) {
        drawLost(g, "游戏通关！");
    }

    //游戏运行状态的绘制内容
    private void drawRun(Graphics g) {
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        //绘制地图的碰撞层
        gameMap.drawBk(g);
        drawEnemies(g);
        myTank.draw(g);
        drawExplodes(g);
        //绘制地图的遮挡层
        gameMap.drawCover(g);
        //子弹和坦克的碰撞的方法
        bulletCollideTank();
        //子弹和所有的地图块的碰撞
        bulletAndTankCollideMapTile();
    }

    //绘制所有的敌人的坦克,如果敌人已经死亡，从容器中移除
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
     * 绘制菜单状态下的内容
     * @param g 画笔对象，系统提供
     */
    private void drawMenu(Graphics g) {
        //制绘黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        final int STR_WIDTH = 76;
        int x = Constant.FRAME_WIDTH - STR_WIDTH >> 1;
        int y = Constant.FRAME_HEIGHT / 3;
        final int DIS = 50;
        g.setColor(Color.WHITE);
        for (int i = 0; i < Constant.MENUS.length; i++) {
            if (i == menuIndex) {//选中菜单项的颜色设置为红色
                g.setColor(Color.red);
            } else {//其他的为白色
                g.setColor(Color.white);
            }
            g.drawString(Constant.MENUS[i], x, y + DIS * i);
        }
    }

    /*
     * 初始化窗口的事件监听
     */
    private void initEventListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 处理窗口关闭事件，这里我们简单地调用 System.exit(0) 来退出程序
                System.exit(0);
            }
        });
        //添加按键监听事件
        addKeyListener(new KeyAdapter() {
            //按键被按下的时候被回调的方法
            @Override
            public void keyPressed(KeyEvent e) {
                //被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理方法
                switch (gameState) {
                    case Constant.STATE_MENUE -> keyPressedEventMenu(keyCode);
                    case Constant.STATE_HELP -> keyPressedEventHelp(keyCode);
                    case Constant.STATE_ABOUT -> keyPressedEventAbout(keyCode);
                    case Constant.STATE_RUN -> keyPressedEventRun(keyCode);
                    case Constant.STATE_LOST -> keyPressedEventLost(keyCode);
                    case Constant.STATE_WIN -> keyPressedEventWin(keyCode);
                }
            }
            //按键松开的时候回调的内容
            @Override
            public void keyReleased(KeyEvent e) {
                //被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态，给出不同的处理方法
                if (gameState == Constant.STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }

    /*
     * 游戏通关的按键处理
     */
    protected void keyPressedEventWin(int keyCode) {
        keyPressedEventLost(keyCode);
    }

    //按键松开的时候，游戏中的处理方法
    protected void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                    myTank.setState(Tank.STATE_STAND);
        }
    }

    //游戏结束的按键处理
    private void keyPressedEventLost(int keyCode) {
        //结束游戏
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            setGameState(Constant.STATE_MENUE);
            //很多游戏的操作需要关闭，游戏的某些属性需要重置
            resetGame();
        }
    }

    //重置游戏状态
    private void resetGame() {
        killEnemyCount = 0;
        menuIndex = 0;
        //先让自己的坦克的子弹还回对象池
        myTank.bulletsReturn();
        //销毁自己的坦克
        myTank = null;
        //清空所有敌人与敌人子弹
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
            enemy = null;
        }
        enemies.clear();
        //清空地图资源
        gameMap = null;
    }

    //游戏运行中的按键的处理方法
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

    //菜单状态下的按键的处理
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
                    //继续游戏，进入关卡选择界面
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
     * 开始游戏
     * 并加载关卡信息
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
        //创建坦克对象
        myTank = new MyTank(Constant.FRAME_WIDTH / 3, Constant.FRAME_HEIGHT - Tank.RADIUS, Tank.DIR_UP);
        //使用一个单独的线程用于控制生产敌人的坦克
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
                    //只有在游戏RUN状态下才创建敌人
                } while (gameState == Constant.STATE_RUN);
            }
        }.start();
    }

    @Override
    public void run() {
        while (true) {
            //在此调用repaint， 回调update
            repaint();
            try {
                Thread.sleep(Constant.REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //敌人的坦克的子弹和我的坦克的碰撞
    //我的坦克的子弹和所有的敌人的碰撞
    private void bulletCollideTank() {
        //我的坦克的子弹和所有的敌人的碰撞
        for (Tank enemy : enemies) {
            enemy.collideBullets(myTank.getBullets());
        }
        //敌人的坦克的子弹和我的坦克的碰撞
        for (Tank enemy : enemies) {
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //所有的子弹和坦克和地图块的碰撞
    private void bulletAndTankCollideMapTile() {
        //坦克的子弹和地图块的碰撞
        myTank.bulletsCollideMapTiles(gameMap.getTiles());
        for (Tank enemy : enemies) {
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //坦克和地图的碰撞
        if (myTank.isCollideTile(gameMap.getTiles())) {
            myTank.back();
        }
        //敌人的坦克和地图的碰撞
        for (Tank enemy : enemies) {
            if (enemy.isCollideTile(gameMap.getTiles())) {
                enemy.back();
            }
        }
        //清理所有的被销毁的地图块
        gameMap.clearDestoriedTile();
    }

    //所有的坦克上的爆炸效果
    private void drawExplodes(Graphics g) {
        for (Tank enemy : enemies) {
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    //获得游戏状态
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    //修改游戏状态
    public static int getGameState() {
        return gameState;
    }

    //开始过关动画
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = Constant.FRAME_WIDTH / RECT_WIDTH + 1;
    public static boolean isOpen = false;

    /**
     * 游戏是否是最后一关
     */
    public static boolean isLastLevel() {
        //当前关卡和总关卡数一致
        int currentLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currentLevel == levelCount;

    }

    /**
     * 判断是否过关
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
     * 进入下一关的方法
     */
    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel() + 1);
    }

    public static void startCrossLevel() {
        gameState = Constant.STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }

    //绘制过关动画
    public void drawCross(Graphics g) {
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);
        g.setColor(Color.black);
        //关闭百叶窗的效果
        if (!isOpen) {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Constant.FRAME_HEIGHT);
            }
            //所有的叶片都关闭了
            if (flashTime++ - RECT_WIDTH > 5) {
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInfo.getInstance().getLevel() + 1);
            }
        } else {
            //开百叶窗的效果
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Constant.FRAME_HEIGHT);
            }
            if (flashTime-- == 0) {
                startGame(LevelInfo.getInstance().getLevel());
            }
        }
    }

}
