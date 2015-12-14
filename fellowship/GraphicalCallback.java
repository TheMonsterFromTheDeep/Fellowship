package fellowship;

import fettuccine.Callback;
import fettuccine.graphics.Renderer;

interface GraphicalCallback extends Callback {
    void render(Renderer r, int y);
}