package uon.inft2051.lab04;

import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

// running caveman sprite sheet from https://opengameart.org/content/running-caveman-spritesheet
// edited by David Cornforth to apply right facing characters and transparent background

public class Character
{
    private final double minSpeedX = 4.0;
    private final double maxSpeedX = 5.0;
    private final double incSpeed = 0.1;
    private final double minSpeedY = 4.0;
    private final double maxSpeedY = 5.0;
    //final double gravity = 2.0;

    private int posX, posY;
    private double speedX, speedY;
    private Image spriteSheet;
    private int imageSize, border, spriteCols, spriteRows, spriteIndex;
    private int minLeft, maxLeft, minRight, maxRight, stdLeft, stdRight;
    private int minUp, maxUp, minDown, maxDown, stdUp, stdDown;
    private boolean isJumping;
    private TileMap tmScene;   // stage 4
    private char Zchar;   // stage 4
    private MoveCircle circle;

    public Character(TileMap tmScene, String fileName, int size, int border)
    {
        this.tmScene = tmScene;
        posX = 0;
        posY = 0;
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
        isJumping = false;
    }

    public int getSceneX()   // stage 3
    {
        return posX;
    }

    public int getSceneY()
    {
        return posY;
    }

    // define which sprites to use for which function
    public void setSprites(int minLeft, int maxLeft, int minRight, int maxRight, int stdLeft, int stdRight, int minUp, int maxUp, int minDown, int maxDown, int stdUp, int stdDown)
    {
        this.minLeft = minLeft;
        this.maxLeft = maxLeft;
        this.minRight = minRight;
        this.maxRight = maxRight;
        this.stdLeft = stdLeft;
        this.stdRight = stdRight;
        this.minUp = minUp;
        this.maxUp = maxUp;
        this.minDown = minDown;
        this.maxDown = maxDown;
        this.stdUp = stdUp;
        this.stdDown = stdDown;
    }

    public void initCharacter(int posX, int posY, boolean isfacingRight)
    {
        this.posX = posX;
        this.posY = posY;
        speedX = 0;
        speedY = 0;
        if (isfacingRight)
        {
            spriteIndex = stdRight;
        }
        else
        {
            spriteIndex = stdLeft;
        }
        Zchar = 'A';   // stage 4
    }

    public void walk(int pointerX, int pointerY) {
        int moveX = pointerX;
        int moveY = pointerY;

        walkAnim(moveX,moveY);
        if (posX != moveX) {
            if (posX < (moveX - moveX/10)) {
                if (speedX <= 0) {
                    speedX = minSpeedX;
                } else {
                    speedX += incSpeed;
                    if (speedX > maxSpeedX) {
                        speedX = maxSpeedX;
                    }
                }
                posX += speedX;
                collideX(moveX);
            } else if (posX > (moveX + moveX/10)) {
                if (speedX >= 0) {
                    speedX = minSpeedX;
                } else {
                    speedX += incSpeed;
                    if (speedX > maxSpeedX) {
                        speedX = maxSpeedX;
                    }
                }
                posX -= speedX;
                collideX(moveX);
            }
        }
        if (posY != moveY) {
            if (posY < (moveY - moveY/10)) {
                if (speedY <= 0) {
                    speedY = minSpeedY;
                } else {
                    speedY += incSpeed;
                    if (speedY > maxSpeedY) {
                        speedY = maxSpeedY;
                    }
                }
                posY += speedY;
                collideY(moveY);
            } else if (posY > (moveY + moveY/10)) {
                if (speedY >= 0) {
                    speedY = minSpeedY;
                } else {
                    speedY += incSpeed;
                    if (speedY > maxSpeedY) {
                        speedY = maxSpeedY;
                    }
                }
                posY -= speedY;
                collideY(moveY);
            }
        }
    }

