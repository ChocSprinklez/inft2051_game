package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

import java.util.Random;

// by D. Cornforth April 2019
// coin sprites from https://opengameart.org/content/coin-animation

public class Chest
{
    int posX, posY;
    Image spriteSheet;
    int imageSize, border, spriteCols, spriteRows, spriteIndex, itemSprite;
    Item item;

    public Chest(String fileName, int size, int border, int posX, int posY, float scale, Item item)
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
        spriteIndex = 0;
        this.posX = posX;
        this.posY = posY;
        this.item = item;
        Random RAND = new Random();
        itemSprite = RAND.nextInt(3)+1;
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
        item.setSprite(itemSprite);
        if (spriteIndex >= spriteCols * spriteRows)
        {
            spriteIndex = 1;
        }
    }
    public boolean isOpen()
    {
        if (spriteIndex == 1)
        {
            return true;
        }
        return false;
    }

    public void render(Graphics g, int offsetX, int offsetY)
    {
        g.setClip(posX - offsetX, posY - offsetY, imageSize, imageSize);
        int indexX = spriteIndex % spriteCols;
        int renderX = posX - indexX * (imageSize + border);
        int indexY = spriteIndex / spriteCols;
        int renderY = posY - indexY * (imageSize + border);
        g.drawImage(spriteSheet, renderX - offsetX, renderY - offsetY);
        item.render(g,offsetX,offsetY);
    }
}
