package uon.inft2051.lab04;

import com.codename1.ui.Display;

import java.io.InputStream;

import com.codename1.ui.Form;
import com.codename1.io.Util;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

import java.util.ArrayList;

// implementation of a simple Tile Map for 2D graphics
// loads scene definition from a comma separated file (CSV)
// renders the scene using square tiles from files named in the CSV
// by D. Cornforth April 2019
// some ideas from https://github.com/chen-fishbein/core2d-cn1lib/blob/master/core2d/src/com/nlcode/cn1/core/graphics/TileMap.java
// tiles from https://opengameart.org/content/generic-platformer-tiles

public class TileMap
{
    String[][] tileNames;
    int tileSize, spriteCols, spriteRows, width, height, border;
    float scale;
    Image spriteSheet;
    ArrayList<String> animats;

    public TileMap(int tileSize, float scale)
    {
        tileNames = null;
        this.tileSize = (int)(tileSize*scale);
        spriteCols = 0;
        spriteRows = 0;
        width = 0;
        height = 0;
        border = 0;
        spriteSheet = null;
        animats = new ArrayList<String>();
        this.scale = scale;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public ArrayList<String> getAnimats()
    {
        return animats;
    }

    public void loadScene(String filename)
    {
        String sText = "";
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, filename);
        if (in != null)
        {
            try
            {
                sText = com.codename1.io.Util.readToString(in);
                in.close();
                String[] lines = Util.split(sText, "\r\n");
                int valid_lines = lines.length;
                for (int row = 0; row < lines.length; row++)
                {
                    if (!lines[row].contains(","))
                    {
                        valid_lines = row;
                        break;
                    }
                }
                height = valid_lines * tileSize;
                tileNames = new String[valid_lines][];
                int maxTilesX = 0;
                for (int row = 0; row < tileNames.length; row++)
                {
                    String[] cells = Util.split(lines[row], ",");
                    tileNames[row] = new String[cells.length];
                    if (cells.length > maxTilesX)
                    {
                        maxTilesX = cells.length;
                    }
                    for (int col = 0; col < cells.length; col++)   // for each cell
                    {
                        if (cells[col].length() == 4 || cells[col].length() == 3)   // a valid background cell
                        {
                            tileNames[row][col] = cells[col];
                        }
                        else if (cells[col].length() == 5 || cells[col].length() == 6)   // a character starting position
                        {
                            tileNames[row][col] = "";
                            int startX = col * tileSize;
                            int startY = row * tileSize;
                            animats.add(cells[col] + ":" + startX + ":" + startY);
                            System.out.println("Add animat " + cells[col] + ":" + startX + ":" + startY);
                        }
                        else
                        {
                            tileNames[row][col] = "";
                        }
                    }
                }
                width = maxTilesX * tileSize;
            }
            catch (Exception ex)
            {
            }
        }
    }

    // load sprite sheet for background scenery
    public void loadImages(String filename, int border)
    {
        this.border = (int)(border*scale);
        try
        {
            spriteSheet = Image.createImage(filename);
        }
        catch (Exception exp)
        {
            spriteSheet = null;
        }
        int y = (int)(spriteSheet.getHeight()*scale);
        int x = (int)(spriteSheet.getWidth()*scale);
        spriteSheet.scale(x,y);
        spriteCols = spriteSheet.getWidth() / (tileSize + border);
        spriteRows = spriteSheet.getHeight() / (tileSize + border);
    }

    public void render(Graphics g, int offsetX, int offsetY)
    {
        for (int row = 0; row < tileNames.length; row++)
        {
            for (int col = 0; col < tileNames[row].length; col++)
            {
                String descriptor = tileNames[row][col];
                if (descriptor.length() > 0)
                {
                    int tilePosX = col * tileSize;   // the absolute position to draw this tile
                    int tilePoxY = row * tileSize;
                    g.setClip(tilePosX - offsetX, tilePoxY - offsetY, tileSize, tileSize);
                    int index = Integer.parseInt(descriptor.substring(1));
                    int indexX = index % spriteCols;   // which part of the sprite sheet to use
                    int indexY = index / spriteCols;
                    int renderX = tilePosX - indexX * (tileSize + border);
                    int renderY = tilePoxY - indexY * (tileSize + border);
                    g.drawImage(spriteSheet, renderX - offsetX, renderY - offsetY);
                }
            }
        }
    }

    public String posToTile(int x, int y)
    {
        String tileSpec = "Z";
        int indexX = x / tileSize;
        int indexY = y / tileSize;
        try
        {
            tileSpec = tileNames[indexY][indexX];
        }
        catch (Exception exp)
        {
            tileSpec = "Z";
        }
        if (tileSpec.length() == 0)
        {
            tileSpec = "Z";
        }
        return tileSpec;
    }
}
