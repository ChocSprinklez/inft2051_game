package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

// by D. Cornforth April 2019
// coin sprites from https://opengameart.org/content/coin-animation

public class Door
{
    int posX, posY;
    Image spriteSheet;
    int imageSize, border, spriteCols, spriteRows, spriteIndex;
    String side;

    public Door(String fileName, int size, int border, int posX, int posY, float scale, String side, int levelNo)
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
        if (side.equals("top") && levelNo == 1)
        {
            spriteIndex = 0;
        }
        else if (side.equals("top") && levelNo == 3)
        {
            spriteIndex = 2;
        }else if (side.equals("top") && levelNo == 2)
        {
            spriteIndex = 4;
        }
        else if (side.equals("left") && levelNo == 1)
        {
            spriteIndex = 6;
        }
        else if (side.equals("left") && levelNo == 3)
        {
            spriteIndex = 8;
        }else if (side.equals("left") && levelNo == 2)
        {
            spriteIndex = 10;
        }
        else if (side.equals("right") && levelNo == 1)
        {
            spriteIndex = 12;
        }
        else if (side.equals("right") && levelNo == 3)
        {
            spriteIndex = 14;
        }else if (side.equals("right") && levelNo == 2)
        {
            spriteIndex = 16;
        }
        this.posX = posX;
        this.posY = posY;
        this.side = side;
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
        if (!isOpen())
        {
            spriteIndex++;
        }
    }

    public String getSide()
    {
        return side;
    }

    public boolean isOpen()
    {
        if (spriteIndex == 1 || spriteIndex == 3 || spriteIndex == 5)
        {
            return true;
        }
        if (spriteIndex == 7 || spriteIndex == 9 || spriteIndex == 11)
        {
            return true;
        }
        if (spriteIndex == 13 || spriteIndex == 15 || spriteIndex == 17)
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
