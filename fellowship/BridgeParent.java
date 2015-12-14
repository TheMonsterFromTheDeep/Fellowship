package fellowship;

import fettuccine.sprite.CollisionMap;
import fettuccine.sprite.SpriteSystem;

public class BridgeParent extends Target {

    public BridgeTarget[] targets;
    
    public BridgeParent(int startx, int endx, int y, SpriteSystem system, int id) {
        super(FellowshipGame.TARGET_BRIDGE_START);
        targets = new BridgeTarget[endx - startx];
        
        this.setSystem(system);

        this.setAppearance(id);
        
        y = y * 8 + 4;
        
        for(int i = 0; i < targets.length; i++) {
            targets[i] = new BridgeTarget((startx + i) * 8 + 4, y, this, system, id);
        }
        moveTo(startx * 8 + 4, y);
    }
    
    public void doMaps(CollisionMap map) {
        for(BridgeTarget t : targets) {
            t.setCollisionMap(map.createInstance());
        }
    }
}