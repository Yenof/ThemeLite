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
import net.runelite.client.plugins.PluginManager;

import static javax.swing.SwingUtilities.*;

@Slf4j
@PluginDescriptor(
		name = "ThemeLite",
		description = "Recolors client UI elements",
		tags = {"Theme", "color", "custom", "chrome", "accessibility", "ThemeLite", "sidebar", "title", "bar", "UI", "skin", "recolor", "Kalil", "icon", "icons", "resize", "reorder", "rearrange", "font", "size"}
)

public class ThemeLite extends Plugin
{
	@Inject
	private PluginManager pluginManager;
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
	protected void startUp() {
		applyColorFromConfig();
		Helper.tabMemory.clear();

		Helper.originalIcons.clear();


		iconUpdateTimer.restart();


	}

	@Override
	protected void shutDown() {
		for (Window window : Window.getWindows()) {
			ShutDowner.resetToDefaultColors();
			ShutDowner.deepRecolor(window, config);
			UIRefresher.refreshAll();
			ThemeLiteConfig fakeConfig = () -> {
				return ""; // Resets the plugin toolbar icon order to default
			};
			invokeLater(() -> {
				Helper.reorder(fakeConfig);
			});

		}
	}


	@Override
	public void resetConfiguration() {
			configUpdateTimer.restart();
	}

	@Subscribe
	public void onProfileChanged(ProfileChanged event) {
		if (pluginManager.isPluginEnabled(this)){
			Helper.tabMemory.clear();
			Helper.originalIcons.clear();

			configUpdateTimer.restart();
			iconUpdateTimer.restart();
			}
	}

	private static final int CONFIG_UPDATE_DELAY_MS = 600; // Without this it takes like 10 sec to switch profiles with ThemeLite enabled, it seems to refresh the UI for each config entry when switching.
	private final Timer configUpdateTimer = new Timer(CONFIG_UPDATE_DELAY_MS, e -> { // This brings it down to around 5 seconds.
		applyColorFromConfig();
	});
	{
		configUpdateTimer.setRepeats(false);
	}
	private final Timer iconUpdateTimer = new Timer(CONFIG_UPDATE_DELAY_MS, e -> {
		Helper.reorder(config);
	});
	{
		iconUpdateTimer.setRepeats(false);
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (pluginManager.isPluginEnabled(this) && ThemeLite.CONFIG_GROUP.equals(event.getGroup())) {
			if (event.getKey().equals("themes") && ThemeLiteConfig.Themes.NONE.name().equals(event.getNewValue())) {
				configUpdateTimer.restart();
			} if (event.getKey().equals("iconOrder")){
				iconUpdateTimer.restart();
			}
			else {
				configUpdateTimer.restart();
			}
		}else if (pluginManager.isPluginEnabled(this) && event.getKey().equals("externalPlugins"))
		{
			configUpdateTimer.restart(); // This is because when installing or uninstalling plugins it seems to refresh the configuration panel and TL colors would be lost without.
		} // If using the plugin filter dropdown when uninstalling
	}

	public void applyColorFromConfig() {
		Recolorer.applyColors(config);
		invokeLater(() -> {
			for (Window window : Window.getWindows()) {
				Recolorer.deepRecolor(window, config);
				UIRefresher.refreshAll();

			}
		});
	}
}
