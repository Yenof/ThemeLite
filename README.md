![themeLitepicfix](https://github.com/user-attachments/assets/287edeb5-a506-43a8-b0e9-f7fac6ae472e)
<br>
Welcome to ThemeLite, a plugin which crudely recolors elements of the RuneLite client window.

Customize the Titlebar and Sidebar colors, for better or worse!


_*Currently, while switching RuneLite config profiles ThemeLite has been known to cause delays. (do not switch profiles in unsafe areas.)<br>
*ThemeLite requires RuneLite's Custom Chrome for many things, to enable go to: `Config > RuneLite > Enable custom window chrome`._



# Getting Started

The Themes config option contains a few pre-made themes to give you some ideas.
Any other config options apply _after_ Themes, so you can override desired elements.

_Please be patient while changing ThemeLite settings or switching profiles, it redraws the client window on changes and can take a moment._

ThemeLite config options are split into groups based on the part of the interface being colored.
The current groups are:

Themes <br>
Titlebar<br>
Sidebar<br>
Sidepanel<br>
Extra<br>

_*Hovering over config options with the cursor will display these tooltips with sometimes helpful information._
<br>

<br>
Titlebar-<br>
Window Buttons - Color of the minimize, maximize, and exit buttons. Also title bar text color for sub-windows.<br>
Title Bar Background (A) - Active/focused Title bar Background color. Also affects child windows like Color Picker and Chat Panel.<br>
Title Bar Background (I) - Color of the title bar when it's inactive.<br>
Title Bar Text (I) - Color of the text inside the inactive title bar.<br>
Toolbar - Color of the toolbar in the title bar. (Screenshot, Account, Sidebar button housing)<br>
Title Bar Font Size - Font used in the title bar.<br>
<br>
Sidebar:<br>
Sidebar Background – Background color of the sidebar/plugin toolbar.<br>
Selected Tab – Color of the selected tab. (TabbedPane.selectedBackground)<br>
Selection Indicator – Color of the line near the selected tab. (TabbedPane.inactiveUnderlineColor)<br>
Sidebar thickness – Thickness of the sidebar/plugin toolbar.<br>
Plugin Icon Center Alignment – Aligns the plugin icons to the center of the sidebar.<br>

<br>
SidePanel:<br>
Side Panel Font Color – Font color used in the configuration menu/plugin list. (And sometimes plugin configs).<br>
Selected Buttons – Color of the selected or enabled buttons and toggles.<br>
Inactive Buttons – Color of the deselected buttons, and Titlebar toolbox buttons.<br>
Scroll Bar Color – Color of the scroll bar 'thumb' (the draggable bit). (ScrollBar.thumb)<br>
Scroll Bar Background Color – Color of the scroll bar background. (ScrollBar.track)<br>
Side Panel Bold Font – Font used in the plugin list. (And sometimes plugin configs).<br>
Side Panel Font Size – Font size used in the plugin list. (And sometimes plugin configs).<br>

_The Sidepanel settings on occasion affect the config tab of plugins, currently I can't find a way to make this consistent one way or the other._
<br>

<br>
Extra:

The "Extra" group contains `Custom UIManager Strings` which will allow you to enter strings like `TitlePane.background #A21212` to recolor interface elements currently unsupported by ThemeLite.
You can enter multiple strings separated by commas.
`Custom UIManager Strings` is intended as a way to crowdsource desirable UIManager strings.
If you find a UIManager string that you think would be nice to add to ThemeLite, please comment on the GitHub issue "UIManager Strings", or use Discord contact info below.


# Notes

If you are reading this then ThemeLite is very early in development; while I have tried to find all the bugs and weirdness I can, do expect to find some, and feel free to report them on GitHub issues.

Theme sharing hopefully coming someday.
