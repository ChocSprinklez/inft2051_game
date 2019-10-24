package uon.inft2051.lab04;


import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.*;


import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;   // stage 5


public class GameComponent extends Component
{
    Date currentTime;
    long lastRenderedTime;
    String sMessage;
    int pointerX, pointerY;
    boolean isPressed = false;
    boolean isPaused;   // stage 5
    Character Hero;
    TileMap tmScene;
    TileMap tmTop;
    MoveCircle turnCircle;
    int screenX, screenY;
    ArrayList<Chest> alCoins;   // stage 5
    ArrayList<Enemy> alEnemys;
    ArrayList<Door> doorList;
    int lives, score, pauseCount;   // stage 5
    int invuln, enemyTurn;
    Image imHeart;
    Turn PlayerTurn;
    Image endTurn;
    Button turnEnd;
    Media backgroundMusic;

    Image attack;
    Button attackB;
    Attack swordSwing;
    float scaleImages;
    int levelNo = 1;
    int enemies = 0;
    String loadSide;
    boolean levelComplete = false;

    public void initComponent()
    {
        sMessage = "begin game";
        currentTime = new Date();
        lastRenderedTime = 0;
        pointerX = 0;
        pointerY = 0;
        levelComplete = false;
        lives = 3;
        score = 0;
        pauseCount = 0;
        isPaused = false;
        invuln = 0;
        enemyTurn = 0;
        String f = "jar://theme-2.mp3";
        try
        {
            backgroundMusic = MediaManager.createBackgroundMedia(f);
            backgroundMusic.play();
        }
        catch (IOException err)
        {
            System.out.println("Music not playing");
        }
    }

    public void initialize(String load_side)
    {
        String bottomMapFile = "/Map" + levelNo + "_Tile Layer.csv";
        String topMapFile = "/Map" + levelNo + "_Collision Layer.csv";
        String EnemyFile = "/monster" + levelNo + ".png";

        tmScene = new TileMap(16, scaleImages);   // stage 3
        tmScene.loadScene(bottomMapFile);   // stage 3
        tmScene.loadImages("/tileset.png", 0);
        screenX = 0;
        screenY = 0;

        tmTop = new TileMap(16, scaleImages);   // stage 3
        tmTop.loadScene(topMapFile);   // stage 3
        tmTop.loadImages("/tileset.png", 0);
        screenX = 0;
        screenY = 0;

        Hero = new Character(tmTop, "/5.png", 16, 0, scaleImages);
        Hero.setSprites(4, 7, 0, 3, 4, 0, 12, 15, 8, 11, 12, 8);

        isPressed = false;

        alCoins = new ArrayList<Chest>();   // stage 5
        alEnemys = new ArrayList<Enemy>();
        doorList = new ArrayList<Door>();
        ArrayList<String> charSpecs = tmTop.getAnimats();
        for (String charSpec : charSpecs)
        {
            String[] tokens = charSpec.split(":");
            if (tokens.length == 3)
            {
                char startZ = tokens[0].charAt(0);
                String name = tokens[0].substring(1);
                int startX = Integer.parseInt(tokens[1]);
                int startY = Integer.parseInt(tokens[2]);
                if (name.equals("chest"))
                {
                    Chest newCoin = new Chest("/little-treasure-chest.png", 16, 0, startX, startY, scaleImages);
                    alCoins.add(newCoin);
                }
                else if (name.equals("enemy"))
                {
                    Enemy newEnemy = new Enemy(tmTop,EnemyFile, 16, 0, 2, scaleImages);
                    newEnemy.setSprites(8,11,12,15,8,12,4,7,0,3,4,0);
                    newEnemy.initCharacter(startX,startY,true);
                    alEnemys.add(newEnemy);
                    enemies++;
                }
                else if (name.equals("doorT"))
                {
                    String side = "top";
                    Door newDoor = new Door("/doors.png",16,0, startX, startY, scaleImages,side, levelNo);
                    doorList.add(newDoor);
                }
                else if (name.equals("doorL"))
                {
                    String side = "left";
                    Door newDoor = new Door("/doors.png",16,0, startX, startY, scaleImages,side, levelNo);
                    doorList.add(newDoor);
                }
                else if (name.equals("doorR"))
                {
                    String side = "right";
                    Door newDoor = new Door("/doors.png",16,0, startX, startY, scaleImages,side, levelNo);
                    doorList.add(newDoor);
                }
                if (load_side.equals("")) {
                    if (name.equals("hero")) {
                        Hero.initCharacter(startX, startY, true);
                    }
                } else
                {
                    for (Door thisDoor: doorList)
                    {
                        if (thisDoor.getSide().equals(load_side)) {
                            System.out.println("Starting at " + load_side);
                            Hero.initCharacter(thisDoor.getSceneX(), thisDoor.getSceneY(), true);
                        }
                    }
                }
            }
        }
        swordSwing = new Attack("/att.png",32,0,Hero,scaleImages);
        turnCircle = new MoveCircle(Hero, tmScene, "/circlefile.png",16,5,scaleImages);
        PlayerTurn = new Turn(Hero, turnCircle);
        try
        {
            // image from https://openclipart.org/detail/29043/heart
            imHeart = Image.createImage("/heart.png");
            int x = (int)(64*scaleImages);
            int y = (int)(64*scaleImages);
            System.out.println("x = " + x + " y = " + y);
            imHeart.scale(x,y);
        }
        catch (Exception exp)
        {
            imHeart  = null;
        }


        try
        {
            endTurn = Image.createImage("/x.png");
            int x = (int)(64*scaleImages);
            int y = (int)(64*scaleImages);
            endTurn.scale(x,y);
        }catch (Exception exp)
        {
            endTurn = null;
        }

        turnEnd = new Button(endTurn, (int)(64*scaleImages), (int)(Display.getInstance().getDisplayWidth() - (64*scaleImages)),(int)(Display.getInstance().getDisplayHeight() - (64*scaleImages)));
        try
        {
            attack = Image.createImage("/sword.png");
            int x = (int)(64*scaleImages);
            int y = (int)(64*scaleImages);
            attack.scale(x,y);
        } catch (Exception exp)
        {
            attack = null;
        }

        attackB = new Button(attack, (int)(64*scaleImages), (int)(0*scaleImages), (int)(Display.getInstance().getDisplayHeight() - (64*scaleImages)));
    }

