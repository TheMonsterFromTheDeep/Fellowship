package fellowship;

import fettuccine.sprite.Properties;

public class ObstacleProperties implements Properties {

    @Override
    public boolean areEqual(Properties p) {
        return false;
    }

    @Override
    public boolean isType(int type) {
        return type == 2;
    }
    
}