package fr.utln.jmonkey.tutorials.beginner.projetTP;

import java.util.*;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
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
	private float facteurTemps = 1;
	private int indexPlanete = 0;
	private ChaseCamera chaseCam;
	private BitmapText hudText;

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

		planetes.add(new Planet(assetManager, "Soleil", 20, 0, 0, 5, 0, 14.4f));
		planetes.add(new Planet(assetManager, "Mercure", 3, 25, 2, 3, 4.15f, 23.4f));
		planetes.add(new Planet(assetManager, "Venus", 4, 43, 3, 4, 4.15f, 23.4f));
		planetes.add(new Planet(assetManager, "Terre", 7, 63, 1, 2, 4.15f, 23.4f));
		
		Planet lune = new Planet(assetManager, "Lune", 1, 10, 1,1,4.13f,0);
		planetes.get(3).addSatellites(lune);

		for (Planet p : planetes) {
			rootNode.attachChild(p.getOrbitePlanete());
		}

		// cam.setLocation(new Vector3f(0,200,0));
		// cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		flyCam.setEnabled(false);

		chaseCam = new ChaseCamera(cam, planetes.get(0).getPlanete(), inputManager);
        chaseCam.setDefaultDistance(50); // Distance initiale de la caméra
        chaseCam.setMinDistance(10);  // Distance minimale
        chaseCam.setMaxDistance(200); // Distance maximale
        chaseCam.setRotationSpeed(3); // Vitesse de rotation
        chaseCam.setDragToRotate(true);
		chaseCam.setLookAtOffset(new Vector3f(0,5,0));

		// Pour augmenter ou réduire la vitesse de rotation en orbite
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Rewind", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addListener(actionListenerSpeed, "Forward", "Rewind");

		// Pour changer de vue de planète
		inputManager.addMapping("NextPlanet", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("PreviousPlanet", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(actionListenerView, "NextPlanet", "PreviousPlanet");

		// Pour afficher du texte
		BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        hudText = new BitmapText(font);
        hudText.setSize(font.getCharSet().getRenderedSize()*2); // Taille du texte
        hudText.setColor(com.jme3.math.ColorRGBA.White); // Couleur blanche
        hudText.setText("Soleil"); // Texte initial
        hudText.setLocalTranslation(10, settings.getHeight() - 10, 0); // Position en haut à gauche
        guiNode.attachChild(hudText);
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
					facteurTemps += 1;}
				if (name.equals("Rewind")) {
					facteurTemps -= 1;}
			}
		};
	};

	private ActionListener actionListenerView = new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (isPressed) {
				if (name.equals("NextPlanet")) {
					indexPlanete += 1;
					indexPlanete = Math.floorMod(indexPlanete,planetes.size());
				}
				if (name.equals("PreviousPlanet")) {
					indexPlanete -= 1;
					indexPlanete = Math.floorMod(indexPlanete,planetes.size());
				}
				chaseCam.setSpatial(planetes.get(indexPlanete).getPlanete());
				hudText.setText(planetes.get(indexPlanete).getNom());
			}
		};
	};
}
