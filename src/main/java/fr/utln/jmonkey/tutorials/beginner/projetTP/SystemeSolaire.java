package fr.utln.jmonkey.tutorials.beginner.projetTP;

import java.util.*;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the planeteSoleil character
 * rotate continuously. */
public class SystemeSolaire extends SimpleApplication {

	// https://hub.jmonkeyengine.org/t/a-little-help-with-a-solar-system/42191
	private List<Planet> planetes;
	private float facteurTemps = 0.1f;
	private boolean moving = false;
	private float moveSpeed = 0.1f; // Vitesse du déplacement (ajuster si nécessaire)
	private Vector2f lastMousePosition = null;

	/**
	 * The main method
	 * @param args the main method arguments
	 */
	public static void main(String[] args){
		SystemeSolaire app = new SystemeSolaire();
		app.start();
	}

	/**
	 * Default constructor
	 */
	public SystemeSolaire(){}

	@Override
	public void simpleInitApp() {
		planetes = new ArrayList<>();

		planetes.add(new Planet(assetManager, "soleil", 20, 0, 0, 5, 0, 14.4f));
		planetes.add(new Planet(assetManager, "mercure", 3, 25, 2, 3, 4.15f, 23.4f));
		planetes.add(new Planet(assetManager, "venus", 4, 43, 3, 4, 4.15f, 23.4f));
		planetes.add(new Planet(assetManager, "terre", 7, 63, 1, 2, 4.15f, 23.4f));
		
		Planet lune = new Planet(assetManager, "lune", 1, 10, 1,1,4.13f,0);
		planetes.get(3).addSatellites(lune);

		for (Planet p : planetes) {
			rootNode.attachChild(p.getOrbitePlanete());
		}

		cam.setLocation(new Vector3f(0,200,0));
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Rewind", new KeyTrigger(KeyInput.KEY_LEFT));

		inputManager.addListener(actionListenerSpeed, "Forward", "Rewind");

		flyCam.setEnabled(false);
		inputManager.addMapping("MoveCam", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListenerMove, "MoveCam");

		// Associe les mouvements de la souris
		inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addListener(analogListenerMove, "MouseMove");
	}

	/* Use the main event loop to trigger repeating actions. */
	@Override
	public void simpleUpdate(float tpf) {
		for (Planet p : planetes) {
			p.update(facteurTemps*tpf);
		}

	}

	@Override
	public void start() {
		AppSettings settings = new AppSettings(true);
		settings.setWidth(1920);
		settings.setHeight(1080);
		settings.setFullscreen(false);
		setSettings(settings);
		super.start();
	}

	private ActionListener actionListenerSpeed = new ActionListener() {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
			if (name.equals("Forward")) {
				facteurTemps += 2;}
			if (name.equals("Rewind")) {
				facteurTemps -= 2;
			}
		}
		
	};
};

	private ActionListener actionListenerMove = new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (name.equals("MoveCam")) {
				moving = isPressed;
				if (moving) {
					lastMousePosition = inputManager.getCursorPosition().clone(); // Capture la position initiale
				}
			}
		}
	};

	private AnalogListener analogListenerMove = new AnalogListener() {
		@Override
		public void onAnalog(String name, float value, float tpf) {
			if (moving && lastMousePosition != null) { // Vérifie si le clic gauche est maintenu
				Vector2f currentMousePos = inputManager.getCursorPosition();
				Vector2f delta = currentMousePos.subtract(lastMousePosition); // Calcul du déplacement de la souris
	
				// Appliquer le déplacement à la caméra (indépendant du FPS)
				cam.setLocation(cam.getLocation()
					.add(cam.getLeft().mult(-delta.x * moveSpeed))  // Gauche/Droite
					.add(cam.getUp().mult(delta.y * moveSpeed)));  // Haut/Bas
	
				lastMousePosition.set(currentMousePos); // Mise à jour de la dernière position de la souris
			}
		}
	};
}
