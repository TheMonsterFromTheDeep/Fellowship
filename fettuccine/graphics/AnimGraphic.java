package fettuccine.graphics;

import fettuccine.util.Resources;
import java.awt.image.BufferedImage;

/**
 * Contains image data.<br /><br />
 * 
 * @author TheMonsterFromTheDeep
 */
public class AnimGraphic implements Graphic {
    /** The graphical data of the Graphic. */
    BufferedImage[] data;
    
    int renderIndex;
    
    public AnimGraphic(BufferedImage[] data) {
        this.data = data;
        renderIndex = 0;
    }
    
    public AnimGraphic(String relPath, int tilewidth, int tileheight) {
        BufferedImage loadBuffer = Resources.loadImageResource(relPath);
        data = new BufferedImage[loadBuffer.getWidth() / tilewidth];
        for(int i = 0; i < data.length; i++) {
            data[i] = loadBuffer.getSubimage(tilewidth * i,0, tilewidth, tileheight);
        }
        renderIndex = 0;
    }
    
    @Override
    public void render(Renderer target, float x, float y) {
        target.drawImageCentered(data[renderIndex], x, y);
    }
    
    public void render(Renderer target, float x, float y, int renderIndex) {
        if(renderIndex < 0) { renderIndex = 0; }
        renderIndex %= data.length;
        target.drawImageCentered(data[renderIndex], x, y);
    }
    
    /**
     * Gets the width of the ImageGraphic. This is the width, in pixels, of the image that it represents.
     * @returns The width, in pixels, of the ImageGraphic.
     */
    @Override
    public int getWidth() { return data[renderIndex].getWidth(); }
    /**
     * Gets the height of the ImageGraphic. This is the height, in pixels, of the image that it represents.
     * @returns The height, in pixels, of the ImageGraphic.
     */
    @Override
    public int getHeight() { return data[renderIndex].getHeight(); }
}