package pl.mwski.html5game;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		final LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "my-gdx-game";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;

		final JoystickSupport joystickSupport = new JoystickSupport() {

			@Override
			public int getAxis() {

				Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

				for(int i =0;i<ca.length;i++){
					if (ca[i].getType() == Type.STICK) {
						Controller firstJoystick = ca[i];

						if (firstJoystick.poll()) {

							Component[] components = ca[i].getComponents();

							for(int j=0;j<components.length;j++){

								if (!components[j].isRelative()) {
									Identifier identifier = components[j].getIdentifier();

									if (components[j].isAnalog()) {
										if (identifier.equals(Identifier.Axis.X)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
										if (identifier.equals(Identifier.Axis.Y)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
										if (identifier.equals(Identifier.Axis.Z)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
									} else {
										if (identifier.equals(Identifier.Button._0)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
										if (identifier.equals(Identifier.Button._1)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
										if (identifier.equals(Identifier.Button._2)) {
											System.out.println(identifier.toString() + components[j].getPollData());
										}
									}

								}

							}

						}

					}

				}

				return 0;
			}
		};

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MyGameFrame window = new MyGameFrame();
				window.setJoystickSupport(joystickSupport);
				window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});

	}
}