    public void setScaleImages (float scale)
    {
        float f = 512;
        scaleImages = scale/f;
        System.out.println("scale ratio: " + scaleImages);
    }

    public void setLevelNo(int level)
    {
        levelNo = level;
    }
    public void setLoadSide(String side)
    {
        loadSide = side;
    }

    public boolean getLevelComplete()
    {
        return levelComplete;
    }

    public String getLoadSide()
    {
        return loadSide;
    }


    public int getLevelNo()
    {
        return levelNo;
    }

    public void nextLevel()
    {
        String side = loadSide;
        if (levelNo == 1)
        {
            System.out.println("Loading Level 2");
            levelNo = 2;
            initialize(side);
        } else if (levelNo == 2)
        {
            System.out.println("Loading Level 3");
            levelNo = 3;
            initialize(side);
        } else if (levelNo == 3)
        {
            System.out.println("Loading Level 1");
            levelNo = 1;
            initialize(side);
        }
    }

    public void paint(Graphics g)
    {
        // calc position to render main char on viewport
        screenX = 0;

             // render the objects of the scene
        tmScene.render(g, screenX, screenY);
        turnCircle.render(g, screenX, screenY);
        tmTop.render(g, screenX, screenY);
        for (Chest thisChest : alCoins)   // stage 5
        {
            thisChest.render(g, screenX, screenY);
        }
        for (Enemy thisEnemy : alEnemys)
        {
            thisEnemy.render(g, screenX, screenY);
        }
        for (Door thisDoor: doorList)
        {
            thisDoor.render(g,screenX,screenY);
        }

        Hero.render(g, screenX, screenY);
        swordSwing.render(g,screenX,screenY);
        turnEnd.render(g);
        attackB.render(g);

        g.setClip(0, 0, getWidth(), getHeight());   // reset clipping rect

        for (int ndx = 0; ndx < lives; ndx++)   // stage 5
        {
            g.drawImage(imHeart, (int)((ndx * 34 + 5)*scaleImages), 0);
        }
        g.setColor(0xff0000);
        //g.drawString(sMessage + " : " + Hero.getSceneX(), 10, 10);
        g.drawString("Score: " + score + " : " + sMessage, 200, 10);
        g.drawString("PointerX: " + pointerX + " PointerY: " + pointerY,200,20);
    }

