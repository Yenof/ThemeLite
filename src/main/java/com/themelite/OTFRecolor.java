package com.themelite;


import net.runelite.client.ui.ColorScheme;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public class OTFRecolor { // On The Fly Recolor is a slightly stripped down DeepRecolor, aimed to refresh only the config panel elements as quickly as possible.
    private static ThemeLiteConfig config;

    public OTFRecolor(ThemeLiteConfig config) {OTFRecolor.config = config;}

    static void OTFDeepRecolor(Component comp, ThemeLiteConfig config) {
        if (comp instanceof AbstractButton) {

            AbstractButton button = (AbstractButton) comp;
            for (PropertyChangeListener l : button.getPropertyChangeListeners("icon")) { // This removes any listener placed by Helper or Recolorer.
                button.removePropertyChangeListener("icon", l);
            }

            if (button.getIcon() instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) button.getIcon();
                Color color;
                Component parent = button.getParent();
                if (parent != null && parent.getClass().getSimpleName().equals("ClientToolbarPanel")) {
                    color = config.toolbarButtonsColor() != null ? config.toolbarButtonsColor() : config.theme().getColor1();
                    Image recolored = Helper.recolorImage(icon.getImage(), color);
                    if (config.toolbarButtonsColor() != null || config.theme() != ThemeLiteConfig.Themes.NONE) {
                        button.setIcon(new ImageIcon(recolored));
                    }
                } else {
                    color = config.inactiveButtons() != null ? config.inactiveButtons() : (config.theme() != ThemeLiteConfig.Themes.NONE ? config.theme().getColor2() : null);
                    Image imageToUse = icon.getImage();
                    if (color != null) {
                        imageToUse = Helper.recolorImage(icon.getImage(), color);
                    }
                    button.setIcon(new ImageIcon(imageToUse));
                }

                if (button.getSelectedIcon() instanceof ImageIcon) {
                    color = config.selectedButtons() != null ? config.selectedButtons() : (config.theme() != ThemeLiteConfig.Themes.NONE ? config.theme().getColor4() : null);
                    ImageIcon selectedIcon = (ImageIcon) button.getSelectedIcon();
                    Image recoloredSelected = Helper.recolorImage(selectedIcon.getImage(), config.selectedButtons() != null ? config.selectedButtons() : (config.theme() != ThemeLiteConfig.Themes.NONE ? config.theme().getColor4() : null));
                    if (color != null) {
                        button.setSelectedIcon(new ImageIcon(recoloredSelected));
                    }
                }
            }
            if (button.getToolTipText() != null && button.getToolTipText().equals("Edit plugin configuration")) {
                final long[] lastRecolorTime = {0};
                    button.addPropertyChangeListener("icon", evt -> { // Without this the config buttons switch to default when clicked.
                        String prop = evt.getPropertyName();
                        if ("icon".equals(prop)) {
                            long now = System.currentTimeMillis();
                            if (now - lastRecolorTime[0] > 250) { // Needs this cooldown or it loops.
                                lastRecolorTime[0] = now;
                                if (button.getIcon() instanceof ImageIcon) {
                                    ImageIcon icon = (ImageIcon) button.getIcon();
                                    Color color = (config.inactiveButtons() != null ? config.inactiveButtons() : config.theme().getColor2());
                                    Image recolored = Helper.recolorImage(icon.getImage(), color);
                                    if (config.inactiveButtons() != null || config.theme() != ThemeLiteConfig.Themes.NONE){

                                    button.setIcon(new ImageIcon(recolored));}
                                    if (config.forceConfigPanelColor()){
                                        SwingUtilities.invokeLater(() -> {
                                            for (Window window : Window.getWindows())
                                                OTFRecolor.OTFDeepRecolor(window, config);
                                        });

                                    }

                                }
                            }
                        }
                        button.repaint();
                    });
                button.repaint();
                return;
            }
            button.repaint();
        }
        if (comp instanceof JLabel && !comp.getClass().getName().contains("FlatTitlePane")) {
            JLabel label = (JLabel) comp;
            String path = "";
            int panelCountBeforeLabel = 0;
            boolean countingPanels = true;
            Component current = comp;

            while (current != null) {
                path = current.getClass().getSimpleName() + "/" + path;
                if (countingPanels) {
                    if (current.getClass().getSimpleName().equals("JPanel")) {
                        panelCountBeforeLabel++;
                    } else if (!current.getClass().getSimpleName().equals("JLabel")) {
                        countingPanels = false;
                    }
                }
                current = current.getParent();
            }

            Color color;
            if (path.contains("ConfigPanel") && path.endsWith("JPanel/JLabel/") && panelCountBeforeLabel == 2) {
                if (config.configSectionColor() != null) {
                    color = config.configSectionColor();
                } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                    color = config.theme().getColor2();
                } else {
                    color = ColorScheme.BRAND_ORANGE;
                }
            } else {
                if (config.sidePanelFontColor() != null) {
                    color = config.sidePanelFontColor();
                } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                    color = config.theme().getColor1();
                } else {
                    color = Color.WHITE;
                }
            }

            label.setForeground(color);

            if (label.getFont() != null) {
                int style = label.getFont().getStyle();

                if (panelCountBeforeLabel == 2) {
                    style = style | Font.BOLD;
                } else if (config.sidePanelBold()) {
                    style = style | Font.BOLD;
                }

                label.setFont(label.getFont().deriveFont(style, (float) config.sidePanelFontSize()));
            }
        }
        if (comp instanceof Container) // Very needed bit
        {
            Component[] children = ((Container) comp).getComponents();
            for (Component child : children)
            {
                OTFDeepRecolor(child, config);
            }
        }
    }

}