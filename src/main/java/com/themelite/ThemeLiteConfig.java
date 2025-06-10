package com.themelite;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("ThemeLite")
public interface ThemeLiteConfig extends Config
{

	enum Themes {
		NONE("#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF"), // Exit buttons, text, titlebar background, inactive icons, sidebar background, underline... Roughly.
		CANDY("#199AE2", "#BA40A0", "#BA40A0", "#199AE2", "#BA40A0", "#199AE2"),
		SPOOKY("#DC8900", "#000000", "#000000", "#DC8900", "#000000", "#DC8900"),
		TOXIC("#16EA27", "#A600DD", "#53076C", "#16EA27", "#53076C", "#16EA27"),
		GUTHIX("#9D6B0F", "#9D6B0F", "#0B6100", "#0B6100", "#0B6100", "#9D6B0F"),
		SARADOMIN("#DDD10F", "#0056CF", "#0056CF", "#DDD10F", "#0056CF", "#DDD10F"),
		ZAMORAK("#AA0505", "#000000", "#000000", "#AA0505", "#000000", "#AA0505"),
		ZAROS("#8929FF", "#000000", "#000000", "#8929FF", "#000000", "#8929FF");


		private final Color color1;
		private final Color color2;
		private final Color color3;
		private final Color color4;
		private final Color color5;
		private final Color color6;

		Themes(String hex1, String hex2, String hex3, String hex4, String hex5, String hex6) {
			this.color1 = Color.decode(hex1);
			this.color2 = Color.decode(hex2);
			this.color3 = Color.decode(hex3);
			this.color4 = Color.decode(hex4);
			this.color5 = Color.decode(hex5);
			this.color6 = Color.decode(hex6);
		}

		public Color getColor1() {return color1;}
		public Color getColor2() {return color2;}
		public Color getColor3() {return color3;}
		public Color getColor4() {return color4;}
		public Color getColor5() {return color5;}
		public Color getColor6() {return color6;}

	}
	@ConfigItem(
			keyName = "warning",
			name = "Please be patient while recoloring.",
			description = "ThemeLite refreshes and recolors lots of UI elements, this can take a few moments, please be patient.",
			position = 0
	)
	default String[] warning()
	{
		return new String[0];
	}

	@ConfigSection(
			name = "Themes",
			description = "Pre-made themes",
			closedByDefault = false,
			position = 1
	)
	String themesSection = "Themes";


	@ConfigSection(
			name = "TitleBar",
			description = "Title Bar config items. (A) means Active, (I) means Inactive.",
			closedByDefault = true,
			position = 2
	)
	String titleBarSection = "titleBar";

	@ConfigSection(
			name = "SideBar",
			description = "Side Bar config items",
			closedByDefault = true,
			position = 4
	)
	String sidebarSection = "sideBar";

	@ConfigSection(
			name = "SidePanel",
			description = "Side Panel config items",
			closedByDefault = true,
			position = 8
	)
	String sidePanelSection = "sidePanel";

	@ConfigSection(
			name = "Extra",
			description = "Extra settings, oooooo!",
			closedByDefault = true,
			position = 15
	)
	String extraSection = "extra";

	@ConfigItem(
			keyName = "themes",
			name = "Theme",
			description = "Choose from pre-made themes. Other config entries apply after this, so you can modify theme colors.",
			section = themesSection,
			position = 2
	)
	default Themes theme() {
		return Themes.NONE;
	}

	@ConfigItem(
			keyName = "titleBarTextColor",
			name = "Window Buttons",
			description = "Color of the minimize, maximize, and exit buttons. Also title bar text color for sub-windows.",
			position = 2,
			section = titleBarSection
	)
	default Color titleBarTextColor()
	{
		return null;
	}

	@ConfigItem(
		keyName = "titleBarColor",
		name = "Title Bar Background (A)",
		description = "Active/focused Title bar Background color. Also affects child windows like Color Picker and Chat Panel.",
		position = 2,
		section = titleBarSection
	)
	default Color titlebarColor()
	{
		return null;
	}

	@ConfigItem(
			keyName = "titleBarInactiveBackground",
			name = "Title Bar Background (I)",
			description = "Color of the title bar when it's inactive.",
			position = 4,
			section = titleBarSection
	)
	default Color titleBarInactiveBackground()
	{
		return null;
	}

	@ConfigItem(
			keyName = "titleBarInactiveTextColor",
			name = "Title Bar Text (I)",
			description = "Color of the text inside the inactive title bar.",
			position = 7,
			section = titleBarSection
	)
	default Color titleBarInactiveTextColor()
	{
		return null;
	}


	@ConfigItem(
			keyName = "titleBarToolBarColor",
			name = "Toolbar",
			description = "Color of the toolbar inside the title bar. (Screenshot, Account, Sidebar button housing)",
			position = 9,
			section = titleBarSection
	)
	default Color titleBarToolbarColor()
	{
		return null;
	}

