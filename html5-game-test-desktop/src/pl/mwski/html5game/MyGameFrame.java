package pl.mwski.html5game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

import pl.mawe.games.commons.GameTopMenu;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

@SuppressWarnings("serial")
public class MyGameFrame extends JFrame implements ActionListener {

	private final Logger log = Logger.getLogger("MyGameFrame");

	private LwjglAWTCanvas canvas;
	private JPanel container = new JPanel();
	private MyGdxGame game;
	private JoystickSupport joystickSupport = null;

	private final GameTopMenu gameTopMenu = new GameTopMenu(this);

	private class MyWindowListener extends WindowAdapter {

		public MyWindowListener() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void windowClosing(WindowEvent e) {

		}

		@Override
		public void windowClosed(WindowEvent e) {

		}
	}

	public MyGameFrame() {
		super("My Game.");
		log.setLevel(Level.ALL);

		addWindowListener(new MyWindowListener());

		game = new MyGdxGame();

		try {
			Controllers.create();
		} catch (LWJGLException e) {
			JOptionPane.showMessageDialog(this, e, "Error!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		setJMenuBar(gameTopMenu);

		canvas = new LwjglAWTCanvas(game, false);
		canvas.getCanvas().setSize(800, 600);

		container.setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		container.add(canvas.getCanvas(), BorderLayout.CENTER);
		add(container, BorderLayout.CENTER);

		pack();
		setVisible(true);
		setSize(800, 600);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (GameTopMenu.AC_M_PAUSE.equals(e.getActionCommand())) {
			game.setPaused(gameTopMenu.isJMenuItemPausedSelected());
		} else if (GameTopMenu.AC_M_QUIT.equals(e.getActionCommand())) {
			dispose();
		}
	}

	@Override
	public void dispose() {
		MyGameFrame.this.log.logp(Level.INFO, this.getClass().getName(), "dispose()", "*****Closing window...*****");
		MyGameFrame.this.canvas.exit();
		MyGameFrame.this.log.logp(Level.INFO, this.getClass().getName(), "dispose()", "*****Window closed.*****");
	}

	public JoystickSupport getJoystickSupport() {
		return joystickSupport;
	}

	public void setJoystickSupport(JoystickSupport joystickSupport) {
		this.joystickSupport = joystickSupport;
	}

}
