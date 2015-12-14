package fellowship;

import fettuccine.Callback;
import fettuccine.Game;
import fettuccine.control.KeyControl;
import fettuccine.geom.Rectangle;
import fettuccine.geom.Vector2;
import fettuccine.graphics.AnimGraphic;
import fettuccine.graphics.Renderer;
import fettuccine.sprite.Camera;
import fettuccine.sprite.CollisionMap;
import fettuccine.sprite.Entity;
import fettuccine.sprite.Sprite;
import fettuccine.sprite.SpriteSystem;
import fettuccine.sprite.World;
import fettuccine.util.Resources;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;


//MUZAK:
//http://www.beepbox.co/#5s0kbl00e0zt7a3g0zj7i0r1w1111f0000d1111c0000h0000v0000o3210b018O4xgx8OkBgi4zhj8icx9D4h4h4h4h4h8y4h4h8y8y4h4h4h4h4h8h4h4h8h4h8h4h4h4h4h4h4h0h4h4h0h4h0y8y8h4hp239FzBdAAC7A3tj9RR2QQSidJwaWHbbGsI6HcGjbU69TRiJ8HC6JB0G4E5kS3eKxpeGSDpV8a8mOSGGGOCOSA56CSI5dllqp-sExXAgmmQRSwE1ggkNWpzRmkCvmO7JiBceWV6GiGGGJGGGAGGGwaoWkDvk6KwdCvdVllqpsy0F2noA1mi3aGGGi8A5c12yyw50aaq0k0E20
//ACTUALLY:
//http://www.beepbox.co/#5s0kbl02e0mt7a3g0zj7i0r1w1111f0000d1111c0000h0000v0000o3210b018O4xgx8OkBgi4z9j8icx9D4h4h4h4h4h8y4h4h4y8y4h4h4h4h4h8h4h4h8h4h4h4h4h4h4h4h4h0h4h4h0h4h4y8y8h4hp239FzBdAAC7A3tj9RR2QQSidJwaWHbbGsI6HcGjbU69TRiJ8HC6JB0G4E5kS3eKxpeGSDpV8a8mOSGGGOCOSA56CSI5dllqp-sExXAgmmQRSwE1ggkNWpzRmkCvmO7JiBceWV6GiGGGJGGGAGGGwaoWkDvk6KwdCvdVllqpsy0F2noA1mi3aGGGi8A5c12yyw50aaq0k0E20
//MENU: http://www.beepbox.co/#5s0kbl00e00t7a7g00j7i0r1w1111f0000d1111c0000h0000v0000o3210b4h4p216FzzMIzrbRlmkOJJtZtdw2Cf0q9aC1F3g6yGCGA82CeI6wc7vk6wd0qGGww95ch2YLjih00

public class FellowshipGame extends Game implements GraphicalCallback {

    static final int GAME_WIDTH = 120;
    static final int GAME_HEIGHT = 90;
    
    static final int TYPE_PLAYER = 0;
    static final int TYPE_MOUSE = 1;
    static final int TYPE_PROP = 2;
    static final int TYPE_DOOR = 3;
    static final int TYPE_TILE = 4;
    static final int TYPE_BG = 5;
    
    static final int TILE_GRASS = 0;
    static final int TILE_DIRT = 1;
    static final int TILE_GOLD = 2;
    static final int TILE_BLOCK = 3;
    static final int TILE_BRIDGE_LEFT = 4;
    static final int TILE_BRIDGE_RIGHT = 5;
    static final int TILE_BRIDGE = 6;
    static final int TILE_LAVA = 7;
    static final int TILE_SPRING_GREEN = 8;
    static final int TILE_SPRING_RED = 9;
    static final int TILE_SPRING_BLUE = 10;
    static final int TILE_SPIKES = 11;
    
    static final int TARGET_BLOCK = 0;
    static final int TARGET_BRIDGE_START = 1;
    static final int TARGET_BRIDGE_NODE = 2;
    static final int TARGET_SPRING_GREEN = 3;
    static final int TARGET_SPRING_RED = 4;
    static final int TARGET_SPRING_BLUE = 5;
    
    static final int LAYER_BG = 0;
    static final int LAYER_TILE = 1;
    static final int LAYER_PLAYER = 2;
    static final int LAYER_PROP = 3;
    static final int LAYER_MOUSE = 4;
    
    static final Color BG_COLOR = new Color(0x5dced5);
    
    int ID_PLAYER_LEFT;
    int ID_PLAYER_RIGHT;
    int ID_MOUSE_DESTROY;
    int ID_MOUSE_NEUTRAL;
    int ID_MOUSE_CREATE;
    int ID_DOOR_BOTTOM;
    int ID_DOOR_TOP;
    int ID_TILE_TARGET;
    int ID_TILE_GRASS;
    int ID_CHUNK_GRASS;
    int ID_TILE_DIRT;
    int ID_CHUNK_DIRT;
    int ID_TILE_GOLD;
    int ID_TILE_BLOCK;
    int ID_TILE_BRIDGE_LEFT;
    int ID_TILE_BRIDGE_RIGHT;
    int ID_TILE_BRIDGE;
    int ID_TILE_LAVA;
    int ID_TILE_SPRING_GREEN;
    int ID_TILE_SPRING_RED;
    int ID_TILE_SPRING_BLUE;
    int ID_TILE_SPIKES;
    int ID_BG_BLOCK;
    int ID_BG_BRIDGE;
    int ID_BG_SPRING;
    
    SpriteSystem system;
    World world;
    Camera camera;
    
    float startx;
    float starty;
    
    Sprite player;
    Sprite playerWallTracker;
    Sprite playerFloorTracker;
    byte playerDirection;
    float playerVelY;
    float playerVelX;
    
    TileProperties wallComp;
    TileProperties springCompGreen;
    TileProperties springCompRed;
    TileProperties springCompBlue;
    ObstacleProperties obstacleComp;
    
    Sprite mouseTracker;
    int mouseCooldown;
    
    Sprite[] nokill;
    
    Target[] targets;
    Target currentTarget;
    Target[] reverseTargets;
    
    static final byte NO_TARGET = 0;
    static final byte TARGET = 1;
    static final byte TARGET_REVERSE = 2;
    
    byte targetState;
    
    Sprite[] createdTiles;
    
    CollisionMap tileMap;
    
    Clip click;
    
    Clip menuMusic;
    Clip gameMusic;
    
    Clip build;
    Clip destroy;
    
    Clip explode;
    Clip spring;
    
    Clip start;
    Clip end;
    
    BufferedImage background;
    
    int level;
    
    int unlocked;

    class PauseCallback implements Callback {

        static final int DELAY = 30;
        static final int MULTIPLIER = 90 / DELAY;
        
