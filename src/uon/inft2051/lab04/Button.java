package uon.inft2051.lab04;

import com.codename1.ui.Image;
import com.codename1.ui.Graphics;

public class Button {
    int posX;
    int posY;
    Image Button;
    int imageSize;


    public Button (Image Button, int size, int posX, int posY)
    {
        this.Button = Button;
        imageSize = size;
        this.posX = posX;
        this.posY = posY;
    }

    public void render(Graphics g)
    {
        g.setClip(posX, posY,imageSize,imageSize);
        g.drawImage(Button,posX, posY);
    }

    public boolean isClicked(int relX, int relY)
    {
        if (relX >= posX && relX <= posX + imageSize && relY >= posY && relY <= posY + imageSize)
        {
            return true;
        }
        return false;
    }

}
