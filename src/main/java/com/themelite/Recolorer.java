package com.themelite;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Recolorer extends PluginPanel {

    private final ThemeLiteConfig config;
    private static final Logger logger = LoggerFactory.getLogger(Recolorer.class);

    public Recolorer(ThemeLiteConfig config) {
        this.config = config;
    }

    private static void setUIManager(String key, Object value) {
        if (value != null) {
            UIManager.put(key, value);
        }
    }

    public static void applyColorToAllColors(ThemeLiteConfig config) {
        if (config.theme() != ThemeLiteConfig.Themes.NONE)
        {
            ThemeHandler.applyColors(config);
        }

        setUIManager("TitlePane.foreground", config.titleBarTextColor()); // Title bar Title Text except for focused RuneLite, idk why. Exit buttons too.
        setUIManager("TitlePane.activeForeground", config.titleBarTextColor()); // Inactive text color
        setUIManager("TitlePane.background", config.titlebarColor()); // Active background
        setUIManager("TitlePane.inactiveBackground", config.titleBarInactiveBackground()); // Inactive background
        setUIManager("TitlePane.inactiveForeground", config.titleBarInactiveTextColor()); // Inactive text color


        setUIManager("TabbedPane.selectedBackground", config.selectedTabBackground());// Sidebar selected tab
        setUIManager("TabbedPane.disabledBackground", config.selectedTabBackground());
        setUIManager("TabbedPane.disabledForeground", config.selectedTabBackground()); // Seems to need all of this or sometimes colors reset.
        setUIManager("TabbedPane.focusColor", config.selectedTabBackground());
        setUIManager("TabbedPane.underlineColor", config.selectionLineColor());
        setUIManager("TabbedPane.inactiveUnderlineColor", config.selectionLineColor());
        setUIManager("TabbedPane.disabledUnderlineColor", config.selectionLineColor());


        setUIManager("ScrollBar.thumb", config.scrollBarColor()); // The scrollbar position indicator.
        setUIManager("ScrollBar.track", config.scrollBarTrackColor());// What I would consider the scrollbar background.


        if (config.sidebarThickness() != 0){
            setUIManager("TabbedPane.minimumTabWidth", config.sidebarThickness()); // minimum width of tabs.
        }
//        if (config.scrollingSidebar()){
//            setUIManagerProperty("TabbedPane.tabLayoutPolicy", "scroll"); // Scrollable sidebar, also entirely breaks the sidebar XD
//            setUIManagerProperty("TabbedPane.tabsPopupPolicy", "never"); // Would be a cool feature someday though.
//        } else {
//            setUIManagerProperty("TabbedPane.tabLayoutPolicy", "wrap");
//        }

        if (config.iconAlignment()){
            setUIManager("TabbedPane.tabAreaAlignment", "center");
        } else {
            setUIManager("TabbedPane.tabAreaAlignment", "leading");

        }
        Font defaultTitleFont = UIManager.getFont("TitlePane.font");
        if (defaultTitleFont != null)
        {
            setUIManager("TitlePane.font", defaultTitleFont.deriveFont((float) config.titleBarFontSize()));
            setUIManager("TitlePane.foreground", config.titleBarTextColor()); // Title bar Title Text color
        }

        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows())
            {
                UIRefresher.refreshAll();
                SwingUtilities.updateComponentTreeUI(window);
            }
        });
    }


    public static void applyCustomUIManagerSettings(String settings) {
        if (settings == null || settings.trim().isEmpty()) {
            return;
        }
        String settings2 = settings.trim();
        String[] pairs = settings2.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.trim().split(" ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                if (isValidColor(value)) {
                    UIManager.put(key, Color.decode(value));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid color setting: " + value, "Invalid UI Setting", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid setting: " + pair, "Invalid UI Setting", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static boolean isValidColor(String value) {
        if (value.startsWith("#")) {
            String hex = value.substring(1);
            return hex.matches("[0-9a-fA-F]{6}|[0-9a-fA-F]{8}");
        }
        return false;
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

    static void forceDeepRecolor(Component comp, ThemeLiteConfig config) {
        if (comp.getClass().getName().contains("ClientToolbarPanel") && config.titleBarToolbarColor() != null) // Title bar thing with screenshot, account, and chevron.
        {
            comp.setBackground(config.titleBarToolbarColor());
        }
        if (comp.getClass().getName().contains("JTabbedPane") && config.sidebarBackgroundColor() != null) { // Sidebar background
            comp.setBackground(config.sidebarBackgroundColor());
        }
        if (comp instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) comp;
            if (button.getIcon() instanceof ImageIcon && config.inactiveButtons() != null) {
                ImageIcon icon = (ImageIcon) button.getIcon();
                Image recolored = recolorImage(icon.getImage(), config.inactiveButtons()); // Deselected things, favorites, toggles. Turns back to default when selected sometimes.
                button.setIcon(new ImageIcon(recolored));
            }
            if (button.getSelectedIcon() instanceof ImageIcon && config.selectedButtons() != null) {
                ImageIcon selectedIcon = (ImageIcon) button.getSelectedIcon();
                Image recoloredSelected = recolorImage(selectedIcon.getImage(), config.selectedButtons());
                button.setSelectedIcon(new ImageIcon(recoloredSelected));
            }

            button.repaint();
        }
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            if (config.sidePanelFontColor() != null){
                label.setForeground(config.sidePanelFontColor());
            }
            Font defaultTitleFont = label.getFont();

            if (defaultTitleFont != null) {
                int currentStyle = defaultTitleFont.getStyle();
                if (config.sidePanelBold()) {
                    currentStyle = currentStyle | Font.BOLD;
                }
                label.setFont(defaultTitleFont.deriveFont(currentStyle, (float) config.sidePanelFontSize()));
            }

        }

        if (comp instanceof JComponent && config.innerBorder() != null) {
            JComponent j = (JComponent) comp;
            Border b = j.getBorder();
            j.addPropertyChangeListener("border", evt -> { // The border changes back to default colors when minimized and restored without this.
                Border current = j.getBorder();
                if (current instanceof MatteBorder) {
                    MatteBorder mb = (MatteBorder) current;
                    if (config.innerBorder() != null && !config.innerBorder().equals(mb.getMatteColor())) {
                        j.setBorder(BorderFactory.createMatteBorder(
                                mb.getBorderInsets().top,
                                mb.getBorderInsets().left,
                                mb.getBorderInsets().bottom,
                                mb.getBorderInsets().right,
                                config.innerBorder()
                        ));
                    }
                }
            });
            if (b instanceof MatteBorder) {
                MatteBorder old = (MatteBorder) b;
                j.setBorder(BorderFactory.createMatteBorder(
                        old.getBorderInsets().top,
                        old.getBorderInsets().left,
                        old.getBorderInsets().bottom,
                        old.getBorderInsets().right,
                        config.innerBorder()
                ));
            }
        }
        if (comp instanceof Container)
        {
            Component[] children = ((Container) comp).getComponents();
            for (Component child : children)
            {
                forceDeepRecolor(child, config);
            }
        }
    }
}

