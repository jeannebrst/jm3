package fr.utln.jmonkey.tutorials.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the planeteSoleil character
 * rotate continuously. */
public class HelloLoop extends SimpleApplication {

    // https://hub.jmonkeyengine.org/t/a-little-help-with-a-solar-system/42191

    private Geometry planeteSoleil;
    private Geometry planeteTerre;
    private Geometry planeteLune;
    private Geometry planeteMercure;
    private Node pivotSoleil;
    private Node pivotTerre;
    private Node pivotLune;
    private Node pivotMercure;

    /**
     * The main method
     * @param args the main method arguments
     */
    public static void main(String[] args){
        HelloLoop app = new HelloLoop();
        app.start();
    }

    /**
	 * Default constructor
	 */
    public HelloLoop(){
	}

    @Override
    public void simpleInitApp() {
        /** this blue box is our planeteSoleil character */
        Sphere bsoleil = new Sphere(32,32, 10);
        planeteSoleil = new Geometry("sun", bsoleil);
        planeteSoleil.setLocalTranslation(new Vector3f(0,0,0));
        Material matsoleil = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        matsoleil.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/sun.jpg"));
        planeteSoleil.setMaterial(matsoleil);
        rootNode.attachChild(planeteSoleil);

        planeteSoleil.rotate(-FastMath.PI/2,0,0);

        Sphere bterre = new Sphere(32,32, 2);
        planeteTerre = new Geometry("terre", bterre);
        planeteTerre.setLocalTranslation(new Vector3f(0,0,0));
        Material matterre = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        matterre.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/terre.jpg"));
        planeteTerre.setMaterial(matterre);
        rootNode.attachChild(planeteTerre);

        planeteTerre.rotate(-FastMath.PI/2,0,0);

        Sphere blune = new Sphere(32,32, 0.5f);
        planeteLune = new Geometry("moon", blune);
        planeteLune.setLocalTranslation(new Vector3f(4f,0,0));
        Material matlune = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        matlune.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/moon.jpg"));
        planeteLune.setMaterial(matlune);
        rootNode.attachChild(planeteLune);

        planeteLune.rotate(-FastMath.PI/2,0,0);

        Sphere bmercure = new Sphere(32,32, 0.66f);
        planeteMercure = new Geometry("mercure", bmercure);
        planeteMercure.setLocalTranslation(new Vector3f(0,0,0));
        Material matmercure = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        matmercure.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/mercure.jpg"));
        planeteMercure.setMaterial(matmercure);
        rootNode.attachChild(planeteMercure);

        planeteMercure.rotate(-FastMath.PI/2,0,0);

        pivotSoleil = new Node("pivot");
        pivotSoleil.setLocalTranslation(new Vector3f(0,0,0));
        pivotSoleil.move(0,0,-50);
        rootNode.attachChild(pivotSoleil); // put this node in the scene

        pivotTerre = new Node("pivotTerre");
        pivotTerre.setLocalTranslation(new Vector3f(0,0,0));
        pivotTerre.move(0,0,-50);
        rootNode.attachChild(pivotTerre); // put this node in the scene

        pivotLune = new Node("pivotLune");
        pivotLune.setLocalTranslation(new Vector3f(0,0,0));
        pivotLune.move(5,0,0);
        rootNode.attachChild(pivotTerre); // put this node in the scene

        pivotMercure = new Node("pivotMercure");
        pivotMercure.setLocalTranslation(new Vector3f(0,0,0));
        pivotMercure.move(0,0,-50);
        rootNode.attachChild(pivotMercure); // put this node in the scene

        pivotSoleil.attachChild(planeteSoleil);
        pivotSoleil.attachChild(pivotTerre);
        pivotTerre.attachChild(planeteTerre);
        pivotLune.attachChild(planeteLune);
        pivotMercure.attachChild(planeteMercure);
    }

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        // make the planeteSoleil rotate:
        pivotSoleil.rotate(0,1*tpf, 0);
        pivotTerre.rotate(0,2*tpf,0);
        planeteTerre.rotate(0,2*tpf,0);
    }
}

