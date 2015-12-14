package fellowship;

import fettuccine.sprite.SpriteSystem;

public class BridgeTarget extends Target {

    public BridgeParent parent;
    
    public BridgeTarget(int rx, int ry, BridgeParent parent, SpriteSystem system, int id) {
        super(FellowshipGame.TARGET_BRIDGE_NODE);
        this.parent = parent;
        
        this.setSystem(system);

        this.setAppearance(id);
        
        moveTo(rx, ry);
    }

    
}