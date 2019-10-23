package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.TextSelection;

// by D. Cornforth April 2019
// coin sprites from https://opengameart.org/content/coin-animation

public class Attack
{
    int posX, posY;
    Image spriteSheet;
    boolean attack;
    int imageSize, border, spriteCols, spriteRows, spriteIndex;

    public Attack(String fileName, int size, int border, Character Hero, float scale)
    {
        try
        {
            spriteSheet = Image.createImage(fileName);
        }
        catch (Exception exp)
        {
            spriteSheet = null;
        }
        imageSize = (int)(size*scale);
        int y = (int)(spriteSheet.getHeight()*scale);
        int x = (int)(spriteSheet.getWidth()*scale);
        spriteSheet.scale(x,y);
        this.border = (int)(border*scale);
        spriteCols = spriteSheet.getWidth() / (imageSize + border);
        spriteRows = spriteSheet.getHeight() / (imageSize + border);
        spriteIndex = 4;
        this.posX = Hero.getSceneX();
        this.posY = Hero.getSceneY();
    }

    public int getSceneX()
    {
        return posX;
    }

    public int getSceneY()
    {
        return posY;
    }

    // switch sprites to give appearance of rotation
    public void animate()
    {
        spriteIndex++;
        if (spriteIndex >= spriteCols * spriteRows)
        {
            attack = false;
        }

    }
    public boolean attackActive()
    {
        if (spriteIndex > 2)
        {
            return false;
        }
        return true;
    }

    public boolean getAttack()
    {
        return attack;
    }
    public void doAttack(Character Hero)
    {
        if (Hero.isFacing() == 1) {
            this.posX = Hero.getSceneX() - (imageSize - 16)/2;
            this.posY = Hero.getSceneY() - imageSize;
        } else if (Hero.isFacing() == 2) {
            this.posX = Hero.getSceneX() - (imageSize - 16)/2;
            this.posY = Hero.getSceneY() + (imageSize - 16);
        } else if (Hero.isFacing() == 3) {
            this.posX = Hero.getSceneX() - imageSize;
            this.posY = Hero.getSceneY() - (imageSize - 16)/2;
        } else if (Hero.isFacing() == 4) {
            this.posX = Hero.getSceneX() + (imageSize - 16);
            this.posY = Hero.getSceneY() - (imageSize - 16)/2;
        }
        attack = true;
        spriteIndex = 0;
    }


    public void render(Graphics g, int offsetX, int offsetY)
    {
        g.setClip(posX - offsetX, posY - offsetY, imageSize, imageSize);
        int indexX = spriteIndex % spriteCols;
        int renderX = posX - indexX * (imageSize + border);
        int indexY = spriteIndex / spriteCols;
        int renderY = posY - indexY * (imageSize + border);
        g.drawImage(spriteSheet, renderX - offsetX, renderY - offsetY);
    }
}
