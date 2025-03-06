package fr.utln.jmonkey.tutorials.beginner.projetTP;

import java.util.*;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
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
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;

import java.text.SimpleDateFormat;
import java.time.Instant;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the planeteSoleil character
 * rotate continuously. */
public class SystemeSolaire extends SimpleApplication {

	private List<Planet> planetes;
	private float facteurTemps = 1;
	private double refTime;
	private double antTime;
	private double cptTime;
	//private double actualTime;
	SimpleDateFormat formatDate;
	private Label dateLabel;
	private Label facteurLabel;
	private String[] diametres;
	private Label diametre;
	private String[] masses;
	private Label masse;
	private int indexPlanete = 0;
	private ChaseCamera chaseCam;
	private BitmapText hudText;
	private boolean isChaseCamActive = true;

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

		diametres = new String[]{
			"1 392 684 km", //Soleil
			"4 879 km", //Mercure
			"12 742 km", //Venus
			"12 104 km", //Terre
			"3 474 km", //Lune
			"3 390 km", //Mars
			"12.4 km",  //Phobos
			"22.2 km", //Deimos
			"139 820 km", //Jupiter
			"3 122 km", //Europe
			"3 642 km", //Io
			"116 460 km", //Saturne
			"50 724 km", //Uranus
			"49 528 km" //Neptune
			};

		masses = new String[]{ 
			"1.989 x 10^30 kg", // Soleil
			"3.301 x 10^23 kg", // Mercure
			"4.867 x 10^24 kg", // Vénus
			"5.972 x 10^24 kg", // Terre
			"7.348 x 10^22 kg", // Lune
			"6.417 x 10^23 kg", // Mars
			"1.0659 x 10^16 kg", // Phobos
			"1.476 x 10^15 kg", // Deimos
			"1.898 x 10^27 kg", // Jupiter
			"4.799 x 10^22 kg", // Europe
			"8.931 x 10^22 kg", // Io
			"5.683 x 10^26 kg", // Saturne
			"8.681 x 10^25 kg", // Uranus
			"1.024 x 10^26 kg"  // Neptune
			};
			
		
		planetes.add(new Planet(assetManager, 10, 0));
		planetes.add(new Planet(assetManager, "Mercure", 0.24f, 87.9693f, 58.6462f, 57.91f+planetes.get(0).getTaillePlanete(), 0.206f,0.03f,7));
		planetes.add(new Planet(assetManager, "Venus", 0.6f, 224.7010f, -243.018f, 108.2f+planetes.get(0).getTaillePlanete(), 0.0068f,177.36f,3.39f));
		planetes.add(new Planet(assetManager, "Terre", 0.63f,  365.2569f, 0.9972f, 149.6f+planetes.get(0).getTaillePlanete(), 0.0167f,23.44f,0));
		planetes.add(new Planet(assetManager, "Mars", 0.33f, 686.9601f, 1.0259f, 227.9f+planetes.get(0).getTaillePlanete(), 0.093f,25.19f,1.85f));
		planetes.add(new Planet(assetManager, "Jupiter", 6.9f, 4335.3545f, 0.4135f, 778.3f+planetes.get(0).getTaillePlanete(), 0.048f,3.12f,1.304f));
		planetes.add(new Planet(assetManager, "Saturne", 5.8f, 10757.7365f, 0.444f, 1429+planetes.get(0).getTaillePlanete(), 0.056f,26.73f,2.485f));
		planetes.get(6).addRings(assetManager, "Anneaux_Sat");
		planetes.add(new Planet(assetManager, "Uranus", 2.53f, 30687.15f, -0.7183f, 2875+planetes.get(0).getTaillePlanete(), 0.046f,97.8f,0.772f));
		planetes.add(new Planet(assetManager, "Neptune", 2.4622f, 60224.9036f, 0.6712f, 4504+planetes.get(0).getTaillePlanete(), 0.0086f,29.58f,1.769f));
		
		Planet lune = new Planet(assetManager, "Lune", 0.17374f, 27.3220f, 27.322f, planetes.get(3).getTaillePlanete()+3.844f, 0.0554f,1.54f,5.145f);
		planetes.get(3).addSatellites(lune);
		Planet phobos = new Planet(assetManager, "Phobos", 0.11267f, 0.3189f, 0.3189f, planetes.get(4).getTaillePlanete()+0.93771f, 0.015f,0,1.075f);
		planetes.get(4).addSatellites(phobos);
		Planet deimos = new Planet(assetManager, "Deimos", 0.06f, 1.2624f, 1.2624f, planetes.get(4).getTaillePlanete()+2.3460f, 0,0,1.075f);
		planetes.get(4).addSatellites(deimos);
		Planet europe = new Planet(assetManager, "Europe", 0.15608f, 3.551f, 3.551f, planetes.get(5).getTaillePlanete()+8.71f, 0.0094f,0.47f,0.466f);
		planetes.get(5).addSatellites(europe);
		Planet io = new Planet(assetManager, "Io", 0.18216f, 1.769f, 1.769f, planetes.get(5).getTaillePlanete()+4.218f, 0.004f,0.03f,0.036f);
		planetes.get(5).addSatellites(io);

