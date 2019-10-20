package uon.inft2051.lab04;

import java.awt.*;

import com.codename1.maps.Tile;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

public class MoveCircle {
     int centerX;
     int centerY;
     TileMap tmTop;
     Image square;
     int imageSize;
     int circleSize;



    public MoveCircle(Character Hero, TileMap tmTop, String fileName, int size, int radius)
    {
        this.tmTop = tmTop;
        centerX = Hero.getSceneX();
        centerY = Hero.getSceneY();
        Hero.moveCircle(this);
        try
        {
            square = Image.createImage(fileName);
        } catch (Exception exp)
        {
            square = null;
        }
        imageSize = size;
        circleSize = radius;
    }
    public MoveCircle(Enemy Enemy, TileMap tmTop, String fileName, int size, int radius)
    {
        this.tmTop = tmTop;
        centerX = Enemy.getSceneX();
        centerY = Enemy.getSceneY();
        Enemy.moveCircle(this);
        try
        {
            square = Image.createImage(fileName);
        } catch (Exception exp)
        {
            square = null;
        }
        imageSize = size;
        circleSize = radius;
    }
    public void render(Graphics g, int offsetX, int offsetY)
    {
        int arraySize = circleSize*2 - 1;
        int[][] sqArrayX = new int[arraySize][arraySize];
        int[][] sqArrayY = new int[arraySize][arraySize];
        for (int i = 0; i < arraySize; i++)
        {
            for (int j = 0; j < arraySize; j++) {
                sqArrayX[i][j] = centerX - 5 * imageSize + (i+1) * imageSize;
                sqArrayY[i][j] = centerY - 5 * imageSize + (j+1) * imageSize;
            }
        }
        for (int i = 0; i < arraySize; i++)
        {
            for (int j = 0; j < arraySize; j++) {
                g.setClip(sqArrayX[i][j] - offsetX, sqArrayY[i][j] - offsetY, imageSize, imageSize);
                g.drawImage(square, sqArrayX[i][j] - offsetX, sqArrayY[i][j] - offsetY);
            }
        }
    }

    public void changeSize(int radius)
    {
        circleSize = radius;
    }

    public void setCenter(Character Hero)
    {
        centerX = Hero.getSceneX();
        centerY = Hero.getSceneY();
    }
    public void setCenter(Enemy Enemy)
    {
        centerX = Enemy.getSceneX();
        centerY = Enemy.getSceneY();
    }
    public int[] getCenter()
    {
        int[] center = new int[3];
        center[0] = centerX;
        center[1] = centerY;
        center[2] = circleSize;
        return center;
    }
}
