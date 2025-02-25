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
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.scene.Geometry;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.scene.Node;

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

	public void AjoutOrbite(Planet planete, float demiGrandAxe, float excentricite, Node noeudAttache) {
		int points = 100;
		float focalOffset = excentricite * demiGrandAxe;
		Vector3f[] pointsOrbite = new Vector3f[points];
		for (int i=0; i<points; i++) {
			float angle = i*FastMath.TWO_PI/points;
			float x = demiGrandAxe*FastMath.cos(angle)-focalOffset;
			float z = demiGrandAxe*FastMath.sqrt(1-excentricite*excentricite)*FastMath.sin(angle);
			pointsOrbite[i] = new Vector3f(x,0,z);
		}
		
		Mesh orbitMesh = new Mesh();
		orbitMesh.setMode(Mesh.Mode.LineLoop); // Utilisation du mode ligne continue
		orbitMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(pointsOrbite));
		orbitMesh.updateBound();
		orbitMesh.setStatic();

		// Création de la géométrie pour afficher la ligne
		Geometry orbitGeom = new Geometry("Orbit", orbitMesh);
		Material orbitMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		orbitMat.setColor("Color", com.jme3.math.ColorRGBA.White.mult(0.5f));
		orbitGeom.setMaterial(orbitMat);
		noeudAttache.attachChild(orbitGeom);
	}

	@Override
	public void simpleInitApp() {
		planetes = new ArrayList<>();

		planetes.add(new Planet(assetManager, 100, 0));
		planetes.add(new Planet(assetManager, "Mercure", 2.4f, 0.1728f, 3, 57.91f+planetes.get(0).getTaillePlanete(), 0.206f));
		planetes.add(new Planet(assetManager, "Venus", 6, 0.126f, 4, 108.2f+planetes.get(0).getTaillePlanete(), 0.0068f));
		planetes.add(new Planet(assetManager, "Terre", 6.3f,  0.1044f, 2, 149.6f+planetes.get(0).getTaillePlanete(), 0.0167f));
		planetes.add(new Planet(assetManager, "Mars", 3.3f, 0.0864f, 4, 227.9f+planetes.get(0).getTaillePlanete(), 0.093f));
		planetes.add(new Planet(assetManager, "Jupiter", 69, 0.0468f, 2, 778.3f+planetes.get(0).getTaillePlanete(), 0.048f));
		planetes.add(new Planet(assetManager, "Saturne", 58, 0.036f, 2, 1429+planetes.get(0).getTaillePlanete(), 0.056f));
		planetes.get(6).addRings(assetManager, "Anneaux_Sat");
		planetes.add(new Planet(assetManager, "Uranus", 25.3f, 0.0252f, 3, 2875+planetes.get(0).getTaillePlanete(), 0.046f));
		planetes.add(new Planet(assetManager, "Neptune", 24.622f, 0.018f, 5, 4504+planetes.get(0).getTaillePlanete(), 0.0086f));
		
		Planet lune = new Planet(assetManager, "Lune", 1.7374f, 0.003f, 1, planetes.get(3).getTaillePlanete()+3.844f, 0.0554f);
		planetes.get(3).addSatellites(lune);
		Planet europe = new Planet(assetManager, "Europe", 1.5608f, 0.0504f, 4, planetes.get(5).getTaillePlanete()+6.71f, 0.0094f);
		planetes.get(5).addSatellites(europe);

		for (Planet p : planetes) {
			rootNode.attachChild(p.getOrbitePlanete());
			AjoutOrbite(p, p.getGrandAxe(), p.getExcentricite(),rootNode);
			if (p.getSatellites()!=null) {
				for (Planet s : p.getSatellites()) {
					p.getAxePlanete().attachChild(s.getOrbitePlanete());
					AjoutOrbite(s, s.getGrandAxe(), s.getExcentricite(),s.getOrbitePlanete());
				}
			}
			
		}
		// Pour que le soleil soit une source de lumière
		PointLight sunLight = new PointLight();
		sunLight.setColor(ColorRGBA.White);
		sunLight.setRadius(0);
		sunLight.setPosition(Vector3f.ZERO);
		rootNode.addLight(sunLight);
		// Pour ajouter un effet de brillance sur le soleil
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);

		// Pour ajouter une lumière ambiente faible (sinon le dos des planètes était vraiment tout noir, pas très joli)
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White.mult(0.02f));
		rootNode.addLight(ambient);

		// Réglages caméra et souris
		flyCam.setEnabled(false);
		cam.setFrustumFar(30000);
		chaseCam = new ChaseCamera(cam, planetes.get(0).getPlanete(), inputManager);
		chaseCam.setHideCursorOnRotate(false);
		chaseCam.setInvertVerticalAxis(true);
        chaseCam.setDefaultDistance(planetes.get(0).getTaillePlanete()*50); // Distance initiale de la caméra
        chaseCam.setMinDistance(planetes.get(0).getTaillePlanete()*2);  // Distance minimale
        chaseCam.setMaxDistance(planetes.get(0).getTaillePlanete()*500); // Distance maximale
        chaseCam.setRotationSpeed(3); // Vitesse de rotation
		chaseCam.setZoomSensitivity(planetes.get(0).getTaillePlanete());
        chaseCam.setDragToRotate(true);
		chaseCam.setMinVerticalRotation(-FastMath.PI/2);

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
			if (p.getSatellites()!=null) {
				for (Planet s : p.getSatellites()) {
					s.update(facteurTemps*tpf);
				}
			}
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
				chaseCam.setDefaultDistance(planetes.get(indexPlanete).getTaillePlanete()*20);
				chaseCam.setMinDistance(planetes.get(indexPlanete).getTaillePlanete()*2);
				chaseCam.setZoomSensitivity(planetes.get(indexPlanete).getTaillePlanete());
				hudText.setText(planetes.get(indexPlanete).getNom());
			}
		};
	};
}
