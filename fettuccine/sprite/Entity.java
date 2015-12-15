package fettuccine.sprite;

public class Entity {
    protected int layer;
    
    protected float x;
    protected float y;
    
    public int graphic = 0;
    
    public int type;
    
    public boolean isSprite;
    
    public Entity() {
        isSprite = false;
        
        x = y = 0;
        layer = 0;
    }
    
    public final int getLayer() {
        return layer;
        
    }
    
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    
    public final float getX() { return x; }
    public final float getY() { return y; }
}