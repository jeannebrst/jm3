package fr.utln.jmonkey.tutorials.beginner.projetTP;

import java.util.*;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.util.SkyFactory;
import com.jme3.texture.Texture;

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

		planetes.add(new Planet(assetManager, 2, 0));
		planetes.add(new Planet(assetManager, "Mercure", 0.38f, 0.1f, 3, 3, 4));
		planetes.add(new Planet(assetManager, "Venus", 0.95f, 0.07f, 4, 5.5f, 7));
		planetes.add(new Planet(assetManager, "Terre", 1,  0.05f, 2, 8, 10));
		
		Planet lune = new Planet(assetManager, "Lune", 1.5f, 0.02f, 1, 2.5f, 3);
		planetes.get(3).addSatellites(lune);

		for (Planet p : planetes) {
			rootNode.attachChild(p.getOrbitePlanete());
		}
		
		PointLight sunLight = new PointLight();
		sunLight.setColor(ColorRGBA.White); // Lumière intense
		sunLight.setRadius(0); // Portée de la lumière
		sunLight.setPosition(Vector3f.ZERO); // Centre de la scène (même position que le Soleil)
		rootNode.addLight(sunLight);
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);

		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White.mult(0.05f));
		rootNode.addLight(ambient);


		flyCam.setEnabled(false);
		chaseCam = new ChaseCamera(cam, planetes.get(0).getPlanete(), inputManager);
		chaseCam.setHideCursorOnRotate(false);
		chaseCam.setInvertVerticalAxis(true);
        chaseCam.setDefaultDistance(planetes.get(0).getTaillePlanete()*10); // Distance initiale de la caméra
        chaseCam.setMinDistance(planetes.get(0).getTaillePlanete()*2);  // Distance minimale
        chaseCam.setMaxDistance(planetes.get(0).getTaillePlanete()*50); // Distance maximale
        chaseCam.setRotationSpeed(3); // Vitesse de rotation
        chaseCam.setDragToRotate(true);
		//chaseCam.setLookAtOffset(new Vector3f(0,5,0));

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

		// Pour ajouter un fond
		viewPort.setBackgroundColor(null);
        Texture skyTexture = assetManager.loadTexture("Textures/Terrain/Espace.jpg");
        rootNode.attachChild(SkyFactory.createSky(assetManager, skyTexture, skyTexture, skyTexture, skyTexture, skyTexture, skyTexture));
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
		// settings.setWidth(1850);
		// settings.setHeight(1010);
		settings.setFullscreen(true);
		settings.setResolution(1920, 1080);
		setDisplayStatView(false);
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
				chaseCam.setDefaultDistance(planetes.get(indexPlanete).getTaillePlanete()*10); // Distance initiale de la caméra
				chaseCam.setMinDistance(planetes.get(indexPlanete).getTaillePlanete()*2);  // Distance minimale
				chaseCam.setMaxDistance(planetes.get(indexPlanete).getTaillePlanete()*50);
				hudText.setText(planetes.get(indexPlanete).getNom());
			}
		};
	};
}