        public static final boolean DIRECTION_UP = false;
        public static final boolean DIRECTION_DOWN = true;
        
        //True - screens move down, false - screens move up
        boolean direction;
        
        GraphicalCallback newCallback;
        GraphicalCallback oldCallback;
        int delay;
        public PauseCallback(GraphicalCallback newCallback, GraphicalCallback oldCallback) {
            this.newCallback = newCallback;
            this.oldCallback = oldCallback;
            this.delay = DELAY;
            
            direction = new Random().nextBoolean();
        }
        
        public PauseCallback(GraphicalCallback newCallback, GraphicalCallback oldCallback, boolean direction) {
            this.newCallback = newCallback;
            this.oldCallback = oldCallback;
            this.delay = DELAY;
            
            this.direction = direction;
        }
        
        @Override
        public void activate(long tick) {
        }

        @Override
        public void loop(long tick) {
            delay--;
            
            if(direction) {
                newCallback.render(renderer, ((DELAY - delay) * MULTIPLIER) - 90);
                oldCallback.render(renderer, (DELAY - delay) * MULTIPLIER);
            }
            else {
                newCallback.render(renderer, delay * MULTIPLIER);
                oldCallback.render(renderer,(delay * MULTIPLIER) - 90);
            }
            
            if(delay <= 0) {
                setCallback(newCallback);
            }
        }
        
    }
    
    class MenuCallback implements GraphicalCallback {

        static final int PLAY_X = 14;
        static final int PLAY_Y = 64;
        static final int HELP_X = 75;
        static final int HELP_Y = 64;
        
        BufferedImage menu;
        
        BufferedImage playButton;
        BufferedImage helpButton;
        
        Rectangle playButtonRect;
        Rectangle helpButtonRect;
        
        public MenuCallback() {
            menu = Resources.loadImageResource("resrc/menu.png");
            playButton = Resources.loadImageResource("resrc/menu/play.png");
            helpButton = Resources.loadImageResource("resrc/menu/help.png");
            playButtonRect = new Rectangle(PLAY_X, PLAY_Y, 31, 14);
            helpButtonRect = new Rectangle(HELP_X, HELP_Y, 29, 14);
        }
        
        @Override
        public void loop(long tick) {
            playMusic(menuMusic);
            
            renderer.begin();
            renderer.drawImage(menu, 0, 0);
            
            Vector2 mouse = new Vector2(getMouseX(), getMouseY());
            if(playButtonRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(playButton, PLAY_X, PLAY_Y);
                if(isLMBDown()) {
                    playSound(click);
                    setCallback(new PauseCallback(levelSelectCallback, this));
                }
            }
            if(helpButtonRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(helpButton, HELP_X, HELP_Y);
                if(isLMBDown()) {
                    playSound(click);
                    setCallback(new PauseCallback(helpCallback, this));
                }
            }
        }

        @Override
        public void activate(long tick) {
            renderer.begin();
            renderer.drawImage(menu, 0, 0);
        }

        @Override
        public void render(Renderer r, int y) {
            r.drawImage(menu, 0, y);
        }
        
    }
    
    class HelpCallback implements GraphicalCallback {

        static final int PREVIOUS_X = 3;
        static final int MENU_X = 59;
        static final int NEXT_X = 90;
        static final int Y = 76;
        
        BufferedImage[] screens;
        
        BufferedImage previousButton;
        BufferedImage menuButton;
        BufferedImage nextButton;
        
        Rectangle previousRect;
        Rectangle menuRect;
        Rectangle nextRect;
        
        int currentIndex;
        int transitionIndex;
        int transitionVel;
        int transition;
        
        //False for display, true for transitioning
        boolean mode;
        
        public HelpCallback() {
            screens = new BufferedImage[6];
            for(int i = 0; i < screens.length; i++) {
                screens[i] = Resources.loadImageResource("resrc/help/screen" + Integer.toString(i + 1) + ".png");
            }
            previousButton = Resources.loadImageResource("resrc/help/previous.png");
            menuButton = Resources.loadImageResource("resrc/help/menu.png");
            nextButton = Resources.loadImageResource("resrc/help/next.png");
            
            previousRect = new Rectangle(PREVIOUS_X, Y, 51, 11);
            menuRect = new Rectangle(MENU_X, Y, 27, 11);
            nextRect = new Rectangle(NEXT_X, Y, 27, 11);
            
            currentIndex = 0;
            transitionIndex = 1;
            transitionVel = -1;
            transition = 120;
        }
        
        void frobnicate() {
            if(currentIndex < 0) {
                currentIndex = screens.length - 1;
            }
            if(currentIndex >= screens.length) {
                currentIndex = 0;
            }
        }
        
        //TODO: IMPLEMENT HELP MENU
        @Override
        public void loop(long tick) {
            playMusic(menuMusic);
            
            renderer.begin();
            if(mode) {
                if(transition == 0) {
                    mode = false;
                }
                renderer.drawImage(screens[transitionIndex], transition + (transitionVel * 120), 0);
                renderer.drawImage(screens[currentIndex], transition, 0);
                transition += transitionVel * 3;
            }
            else {
                renderer.drawImage(screens[currentIndex], 0, 0);
                Vector2 mouse = new Vector2(getMouseX(), getMouseY());
                if(previousRect.contains(mouse.x, mouse.y)) {
                    renderer.drawImage(previousButton, PREVIOUS_X, Y);
                    if(isLMBDown()) {
                        transitionIndex = currentIndex;
                        currentIndex--;
                        frobnicate();
                        transition = -120;
                        transitionVel = 1;
                        
                        playSound(click);
                        mode = true;
                    }
                }
                else if(nextRect.contains(mouse.x, mouse.y)) {
                    renderer.drawImage(nextButton, NEXT_X, Y);
                    if(isLMBDown()) {
                        transitionIndex = currentIndex;
                        currentIndex++;
                        frobnicate();
                        transition = 120;
                        transitionVel = -1;
                        
                        playSound(click);
                        mode = true;
                    }
                }
                if(menuRect.contains(mouse.x, mouse.y)) {
                    renderer.drawImage(menuButton, MENU_X, Y);
                    if(isLMBDown()) {
                        playSound(click);
                        setCallback(new PauseCallback(menuCallback, this));
                    }
                }
            }
        }

        @Override
        public void activate(long tick) {
        }

        @Override
        public void render(Renderer r, int y) {
            renderer.drawImage(screens[currentIndex], 0, y);
        }
        
    }
    
    class LevelSelectCallback implements GraphicalCallback {

        static final int BACK_X = 1;
        static final int BACK_Y = 73;
        
        static final int BEGIN_X = 27;
        static final int BEGIN_Y = 21;
        
