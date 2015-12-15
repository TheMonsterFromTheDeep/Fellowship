package fettuccine.sprite;

import fettuccine.graphics.Graphic;
import fettuccine.graphics.ImageGraphic;
import fettuccine.util.Resources;

/**
 * The SpriteSystem manages Sprite graphical resources and collision maps.
 * @author TheMonsterFromTheDeep
 */
public class SpriteSystem {
    SpriteData[] data;
    
    public SpriteSystem(int dataCount) {
        dataCount = dataCount >= 0 ? dataCount : 0;
        data = new SpriteData[dataCount];
    }
    
    public void addData(Graphic g, CollisionMap cm, String name) {
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null) {
                data[i] = new SpriteData(g, cm, name);
                return;
            }
        }
        
        SpriteData[] tmp = data;
        data = new SpriteData[data.length + 1];
        System.arraycopy(tmp,0,data,0,tmp.length);
        data[tmp.length] = new SpriteData(g, cm, name);
    }
    
    public void addData(Graphic graphic, String mapPath, String name) {
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null) {
                data[i] = new SpriteData(graphic, CollisionMap.createMapFromMapImage(Resources.loadImageResource(mapPath)), name);
                return;
            }
        }
        
        SpriteData[] tmp = data;
        data = new SpriteData[data.length + 1];
        System.arraycopy(tmp,0,data,0,tmp.length);
        data[tmp.length] = new SpriteData(graphic, CollisionMap.createMapFromMapImage(Resources.loadImageResource(mapPath)), name);
    }
    
    public void addData(String imgPath, CollisionMap map, String name) {
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null) {
                data[i] = new SpriteData(new ImageGraphic(imgPath), map, name);
                return;
            }
        }
        
        SpriteData[] tmp = data;
        data = new SpriteData[data.length + 1];
        System.arraycopy(tmp,0,data,0,tmp.length);
        data[tmp.length] = new SpriteData(new ImageGraphic(imgPath), map, name);
    }
    
    public void addData(String imgPath, String mapPath, String name) {
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null) {
                data[i] = new SpriteData(new ImageGraphic(imgPath), CollisionMap.createMapFromMapImage(Resources.loadImageResource(mapPath)), name);
                return;
            }
        }
        
        SpriteData[] tmp = data;
        data = new SpriteData[data.length + 1];
        System.arraycopy(tmp,0,data,0,tmp.length);
        data[tmp.length] = new SpriteData(new ImageGraphic(imgPath), CollisionMap.createMapFromMapImage(Resources.loadImageResource(mapPath)), name);
    }
    
    public void packData() {
        for(int i = 0; i < data.length; i++) {
            data[i].setId(i);
            if(data[i] == null) {
                SpriteData[] tmp = data;
                data = new SpriteData[i];
                System.arraycopy(tmp,0,data,0,data.length);
                return;
            }
        }
    }
    
    public Camera getCamera(float width, float height) {
        Camera cam = new Camera(width, height);
        cam.graphics = new Graphic[data.length];
        for(int i = 0; i < data.length; i++) {
            cam.graphics[i] = data[i].graphic;
        }
        return cam;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public int getIdOfName(String name) {
        for(SpriteData d : data) {
            if(d.name.equals(name)) {
                return d.getId();
            }
        }
        return -1;
    }
    
    public CollisionMap getMapByName(String name) {
        for(SpriteData d : data) {
            if(d.name.equals(name)) {
                return d.getMapInstance();
            }
        }
        return null;
    }
    
    public CollisionMap getMapById(int id) {
        for(SpriteData d : data) {
            if(d.id == id) {
                return d.getMapInstance();
            }
        }
        return null;
    }
}