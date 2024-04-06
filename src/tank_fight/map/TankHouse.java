package tank_fight.map;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import tank_fight.util.Constant;

/*
 * 玩家的大本营
 */
public class TankHouse {
    //老巢的x, y坐标
    public static final int HOUSE_X = (Constant.FRAME_WIDTH - 3 * MapTile.tileW) / 2 + 2;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT - 2 * MapTile.tileH;

    private final List<MapTile> tiles = new ArrayList<MapTile>();

    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X, HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X, HOUSE_Y + MapTile.tileH));
        tiles.add(new MapTile(HOUSE_X + MapTile.tileW, HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X + MapTile.tileW * 2, HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X + MapTile.tileW * 2, HOUSE_Y + MapTile.tileH));
        //有文字的块
        tiles.add(new MapTile(HOUSE_X + MapTile.tileW, HOUSE_Y + MapTile.tileH));
        //设置老窝地图块的类型
        tiles.get(tiles.size() - 1).setType(MapTile.TYPE_HOUSE);
    }

    public void draw(Graphics g) {
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }


}
