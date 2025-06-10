package com.themelite;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.awt.*;

@Slf4j
@PluginDescriptor(
		name = "ThemeLite",
		description = "Recolors client UI elements",
		tags = {"Theme", "color", "custom", "chrome", "ThemeLite", "sidebar", "title", "bar", "UI", "skin", "recolor", "accessibility", "Kalil"}
)

public class ThemeLite extends Plugin
{

	public static final String CONFIG_GROUP = "ThemeLite";

	@Inject
	private Client client;

	@Inject
	private ThemeLiteConfig config;

	@Provides
	ThemeLiteConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ThemeLiteConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		applyColorFromConfig();
	}

	@Override
	protected void shutDown(){} // In future have this restore default colors maybe.

	@Subscribe
	public void onProfileChanged(ProfileChanged event) throws Exception {
		SwingUtilities.invokeLater(this::applyColorFromConfig);
	}

	private static final int CONFIG_UPDATE_DELAY_MS = 55; // Without all this maddness it takes like 25 sec to switch profiles with ThemeLite enabled.
	private final Timer configUpdateTimer = new Timer(CONFIG_UPDATE_DELAY_MS, e -> { // This brings it down to ~10 seconds.
		applyColorFromConfig();
	});
	{
		configUpdateTimer.setRepeats(false);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (CONFIG_GROUP.equals(event.getGroup()) || ThemeLite.CONFIG_GROUP.equals(event.getGroup())) {
			configUpdateTimer.restart();
		}
	}

	private void applyColorFromConfig() {
		Recolorer.applyColorToAllColors(config);
		Recolorer.applyCustomUIManagerSettings(config.customUIManagers());
		SwingUtilities.invokeLater(() -> {
			for (Window window : Window.getWindows()) {
				UIRefresher.refreshAll();
				Recolorer.forceDeepRecolor(window, config);
			}
		});
	}
}