        static final int BUTTON_WIDTH = 15;
        static final int BUTTON_HEIGHT = 11;
        
        static final int FINAL_BUTTON_X = 27;
        static final int FINAL_BUTTON_Y = 73;
        
        static final int FINAL_LEVEL = 17;
        
        Rectangle backButtonBounds;
        Rectangle finalRect;
        
        BufferedImage screen;
        BufferedImage backButton;
        BufferedImage selectBox;
        BufferedImage selectBoxRed;
        BufferedImage selectBoxFinal;
        BufferedImage selectBoxRedFinal;
        
        Clip select;
        
        int last = 0;
        
        public LevelSelectCallback() {
            screen = Resources.loadImageResource("resrc/lvlsel/screen.png");
            backButton = Resources.loadImageResource("resrc/lvlsel/back.png");
            selectBox = Resources.loadImageResource("resrc/lvlsel/select.png");
            selectBoxRed = Resources.loadImageResource("resrc/lvlsel/selectred.png");
            selectBoxFinal = Resources.loadImageResource("resrc/lvlsel/selectfinal.png");
            selectBoxRedFinal = Resources.loadImageResource("resrc/lvlsel/selectredfinal.png");
            
            select = Resources.loadAudioResource("resrc/fx/select.wav");
            
            backButtonBounds = new Rectangle(BACK_X, BACK_Y, 23, 15);
            finalRect = new Rectangle(FINAL_BUTTON_X, FINAL_BUTTON_Y, 66, 11);
        }
        
        @Override
        public void loop(long tick) {
            playMusic(menuMusic);
            
            renderer.begin();
            renderer.drawImage(screen, 0, 0);
            
            Vector2 mouse = new Vector2(getMouseX(), getMouseY());
            if(backButtonBounds.contains(mouse.x, mouse.y)) {
                renderer.drawImage(backButton, BACK_X, BACK_Y);
                if(isLMBDown()) {
                    playSound(click);
                    setCallback(new PauseCallback(menuCallback, this));
                }
            }
            else {
                int selected;
                Rectangle checkButton = new Rectangle(BEGIN_X, BEGIN_Y, 15, 11);
                for(int y = 0; y < 4; y++) {
                    for(int x = 0; x < 4; x++) {
                        if(checkButton.contains(mouse.x, mouse.y)) { 
                            selected = (y * 4) + x + 1;
                            if(selected != last) {
                                playSound(select);
                                last = selected;
                            }
                            if(selected <= unlocked) {
                                renderer.drawImage(selectBox, checkButton.x, checkButton.y);
                                if(isLMBDown()) {
                                    playSound(click);
                                    stopMusic(menuMusic);
                                    startLevel(selected);
                                }
                            }
                            else {
                                renderer.drawImage(selectBoxRed, checkButton.x, checkButton.y);
                            }
                        }
                        checkButton.shift(BUTTON_WIDTH + 2, 0);
                    }
                    checkButton.x = BEGIN_X;
                    checkButton.shift(0, BUTTON_HEIGHT + 2);
                }
                
            }
            if(finalRect.contains(mouse.x, mouse.y)) {
                if(FINAL_LEVEL != last) {
                    playSound(select);
                    last = FINAL_LEVEL;
                }
                if(FINAL_LEVEL == unlocked) {
                    renderer.drawImage(selectBoxFinal, FINAL_BUTTON_X, FINAL_BUTTON_Y);
                    if(isLMBDown()) {
                        playSound(click);
                        stopMusic(menuMusic);
                        startLevel(FINAL_LEVEL);
                    }
                }
                else {
                    renderer.drawImage(selectBoxRedFinal, FINAL_BUTTON_X, FINAL_BUTTON_Y);
                }
            }
        }

        @Override
        public void activate(long tick) {
            renderer.begin();
            renderer.drawImage(screen, 0, 0);
        }

        @Override
        public void render(Renderer r, int y) {
            r.drawImage(screen, 0, y);
        }
        
    }
    
    class LevelStartCallback implements GraphicalCallback {

        @Override
        public void render(Renderer r, int y) {
            r.drawImage(background, 0, y);
            camera.track(player);
            camera.drawSprite(player, renderer);
        }

        @Override
        public void activate(long tick) {
            player.moveTo(startx, starty - 128);
            camera.track(player);
            playSound(start);
        }

        @Override
        public void loop(long tick) {
            if(player.getY() < starty) {
                player.moveY(8);
                camera.track(player);
            }
            else {
                setCallback(FellowshipGame.this);
            }
            
            renderer.begin();
            camera.render(world, renderer);
        }
        
    }
    
    class NextLevelCallback implements GraphicalCallback {

        static final int REPLAY_X = 40;
        static final int REPLAY_Y = 32;
        static final int MENU_X = 43;
        static final int MENU_Y = 69;
        static final int SELECT_X = 2;
        static final int SELECT_Y = 69;
        static final int NEXT_X = 91;
        static final int NEXT_Y = 69;
        
        BufferedImage screen;
        BufferedImage screenFinal;
        
        BufferedImage replayButton;
        BufferedImage menuButton;
        BufferedImage selectButton;
        BufferedImage nextButton;
        
        Rectangle replayRect;
        Rectangle menuRect;
        Rectangle selectRect;
        Rectangle nextRect;
        
        public NextLevelCallback() {
            screen = Resources.loadImageResource("resrc/nextlvl/screen.png");
            screenFinal = Resources.loadImageResource("resrc/nextlvl/screenfinal.png");
            replayButton = Resources.loadImageResource("resrc/nextlvl/replay.png");
            menuButton = Resources.loadImageResource("resrc/nextlvl/menu.png");
            selectButton = Resources.loadImageResource("resrc/nextlvl/select.png");
            nextButton = Resources.loadImageResource("resrc/nextlvl/next.png");
            
            replayRect = new Rectangle(REPLAY_X, REPLAY_Y, 39, 24);
            menuRect = new Rectangle(MENU_X, MENU_Y, 27, 19);
            selectRect = new Rectangle(SELECT_X, SELECT_Y, 39, 19);
            nextRect = new Rectangle(NEXT_X, NEXT_Y, 27, 19);
        }

        @Override
        public void activate(long tick) {
        }

