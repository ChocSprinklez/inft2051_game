package uon.inft2051.lab04;


import com.codename1.ui.Graphics;
import com.codename1.ui.Component;


import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;   // stage 5

import com.codename1.ui.Image;   // stage 5


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
    int lives, score, pauseCount;   // stage 5
    int invuln, enemyTurn;
    Image imHeart;
    Turn PlayerTurn;
    Image endTurn;
    Button turnEnd;
    Image attack;
    Button attackB;
    Attack swordSwing;

    public void initComponent()
    {
        sMessage = "begin game";
        currentTime = new Date();
        lastRenderedTime = 0;
        pointerX = 0;
        pointerY = 0;

        tmScene = new TileMap(16);   // stage 3
        tmScene.loadScene("/Map1_Tile Layer.csv");   // stage 3
        tmScene.loadImages("/tileset.png", 0);
        screenX = 0;
        screenY = 0;

        tmTop = new TileMap(16);   // stage 3
        tmTop.loadScene("/Map1_Collision Layer.csv");   // stage 3
        tmTop.loadImages("/tileset.png", 0);
        screenX = 0;
        screenY = 0;

        Hero = new Character(tmTop, "/5.png", 16, 0);
        Hero.setSprites(4, 7, 0, 3, 4, 0, 12, 15, 8, 11, 12, 8);

        isPressed = false;

        alCoins = new ArrayList<Chest>();   // stage 5
        alEnemys = new ArrayList<Enemy>();
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
                    Chest newCoin = new Chest("/little-treasure-chest.png", 16, 0, startX, startY);
                    alCoins.add(newCoin);
                }
                else if (name.equals("hero"))
                {
                    Hero.initCharacter(startX, startY, true);
                }
                else if (name.equals("enemy"))
                {
                    Enemy newEnemy = new Enemy(tmTop,"/monster1.png", 16, 0, 2);
                    newEnemy.setSprites(8,11,12,15,8,12,4,7,0,3,4,0);
                    newEnemy.initCharacter(startX,startY,true);
                    alEnemys.add(newEnemy);
                }
            }
        }
        swordSwing = new Attack("/att.png",32,0,Hero);
        turnCircle = new MoveCircle(Hero, tmScene, "/circlefile.png",16,5);
        PlayerTurn = new Turn(Hero, turnCircle);
        try
        {
            // image from https://openclipart.org/detail/29043/heart
            imHeart = Image.createImage("/heart.png");
        }
        catch (Exception exp)
        {
            imHeart  = null;
        }
        lives = 3;
        score = 0;
        pauseCount = 0;
        isPaused = false;
        invuln = 0;
        enemyTurn = 0;


        try
        {
            endTurn = Image.createImage("/x.png");
        }catch (Exception exp)
        {
            endTurn = null;
        }
        turnEnd = new Button(endTurn, 64, 448,224);
        try
        {
            attack = Image.createImage("/sword.png");
        } catch (Exception exp)
        {
            attack = null;
        }
        attackB = new Button(attack, 64, 0, 224);
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

        Hero.render(g, screenX, screenY);
        swordSwing.render(g,screenX,screenY);
        turnEnd.render(g);
        attackB.render(g);

        g.setClip(0, 0, getWidth(), getHeight());   // reset clipping rect

        for (int ndx = 0; ndx < lives; ndx++)   // stage 5
        {
            g.drawImage(imHeart, ndx * 34 + 5, 0);
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
            for (Enemy thisEnemy : alEnemys) {
                if (thisEnemy.collide(swordSwing) && swordSwing.attackActive()) {
                    alEnemys.remove(thisEnemy);
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