		for (Planet p : planetes) {
			rootNode.attachChild(p.getOrbitePlanete());
			if (p.getSatellites()!=null) {
				for (Planet s : p.getSatellites()) {
					p.getAxePlanete().attachChild(s.getOrbitePlanete());
				}
			}
		}

		// Gestion du temps
		formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		refTime = System.currentTimeMillis();
		antTime = System.currentTimeMillis();

		GuiGlobals.initialize(this); // Initialisation Lemur
		dateLabel = new Label("Date");
		dateLabel.setFontSize(30);
		dateLabel.setColor(ColorRGBA.White);
		dateLabel.setLocalTranslation(settings.getWidth()-450, settings.getHeight() - 1000, 0);
		guiNode.attachChild(dateLabel);

		facteurLabel = new Label("Facteur");
		facteurLabel.setFontSize(30);
		facteurLabel.setColor(ColorRGBA.White);
		facteurLabel.setLocalTranslation(settings.getWidth()-150, settings.getHeight() - 1000, 0);
		guiNode.attachChild(facteurLabel);

		diametre = new Label("diamètre");
		diametre.setFontSize(35);
		diametre.setColor(ColorRGBA.White);
		diametre.setLocalTranslation(20, settings.getHeight() - 150, 0);
		diametre.setText("diamètre : "+diametres[0]);
		guiNode.attachChild(diametre);

		masse = new Label("masse");
		masse.setFontSize(35);
		masse.setColor(ColorRGBA.White);
		masse.setLocalTranslation(20, settings.getHeight() - 200, 0);
		masse.setText("masse : "+ masses[0]);
		guiNode.attachChild(masse);

		String[] models = {
            "Models/Asteroides/Eros.glb",
			"Models/Asteroides/Itokawa.glb",
			"Models/Asteroides/Vesta.glb",
			"Models/Asteroides/Bennu.glb"
        };

		Asteroide asteroideGenerator = new Asteroide(assetManager, models);
        asteroideGenerator.generateAsteroids(rootNode);
		
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
		chaseCam.setZoomSensitivity(planetes.get(0).getTaillePlanete()*2);
        chaseCam.setDragToRotate(true);
		chaseCam.setMinVerticalRotation(-FastMath.PI/2);

