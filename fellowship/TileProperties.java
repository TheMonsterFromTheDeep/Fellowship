package fellowship;

import fettuccine.sprite.Properties;

public class TileProperties implements Properties {
    public int tileid;
    //0 for id, 1 for solid
    public int mode;
    public boolean solid;
    public boolean dangerous;

    public TileProperties(int id, boolean solid, boolean dangerous) {
        this.tileid = id;
        this.solid = solid;
        this.dangerous = dangerous;
    }
    
    public TileProperties(int tileid) {
        this.tileid = tileid;
        this.solid = false;
        this.mode = 0;
        dangerous = false;
    }
    
    public TileProperties(boolean solid) {
        this.tileid = 0;
        this.solid = solid;
        this.mode = 1;
        dangerous = false;
    }
    
    @Override
    public boolean areEqual(Properties p) {     
        if(p.isType(1)) {
            TileProperties comp = (TileProperties)p;
            if(comp.mode == 0) {
                return tileid == comp.tileid;
            }
            if(comp.mode == 1) {
                return solid == comp.solid;
            }
        }
        if(p.isType(2)) {
            return dangerous;
        }
        return false;
    }

    //Types: 1->tileproperties, 2->obstacleproperties
    @Override
    public boolean isType(int type) {
        return type == 1;
    }
}