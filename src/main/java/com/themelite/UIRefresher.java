package com.themelite;

import javax.swing.*;
import java.awt.*;

public class UIRefresher
{
    public static void refreshAll()
    {
        for (Window window : Window.getWindows()) // Very important.
        {
            SwingUtilities.updateComponentTreeUI(window);
            repaintAll(window);
            window.invalidate();
            window.validate();
            window.repaint();
        }
    }
    private static void repaintAll(Component component)
    {
        component.repaint();

        if (component instanceof Container)
        {
            for (Component child : ((Container) component).getComponents())
            {
                repaintAll(child);
            }
        }
    }
}