    public void walkAnim(int moveX, int moveY)
    {
        if(posX != moveX && posY == moveY) //move left or right
        {
            if (posX < moveX) //move right
            {
                if (spriteIndex < minRight || spriteIndex >= maxRight)
                {
                    spriteIndex = minRight;
                }
                else
                {
                    spriteIndex++;
                }
            } else if (posX > moveX) //move left
            {
                if (spriteIndex < minLeft || spriteIndex >= maxLeft)
                {
                    spriteIndex = minLeft;
                }
                else
                {
                    spriteIndex++;
                }
            } else
            {
                standStill();
            }
        } else if (posX == moveX && posY != moveY) //up or down
        {
            if (posY > moveY) //move up
            {
                if (spriteIndex < minUp || spriteIndex >= maxUp)
                {
                    spriteIndex = minUp;
                }
                else
                {
                    spriteIndex++;
                }
            } else if (posY < moveY) //move down
            {
                if (spriteIndex < minDown || spriteIndex >= maxDown)
                {
                    spriteIndex = minDown;
                }
                else
                {
                    spriteIndex++;
                }
            } else
            {
                standStill();
            }
        }
        else if (posX != moveX && posY != moveY) //diagonal movement
        {
            if (posX < moveX && posY < moveY) //down and right
            {
                if((moveX - posX) < (moveY - posY)) //down > right
                {
                    if (spriteIndex < minDown || spriteIndex >= maxDown)
                    {
                        spriteIndex = minDown;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                } else //right > down
                {
                    if (spriteIndex < minRight || spriteIndex >= maxRight)
                    {
                        spriteIndex = minRight;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                }
            }
            else if (posX > moveX && posY < moveY) //down and left
            {
                if((posX - moveX) < (moveY - posY)) //down > left
                {
                    if (spriteIndex < minDown || spriteIndex >= maxDown)
                    {
                        spriteIndex = minDown;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                } else //left > down
                {
                    if (spriteIndex < minLeft || spriteIndex >= maxLeft)
                    {
                        spriteIndex = minLeft;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                }
            } else if (posX < moveX && posY > moveY) //up and right
            {
                if((moveX - posX) < (posY - moveY)) //up > right
                {
                    if (spriteIndex < minUp || spriteIndex >= maxUp)
                    {
                        spriteIndex = minUp;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                } else //right > up
                {
                    if (spriteIndex < minRight || spriteIndex >= maxRight)
                    {
                        spriteIndex = minRight;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                }
            } else if (posX > moveX && posY > moveY)
            {
                if((posX - moveX) < (posY - moveX))
                {
                    if (spriteIndex < minUp || spriteIndex >= maxUp)
                    {
                        spriteIndex = minUp;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                } else
                {
                    if (spriteIndex < minLeft || spriteIndex >= maxLeft)
                    {
                        spriteIndex = minLeft;
                    }
                    else
                    {
                        spriteIndex++;
                    }
                }
            }

        }
    }



    public void standStill()
    {
        speedX = 0;
        if (spriteIndex >= minLeft && spriteIndex <= maxLeft)
        {
            spriteIndex = stdLeft;
        }
        if (spriteIndex >= minRight && spriteIndex <= maxRight)
        {
            spriteIndex = stdRight;
        }
        if (spriteIndex >= minUp && spriteIndex <= maxUp)
        {
            spriteIndex = stdUp;
        }
        if (spriteIndex >= minDown && spriteIndex <= maxDown)
        {
            spriteIndex = stdDown;
        }
    }


    public boolean jumping()
    {
        return isJumping;
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

    public void moveCircle(MoveCircle circle)
    {
        this.circle = circle;
    }


    public void collideX(int moveX)
    {
        char[] tileZ = {'Z', 'Z', 'Z', 'Z'};   // char array not string - has to be mutable
        int newX = posX;
        if (posX % imageSize != 0)   // overlap tiles in X direction
        {
            if (moveX < posX)   // going left
            {
                tileZ[0] = tmScene.posToTile(posX, posY).charAt(0);   // tile under top left corner
                if (posY % imageSize != 0)   // overlap tiles in Y direction
                {
                    tileZ[2] = tmScene.posToTile(posX, posY + imageSize).charAt(0);   // bottom left tile
                }
                newX = (int) (posX / imageSize + 1) * imageSize;
            }
            else if (moveX > posX)   // going right
            {
                tileZ[1] = tmScene.posToTile(posX + imageSize, posY).charAt(0);   // tile under top right corner
                if (posY % imageSize != 0)   // overlap tiles in Y direction)
                {
                    tileZ[3] = tmScene.posToTile(posX + imageSize, posY + imageSize).charAt(0);   // bottom right tile
                }
                newX = (int) (posX / imageSize) * imageSize;
            }
            for (int ndx = 0; ndx < 4; ndx++)
            {
                if (tileZ[ndx] <= Zchar)   // collision!
                {
                    posX = newX;
                    speedX = 0;
                    break;
                }
            }
            int[] turnBorder;
            turnBorder = circle.getCenter();
            if (turnBorder[0] + (turnBorder[2]-1)*16 <= posX || turnBorder[0] - (turnBorder[2]-1)*16 >= posX)
            {
                if(moveX < posX)
                    newX = (int) (posX / imageSize + 1)* imageSize;
                else if (moveX > posX)
                    newX = (int) (posX / imageSize)* imageSize;
                posX = newX;
                speedX = 0;
            }
        }
    }

    // check for collision in Y direction, and correct
    public void collideY(int moveY)
    {
        char[] tileZ = {'Z', 'Z', 'Z', 'Z'};   // char array not string - has to be mutable
        int newY = posY;
        if (posY % imageSize != 0)   // overlap tiles in Y direction
        {
            if (moveY < posY)   // going up
            {
                tileZ[0] = tmScene.posToTile(posX, posY).charAt(0);   // tile under top left corner
                if (posX % imageSize != 0)   // overlap tiles in X direction
                {
                    tileZ[1] = tmScene.posToTile(posX + imageSize, posY).charAt(0);   // top right tile
                }
                newY = (int) (posY / imageSize + 1) * imageSize;
            }
            else if (moveY > posY)   // going down
            {
                tileZ[2] = tmScene.posToTile(posX, posY + imageSize).charAt(0);   // bottom left tile
                if (posX % imageSize != 0)   // overlap tiles in X direction)
                {
                    tileZ[3] = tmScene.posToTile(posX + imageSize, posY + imageSize).charAt(0);   // bottom right tile
                }
                newY = (int) (posY / imageSize) * imageSize;
            }
            char minZ = 'Z';   // stage 4
            boolean collision = false;   // stage 4
            for (int ndx = 0; ndx < 4; ndx++)
            {
                if (tileZ[ndx] <= Zchar)   // stage 4
                {
                    collision = true;
                }
                if (tileZ[ndx] < minZ)   // stage 4
                {
                    minZ = tileZ[ndx];
                }
            }
            if (collision)   // stage 4
            {
                posY = newY;
                speedY = 0;
                isJumping = false;
                Zchar = minZ;
            }
            int[] turnBorder;
            turnBorder = circle.getCenter();
            if (turnBorder[1] + (turnBorder[2]-1)*16 <= posY || turnBorder[1] - (turnBorder[2]-1)*16 >= posY)
            {
                if(moveY < posY)
                    newY = (int) (posY / imageSize + 1)* imageSize;
                else if (moveY > posY)
                    newY = (int) (posY / imageSize)* imageSize;
                posY = newY;
                speedY = 0;
            }
        }
    }

    public boolean collide(Chest thisChest)   // stage 5
    {
        int distX = this.posX - thisChest.getSceneX();
        int distY = this.posY - thisChest.getSceneY();
        double distance = Math.sqrt(distX * distX + distY * distY);
        return (distance < imageSize);
    }

    public boolean checkBoundsX()   // stage 5
    {
        boolean moveOK = true;
        int sceneWidth = tmScene.getWidth();
        if (posX < 0)
        {
            posX = 0;
        }
        if (posX > sceneWidth - imageSize)
        {
            posX = sceneWidth - imageSize;
            isJumping = false;
            moveOK = false;
        }
        return moveOK;
    }

    public boolean checkBoundsY()   // stage 5
    {
        boolean moveOK = true;
        int height = tmScene.getHeight();
        if (posY < 0)
        {
            posY = 0;
        }
        if (posY > height - imageSize/2)
        {
            posY = height - imageSize/2;
            isJumping = false;
            moveOK = false;
        }
        return moveOK;
    }
}