		// Pour switch en flycam et gérer les deplacements
		inputManager.addMapping("SwitchCam", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addListener(actionListenerCam, "SwitchCam");
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(analogListenerMove, "Forward", "Backward", "Left", "Right");

		// Pour augmenter ou réduire la vitesse de rotation en orbite
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Rewind", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addListener(actionListenerSpeed, "Forward", "Rewind");

		// Pour changer de vue de planète
		inputManager.addMapping("NextPlanet", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("PreviousPlanet", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(actionListenerView, "NextPlanet", "PreviousPlanet");

		// Pour reset le temps
		inputManager.addMapping("ResetTime", new KeyTrigger(KeyInput.KEY_BACK));
		inputManager.addListener(actionListenerResetTime,"ResetTime");

		//Pour cacher/afficher les orbites
		inputManager.addMapping("HideOrbit", new KeyTrigger(KeyInput.KEY_O));
		inputManager.addListener(actionListenerHideOrbit,"HideOrbit");

		// Pour afficher du texte
		BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        hudText = new BitmapText(font);
        hudText.setSize(font.getCharSet().getRenderedSize()*((float)2.5)); // Taille du texte
        hudText.setColor(com.jme3.math.ColorRGBA.White); // Couleur blanche
        hudText.setText("Soleil"); // Texte initial
        hudText.setLocalTranslation(20, settings.getHeight() - 60, 0); // Position en haut à gauche
        guiNode.attachChild(hudText);

		// Pour ajouter un fond
		viewPort.setBackgroundColor(null);
        Texture skyTexture = assetManager.loadTexture("Textures/Terrain/Espace.jpg");
        rootNode.attachChild(SkyFactory.createSky(assetManager, skyTexture, skyTexture, skyTexture, skyTexture, skyTexture, skyTexture));

		//initUI();
	}

	// private void initUI() {
    //     Container container = new Container();
    //     container.setLocalTranslation(10, settings.getHeight() - 10, 0);
    //     guiNode.attachChild(container);

    //     Label label = container.addChild(new Label("Vitesse du Temps"));
    //     Slider slider = container.addChild(new Slider());
    //     slider.setDelta(0.1f);
    //     slider.setModel(new DefaultRangedValueModel(-1000000f, 1000000f, facteurTemps));
    //     slider.setSnapValues(new float[]{0.01f, 0.1f}, false);
    //     slider.addChangeListener((source, value) -> facteurTemps = value.floatValue());
    // }

	/* Use the main event loop to trigger repeating actions. */
	@Override
	public void simpleUpdate(float tpf) {
		double actualTime = System.currentTimeMillis();
		double passedTime = (actualTime-antTime)*facteurTemps;
		cptTime += passedTime;
		antTime = actualTime;
		double time = refTime+cptTime;

		Date date = new Date((long)(time));
		dateLabel.setText(formatDate.format(date));
		facteurLabel.setText("x"+facteurTemps);

		for (Planet p : planetes) {
			//p.update(facteurTemps*tpf);
			if (p.getNom()!="Soleil"){
				p.rotate(time);
				p.rotateSelf(passedTime);
				if (p.getSatellites()!=null) {
					for (Planet s : p.getSatellites()) {
						s.rotate(time);
						s.rotateSelf(passedTime);
					}
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

	private void resetTime(){
		refTime = System.currentTimeMillis();
		antTime = System.currentTimeMillis();
		cptTime = 0;
		facteurTemps = 1;
	}

	private ActionListener actionListenerResetTime = new ActionListener() {
	@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (isPressed) {
				resetTime();
			}
		}
	};

	private ActionListener actionListenerHideOrbit = new ActionListener() {
		@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (isPressed) {
					List<Planet> systemeOrdonne = new ArrayList<>();
					for (Planet planete : planetes) {
						if (!planete.getNom().equals("Soleil")){
							systemeOrdonne.add(planete);
							if (planete.getSatellites()!=null) {
								for (Planet sat : planete.getSatellites()) {
									systemeOrdonne.add(sat);
								}
							}
						}
					}
					for (Planet p : systemeOrdonne) {
						if (p.getOrbite().getOrbiteNode().getCullHint().equals(Spatial.CullHint.Always)) {
							p.getOrbite().getOrbiteNode().setCullHint(Spatial.CullHint.Inherit);
						}
						else {
							p.getOrbite().getOrbiteNode().setCullHint(Spatial.CullHint.Always);
						}

					}
				}
			}
		};

	private ActionListener actionListenerSpeed = new ActionListener() {
	@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (isPressed) {
				if (name.equals("Forward")) {
					if (facteurTemps==-1) {
						facteurTemps = 0;
					}
					else if (facteurTemps==0) {
						facteurTemps = 1;
					}
					else if (facteurTemps < -1) {
						facteurTemps /= 10;
					}
					else {
						facteurTemps *= 10;
					}
				}
				if (name.equals("Rewind")) {
					if (facteurTemps==1) {
						facteurTemps = 0;
					}
					else if (facteurTemps==0) {
						facteurTemps = -1;
					}
					else if (facteurTemps > 1) {
						facteurTemps /= 10;
					}
					else {
						facteurTemps *= 10;
					}
				}
			}
		};
	};

	private ActionListener actionListenerView = new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			List<Planet> systemeOrdonne = new ArrayList<>();
			for (Planet planete : planetes) {
				systemeOrdonne.add(planete);
				if (planete.getSatellites()!=null) {
					for (Planet sat : planete.getSatellites()) {
						systemeOrdonne.add(sat);
					}
				}
			}
			if (isPressed) {
				if (name.equals("NextPlanet")) {
					indexPlanete += 1;
					indexPlanete = Math.floorMod(indexPlanete,systemeOrdonne.size());
				}
				if (name.equals("PreviousPlanet")) {
					indexPlanete -= 1;
					indexPlanete = Math.floorMod(indexPlanete,systemeOrdonne.size());
				}
				chaseCam.setSpatial(systemeOrdonne.get(indexPlanete).getPlanete());
				chaseCam.setDefaultDistance(systemeOrdonne.get(indexPlanete).getTaillePlanete()*20);
				chaseCam.setMinDistance(systemeOrdonne.get(indexPlanete).getTaillePlanete()*2);
				chaseCam.setZoomSensitivity(systemeOrdonne.get(indexPlanete).getTaillePlanete());
				hudText.setText(systemeOrdonne.get(indexPlanete).getNom());
				diametre.setText("diamètre : "+ diametres[indexPlanete]);
				masse.setText("masse : "+  masses[indexPlanete]);
			}
		};
	};

	private final ActionListener actionListenerCam = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("SwitchCam") && isPressed) {
                if (isChaseCamActive) {
                    // Désactiver la ChaseCam et activer la FlyCam
                    chaseCam.setEnabled(false);
                    flyCam.setEnabled(true);
					inputManager.setCursorVisible(true);
					flyCam.setMoveSpeed(500);
					flyCam.setDragToRotate(true);
                } else {
                    // Activer la ChaseCam et désactiver la FlyCam
                    flyCam.setEnabled(false);
                    chaseCam.setEnabled(true);
                }
                isChaseCamActive = !isChaseCamActive; // Inverser l'état
            }
        }
    };

	private AnalogListener analogListenerMove = new AnalogListener() {
		@Override
		public void onAnalog(String name, float value, float tpf) {
			float speed = 20f * tpf;
			Vector3f camDir = cam.getDirection().clone().mult(speed);
			Vector3f camLeft = cam.getLeft().clone().mult(speed);
			if (name.equals("Forward")) {
				cam.setLocation(cam.getLocation().add(camDir));
			} else if (name.equals("Backward")) {
				cam.setLocation(cam.getLocation().subtract(camDir));
			} else if (name.equals("Left")) {
				cam.setLocation(cam.getLocation().add(camLeft));
			} else if (name.equals("Right")) {
				cam.setLocation(cam.getLocation().subtract(camLeft));
			}
		}
	};
}
