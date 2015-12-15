package fettuccine.sprite;

import fettuccine.geom.Polygon;
import fettuccine.geom.Rectangle;
import fettuccine.geom.Vector2;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import fettuccine.util.Resources;

public class CollisionMap {
    Polygon[] components;
    /** The anchor stores the upper-left corner of the collision map in its default state. */
    Vector2 anchor;
    /** The center stores the center of the CollisionMap. */
    Vector2 center;
    
    public CollisionMap() {
        anchor = new Vector2(0);
        center = new Vector2(0);
    }
    
    public CollisionMap(CollisionMap cm) {
        components = new Polygon[cm.components.length];
        for(int i = 0; i < components.length; i++) {
            components[i] = cm.components[i].getCopy();
        }
        anchor = new Vector2(cm.anchor);
        center = new Vector2(cm.center);
    }
    
    public boolean collides(CollisionMap m) {
        for(Polygon p1 : components) {
            for(Polygon p2 : m.components) {
                if(p1.intersects(p2)) { return true; }
            }
        }
        return false;
    }
    
    public void setAnchor(float x, float y) {
        float dx = x - anchor.x;
        float dy = y - anchor.y;
        for(Polygon p : components) {
            p.shift(dx, dy);
        }
        anchor = new Vector2(dx, dy);
    }
    
    public void shift(float x, float y) {
        for(Polygon p : components) {
            p.shift(x, y);
        }
        anchor.shift(x, y);
        center.shift(x, y);
    }
    
    public void rotate(double degrees) {
        for(Polygon p : components) {
            p.rotate(center.x, center.y, degrees);
        }
        anchor.rotate(center.x, center.y, degrees);
    }
    
    public CollisionMap createInstance() {
       return new CollisionMap(this);
    }
    
    public Polygon[] getPolygons() {
        return components;
    }
    
    private static Polygon createMapComponentFromMap(BufferedImage b, int red) {
        Vector2[] data;

        int pointCount = 0;

        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1 && c.getRed() == red) {
                    if(pointCount < c.getGreen()) {
                        pointCount = c.getGreen();
                    }
                }
            }
        }

        data = new Vector2[pointCount + 1];

        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1 && c.getRed() == red) {
                    data[c.getGreen()] = new Vector2(x, y);
                }
            }
        }

        return new Polygon(data);
    }
    
    public static CollisionMap createMapFromMapImage(BufferedImage b) {
        int mapCompCount = -1;
        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1) {
                    if(mapCompCount < c.getRed()) {
                        mapCompCount = c.getRed();
                    }
                }
            }
        }
        
        CollisionMap cm = new CollisionMap();
        cm.components = new Polygon[mapCompCount + 1];
        for(int i = 0; i <= mapCompCount; i++) {
            cm.components[i] = createMapComponentFromMap(b, i);
        }
        
        return cm;
    }
    
    public static CollisionMap createMapFromMapImage(String path) {
        return createMapFromMapImage(Resources.loadImageResource(path));
    }
    
    public static CollisionMap createMapFromString(String s) {
        s = s.trim();
        String[] data = s.split(" ");
        Vector2[] points = new Vector2[data.length / 2];
        for(int i = 0; i < points.length; i++) {
            points[i] = new Vector2(Float.parseFloat(data[2 * i]), Float.parseFloat(data[(2 * i) + 1]));
        }
        
        CollisionMap map = new CollisionMap();
        map.components = new Polygon[1];
        map.components[0] = new Polygon(points);
        
        return map;
    }
    
    public static CollisionMap createMapFromTxt(String url) {
        return createMapFromString(Resources.loadStringResource(url));
    }
}