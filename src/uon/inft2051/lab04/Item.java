package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

// by D. Cornforth April 2019
// coin sprites from https://opengameart.org/content/coin-animation

public class Item
{
    int posX, posY;
    Image spriteSheet;
    int imageSize, border, spriteCols, spriteRows, spriteIndex;

    public Item(String fileName, int size, int border, int posX, int posY, float scale)
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
    }

    public int getSceneX()
    {
        return posX;
    }

    public int getSceneY()
    {
        return posY;
    }

    // sprite chosen for specific item
    public void animate(int sprite)
    {
        spriteIndex = sprite;
    }

    public boolean isClicked(int relX, int relY)
    {
        if (relX >= posX + (imageSize/4) && relX <= posX + imageSize - (imageSize/4) && relY >= posY+ (imageSize/4) && relY <= posY + imageSize - (imageSize/4))
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
    }
}
