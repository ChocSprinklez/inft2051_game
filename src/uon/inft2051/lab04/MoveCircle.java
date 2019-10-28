package uon.inft2051.lab04;


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



    public MoveCircle(Character Hero, TileMap tmTop, String fileName, int size, int radius, float scale)
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
        imageSize = (int)(size*scale);
        int y = (int)(square.getHeight()*scale);
        int x = (int)(square.getWidth()*scale);
        square.scale(x,y);
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
                sqArrayX[i][j] = centerX - circleSize * imageSize + (i+1) * imageSize;
                sqArrayY[i][j] = centerY - circleSize * imageSize + (j+1) * imageSize;
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
    public int[] getCenter()
    {
        int[] center = new int[3];
        center[0] = centerX;
        center[1] = centerY;
        center[2] = circleSize;
        return center;
    }
}
