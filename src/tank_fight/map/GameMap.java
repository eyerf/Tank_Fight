package tank_fight.map;

import java.awt.Graphics;
import java.io.FileInputStream;
import java.util.*;

import tank_fight.game.GameFrame;
import tank_fight.game.LevelInfo;
import tank_fight.tank.Tank;
import tank_fight.util.Constant;
import tank_fight.util.MapTilePool;

/*
 * 游戏地图类
 */
public class GameMap {

    private static final int MAP_X = Tank.RADIUS * 2;
    private static final int MAP_WIDTH = Constant.FRAME_WIDTH - Tank.RADIUS * 4;
    private static final int MAP_HEIGHT = Constant.FRAME_HEIGHT - Tank.RADIUS * 6 - GameFrame.titleBarH;
    private static final int MAP_Y = Tank.RADIUS * 2 + GameFrame.titleBarH;

    //游戏地图元素块的容器
    private List<MapTile> tiles = new ArrayList<MapTile>();
    //大本营
    private TankHouse house;

    public GameMap() {

    }

    /*
     * 初始化地图元素块,level : 第几关
     */
    public void initMap(int level) {
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     *
     * @param level
     */
    private void loadLevel(int level) throws Exception {
        //获得关卡信息类的唯一实例对象
        LevelInfo levelInfo = LevelInfo.getInstance();
        //设置敌人数量
        levelInfo.setLevel(level);


        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_" + level));

        //将所有的地图信息都加载进来
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人类型
        levelInfo.setEnemyCount(enemyCount);

        //关卡难度
        //如果没有设计游戏难度，就按默认处理
        String levelType = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType.equals(null) ? "1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));

        //0,1 对敌人类型的解析
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < enemyType.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        levelInfo.setEnemyType(type);

        //把实参都读取到数组中来
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i - 1] = prop.getProperty("param" + i);
        }
        //使用解析到的参数，调用对应的方法
        //TODO
        System.out.println(enemyCount);
        System.out.println(methodName);
        System.out.println(params[0]);

        invokeMethod(methodName, params);
    }

    //根据方法发名字和参数，调用对应的方法
    private void invokeMethod(String name, String[] params) {
        for (String param : params) {
            //获得每一行的方法发参数，解析
            String[] split = param.split(",");
            int[] arr = new int[split.length];
            int i;
            for (i = 0; i < split.length - 1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //块之间的间隔是地图块的倍数
            final int DIS = MapTile.tileW;
            //解析最后一个double值
            int dis = (int) (Double.parseDouble(split[i]) * DIS);
            switch (name) {
                case "addRow" -> {
                    System.out.println(MAP_Y);
                    addRow(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_X + MAP_WIDTH - arr[2] * DIS, arr[3], dis);
                }
                case "addCol" ->
                        addCol(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_Y + MAP_HEIGHT - arr[2] * DIS, arr[3], dis);
                case "addRect" ->
                        addRect(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_X + MAP_WIDTH - arr[2] * DIS, MAP_Y + MAP_HEIGHT - arr[3] * DIS, arr[4], dis);
            }
        }
    }

    //将老巢的所有的元素块添加到地图的容器中来
    private void addHouse() {
        tiles.addAll(house.getTiles());
    }

    /*
     * 判断某一个点确定的地图块，是否和tiles集合中所有的块有重叠的部分
     * 有重叠返回true否则返回false
     */
    private boolean isCollide(List<MapTile> tiles, int x, int y) {
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX - x) < MapTile.tileW && Math.abs(tileY - y) < MapTile.tileH) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只对没有遮挡效果的块进行绘制
     *
     * @param g
     */
    public void drawBk(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() != MapTile.TYPE_COVER) {
                tile.draw(g);
            }
        }
    }

    /**
     * 只绘制有遮挡效果的块
     *
     * @param g
     */
    public void drawCover(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() == MapTile.TYPE_COVER) {
                tile.draw(g);
            }
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }

    /*
     * 将所有的不可见的地图块从容器中移除
     */
    public void clearDestoriedTile() {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible()) {
                tiles.remove(i);
                i--;
            }
        }
    }

    /**
     * 往地图容器添加一行指定类型的地图块
     *
     * @param startX 添加地图块的起始的X坐标
     * @param startY 添加地图块的起始的Y坐标
     * @param endX   添加地图块的结束的X坐标
     * @param type   地图块的类型
     * @param DIS    地图块之间的中心点的间隔 如果是块的宽度 意味着是连续的
     *               如果大于块的宽度，那就是不连续的
     */
    public void addRow(int startX, int startY, int endX, int type, final int DIS) {
        int count = (endX - startX) / (MapTile.tileW + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW + DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

    /**
     * 往地图元素块容器中添加一列元素
     *
     * @param startX 该列的起始X坐标
     * @param startY 该列的起始Y坐标
     * @param endY   该列的结束的Y坐标
     * @param type   元素类型
     * @param DIS    相邻元素中心点的间距
     */
    public void addCol(int startX, int startY, int endY, int type, final int DIS) {
        int count = (endY - startY) / (MapTile.tileH + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX);
            tile.setY(startY + i * (MapTile.tileH + DIS));
            tiles.add(tile);
        }
    }

    //对指定的矩形区域添加元素块
    public void addRect(int startX, int startY, int endX, int endY, int type, final int DIS) {
        int rows = (endY - startY) / (MapTile.tileW + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, startY + i * (MapTile.tileW + DIS), endX, type, DIS);
        }
    }
}
