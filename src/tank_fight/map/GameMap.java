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
 * ��Ϸ��ͼ��
 */
public class GameMap {

    private static final int MAP_X = Tank.RADIUS * 2;
    private static final int MAP_WIDTH = Constant.FRAME_WIDTH - Tank.RADIUS * 4;
    private static final int MAP_HEIGHT = Constant.FRAME_HEIGHT - Tank.RADIUS * 6 - GameFrame.titleBarH;
    private static final int MAP_Y = Tank.RADIUS * 2 + GameFrame.titleBarH;

    //��Ϸ��ͼԪ�ؿ������
    private List<MapTile> tiles = new ArrayList<MapTile>();
    //��Ӫ
    private TankHouse house;

    public GameMap() {

    }

    /*
     * ��ʼ����ͼԪ�ؿ�,level : �ڼ���
     */
    public void initMap(int level) {
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //��ʼ����Ӫ
        house = new TankHouse();
        addHouse();
    }

    /**
     * ���عؿ���Ϣ
     *
     * @param level
     */
    private void loadLevel(int level) throws Exception {
        //��ùؿ���Ϣ���Ψһʵ������
        LevelInfo levelInfo = LevelInfo.getInstance();
        //���õ�������
        levelInfo.setLevel(level);


        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_" + level));

        //�����еĵ�ͼ��Ϣ�����ؽ���
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //���õ�������
        levelInfo.setEnemyCount(enemyCount);

        //�ؿ��Ѷ�
        //���û�������Ϸ�Ѷȣ��Ͱ�Ĭ�ϴ���
        String levelType = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType.equals(null) ? "1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));

        //0,1 �Ե������͵Ľ���
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < enemyType.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        levelInfo.setEnemyType(type);

        //��ʵ�ζ���ȡ����������
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i - 1] = prop.getProperty("param" + i);
        }
        //ʹ�ý������Ĳ��������ö�Ӧ�ķ���
        //TODO
        System.out.println(enemyCount);
        System.out.println(methodName);
        System.out.println(params[0]);

        invokeMethod(methodName, params);
    }

    //���ݷ��������ֺͲ��������ö�Ӧ�ķ���
    private void invokeMethod(String name, String[] params) {
        for (String param : params) {
            //���ÿһ�еķ���������������
            String[] split = param.split(",");
            int[] arr = new int[split.length];
            int i;
            for (i = 0; i < split.length - 1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //��֮��ļ���ǵ�ͼ��ı���
            final int DIS = MapTile.tileW;
            //�������һ��doubleֵ
            int dis = (int) (Double.parseDouble(split[i]) * DIS);
            switch (name) {
                case "addRow":
                    System.out.println(MAP_Y);
                    addRow(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_X + MAP_WIDTH - arr[2] * DIS, arr[3], dis);
                    break;
                case "addCol":
                    addCol(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_Y + MAP_HEIGHT - arr[2] * DIS, arr[3], dis);
                    break;
                case "addRect":
                    addRect(MAP_X + arr[0] * DIS, MAP_Y + arr[1] * DIS, MAP_X + MAP_WIDTH - arr[2] * DIS, MAP_Y + MAP_HEIGHT - arr[3] * DIS, arr[4], dis);
                    break;
            }
        }
    }

    //���ϳ������е�Ԫ�ؿ���ӵ���ͼ����������
    private void addHouse() {
        tiles.addAll(house.getTiles());
    }

    /*
     * �ж�ĳһ����ȷ���ĵ�ͼ�飬�Ƿ��tiles���������еĿ����ص��Ĳ���
     * ���ص�����true���򷵻�false
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
     * ֻ��û���ڵ�Ч���Ŀ���л���
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
     * ֻ�������ڵ�Ч���Ŀ�
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
     * �����еĲ��ɼ��ĵ�ͼ����������Ƴ�
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
     * ����ͼ�������һ��ָ�����͵ĵ�ͼ��
     *
     * @param startX ��ӵ�ͼ�����ʼ��X����
     * @param startY ��ӵ�ͼ�����ʼ��Y����
     * @param endX   ��ӵ�ͼ��Ľ�����X����
     * @param type   ��ͼ�������
     * @param DIS    ��ͼ��֮������ĵ�ļ�� ����ǿ�Ŀ�� ��ζ����������
     *               ������ڿ�Ŀ�ȣ��Ǿ��ǲ�������
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
     * ����ͼԪ�ؿ����������һ��Ԫ��
     *
     * @param startX ���е���ʼX����
     * @param startY ���е���ʼY����
     * @param endY   ���еĽ�����Y����
     * @param type   Ԫ������
     * @param DIS    ����Ԫ�����ĵ�ļ��
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

    //��ָ���ľ����������Ԫ�ؿ�
    public void addRect(int startX, int startY, int endX, int endY, int type, final int DIS) {
        int rows = (endY - startY) / (MapTile.tileW + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, startY + i * (MapTile.tileW + DIS), endX, type, DIS);
        }
    }
}
