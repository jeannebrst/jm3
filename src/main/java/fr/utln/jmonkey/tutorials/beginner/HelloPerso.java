package fr.utln.jmonkey.tutorials.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;


/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the player character
 * rotate continuously. */
public class HelloPerso extends SimpleApplication {

    private Geometry player;
    private Geometry player2;
    private Node pivot;

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
    public HelloPerso(){
    }

    @Override
    public void simpleInitApp() {
        /** this blue box is our player character */
        Sphere b1 = new Sphere(32,32, 2f);
        player = new Geometry("cube", b1);
        player.setLocalTranslation(new Vector3f(1,1,1));
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/grass.png"));
//        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
//        player.setQueueBucket(Bucket.Transparent);
        mat.setColor("Color", ColorRGBA.Cyan);
        player.setMaterial(mat);
        rootNode.attachChild(player);

        Box b2 = new Box(2, 2, 2);
        player2 = new Geometry("red cube", b2);
        player2.setLocalTranslation(new Vector3f(1,6,1));
        Material mat2 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Red);
        player2.setMaterial(mat2);
        rootNode.attachChild(player2);

        pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene

        pivot.attachChild(player);
        pivot.attachChild(player2);
    }

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        // make the player rotate:
        player.rotate(0, 2*tpf, 0);
        player2.rotate(0, 4*tpf, 0);
        pivot.rotate(2*tpf,0,0);
    }
}