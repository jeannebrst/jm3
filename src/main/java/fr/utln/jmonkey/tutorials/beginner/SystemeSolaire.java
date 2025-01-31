package fr.utln.jmonkey.tutorials.beginner;

import java.util.*;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the planeteSoleil character
 * rotate continuously. */
public class SystemeSolaire extends SimpleApplication {

    // https://hub.jmonkeyengine.org/t/a-little-help-with-a-solar-system/42191
	private Geometry planeteSoleil;

    private Geometry planeteTerre;
    private Node orbiteTerre;
    private Node axeTerre;

    private Geometry planeteLune;

    private Geometry planeteMercure;
    private Node orbiteMercure;
    private Node axeMercure;

    private Geometry planeteVenus;
    private Node orbiteVenus;
    private Node axeVenus;

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
    public SystemeSolaire(){
	}

    private Geometry createPlanet(Geometry nameSphere, float radius, String texture) {
        Sphere forme = new Sphere(32,32,radius);
        nameSphere = new Geometry("sphere", forme);
        Material matplanete = new Material(assetManager,
        "Common/MatDefs/Misc/Unshaded.j3md");
        matplanete.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/"+texture+".jpg"));
        nameSphere.setMaterial(matplanete);
        rootNode.attachChild(nameSphere);
        nameSphere.rotate(-FastMath.PI/2,0,0);
        return nameSphere;
    }

    @Override
    public void simpleInitApp() {
		// Sphere ssoleil = new Sphere(32,32,20);
        // planeteSoleil = new Geometry("soleil", ssoleil);
        // Material matsoleil = new Material(assetManager,
        // "Common/MatDefs/Misc/Unshaded.j3md");
        // matsoleil.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/sun.jpg"));
        // planeteSoleil.setMaterial(matsoleil);
        // rootNode.attachChild(planeteSoleil);
        // planeteSoleil.rotate(-FastMath.PI/2,0,0);

        planeteSoleil = createPlanet(planeteSoleil, 20, "sun");
        planeteTerre = createPlanet(planeteTerre, 2.5f, "terre");
        planeteLune = createPlanet(planeteLune, 0.68f, "moon");
        planeteLune.setLocalTranslation(new Vector3f(4,0,0));
        planeteMercure = createPlanet(planeteMercure, 1.2f, "mercure");
        planeteVenus = createPlanet(planeteVenus, 2.3f, "venus");

        // Sphere sterre = new Sphere(32,32,2.5f);
        // planeteTerre = new Geometry("terre", sterre);
        // planeteTerre.setLocalTranslation(new Vector3f(0,0,0));
        // Material matterre = new Material(assetManager,
        // "Common/MatDefs/Misc/Unshaded.j3md");
        // matterre.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/terre.jpg"));
        // planeteTerre.setMaterial(matterre);
        // planeteTerre.rotate(-FastMath.HALF_PI,0,0);
        // rootNode.attachChild(planeteTerre);

        axeTerre = new Node("axeTerre");
        axeTerre.setLocalTranslation(39.26f,0,0);
        axeTerre.attachChild(planeteTerre);

        orbiteTerre = new Node("orbiteTerre");
        orbiteTerre.attachChild(axeTerre);
        rootNode.attachChild(orbiteTerre);

        axeTerre.attachChild(planeteLune);

        axeMercure = new Node("axeMercure");
        axeMercure.setLocalTranslation(25.09f,0,0);
        axeMercure.attachChild(planeteMercure);

        orbiteMercure = new Node("orbiteMercure");
        orbiteMercure.attachChild(axeMercure);
        rootNode.attachChild(orbiteMercure);

        axeVenus = new Node("axeVenus");
        axeVenus.setLocalTranslation(30.12f,0,0);
        axeVenus.attachChild(planeteVenus);

        orbiteVenus = new Node("orbiteVenus");
        orbiteVenus.attachChild(axeVenus);
        rootNode.attachChild(orbiteVenus);

        cam.setLocation(new Vector3f(0,200,0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
	}

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        planeteSoleil.rotate(0,0,1*tpf);
        orbiteTerre.rotate(0,3*tpf,0);
        axeTerre.rotate(0,3*tpf,0);
        orbiteMercure.rotate(0,5*tpf,0);
        axeMercure.rotate(0,2*tpf,0);
        axeVenus.rotate(0,3*tpf,0);
        orbiteVenus.rotate(0,2*tpf,0);
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
}

