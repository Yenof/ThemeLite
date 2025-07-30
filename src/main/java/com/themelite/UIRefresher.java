package com.themelite;

import javax.swing.*;
import java.awt.*;

public class UIRefresher
{
    public static void refreshAll()
    {
        for (Window window : Window.getWindows()) // Very important.
        {
            UIManager.put("TextField.border", BorderFactory.createEmptyBorder()); // I have no idea why it creates a border around the config search without this
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
