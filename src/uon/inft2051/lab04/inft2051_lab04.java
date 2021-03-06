package uon.inft2051.lab04;

import static com.codename1.ui.CN.*;


import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.*;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.layouts.BorderLayout;

import java.io.IOException;


public class inft2051_lab04 {

    private Form current;
    private Resources theme;
    private BorderLayout blMain;

    private float calculateDPI() {
        //calculateDPI function is an edit version of calculateDPI from https://www.codenameone.com/manual/appendix-game.html
        float pixels = (Display.getInstance().getDisplayWidth());
        System.out.println("Height: " + Display.getInstance().getDisplayHeight() + " Width: " + Display.getInstance().getDisplayWidth());
        return pixels;
    }

    public void init(Object context)
    {
        theme = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        blMain = new BorderLayout();
        Form hi = new Form(blMain);
        Toolbar tb = hi.getToolbar();
        tb.setUIID("Container");
        tb.hideToolbar();
        GameComponent gc = new GameComponent();
        levelSelect(gc,hi);
    }

    private void levelSelect(GameComponent gc, Form hi)
    {
        gc.setScaleImages(calculateDPI());
        gc.setLevelNo(1);
        gc.initialize("");
        hi.add(BorderLayout.CENTER, gc);
        hi.show();
        hi.getComponentForm().registerAnimated(gc);
    }

    public void stop()
    {
        current = getCurrentForm();
        if (current instanceof Dialog)
        {
            ((Dialog) current).dispose();
            current = getCurrentForm();
        }
    }

    public void destroy()
    {
    }

}