        @Override
        public void loop(long tick) {
            playMusic(gameMusic);
            
            renderer.begin();
            renderer.drawImage((level == 17) ? screenFinal : screen, 0, 0);
            
            Vector2 mouse = new Vector2(getMouseX(), getMouseY());
            if(replayRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(replayButton, REPLAY_X, REPLAY_Y);
                if(isLMBDown()) {
                    playSound(click);
                    setCallback(new PauseCallback(levelStartCallback, this));
                }
            }
            else if(menuRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(menuButton, MENU_X, MENU_Y);
                if(isLMBDown()) {
                    playSound(click);
                    stopMusic(gameMusic);
                    setCallback(new PauseCallback(menuCallback, this));
                }
            }
            else if(selectRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(selectButton, SELECT_X, SELECT_Y);
                if(isLMBDown()) {
                    playSound(click);
                    stopMusic(gameMusic);
                    setCallback(new PauseCallback(levelSelectCallback, this));
                }
            }
            else if(nextRect.contains(mouse.x, mouse.y) && level < 17) {
                renderer.drawImage(nextButton, NEXT_X, NEXT_Y);
                if(isLMBDown()) {
                    playSound(click);
                    startLevel(level + 1);
                }
            }
        }
        
        @Override
        public void render(Renderer r, int y) {
            r.drawImage((level == 17) ? screenFinal : screen, 0, y);
        }
        
    }
    
    class PauseMenuCallback implements GraphicalCallback {

        static final int RESUME_X = 40;
        static final int RESUME_Y = 32;
        static final int MENU_X = 6;
        static final int MENU_Y = 39;
        static final int SELECT_X = 40;
        static final int SELECT_Y = 45;
        
        BufferedImage screen;
        
        BufferedImage resumeButton;
        BufferedImage menuButton;
        BufferedImage selectButton;
        
        Rectangle resumeRect;
        Rectangle menuRect;
        Rectangle selectRect;
        
        public PauseMenuCallback() {
            screen = Resources.loadImageResource("resrc/pause/screen.png");
            resumeButton = Resources.loadImageResource("resrc/pause/resume.png");
            menuButton = Resources.loadImageResource("resrc/pause/menu.png");
            selectButton = Resources.loadImageResource("resrc/pause/select.png");
            
            resumeRect = new Rectangle(RESUME_X, RESUME_Y, 39, 11);
            menuRect = new Rectangle(MENU_X, MENU_Y, 27, 19);
            selectRect = new Rectangle(SELECT_X, SELECT_Y, 39, 19);
        }

        @Override
        public void activate(long tick) {
        }

        @Override
        public void loop(long tick) {
            playMusic(gameMusic);
            
            renderer.begin();
            renderer.drawImage(screen, 0, 0);
            
            Vector2 mouse = new Vector2(getMouseX(), getMouseY());
            if(resumeRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(resumeButton, RESUME_X, RESUME_Y);
                if(isLMBDown()) {
                    playSound(click);
                    setCallback(new PauseCallback(FellowshipGame.this, this, PauseCallback.DIRECTION_UP));
                }
            }
            else if(menuRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(menuButton, MENU_X, MENU_Y);
                if(isLMBDown()) {
                    playSound(click);
                    stopMusic(gameMusic);
                    setCallback(new PauseCallback(menuCallback, this));
                }
            }
            else if(selectRect.contains(mouse.x, mouse.y)) {
                renderer.drawImage(selectButton, SELECT_X, SELECT_Y);
                if(isLMBDown()) {
                    playSound(click);
                    stopMusic(gameMusic);
                    setCallback(new PauseCallback(levelSelectCallback, this));
                }
            }
        }
        
        @Override
        public void render(Renderer r, int y) {
            r.drawImage(screen, 0, y);
        }
        
    }
    
    MenuCallback menuCallback;
    HelpCallback helpCallback;
    LevelSelectCallback levelSelectCallback;
    LevelStartCallback levelStartCallback;
    NextLevelCallback levelOverCallback;
    PauseMenuCallback pauseMenuCallback;
    
    KeyControl resetControl;
    int resetCooldown;
    
    KeyControl pauseControl;
    
    public FellowshipGame() {
        super("Fellowship", GAME_WIDTH, GAME_HEIGHT, 5);
    }
    
