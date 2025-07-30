package com.themelite;

import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class Recolorer {
    private static ThemeLiteConfig config;

    public Recolorer(ThemeLiteConfig config) {
        Recolorer.config = config;}

    public static void applyColors(ThemeLiteConfig config) {
        ThemeLiteConfig.Themes selectedTheme = config.theme();
        if (selectedTheme != null) {
              if (config.titleBarTextColor() != null) {
                  UIManager.put("TitlePane.foreground", config.titleBarTextColor());
                  UIManager.put("TitlePane.activeForeground", config.titleBarTextColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TitlePane.foreground", selectedTheme.getColor1());
                  UIManager.put("TitlePane.activeForeground", selectedTheme.getColor1());
              } else {
                  UIManager.put("TitlePane.foreground", ColorScheme.TEXT_COLOR);
                  UIManager.put("TitlePane.activeForeground", ColorScheme.TEXT_COLOR);
              }

              if (config.titlebarColor() != null) {
                  UIManager.put("TitlePane.background", config.titlebarColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TitlePane.background", selectedTheme.getColor3());
              } else {
                  UIManager.put("TitlePane.background", ColorScheme.CONTROL_COLOR);
              }

              if (config.titleBarInactiveBackground() != null) {
                  UIManager.put("TitlePane.inactiveBackground", config.titleBarInactiveBackground());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TitlePane.inactiveBackground", selectedTheme.getColor3());
              } else {
                  UIManager.put("TitlePane.inactiveBackground", ColorScheme.CONTROL_COLOR);
              }

              if (config.titleBarInactiveTextColor() != null) {
                  UIManager.put("TitlePane.inactiveForeground", config.titleBarInactiveTextColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TitlePane.inactiveForeground", selectedTheme.getColor1());
              } else {
                  UIManager.put("TitlePane.inactiveForeground", ColorScheme.LIGHT_GRAY_COLOR.darker());
              }

              if (config.selectedTabBackground() != null) {
                  UIManager.put("TabbedPane.selectedBackground", config.selectedTabBackground());
                  UIManager.put("TabbedPane.disabledBackground", config.selectedTabBackground());
                  UIManager.put("TabbedPane.disabledForeground", config.selectedTabBackground());
                  UIManager.put("TabbedPane.focusColor", config.selectedTabBackground());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TabbedPane.selectedBackground", selectedTheme.getColor3());
                  UIManager.put("TabbedPane.disabledBackground", selectedTheme.getColor3());
                  UIManager.put("TabbedPane.disabledForeground", selectedTheme.getColor3());
                  UIManager.put("TabbedPane.focusColor", selectedTheme.getColor3());
              } else {
                  UIManager.put("TabbedPane.selectedBackground", null);
                  UIManager.put("TabbedPane.disabledBackground", null);
                  UIManager.put("TabbedPane.disabledForeground", ColorScheme.LIGHT_GRAY_COLOR.darker());
                  UIManager.put("TabbedPane.focusColor", null);
              }

              if (config.selectionLineColor() != null) {
                  UIManager.put("TabbedPane.underlineColor", config.selectionLineColor());
                  UIManager.put("TabbedPane.inactiveUnderlineColor", config.selectionLineColor());
                  UIManager.put("TabbedPane.disabledUnderlineColor", config.selectionLineColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("TabbedPane.underlineColor", selectedTheme.getColor6());
                  UIManager.put("TabbedPane.inactiveUnderlineColor", selectedTheme.getColor6());
                  UIManager.put("TabbedPane.disabledUnderlineColor", selectedTheme.getColor6());
              } else {
                  UIManager.put("TabbedPane.underlineColor", ColorScheme.BRAND_ORANGE);
                  UIManager.put("TabbedPane.inactiveUnderlineColor", ColorScheme.BRAND_ORANGE);
                  UIManager.put("TabbedPane.disabledUnderlineColor", ColorScheme.BRAND_ORANGE);
              }

              if (config.scrollBarColor() != null) {
                  UIManager.put("ScrollBar.thumb", config.scrollBarColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("ScrollBar.thumb", selectedTheme.getColor1());
              } else {
                  UIManager.put("ScrollBar.thumb", ColorScheme.MEDIUM_GRAY_COLOR);
              }

              if (config.scrollBarTrackColor() != null) {
                  UIManager.put("ScrollBar.track", config.scrollBarTrackColor());
              } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                  UIManager.put("ScrollBar.track", selectedTheme.getColor3());
              } else {
                  UIManager.put("ScrollBar.track", ColorScheme.SCROLL_TRACK_COLOR);
              }

              if (config.sidebarThickness() != 0){
                  UIManager.put("TabbedPane.minimumTabWidth", config.sidebarThickness());
              }

              if (config.iconAlignment()){
                  UIManager.put("TabbedPane.tabAreaAlignment", "center");
              } else {
                  UIManager.put("TabbedPane.tabAreaAlignment", "leading");

              }
              Font defaultTitleFont = UIManager.getFont("TitlePane.font");
              if (defaultTitleFont != null)
              {
                  UIManager.put("TitlePane.font", defaultTitleFont.deriveFont((float) config.titleBarFontSize()));
                  if (config.titleBarTextColor() != null) {
                      UIManager.put("TitlePane.foreground", config.titleBarTextColor());
                  } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                      UIManager.put("TitlePane.foreground", selectedTheme.getColor1());
                  } else {
                      UIManager.put("TitlePane.foreground", Color.WHITE);
                  }
              }
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

    public static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    static Map<String, Image> originalIcons = new HashMap<>();
    static void deepRecolor(Component comp, ThemeLiteConfig config) {
        if (comp.getClass().getName().contains("ClientToolbarPanel")) {
            Color bg;
            if (config.titleBarToolbarColor() != null) {
                bg = config.titleBarToolbarColor();
            } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                bg = config.theme().getColor3();
            } else {
                bg = ColorScheme.CONTROL_COLOR;
            }
            comp.setBackground(bg);
        }

        if (comp.getClass().getName().contains("JTabbedPane")) {
            Color bg;
            if (config.sidebarBackgroundColor() != null) {
                bg = config.sidebarBackgroundColor();
            } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                bg = config.theme().getColor3();
            } else {
                bg = ColorScheme.CONTROL_COLOR;
            }
            comp.setBackground(bg);

            if (comp instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) comp;

                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    Icon icon = tabbedPane.getIconAt(i);
                    String tooltip = tabbedPane.getToolTipTextAt(i);

                    if (icon instanceof ImageIcon && tooltip != null) {
                        ImageIcon imageIcon = (ImageIcon) icon;

                        originalIcons.putIfAbsent(tooltip, imageIcon.getImage());

                        Image original = originalIcons.get(tooltip);
                        int size = config.iconSize();
                        Image scaled = original.getScaledInstance(size, size, Image.SCALE_AREA_AVERAGING);

                        tabbedPane.setIconAt(i, new ImageIcon(scaled));
                    }
                }
            }
        }

        if (comp instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) comp;
            Component parent = button.getParent();

            for (PropertyChangeListener l : button.getPropertyChangeListeners("icon")) {
                button.removePropertyChangeListener("icon", l);
            }

            if (button.getIcon() instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) button.getIcon();
                Color color;

                if (parent != null && parent.getClass().getSimpleName().equals("ClientToolbarPanel")) {
                    if (config.toolbarButtonsColor() != null) {
                        color = config.toolbarButtonsColor();
                    } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                        color = config.theme().getColor1();
                    } else {
                        color = Color.WHITE;
                    }
                } else {
                    if (config.inactiveButtons() != null) {
                        color = config.inactiveButtons();
                    } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                        color = config.theme().getColor2();
                    } else {
                        color = new Color(92, 92, 92, 255);
                    }
                }

                Image recolored = recolorImage(icon.getImage(), color);
                button.setIcon(new ImageIcon(recolored));
            }

            if (button.getText() != null && button.getText().trim().equals("×")) {
                ActionListener listener = e -> {
                    Timer timer = new Timer(200, evt -> {
                        SwingUtilities.invokeLater(() -> {
                            for (Window window : Window.getWindows()) {
                                OTFRecolor.OTFDeepRecolor(window, config);
                            }
                        });
                    });
                    timer.setRepeats(false);
                    timer.start();
                };
                button.putClientProperty("xListener", listener);
                button.addActionListener(listener);
            }

            if (button.getToolTipText() != null) {
                if (button.getToolTipText().equals("Sign out of RuneLite")) {
                    if (button.getIcon() instanceof ImageIcon) {
                        Color color;
                        if (config.toolbarButtonsColor() != null) {
                            color = config.toolbarButtonsColor();
                        } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                            color = config.theme().getColor1();
                        } else {
                            color = new Color(225, 36, 36, 255);
                        }
                        Image recolored = recolorImage(((ImageIcon) button.getIcon()).getImage(), color);
                        button.setIcon(new ImageIcon(recolored));
                    }
                } else if (button.getToolTipText().equals("Sign in to RuneLite")) {
                    if (button.getIcon() instanceof ImageIcon) {
                        Color color;
                        if (config.toolbarButtonsColor() != null) {
                            color = config.toolbarButtonsColor();
                        } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                            color = config.theme().getColor1();
                        } else {
                            color = new Color(116, 184, 98, 255);
                        }

                        Image recolored = recolorImage(((ImageIcon) button.getIcon()).getImage(), color);
                        button.setIcon(new ImageIcon(recolored));
                    }
                } else if (button.getToolTipText().equals("Edit plugin configuration")) {
                    Color color;
                    if (config.inactiveButtons() != null) {
                        color = config.inactiveButtons();
                    } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                        color = config.theme().getColor2();
                    } else {
                        color = Color.WHITE;
                    }

                    if (color != null && button.getIcon() instanceof ImageIcon) {
                        Image recolored = recolorImage(((ImageIcon) button.getIcon()).getImage(), color);
                        button.setIcon(new ImageIcon(recolored));
                    }

                    final long[] lastRecolorTime = {0};
                    if (config.forceConfigPanelColor()) {
                        button.addPropertyChangeListener("icon", evt -> { // Without this the config buttons switch to default when clicked.
                            long now = System.currentTimeMillis();
                            if (now - lastRecolorTime[0] > 250) {
                                lastRecolorTime[0] = now;
                                if (button.getIcon() instanceof ImageIcon) {
                                    ImageIcon icon = (ImageIcon) button.getIcon();
                                    Image recolored = Helper.recolorImage(icon.getImage(), color);
                                    button.setIcon(new ImageIcon(recolored));
                                    if (config.forceConfigPanelColor()){
                                        SwingUtilities.invokeLater(() -> {
                                            for (Window window : Window.getWindows())
                                                OTFRecolor.OTFDeepRecolor(window, config); // This recolors the plugin config panel on the fly.
                                        });

                                    }

                                }
                            }
                            button.repaint();
                        });
                    }
                }
            }

            if (button.getSelectedIcon() instanceof ImageIcon) {
                Color color;
                if (config.selectedButtons() != null) {
                    color = config.selectedButtons();
                } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                    color = config.theme().getColor4();
                } else {
                    color = ColorScheme.BRAND_ORANGE;
                }
                Image recolored = recolorImage(((ImageIcon) button.getSelectedIcon()).getImage(), color);
                button.setSelectedIcon(new ImageIcon(recolored));
            }
            button.repaint();
        }

        if (comp instanceof JLabel && !comp.getClass().getName().contains("FlatTitlePane")) {
            JLabel label = (JLabel) comp;
            String path = "";
            int panelCountBeforeLabel = 0; // Counts the JPanels to determine what is a config group. 1 or 3 JPanels deep means config item, 2 means section.
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
                    color = config.theme().getColor5();
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
                int newStyle;

                if (panelCountBeforeLabel == 2 || config.sidePanelBold()) {
                    newStyle = Font.BOLD;
                } else {
                    newStyle = Font.PLAIN;
                }

                if (label.getFont().getStyle() != newStyle) {
                    label.setFont(label.getFont().deriveFont(newStyle, (float) config.sidePanelFontSize()));
                }
                label.setFont(label.getFont().deriveFont(label.getFont().getStyle(), (float) config.sidePanelFontSize()));
            }

        }


            if (comp instanceof JComponent && comp.getClass().getName().contains("JPanel")) {
            JComponent j = (JComponent) comp;

            for (PropertyChangeListener l : j.getPropertyChangeListeners("border")) {
                j.removePropertyChangeListener("border", l);
            }

            Color color;
            if (config.innerBorder() != null) {
                color = config.innerBorder();
            } else if (config.theme() != ThemeLiteConfig.Themes.NONE) {
                color = config.theme().getColor3();
            } else {
                color = ColorScheme.DARKER_GRAY_COLOR;
            }

            Border b = j.getBorder();
            if (b instanceof MatteBorder) {
                MatteBorder mb = (MatteBorder) b;
                j.setBorder(BorderFactory.createMatteBorder(
                        mb.getBorderInsets().top,
                        mb.getBorderInsets().left,
                        mb.getBorderInsets().bottom,
                        mb.getBorderInsets().right,
                        color
                ));

                final long[] lastRecolorTime = {0};
                j.addPropertyChangeListener("border", evt -> { // Without this the border color reverts to normal everytime the client is minimized and restored.
                    long now = System.currentTimeMillis();
                    if (now - lastRecolorTime[0] > 70) {
                        lastRecolorTime[0] = now;
                        j.setBorder(BorderFactory.createMatteBorder(
                                mb.getBorderInsets().top,
                                mb.getBorderInsets().left,
                                mb.getBorderInsets().bottom,
                                mb.getBorderInsets().right,
                                color
                        ));
                    }
                });
            }
        }
        if (comp.getClass().getName().contains("MaterialTab")) {
            JComponent jc = (JComponent) comp;

            if ("Configuration".equals(jc.getToolTipText()) && jc.getClientProperty("xListener") == null) {
                ActionListener listener = e -> {
                    Timer timer = new Timer(200, evt -> {
                        SwingUtilities.invokeLater(() -> {
                            for (Window window : Window.getWindows()) {
                                OTFRecolor.OTFDeepRecolor(window, config);
                            }
                        });
                    });
                    timer.setRepeats(false);
                    timer.start();
                };

                jc.putClientProperty("xListener", listener);

                if (jc instanceof AbstractButton) {
                    ((AbstractButton) jc).addActionListener(listener);
                } else {
                    MouseListener ml = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            listener.actionPerformed(new ActionEvent(jc, ActionEvent.ACTION_PERFORMED, null));
                        }
                    };
                    jc.addMouseListener(ml);
                    jc.putClientProperty("xMouseListener", ml);
                }
            }
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                deepRecolor(child, config);
            }
        }
    }
}