	@ConfigItem(
			keyName = "titleBarFontSize",
			name = "Title Bar Font Size",
			description = "Font used in the title bar.",
			position = 11,
			section = titleBarSection
	)
	default int titleBarFontSize()
	{
		return 14;
	}

	@ConfigItem(
			keyName = "innerBorder",
			name = "Border Color",
			description = "Color of the border (MatteBorder)<br>Not really a 'Sidebar' item, affects entire border.",
			position = 0,
			section = sidebarSection
	)
	default Color innerBorder()
	{
		return null;
	}

	@ConfigItem(
			keyName = "sidebarBackgroundColor",
			name = "Sidebar Background",
			description = "Background color of the sidebar/plugin toolbar.",
			position = 2,
			section = sidebarSection
	)
	default Color sidebarBackgroundColor()
	{
		return null;
	}

	@ConfigItem(
			keyName = "selectedTabBackground",
			name = "Selected Tab",
			description = "Color of the selected tab. (TabbedPane.selectedBackground)",
			position = 6,
			section = sidebarSection
	)
	default Color selectedTabBackground()
	{
		return null;
	}

	@ConfigItem(
			keyName = "selectingIndicator",
			name = "Selection Indicator",
			description = "Color of the line near the selected tab. (TabbedPane.inactiveUnderlineColor)",
			position = 8,
			section = sidebarSection
	)
	default Color selectionLineColor()
	{
		return null;
	}


//	@ConfigItem(  This breaks the sidebar opening/closing functionality. Maybe someday if I can fix that.
//			keyName = "scrollingSidebar",
//			name = "Scrolling Sidebar",
//			description = "Allows the sidebar to scroll when it has too many plugins instead of getting wider.",
//			position = 10,
//			section = sidebarSection
//	)
//	default boolean scrollingSidebar()
//	{
//		return false;
//	}

	@Range(max = 100)
	@ConfigItem(
			keyName = "sidebarThickness",
			name = "Sidebar thickness",
			description = "Thickness of the sidebar/plugin toolbar. (Try big increments, max size: 100)",
			position = 12,
			section = sidebarSection
	)
	default int sidebarThickness()
	{
		return 5;
	}

	@ConfigItem(
			keyName = "iconAlignment",
			name = "Plugin Icon Center Alignment",
			description = "Aligns the plugin icons to the center of the sidebar.",
			position = 14,
			section = sidebarSection
	)
	default boolean iconAlignment()
	{
		return false;
	}
	
	@ConfigItem(
			keyName = "sidePanelFontColor",
			name = "Side Panel Font Color",
			description = "Font color used in the configuration menu/plugin list. (And sometimes plugin configs).",
			position = 4,
			section = sidePanelSection
	)
	default Color sidePanelFontColor()
	{
		return null;
	}

	@ConfigItem(
			keyName = "selectedButtons",
			name = "Selected Buttons",
			description = "Color of the selected or enabled buttons and toggles.",
			position = 9,
			section = sidePanelSection
	)
	default Color selectedButtons()
	{
		return null;
	}

	@ConfigItem(
			keyName = "inactiveButtons",
			name = "Inactive Buttons",
			description = "Color of the deselected buttons, and Titlebar toolbox buttons.",
			position = 11,
			section = sidePanelSection
	)
	default Color inactiveButtons()
	{
		return null;
	}


	@ConfigItem(
			keyName = "scrollBarColor",
			name = "Scroll Bar Color",
			description = "Color of the scroll bar 'thumb' (the draggable bit). (ScrollBar.thumb)",
			position = 13,
			section = sidePanelSection
	)
	default Color scrollBarColor()
	{
		return null;
	}

	@ConfigItem(
			keyName = "scrollBarTrackColor",
			name = "Scroll Bar Background Color",
			description = "Color of the scroll bar background. (ScrollBar.track)",
			position = 15,
			section = sidePanelSection
	)
	default Color scrollBarTrackColor()
	{
		return null;
	}


	@ConfigItem(
			keyName = "sidePanelBold",
			name = "Side Panel Bold Font",
			description = "Font used in the plugin list. (And sometimes plugin configs).",
			position = 17,
			section = sidePanelSection
	)
	default boolean sidePanelBold()
	{
		return false;
	}

	@Range(min = 5, max = 30)
	@ConfigItem(
			keyName = "sidePanelFontSize",
			name = "Side Panel Font Size",
			description = "Font size used in the plugin list. (And sometimes plugin configs).",
			position = 19,
			section = sidePanelSection
	)
	default int sidePanelFontSize()
	{
		return 16;
	}

	@ConfigItem(
			keyName = "customUIManagers", // This should only allow modifications to color keys, so users don't break too much stuff.
			name = "Custom UIManager Strings",
			description = "Enter UIManager settings in the format of 'TitlePane.background #A21212', separated by commas.<br> If you find something that you feel should be included in ThemeLite, please open an issue on GitHub.",
			position = 2,
			section = extraSection

	)
	String customUIManagers();

}