    public void pointerPressed(int pressX, int pressY)
    {

        if (!(turnEnd.isClicked(pressX,pressY) || attackB.isClicked(pressX,pressY))) {
            isPressed = true;
            pointerX = pressX;
            pointerY = pressY;
        }
    }

    public void pointerReleased(int releaseX, int releaseY)
    {
        isPressed = false;
        if (turnEnd.isClicked(releaseX,releaseY))
        {
            if (enemyTurn == 0) {
                PlayerTurn.endTurn();
                enemyTurn = 10;
            }
        }
        if (attackB.isClicked(releaseX,releaseY))
        {
            swordSwing.doAttack(Hero);
        }

    }

    public boolean animate()
    {
        if (System.currentTimeMillis() / 100 != lastRenderedTime / 100)
        {
            currentTime.setTime(System.currentTimeMillis());
            lastRenderedTime = System.currentTimeMillis();

            if (!backgroundMusic.isPlaying())
            {
                backgroundMusic.pause();
                backgroundMusic.setTime(0);
                backgroundMusic.play();
            }

            // stage 5
            for (Chest thisChest : alCoins) {
                if (Hero.collide(thisChest)) {
                    if (!thisChest.isOpen()) {
                        score += 10;
                    }
                    thisChest.animate();
                }
            }

            if (isPaused)   // stage 5
            {
                if (pauseCount > 0)
                {
                    pauseCount--;
                }
                if (pauseCount == 0)
                {
                    screenX = 0;
                    screenY = 0;
                    Hero.initCharacter(16, 200, true);
                    PlayerTurn.startTurn();
                    sMessage = "Be careful";
                    lives = 3;
                    isPaused = false;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            if (swordSwing.getAttack())
            {
                swordSwing.animate();
            }

            if (PlayerTurn.isTurn())
            {
                if (isPressed)
                {
                    if (pointerX > getWidth() / 2)
                    {
                        Hero.walk(pointerX,pointerY);
                    }
                    else if (pointerX < getWidth() / 2)
                    {
                        Hero.walk(pointerX,pointerY);
                    }
                }
                else
                {
                    Hero.standStill();
                }
            }
            if (enemies == 0)
            {
                for (Door thisDoor : doorList)
                {
                    thisDoor.animate();
                    if (Hero.collide(thisDoor))
                    {
                        setLoadSide(thisDoor.getSide());
                        levelComplete = true;
                        System.out.println("Level Complete");
                        nextLevel();
                        break;
                    }
                }
            }
            for (Enemy thisEnemy : alEnemys) {
                if (thisEnemy.collide(swordSwing) && swordSwing.attackActive()) {
                    alEnemys.remove(thisEnemy);
                    enemies--;
                    break;
                }
            }
            if (enemyTurn > 0) {
                for (Enemy thisEnemy : alEnemys) {
                    thisEnemy.walk(Hero.getSceneX(), Hero.getSceneY());
                    if (!PlayerTurn.isEnemyTurn()) {
                        //movearea reset
                        thisEnemy.setCenter(thisEnemy.getSceneX(),thisEnemy.getSceneY());
                    }
                    if (Hero.collide(thisEnemy)) {
                        if (thisEnemy.getAttackDelay() == 0) {
                            lives--;
                            thisEnemy.setAttackDelay(10);
                            sMessage = "Life lost";
                            if (lives == 0) {
                                isPaused = true;
                                sMessage = "Game over";
                                enemyTurn = 0;
                                pauseCount = 20;
                            }
                        }
                    }
                    if (thisEnemy.getAttackDelay() > 0) {
                        thisEnemy.setAttackDelay(thisEnemy.getAttackDelay() - 1);
                    }
                }
                PlayerTurn.enemyMove();
                enemyTurn--;
                if (enemyTurn == 0)
                {
                    PlayerTurn.startTurn();
                }
            }

            if (!Hero.checkBoundsX())   // stage 5
            {
                isPaused = true;
                pauseCount = -1;
                sMessage = "Level completed";
            }
            return true;
        }
        return false;
    }
}