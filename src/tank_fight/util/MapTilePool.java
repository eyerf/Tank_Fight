package tank_fight.util;

import java.util.ArrayList;
import java.util.List;

import tank_fight.map.MapTile;

public class MapTilePool {
    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;

    private static final List<MapTile> pool = new ArrayList<MapTile>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    public static MapTile get() {
        MapTile tile = null;
        //³ØÌÁ±»ÌÍ¿ÕÁË
        if (pool.isEmpty()) {
            tile = new MapTile();
        } else {
            tile = pool.remove(0);
        }
        return tile;
    }

    public static void theReturn(MapTile tile) {
        if (pool.size() == POOL_MAX_SIZE) {
            return;
        }
        pool.add(tile);
    }
}
