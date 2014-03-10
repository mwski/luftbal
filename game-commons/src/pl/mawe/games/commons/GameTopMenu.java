package pl.mawe.games.commons;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class GameTopMenu extends JMenuBar {

	public static final String AC_M_CONFIG = "acMConfig";
	public static final String AC_M_OPEN = "acMOpen";
	public static final String AC_M_SAVE = "acMSave";
	public static final String AC_M_SAVE_AS = "acMSaveAs";
	public static final String AC_M_PAUSE = "acMPause";
	public static final String AC_M_FASTER = "acMFaster";
	public static final String AC_M_NORMAL = "acMNormal";
	public static final String AC_M_SLOWER = "acMSlower";
	public static final String AC_M_HELP = "acMHelp";
	public static final String AC_M_VERSION = "acMVersion";
	public static final String AC_M_ABOUT = "acMAbout";
	public static final String AC_M_QUIT = "acMQuit";

	private final JMenu jMenuGameMenu = createJMenu("", new ImageIcon(this.getClass().getResource("games.png"), ""));
	private final JMenu jMenuGameFile = createJMenu("File", null);

	private final JMenu jMenuGameOptions = createJMenu("Options", null);
	private final JCheckBoxMenuItem jMenuItemPaused;

	private final JMenu jMenuGameHelp = createJMenu("Help", null);

	public GameTopMenu(ActionListener actionListener) {
		super();

		jMenuGameMenu.add(createJMenuItem("About...", null, AC_M_ABOUT, actionListener, '/'));
		jMenuGameMenu.addSeparator();
		jMenuGameMenu.add(createJMenuItem("Quit", null, AC_M_QUIT, actionListener, 'Q'));

		jMenuGameFile.add(createJMenuItem("New", null, AC_M_CONFIG, actionListener, 'N'));
		jMenuGameFile.addSeparator();
		jMenuGameFile.add(createJMenuItem("Open...", null, AC_M_OPEN, actionListener, 'O'));
		jMenuGameFile.add(createJMenuItem("Save...", null, AC_M_SAVE, actionListener, 'S'));
		jMenuGameFile.add(createJMenuItem("Save as...", null, AC_M_SAVE_AS, actionListener, 'A'));

		jMenuItemPaused = createCheckBoxJMenuItem("Pause", AC_M_PAUSE, actionListener, 'P');
		jMenuGameOptions.add(jMenuItemPaused);
		jMenuGameOptions.addSeparator();
		jMenuGameOptions.add(createJMenuItem("Faster", null, AC_M_FASTER, actionListener, KeyEvent.VK_PLUS));
		jMenuGameOptions.add(createJMenuItem("Normal Speed", null, AC_M_NORMAL, actionListener, KeyEvent.VK_EQUALS));
		jMenuGameOptions.add(createJMenuItem("Slower", null, AC_M_SLOWER, actionListener, KeyEvent.VK_MINUS));
		jMenuGameOptions.addSeparator();
		final JMenuItem jMenuItem = createJMenuItem("empty", null, null, null, -1);
		jMenuItem.setEnabled(false);
		jMenuGameOptions.add(jMenuItem);

		jMenuGameHelp.add(createJMenuItem("Help...", null, AC_M_HELP, actionListener, 0, KeyEvent.VK_F1));
		jMenuGameHelp.addSeparator();
		jMenuGameHelp.add(createJMenuItem("Version", null, AC_M_VERSION, actionListener, 0, KeyEvent.VK_HELP));

		this.add(jMenuGameMenu, 0);
		this.add(jMenuGameFile, 1);
		this.add(jMenuGameOptions, 2);
		this.add(jMenuGameHelp);

	}

	public static GameTopMenu getInstance(ActionListener actionListener) {
		return new GameTopMenu(actionListener);
	}

	public boolean isJMenuItemPausedSelected() {
		return jMenuItemPaused.isSelected();
	}

	public JMenuItem addToJMenuGameOptions(JMenuItem menuItem) {
		return jMenuGameOptions.add(menuItem);
	}

	private static JMenu createJMenu(String name, Icon icon) {
		JMenu jMenu = new JMenu(name);
		jMenu.setIcon(icon);
		return jMenu;
	}

	private static JMenuItem createJMenuItem(String name, Icon icon, String actionCommand, ActionListener actionListener,
			int inputEvent, int mnemonic) {
		JMenuItem jMenuItem = new JMenuItem(name, icon);
		jMenuItem.setActionCommand(actionCommand);
		jMenuItem.addActionListener(actionListener);
		jMenuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, inputEvent, true));
		return jMenuItem;
	}

	private static JMenuItem createJMenuItem(String name, Icon icon, String actionCommand, ActionListener actionListener,
			int mnemonic) {
		return createJMenuItem(name, icon, actionCommand, actionListener, CTRL_DOWN_MASK, mnemonic);
	}

	private static JCheckBoxMenuItem createCheckBoxJMenuItem(String name, String actionCommand, ActionListener actionListener, 
			int mnemonic) {
		return createCheckBoxJMenuItem(name, actionCommand, actionListener, CTRL_DOWN_MASK, mnemonic);
	}

	private static JCheckBoxMenuItem createCheckBoxJMenuItem(String name, String actionCommand, ActionListener actionListener, 
			int inputEvent, int mnemonic) {
		JCheckBoxMenuItem jMenuItem = new JCheckBoxMenuItem(name);
		jMenuItem.setActionCommand(actionCommand);
		jMenuItem.addActionListener(actionListener);
		jMenuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, inputEvent, true));
		return jMenuItem;
	}

}
