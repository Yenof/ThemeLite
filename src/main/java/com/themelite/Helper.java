package com.themelite;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Helper extends PluginPanel {
    private final ThemeLiteConfig config;
    static final Logger logger = LoggerFactory.getLogger(Helper.class);

    public Helper(ThemeLiteConfig config) {
        this.config = config;
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

    public static void reorder(ThemeLiteConfig config) {
        for (Window window : Window.getWindows()) {
            SwingUtilities.invokeLater(() -> reorderTabbedPanes(window, config));
        }
    }

    static final Map<JTabbedPane, List<TabData>> originalTabOrder = new WeakHashMap<>();

    static final Map<JTabbedPane, List<TabData>> tabMemory = new WeakHashMap<>();
    static final Map<String, Image> originalIcons = new HashMap<>();

        private static class TabData {
        String title;
        Component component;
        Icon icon;
        String tooltip;
        int originalIndex;

        TabData(String title, Component component, Icon icon, String tooltip, int originalIndex) {
            this.title = title;
            this.component = component;
            this.icon = icon;
            this.tooltip = tooltip;
            this.originalIndex = originalIndex;
        }
    }

    private static void reorderTabbedPanes(Container container, ThemeLiteConfig config) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) comp;
                tabMemory.remove(tabbedPane);
                List<TabData> allTabs = new ArrayList<>();
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    String tooltip = tabbedPane.getToolTipTextAt(i);
                    Icon icon = tabbedPane.getIconAt(i);
                    if (tooltip != null && icon instanceof ImageIcon) {
                        Image img = ((ImageIcon) icon).getImage();
                        originalIcons.putIfAbsent(tooltip, img);
                    }
                    allTabs.add(new TabData(
                            tabbedPane.getTitleAt(i),
                            tabbedPane.getComponentAt(i),
                            icon,
                            tooltip,
                            i
                    ));
                }
                tabMemory.put(tabbedPane, allTabs);

                if (!originalTabOrder.containsKey(tabbedPane)) {
                    originalTabOrder.put(tabbedPane, new ArrayList<>(allTabs));
                }

                List<TabData> tabs = new ArrayList<>(tabMemory.get(tabbedPane));
                Component selected = tabbedPane.getSelectedComponent();

                Map<String, String> iconPosition = new HashMap<>();
                for (String entry : config.iconOrder().split(",")) {
                    String[] parts = entry.trim().split(":");
                    if (parts.length == 2) {
                        if(parts[0].trim().equals("Configuration")){ // If the config button isn't #1, config profiles break.
                            parts[0] = "don'tMoveTheConfigButton";
                        }
                        if(parts[1].contains("-")){
                            parts[1] = String.valueOf(0);
                        }
                        iconPosition.put(parts[0].trim(), parts[1].trim());
                    }
                }

                //List<TabData> filteredTabs = tabs.stream() // If anyone can get this working that would be so cool.
                      //  .filter(tab -> { // I get stuff like IndexOutOfBoundsException: Index: 8, Size: 7 when trying to add a plugin after hiding an icon.
                      //      String pos = iconPosition.get(tab.tooltip); // Maybe if somehow I could tell the client when I remove an index????
                      //      return !("!".equals(pos) && !"Configuration".equals(tab.tooltip)); // Invisible and unclickable icons didn't work great when switching profiles.
                      //  })
                      //  .collect(Collectors.toList());

                List<TabData> filteredTabs = new ArrayList<>(tabs);
                filteredTabs.sort(Comparator
                        .comparingInt((TabData tab) -> {
                            String pos = iconPosition.get(tab.tooltip);
                            if (pos == null) {
                                List<TabData> original = originalTabOrder.get(tabbedPane);
                                for (int i = 0; i < original.size(); i++) {
                                    TabData origTab = original.get(i);
                                    if (Objects.equals(origTab.tooltip, tab.tooltip)) {
                                        return i;
                                    }
                                }
                                return tab.originalIndex;
                            }
                            try {
                                return Integer.parseInt(pos);
                            } catch (NumberFormatException e) {
                                return tab.originalIndex;
                            }
                        })
                        .thenComparingInt(t -> t.originalIndex)
                );

                //tabbedPane.removeAll();
                for (TabData tab : filteredTabs) {
                    Icon iconToUse = tab.icon;
                    if (tab.tooltip != null && originalIcons.containsKey(tab.tooltip)) {
                        Image original = originalIcons.get(tab.tooltip);
                        Image scaled = original.getScaledInstance(config.iconSize(), config.iconSize(), Image.SCALE_AREA_AVERAGING);
                        iconToUse = new ImageIcon(scaled);
                    }

                    tabbedPane.addTab(tab.title, iconToUse, tab.component);
                    tabbedPane.setToolTipTextAt(tabbedPane.getTabCount() - 1, tab.tooltip);
                }

                if (selected != null) {
                    filteredTabs.stream()
                            .filter(tab -> tab.component == selected)
                            .findFirst()
                            .ifPresent(tab -> tabbedPane.setSelectedComponent(tab.component));
                }
            }

            if (comp instanceof Container) {
                reorderTabbedPanes((Container) comp, config);
            }
        }
    }
}