    @Override
    protected void setup() {
        this.setIconImage(Resources.loadImageResource("resrc/icon.png"));
        
        system = new SpriteSystem(1);
        
        tileMap = CollisionMap.createMapFromTxt("resrc/tile/map.txt");
        CollisionMap nullMap = CollisionMap.createMapFromTxt("resrc/nullmap.txt");
        
        system.addData(new AnimGraphic("resrc/player/playerleft.png",16,19), "resrc/player/playermapleft.png", "Player.Left");
        system.addData(new AnimGraphic("resrc/player/playerright.png",16,19), "resrc/player/playermapright.png", "Player.Right");
        
        system.addData("resrc/mouse/destroy.png",CollisionMap.createMapFromTxt("resrc/mouse/map.txt"),"Mouse.Destroy");
        system.addData("resrc/mouse/neutral.png",CollisionMap.createMapFromTxt("resrc/mouse/map.txt"),"Mouse.Neutral");
        system.addData("resrc/mouse/create.png",CollisionMap.createMapFromTxt("resrc/mouse/map.txt"),"Mouse.Create");
        
        system.addData("resrc/tile/doorbottom.png",nullMap.createInstance(),"Door.Bottom");
        system.addData("resrc/tile/doortop.png",nullMap.createInstance(),"Door.Top");    
        
        system.addData("resrc/player/playerwall.png","resrc/player/playerwall.png","Player.Wall");
        system.addData("resrc/player/playerfloor.png","resrc/player/playerfloor.png","Player.Floor");
        
        system.addData("resrc/tilemap.png", nullMap.createInstance(),"Target");
        
        system.addData("resrc/tile/grass.png", nullMap.createInstance(),"Tile.Grass");
        system.addData("resrc/tile/grasschunk.png", nullMap.createInstance(),"Tile.Chunk.Grass");
        system.addData("resrc/tile/dirt.png", nullMap.createInstance(),"Tile.Dirt");
        system.addData("resrc/tile/dirtchunk.png", nullMap.createInstance(),"Tile.Chunk.Dirt");
        system.addData("resrc/tile/gold.png", nullMap.createInstance(),"Tile.Gold");
        system.addData("resrc/tile/block.png", nullMap.createInstance(),"Tile.Block");
        system.addData("resrc/tile/bridgeleft.png", nullMap.createInstance(),"Tile.Bridge.Left");
        system.addData("resrc/tile/bridgeright.png", nullMap.createInstance(),"Tile.Bridge.Right");
        system.addData("resrc/tile/bridge.png", nullMap.createInstance(),"Tile.Bridge");
        system.addData("resrc/tile/lava.png", nullMap.createInstance(),"Tile.Lava");
        system.addData("resrc/tile/springgreen.png", nullMap.createInstance(),"Tile.Spring.Green");
        system.addData("resrc/tile/springred.png", nullMap.createInstance(),"Tile.Spring.Red");
        system.addData("resrc/tile/springblue.png", nullMap.createInstance(),"Tile.Spring.Blue");
        system.addData("resrc/tile/spikes.png", nullMap.createInstance(),"Tile.Spikes");
        
        system.addData("resrc/bg/block.png", nullMap.createInstance(),"BG.Block");
        system.addData("resrc/bg/bridge.png", nullMap.createInstance(),"BG.Bridge");
        system.addData("resrc/bg/spring.png", nullMap.createInstance(),"BG.Spring");
        system.packData();
        
        click = Resources.loadAudioResource("resrc/fx/click.wav");
        
        menuMusic = Resources.loadAudioResource("resrc/tunes.wav");
        
        gameMusic = Resources.loadAudioResource("resrc/gametunes.wav");
        
        build = Resources.loadAudioResource("resrc/fx/build.wav");
        destroy = Resources.loadAudioResource("resrc/fx/destroy.wav");
        
        start = Resources.loadAudioResource("resrc/fx/start.wav");
        end = Resources.loadAudioResource("resrc/fx/end.wav");
        
        explode = Resources.loadAudioResource("resrc/fx/explode.wav");
        spring = Resources.loadAudioResource("resrc/fx/spring.wav");
        
        background = Resources.loadImageResource("resrc/background.png");
        
        ID_PLAYER_LEFT = system.getIdOfName("Player.Left");
        ID_PLAYER_RIGHT = system.getIdOfName("Player.Right");
        
        ID_MOUSE_DESTROY = system.getIdOfName("Mouse.Destroy");
        ID_MOUSE_NEUTRAL = system.getIdOfName("Mouse.Neutral");
        ID_MOUSE_CREATE = system.getIdOfName("Mouse.Create");
        
        ID_DOOR_BOTTOM = system.getIdOfName("Door.Bottom");
        ID_DOOR_TOP = system.getIdOfName("Door.Top");
        
        ID_TILE_TARGET = system.getIdOfName("Target");
        ID_TILE_GRASS = system.getIdOfName("Tile.Grass");
        ID_CHUNK_GRASS = system.getIdOfName("Tile.Chunk.Grass");
        ID_TILE_DIRT = system.getIdOfName("Tile.Dirt");
        ID_CHUNK_DIRT = system.getIdOfName("Tile.Chunk.Dirt");
        ID_TILE_GOLD = system.getIdOfName("Tile.Gold");
        ID_TILE_BLOCK = system.getIdOfName("Tile.Block");
        ID_TILE_BRIDGE_LEFT = system.getIdOfName("Tile.Bridge.Left");
        ID_TILE_BRIDGE_RIGHT = system.getIdOfName("Tile.Bridge.Right");
        ID_TILE_BRIDGE = system.getIdOfName("Tile.Bridge");
        ID_TILE_LAVA = system.getIdOfName("Tile.Lava");
        ID_TILE_SPRING_GREEN = system.getIdOfName("Tile.Spring.Green");
        ID_TILE_SPRING_RED = system.getIdOfName("Tile.Spring.Red");
        ID_TILE_SPRING_BLUE = system.getIdOfName("Tile.Spring.Blue");
        ID_TILE_SPIKES = system.getIdOfName("Tile.Spikes");
        
        ID_BG_BLOCK = system.getIdOfName("BG.Block");
        ID_BG_BRIDGE = system.getIdOfName("BG.Bridge");
        ID_BG_SPRING = system.getIdOfName("BG.Spring");
        
        player = new Sprite();
        playerWallTracker = new Sprite();
        playerFloorTracker = new Sprite();
        playerVelX = playerVelY = 0;
        
        wallComp = new TileProperties(true);
        springCompGreen = new TileProperties(TILE_SPRING_GREEN);
        springCompRed = new TileProperties(TILE_SPRING_RED);
        springCompBlue = new TileProperties(TILE_SPRING_BLUE);
        obstacleComp = new ObstacleProperties();
        
        mouseTracker = new Sprite();
        mouseCooldown = 0;
        
        world = new World(system);
        world.addSprite(player);
        
        player.setAppearance(ID_PLAYER_LEFT);
        player.setLayer(LAYER_PLAYER);
        player.animated = true;
        player.type = TYPE_PLAYER;
        playerDirection = 1;
        
        mouseTracker.setSystem(system);
        mouseTracker.setAppearance(ID_MOUSE_NEUTRAL);
        mouseTracker.setLayer(LAYER_MOUSE);
        mouseTracker.type = TYPE_MOUSE;
        
        createdTiles = new Sprite[0];
        
        playerWallTracker.setSystem(system);
        playerWallTracker.setAppearance(system.getIdOfName("Player.Wall"));
        
        playerFloorTracker.setSystem(system);
        playerFloorTracker.setAppearance(system.getIdOfName("Player.Floor"));
        
        nokill = new Sprite[] { player, playerWallTracker, playerFloorTracker, mouseTracker };
        
        level = 1;
        unlocked = 1;
        
        File testSave = new File("fellowship-save.txt");
        if(testSave.exists()) {
            FileReader inputFile;
            try {
                inputFile = new FileReader("fellowship-save.txt");
                BufferedReader input = new BufferedReader(inputFile);
                
                unlocked = Integer.parseInt(input.readLine());
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
            
        }
        
        camera = system.getCamera(GAME_WIDTH, GAME_HEIGHT);
        renderer.setBackgroundColor(BG_COLOR);
        
        resetControl = new KeyControl(KeyEvent.VK_R);
        this.watchControl(resetControl);
        
        pauseControl = new KeyControl(KeyEvent.VK_P);
        this.watchControl(pauseControl);
        
        menuCallback = new MenuCallback();
        helpCallback = new HelpCallback();
        levelSelectCallback = new LevelSelectCallback();
        levelStartCallback = new LevelStartCallback();
        levelOverCallback = new NextLevelCallback();
        pauseMenuCallback = new PauseMenuCallback();
        
        setCallback(menuCallback);
        
        //TODO: KILL THIS LINE OF CODE:
        //playSounds = false;
    }
    
    Sprite createTile(int appearance, int type, float x, float y, boolean dangerous) {
        Sprite newTile = new Sprite();
                    
        world.addSprite(newTile);

        newTile.setAppearance(appearance);
        newTile.setCollisionMap(tileMap.createInstance());
        newTile.moveTo(x, y);
        newTile.type = TYPE_TILE;
        newTile.setLayer(LAYER_TILE);
        newTile.setProperties(new TileProperties(type, false, dangerous));
        
        return newTile;
    }
    
    Sprite createTile(int appearance, int type, float x, float y, boolean dangerous, boolean solidwall) {
        Sprite newTile = new Sprite();
                    
        world.addSprite(newTile);

        newTile.setAppearance(appearance);
        newTile.setCollisionMap(tileMap.createInstance());
        newTile.moveTo(x, y);
        newTile.type = TYPE_TILE;
        newTile.setLayer(LAYER_TILE);
        newTile.setProperties(new TileProperties(type, solidwall, dangerous));
        
        return newTile;
    }
    
    static final int COLOR_IS_TILE = 0xff000000;
    static final int COLOR_IS_TILE_NON_REV = 0xffff0000;
    
    void loadTile(int appearance, int tiletype, int imgx, int imgy, int color, boolean dangerous) {
             
        if(color == COLOR_IS_TILE || color == COLOR_IS_TILE_NON_REV) {
            Sprite newTile = new Sprite();
                    
            world.addSprite(newTile);

            newTile.setAppearance(appearance);   
            newTile.type = TYPE_TILE;
            newTile.setLayer(LAYER_TILE);
            newTile.setCollisionMap(tileMap.createInstance());
            newTile.setProperties(new TileProperties(tiletype, color == COLOR_IS_TILE_NON_REV, dangerous));
            newTile.moveTo(4 + imgx * 8, 4 + imgy * 8);
        }
        else {
            
            Entity newTile = new Entity();
                    
            world.addSprite(newTile);

            newTile.graphic = appearance;  
            newTile.type = TYPE_PROP;
            newTile.setLayer(LAYER_PROP);
            newTile.moveTo(4 + imgx * 8, 4 + imgy * 8);
        }
    }
    
    void loadBG(int appearance, int imgx, int imgy) {
        Entity newBG = new Entity();
                    
        world.addSprite(newBG);

        newBG.graphic = appearance;
        newBG.moveTo(4 + imgx * 8, 4 + imgy * 8);
        
        newBG.type = TYPE_BG;
        newBG.setLayer(LAYER_BG);
    }
    
    static final int COLOR_GRASS = 0xff47f37f;
    static final int COLOR_GRASS_CHUNK = 0xff32cf66;
    static final int COLOR_DIRT = 0xffb19051;
    static final int COLOR_DIRT_CHUNK = 0xff574a30;
    static final int COLOR_GOLD = 0xffd5bb54;
    static final int COLOR_BRIDGE_LEFT = 0xff70451b;
    static final int COLOR_BRIDGE_RIGHT = 0xff583412;
    static final int COLOR_LAVA = 0xffe04f00;
    static final int COLOR_SPIKES = 0xff525252;
    static final int COLOR_SPRING_GREEN = 0xff00f361;
    static final int COLOR_SPRING_RED = 0xffff7640;
    static final int COLOR_SPRING_BLUE = 0xff8272f9;
    static final int COLOR_START = 0xff0000ff;
    static final int COLOR_END = 0xffff0000;
    
    public void startLevel(int number) {
        level = number;
        loadWorld(Resources.loadImageResource("resrc/level/level" + Integer.toString(level) + ".png"));
        setCallback(new PauseCallback(levelStartCallback, (GraphicalCallback)getCallback()));
    }
    
    void loadWorld(BufferedImage data) {
        world.killAll(nokill);
        startx = 0;
        starty = 0;
        int sectionHeight = data.getHeight() / 2;
        int targetCount = 0;
        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < sectionHeight; y++) {
                if(data.getRGB(x, y) == COLOR_GRASS) {
                    loadTile(ID_TILE_GRASS, TILE_GRASS, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_DIRT) {
                    loadTile(ID_TILE_DIRT, TILE_DIRT, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_GOLD) {
                    loadTile(ID_TILE_GOLD, TILE_GOLD, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_BRIDGE_LEFT) {
                    loadTile(ID_TILE_BRIDGE_LEFT, TILE_BRIDGE_LEFT, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_BRIDGE_RIGHT) {
                    loadTile(ID_TILE_BRIDGE_RIGHT, TILE_BRIDGE_LEFT, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_LAVA) {
                    loadTile(ID_TILE_LAVA, TILE_LAVA, x, y, data.getRGB(x, y + sectionHeight), true);
                }
                if(data.getRGB(x, y) == COLOR_SPIKES) {
                    loadTile(ID_TILE_SPIKES, TILE_SPIKES, x, y, data.getRGB(x, y + sectionHeight), true);
                }
                if(data.getRGB(x, y) == COLOR_SPRING_GREEN) {
                    loadTile(ID_TILE_SPRING_GREEN, TILE_SPRING_GREEN, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_SPRING_RED) {
                    loadTile(ID_TILE_SPRING_RED, TILE_SPRING_RED, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_SPRING_BLUE) {
                    loadTile(ID_TILE_SPRING_BLUE, TILE_SPRING_BLUE, x, y, data.getRGB(x, y + sectionHeight), false);
                }
                if(data.getRGB(x, y) == COLOR_GRASS_CHUNK) {
                    Sprite newChunk = new Sprite();
                    
                    world.addSprite(newChunk);

                    newChunk.setAppearance(ID_CHUNK_GRASS);
                    newChunk.moveTo(32 + x * 8, 32 + y * 8);
                    newChunk.type = TYPE_PROP;
                    newChunk.setLayer(LAYER_PROP);
                }
                if(data.getRGB(x, y) == COLOR_DIRT_CHUNK) {
                    Sprite newChunk = new Sprite();
                    
                    world.addSprite(newChunk);

                    newChunk.setAppearance(ID_CHUNK_DIRT);
                    newChunk.moveTo(32 + x * 8, 32 + y * 8);
                    newChunk.type = TYPE_PROP;
                    newChunk.setLayer(LAYER_PROP);
                    
                }
                if(data.getRGB(x, y) == COLOR_START) {
                    startx = x * 8 + 8;
                    starty = y * 8 - 1.5f;
                }
                if(data.getRGB(x, y) == COLOR_END) {
                    Sprite doorTop = new Sprite();
                    Sprite doorBottom = new Sprite();
                    
                    world.addSprite(doorTop);
                    world.addSprite(doorBottom);
                    
                    doorBottom.setAppearance(ID_DOOR_BOTTOM);
                    doorBottom.setCollisionMap(tileMap.createInstance());
                    doorBottom.moveTo(4 + x * 8, 4 + y * 8);
                    doorBottom.type = TYPE_DOOR;
                    doorBottom.setLayer(LAYER_TILE);
                    
                    doorTop.setAppearance(ID_DOOR_TOP);
                    doorTop.setCollisionMap(tileMap.createInstance());
                    doorTop.moveTo(doorBottom.getX(), doorBottom.getY() - 8);
                    doorTop.type = TYPE_DOOR;
                    doorTop.setLayer(LAYER_TILE);
                }
                if(new Color(data.getRGB(x, y + sectionHeight)).getGreen() == 255) {
                    
                    switch(new Color(data.getRGB(x, y + sectionHeight)).getRed()) {
                        case TARGET_BLOCK:
                            loadBG(ID_BG_BLOCK, x, y);
                            targetCount++;
                            break;
                        case TARGET_BRIDGE_START:
                            int lx = x;
                            Color c = new Color(data.getRGB(x, y + sectionHeight));
                            while(!(c.getGreen() == 255 && c.getRed() == TARGET_BRIDGE_NODE)) {
                                loadBG(ID_BG_BRIDGE, lx, y);
                                c = new Color(data.getRGB(lx, y + sectionHeight));
                                lx++;
                            }
                            targetCount += (lx - x);
                            break;
                        case TARGET_SPRING_GREEN:
                        case TARGET_SPRING_RED:
                        case TARGET_SPRING_BLUE:
                            loadBG(ID_BG_SPRING, x, y);
                            targetCount++;
                            break;
                    }
                }
            }
        }
        
        targets = new Target[targetCount];
        int write = 0;
        for(int x = 0; x < data.getWidth(); x++) {
            for(int y = 0; y < sectionHeight; y++) {
                Color c = new Color(data.getRGB(x, y + sectionHeight));
                if(c.getGreen() == 255) {
                    if(c.getRed() == TARGET_BRIDGE_START) {
                        int lx = x;
                        Color lc = new Color(data.getRGB(lx, y + sectionHeight));
                        while(!(lc.getGreen() == 255 && lc.getRed() == TARGET_BRIDGE_NODE)) {
                            lc = new Color(data.getRGB(lx, y + sectionHeight));
                            lx++;
                        }
                        BridgeParent newTarget = new BridgeParent(x, lx, y, system, this.ID_TILE_TARGET);
                        newTarget.doMaps(tileMap);
                        
                        for(BridgeTarget target : newTarget.targets) {
                            targets[write] = target;
                            write++;
                        }
                    }
                    else if(c.getRed() == TARGET_BRIDGE_NODE) {
                        
                    }
                    else {
                        Target newTarget = new Target(c.getRed());

                        newTarget.setSystem(system);

                        newTarget.setAppearance(ID_TILE_TARGET);
                        newTarget.setCollisionMap(tileMap.createInstance());
                        newTarget.moveTo(4 + x * 8, 4 + y * 8);

                        targets[write] = newTarget;
                        write++;
                    }
                }
            }
        }
        
        player.moveTo(startx, starty);
        player.setAppearance(ID_PLAYER_LEFT);
        playerDirection = 1;
    }
    
    void validateMouse() {
        float mouseX = mouseTracker.getX();
        float mouseY = mouseTracker.getY();
        mouseTracker.moveTo(mouseX - camera.x, mouseY - camera.y);
        
        targetState = NO_TARGET;
        
        if(!mouseTracker.collidingWith(player)) {
            for(Target t : targets) {
                if(reverseTargets == null) {
                    if(mouseTracker.collidingWith(t)) {
                        currentTarget = t;
                        targetState = TARGET;
                        break;
                    }
                }
                else {
                    if(mouseTracker.collidingWith(t)) {
                        for(Target c : reverseTargets){
                            if(c == t) {
                                targetState = TARGET_REVERSE;
                            }
                        }
                        if(targetState != TARGET_REVERSE) {
                            currentTarget = t;
                            targetState = TARGET;
                        }
                        break;
                    }
                }
            }
        }
        if(targetState == TARGET) {
            mouseTracker.setAppearance(ID_MOUSE_CREATE);
        }
        else if(targetState == TARGET_REVERSE) {
            mouseTracker.setAppearance(ID_MOUSE_DESTROY);
        }
        else {
            mouseTracker.setAppearance(ID_MOUSE_NEUTRAL);
        }
        
        mouseTracker.moveTo(mouseX, mouseY);
    }
    
    void applyTarget() {
        resetTiles();
        reverseTargets = null;
        if(currentTarget.id == TARGET_BLOCK) {
            Sprite newTile = createTile(ID_TILE_BLOCK, TILE_BLOCK, currentTarget.getX(), currentTarget.getY(), false, false);
            createdTiles = new Sprite[] { newTile };
            reverseTargets = new Target[] { currentTarget };
        }
        /*if(currentTarget.id == TARGET_BRIDGE_START) {
            Target bridgeEnd = null;
            float distX = 0;
            for(Target t : targets) {
                if(t.getX() > currentTarget.getX()) {
                    if(distX == 0) {
                        distX = t.getX() - currentTarget.getX();
                        bridgeEnd = t;
                    }
                    else if(t.getY() == currentTarget.getY() && t.getX() - currentTarget.getX() < distX) {
                        distX = t.getX() - currentTarget.getX();
                        bridgeEnd = t;
                    }
                }
            }

            createdTiles = new Sprite[(int)((bridgeEnd.getX() - currentTarget.getX()) / 8) + 1];
            int write = 0;

            for(float x = currentTarget.getX(); x <= bridgeEnd.getX(); x += 8) {
                createdTiles[write++] = createTile(ID_TILE_BRIDGE, TILE_BRIDGE, x, currentTarget.getY(), false, true);
            }
        }
        if(currentTarget.id == TARGET_BRIDGE_NODE) {
            Target bridgeStart = null;
            float distX = 0;
            for(Target t : targets) {
                if(t.getX() < currentTarget.getX()) {
                    if(distX == 0) {
                        distX = currentTarget.getX() - t.getX();
                        bridgeStart = t;
                    }
                    else if(t.getY() == currentTarget.getY() && currentTarget.getX() - t.getX() < distX) {
                        distX = currentTarget.getX() - t.getX();
                        bridgeStart = t;
                    }
                }
            }

            createdTiles = new Sprite[(int)((currentTarget.getX() - bridgeStart.getX()) / 8) + 1];
            int write = 0;

            for(float x = bridgeStart.getX(); x <= currentTarget.getX(); x += 8) {
                createdTiles[write++] = createTile(ID_TILE_BRIDGE, TILE_BRIDGE, x, currentTarget.getY(), false, true);
            }
        }*/
        if(currentTarget.id == TARGET_BRIDGE_START) {
            BridgeParent bridge = (BridgeParent)currentTarget;
            
            createdTiles = new Sprite[bridge.targets.length];
            
            BridgeTarget[] bridgeTargets = bridge.targets;
            
            for(int i = 0; i < bridgeTargets.length; i++) {
                createdTiles[i] = createTile(ID_TILE_BRIDGE, TILE_BRIDGE, bridgeTargets[i].getX(), bridgeTargets[i].getY(), false, true);
            }
            
            reverseTargets = bridgeTargets;
        }
        if(currentTarget.id == TARGET_BRIDGE_NODE) {
            BridgeParent bridge = ((BridgeTarget)currentTarget).parent;
            
            createdTiles = new Sprite[bridge.targets.length];
            
            BridgeTarget[] bridgeTargets = bridge.targets;
            
            for(int i = 0; i < bridgeTargets.length; i++) {
                createdTiles[i] = createTile(ID_TILE_BRIDGE, TILE_BRIDGE, bridgeTargets[i].getX(), bridgeTargets[i].getY(), false, true);
            }
            
            reverseTargets = bridgeTargets;
        }
        if(currentTarget.id == TARGET_SPRING_GREEN) {
            Sprite newTile = createTile(ID_TILE_SPRING_GREEN, TILE_SPRING_GREEN, currentTarget.getX(), currentTarget.getY(), false, false);
            createdTiles = new Sprite[] { newTile };
            reverseTargets = new Target[] { currentTarget };
        }
        if(currentTarget.id == TARGET_SPRING_RED) {
            Sprite newTile = createTile(ID_TILE_SPRING_RED, TILE_SPRING_RED, currentTarget.getX(), currentTarget.getY(), false, false);
            createdTiles = new Sprite[] { newTile };
            reverseTargets = new Target[] { currentTarget };
        }
        if(currentTarget.id == TARGET_SPRING_BLUE) {
            Sprite newTile = createTile(ID_TILE_SPRING_BLUE, TILE_SPRING_BLUE, currentTarget.getX(), currentTarget.getY(), false, false);
            createdTiles = new Sprite[] { newTile };
            reverseTargets = new Target[] { currentTarget };
        }
    }
    
    void resetTiles() {
        for(Sprite s : createdTiles) {
            world.killSprite(s);
        }
        reverseTargets = null;
    }
    
    void reset() {
        resetTiles();
        player.moveTo(startx, starty);
        playerDirection = 1;
        player.setAppearance(ID_PLAYER_LEFT);
        trackPlayer();
        
        playSound(explode);
    }
    
    void trackPlayer() {
        playerWallTracker.moveTo(player.getX() + playerDirection * 3.5f, player.getY());
        playerFloorTracker.moveTo(player.getX(), player.getY() + 9.5f);
    }
    
    public void movePlayerX() {    
        player.moveX(playerVelX * playerDirection);
        if(playerVelX < 0.6f) {
            playerVelX += 0.1f;
        }
        if(playerVelX > 0.8f) {
            playerVelX -= 0.1f;
        }
        trackPlayer();
    }
    
    public void testPlayerX() {
        if(world.intersects(playerWallTracker, TYPE_TILE)) {
            
            player.moveY(-8);
            trackPlayer();
            
            if(world.intersects(playerWallTracker, TYPE_TILE)) { //The player is blocked in both tiles
                if(!world.intersects(playerWallTracker, wallComp)) {
                    if(playerDirection == 1) {
                        player.setAppearance(ID_PLAYER_RIGHT);
                    }
                    else {
                        player.setAppearance(ID_PLAYER_LEFT);
                    }
                    playerDirection = (byte)(-1 * playerDirection);
                    playerVelX = 0;
                }
            }
            else { //The player is next to a single step
                if(playerVelY == 0) { //Only step up the player if they are on the ground
                    player.moveY(-4);
                }
                //player.moveX(1 * playerDirection);
            }
            
            player.moveY(8);
            trackPlayer();
        }
        
    }
    
    @Override
    public void loop(long tick) {
        mouseTracker.moveTo(getMouseX(), getMouseY());
        validateMouse();
        
        if(mouseCooldown == 0) {
            if(isLMBDown()) {
                if(targetState == TARGET) {
                    applyTarget();
                    mouseCooldown = 20;
                    playSound(build);
                }
                else if(targetState == TARGET_REVERSE) {
                    resetTiles();
                    mouseCooldown = 20;
                    playSound(destroy);
                }
            }
        }
        else { mouseCooldown--; }
        
        if(tick % 20 == 0) {
            player.animframe++;
        }
        
        testPlayerX();
        
        movePlayerX();
        trackPlayer();
        
        player.moveY(playerVelY);
        trackPlayer();
        
        if(world.intersects(playerFloorTracker, TYPE_TILE)) {
            playerVelY = 0;
            float y = (int)((player.getY() + 9.5f) / 8) * 8 - 9f;
            
            player.moveTo(player.getX(), y);
            
            if(world.intersects(playerFloorTracker, springCompGreen)) {
                playSound(spring);
                playerVelY = -3.1f;
                playerVelX = 1.8f;
            }
            else if(world.intersects(playerFloorTracker, springCompRed)) {
                playSound(spring);
                playerVelY = -3.7f;
                playerVelX = 2.9f;
            }
            else if(world.intersects(playerFloorTracker, springCompBlue)) {
                playSound(spring);
                playerVelY = -4.4f;
                playerVelX = 4.1f;
            }
        } 
        else {
            if(playerVelY < 2.3f) {
                playerVelY += 0.2f;
            }
            //playerVelY = 0.2f;
        }
        
        if(world.intersects(player, TYPE_DOOR)) {
            if(unlocked < level + 1) {
                unlocked = level + 1;
            }
            save(); //Save progress to file
            playSound(end);
            setCallback(new PauseCallback(levelOverCallback, this, PauseCallback.DIRECTION_DOWN));
        }
        if(world.intersects(playerFloorTracker, obstacleComp) || world.intersects(playerWallTracker, obstacleComp)) {
            reset();
        }
        if(resetCooldown == 0) {
            if(resetControl.status) {
                reset();
                resetCooldown = 20;
            }
        }
        else { resetCooldown--; }
        if(pauseControl.status) {
            setCallback(new PauseCallback(pauseMenuCallback, this, PauseCallback.DIRECTION_DOWN));
        }
        trackPlayer();
        camera.track(player);
        camera.render(world, renderer);
        camera.drawToLens(mouseTracker, renderer);
        
        playMusic(gameMusic);
    }
    
    @Override
    public void render(Renderer r, int y) {
        r.drawImage(background, 0, y);
        camera.track(player);
        camera.y = y;
        camera.drawSprites(world, renderer);
    }
    
    protected void save() {
        PrintWriter writer;
        try {
            writer = new PrintWriter("fellowship-save.txt", "UTF-8");
            writer.print(unlocked);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        }
    }
    
    @Override
    protected void onClose() {        
        save();
    }
    
    public static void main(String[] args) {
        FellowshipGame game = new FellowshipGame();
        game.run();
    }
    
}
