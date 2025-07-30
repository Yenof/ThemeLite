package com.themelite;

import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import static com.themelite.Helper.recolorImage;

public class ShutDowner { // ShutDowner.java is similar to Recolorer, however ShutDowner is for shutting down or resetting the plugin, and wipes all UI changes made by the plugin.
    public static void resetToDefaultColors() {

        UIManager.put("TitlePane.foreground", ColorScheme.TEXT_COLOR);
        UIManager.put("TitlePane.activeForeground", null);
        UIManager.put("TitlePane.background", ColorScheme.CONTROL_COLOR);
        UIManager.put("TitlePane.inactiveBackground", ColorScheme.CONTROL_COLOR);
        UIManager.put("TitlePane.inactiveForeground", ColorScheme.LIGHT_GRAY_COLOR.darker());


        UIManager.put("TabbedPane.selectedBackground", null);
        UIManager.put("TabbedPane.disabledBackground", null);
        UIManager.put("TabbedPane.disabledForeground", ColorScheme.LIGHT_GRAY_COLOR.darker());
        UIManager.put("TabbedPane.focusColor", null);
        UIManager.put("TabbedPane.underlineColor", ColorScheme.BRAND_ORANGE);
        UIManager.put("TabbedPane.inactiveUnderlineColor", ColorScheme.BRAND_ORANGE);


        UIManager.put("ScrollBar.thumb", ColorScheme.MEDIUM_GRAY_COLOR);
        UIManager.put("ScrollBar.track", ColorScheme.SCROLL_TRACK_COLOR);


        UIManager.put("TabbedPane.minimumTabWidth", null);
        UIManager.put("TabbedPane.tabAreaAlignment", "leading");

        UIManager.put("TitlePane.font", new FontUIResource("RuneScape", Font.BOLD, 16));

    }

