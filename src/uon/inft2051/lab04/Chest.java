package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

// by D. Cornforth April 2019
// coin sprites from https://opengameart.org/content/coin-animation

public class Chest
{
    int posX, posY;
    Image spriteSheet;
    int imageSize, border, spriteCols, spriteRows, spriteIndex;

    public Chest(String fileName, int size, int border, int posX, int posY)
    {
        try
        {
            spriteSheet = Image.createImage(fileName);
        }
        catch (Exception exp)
        {
            spriteSheet = null;
        }
        imageSize = size;
        this.border = border;
        spriteCols = spriteSheet.getWidth() / (imageSize + border);
        spriteRows = spriteSheet.getHeight() / (imageSize + border);
        spriteIndex = 0;
        this.posX = posX;
        this.posY = posY;
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
            spriteIndex = 0;
        }
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
