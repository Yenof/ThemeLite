package com.themelite;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThemeHandler {
    private static ThemeLiteConfig config;

    public ThemeHandler(ThemeLiteConfig config) {
        ThemeHandler.config = config;
    }

    public static void applyColors(ThemeLiteConfig config) {
        ThemeLiteConfig.Themes selectedTheme = config.theme();
        if (selectedTheme != null) {
            UIManager.put("TitlePane.foreground", selectedTheme.getColor1());
            UIManager.put("TitlePane.activeForeground", selectedTheme.getColor1());
            UIManager.put("TitlePane.background", selectedTheme.getColor3());
            UIManager.put("TitlePane.inactiveBackground", selectedTheme.getColor3());
            UIManager.put("TitlePane.inactiveForeground", selectedTheme.getColor1());
            UIManager.put("TabbedPane.selectedBackground", selectedTheme.getColor3()); // Sidebar selected tab
            UIManager.put("TabbedPane.disabledBackground", selectedTheme.getColor3()); // If I don't set it in all these places the colors reset sometimes on focus.
            UIManager.put("TabbedPane.disabledForeground", selectedTheme.getColor3());
            UIManager.put("TabbedPane.focusColor", selectedTheme.getColor3());
            UIManager.put("TabbedPane.underlineColor", config.theme().getColor6());

            UIManager.put("TabbedPane.inactiveUnderlineColor", config.theme().getColor6());
            UIManager.put("TabbedPane.disabledUnderlineColor", config.theme().getColor6());


            UIManager.put("ScrollBar.thumb", config.theme().getColor1()); // The scrollbar position indicator.
            UIManager.put("ScrollBar.track", config.theme().getColor3());// What I would consider the scrollbar background.


            UIRefresher.refreshAll();
            SwingUtilities.invokeLater(() -> {
                for (Window window : Window.getWindows())
                {
                    UIRefresher.refreshAll();
                    forceDeepRecolor2(window, config);
                    SwingUtilities.updateComponentTreeUI(window);
                }
            });
        }
    }

    public static Image recolorImage(Image originalImage, Color color) {
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        BufferedImage recoloredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = recoloredImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return recoloredImage;
    }


    static void forceDeepRecolor2(Component comp, ThemeLiteConfig config) { // Probably combine in future with #1.
        if (comp.getClass().getName().contains("ClientToolbarPanel")) // Titlebar thing with screenshot, account, and sidebar chevron. (background)
        {
            comp.setBackground(config.theme().getColor3());
        }
        if (comp.getClass().getName().contains("JTabbedPane")) { // Sidebar background
            comp.setBackground(config.theme().getColor5());
        }
        if (comp instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) comp;
            if (button.getIcon() instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) button.getIcon();
                Color color;
                Component parent = button.getParent();
                if (parent != null && parent.getClass().getSimpleName().equals("ClientToolbarPanel")) {
                    color = config.theme().getColor1(); // titlebar toolbar buttons
                } else {
                    color = config.theme().getColor2(); // inactive sidepanel toggles
                }
                Image recolored = recolorImage(icon.getImage(), color);
                button.setIcon(new ImageIcon(recolored));
            }
            if (button.getSelectedIcon() instanceof ImageIcon) {
                ImageIcon selectedIcon = (ImageIcon) button.getSelectedIcon();
                Image recoloredSelected = recolorImage(selectedIcon.getImage(), config.theme().getColor4()); // Active toggles
                button.setSelectedIcon(new ImageIcon(recoloredSelected));
            }

            button.repaint();
        }
        if (comp instanceof JComponent) {
            JComponent j = (JComponent) comp;
            Border b = j.getBorder();

            j.addPropertyChangeListener("border", evt -> { // The border changes back to default colors when minimized and restored without this.
                Border current = j.getBorder();
                if (current instanceof MatteBorder && config.innerBorder() == null) {
                    MatteBorder mb = (MatteBorder) current;
                    if (config.theme().getColor3() != null && !config.theme().getColor3().equals(mb.getMatteColor())) {
                        j.setBorder(BorderFactory.createMatteBorder(
                                mb.getBorderInsets().top,
                                mb.getBorderInsets().left,
                                mb.getBorderInsets().bottom,
                                mb.getBorderInsets().right,
                                config.theme().getColor3()
                        ));
                    }
                }
            });

            if (b instanceof MatteBorder && config.innerBorder() == null) { // This 2nd config check is probably unnecessary.
                MatteBorder old = (MatteBorder) b;
                j.setBorder(BorderFactory.createMatteBorder(
                        old.getBorderInsets().top,
                        old.getBorderInsets().left,
                        old.getBorderInsets().bottom,
                        old.getBorderInsets().right,
                        config.theme().getColor3()
                ));
            }
        }
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            label.setForeground(config.theme().getColor1());
            Font defaultTitleFont = label.getFont();

            if (defaultTitleFont != null) {
                int currentStyle = defaultTitleFont.getStyle();
                if (config.sidePanelBold()) {
                    currentStyle = currentStyle | Font.BOLD;
                }
                label.setFont(defaultTitleFont.deriveFont(currentStyle, (float) config.sidePanelFontSize()));
            }
        }
        if (comp instanceof Container) // Very needed bit
        {
            Component[] children = ((Container) comp).getComponents();
            for (Component child : children)
            {
                forceDeepRecolor2(child, config);
            }
        }
    }

}