    public static void deepRecolor(Component comp, ThemeLiteConfig config){
        if (comp == null) return;
        if (comp.getWidth() == 0 && comp.getHeight() == 0) return; // These two things make it like 15% faster to shut down.

        if (comp.getClass().getName().contains("ClientToolbarPanel"))
        {
            comp.setBackground(ColorScheme.CONTROL_COLOR);
        }
        if (comp.getClass().getName().contains("JTabbedPane")) {
            comp.setBackground(ColorScheme.CONTROL_COLOR);
            if (comp instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) comp;

                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    Icon icon = tabbedPane.getIconAt(i);
                    if (icon instanceof ImageIcon && config.iconSize() != 16) {
                        ImageIcon imageIcon = (ImageIcon) icon;
                        Image scaled = Recolorer.getScaledImage(imageIcon.getImage(), 16, 16);
                        tabbedPane.setIconAt(i, new ImageIcon(scaled));
                    }
                }
            }
        }
        if (comp.getClass().getName().contains("MaterialTab")) {
            JComponent jc = (JComponent) comp;

            if ("Configuration".equals(jc.getToolTipText())) {
                Object actionListener = jc.getClientProperty("xListener");
                if (actionListener instanceof ActionListener && jc instanceof AbstractButton) {
                    ((AbstractButton) jc).removeActionListener((ActionListener) actionListener);
                }

                Object mouseListener = jc.getClientProperty("xMouseListener");
                if (mouseListener instanceof MouseListener) {
                    jc.removeMouseListener((MouseListener) mouseListener);
                    jc.putClientProperty("xMouseListener", null);
                }

                jc.putClientProperty("xListener", null);
            }
        }

        if (comp instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) comp;
            for (PropertyChangeListener l : button.getPropertyChangeListeners("icon")) {
                button.removePropertyChangeListener("icon", l); // These removes the listeners placed by Helper or Recolorer.
            }
            if (button.getText() != null && button.getText().trim().equals("×")) {
                Object listener = button.getClientProperty("xListener");
                if (listener instanceof ActionListener) {
                    button.removeActionListener((ActionListener) listener);
                    button.putClientProperty("xListener", null);
                }
            }

            if (button.getIcon() instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) button.getIcon();
                Color color;
                Component parent = button.getParent();
                if (parent != null && parent.getClass().getSimpleName().equals("ClientToolbarPanel")) {
                    color = Color.white;
                } else {
                    color = new Color (92, 92, 92, 255); // This is what my color picker gets, RL uses ImageUtil.grayscaleImage(onSwitcher),0.61f on the switcher_on.png, this outcome seems the same.
                }
                Image recolored = recolorImage(icon.getImage(), (color));
                button.setIcon(new ImageIcon(recolored));
            }
            if (button.getToolTipText() != null && button.getToolTipText().equals("Sign out of RuneLite")) {
                if (button.getIcon() instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) button.getIcon();
                    Image recolored = recolorImage(icon.getImage(), new Color(225, 36, 36, 255));// Color from the sign-out.png
                    button.setIcon(new ImageIcon(recolored));
                }
                button.repaint();
                return;
            }
            if (button.getToolTipText() != null && button.getToolTipText().equals("Sign in to RuneLite")) {
                if (button.getIcon() instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) button.getIcon();
                    Image recolored = recolorImage(icon.getImage(), new Color(116, 184, 98, 255));
                    button.setIcon(new ImageIcon(recolored));
                }
                button.repaint();
                return;
            }
            if (button.getToolTipText() != null && button.getToolTipText().equals("Edit plugin configuration")) {
                for (PropertyChangeListener l : button.getPropertyChangeListeners("icon")) {
                    button.removePropertyChangeListener("icon", l);
                }
                if (button.getIcon() instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) button.getIcon();
                    Image recolored = recolorImage(icon.getImage(), Color.WHITE);
                    button.setIcon(new ImageIcon(recolored));
                }
                button.repaint();
                return;
            }
            if (button.getSelectedIcon() instanceof ImageIcon) {
                ImageIcon selectedIcon = (ImageIcon) button.getSelectedIcon();
                Image recoloredSelected = recolorImage(selectedIcon.getImage(), (ColorScheme.BRAND_ORANGE));
                button.setSelectedIcon(new ImageIcon(recoloredSelected));
            }

            button.repaint();
        }

        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            label.setForeground(Color.WHITE);
            Font defaultTitleFont = label.getFont();
            label.setFont(defaultTitleFont.deriveFont(Font.PLAIN, (float) 16));
        }

        if (comp instanceof JLabel && !comp.getClass().getName().contains("FlatTitlePane")) {
            JLabel label = (JLabel) comp;

            int panelCountBeforeLabel = 0;
            boolean countingPanels = true;
            Component current = comp;

            while (current != null) {
                if (countingPanels) {
                    if (current.getClass().getSimpleName().equals("JPanel")) {
                        panelCountBeforeLabel++;
                    } else if (!current.getClass().getSimpleName().equals("JLabel")) {
                        countingPanels = false;
                    }
                }
                current = current.getParent();
            }

            boolean isConfigSection = panelCountBeforeLabel == 2;
            label.setForeground(isConfigSection ? ColorScheme.BRAND_ORANGE : Color.WHITE);

            Font baseFont = label.getFont();
            if (baseFont != null) {
                int style = isConfigSection ? Font.BOLD : Font.PLAIN;
                label.setFont(baseFont.deriveFont(style, 16f));
            }
        }

        if (comp instanceof JComponent) {
            JComponent j = (JComponent) comp;
            Border b = j.getBorder();

            Border current = j.getBorder();
            if (current instanceof MatteBorder) {
                for (PropertyChangeListener l : j.getPropertyChangeListeners("border")) {
                    j.removePropertyChangeListener("border", l);
                }
                MatteBorder mb = (MatteBorder) current;
                if (!ColorScheme.DARKER_GRAY_COLOR.equals(mb.getMatteColor())) {
                    j.setBorder(BorderFactory.createMatteBorder(
                            mb.getBorderInsets().top,
                            mb.getBorderInsets().left,
                            mb.getBorderInsets().bottom,
                            mb.getBorderInsets().right,
                            ColorScheme.DARKER_GRAY_COLOR
                    ));
                }
            }
            if (b instanceof MatteBorder) {
                MatteBorder old = (MatteBorder) b;
                j.setBorder(BorderFactory.createMatteBorder(
                        old.getBorderInsets().top,
                        old.getBorderInsets().left,
                        old.getBorderInsets().bottom,
                        old.getBorderInsets().right,
                        ColorScheme.DARKER_GRAY_COLOR
                ));
            }
        }
        if (comp instanceof Container)
        {
            Component[] children = ((Container) comp).getComponents();
            for (Component child : children)
            {
                deepRecolor(child, config);
            }
        }
    }

}
