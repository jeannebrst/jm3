package fr.utln.jmonkey.tutorials.beginner.prof;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.math.ColorRGBA;

/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class HelloWorld extends SimpleApplication {

    /**
     * The main method.
     * @param args the main method arguments
     */
    public static void main(String[] args){
    	
    	AppSettings settings=new AppSettings(true);
    	
        HelloWorld app = new HelloWorld();
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start(); // start the game
    }

    /**
	 * The default constructor. 
	 */
    public HelloWorld(){
	}

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1); // create cube shape
        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(geom);              // make the cube appear in the scene
    }
}