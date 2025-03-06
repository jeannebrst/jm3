package fr.utln.jmonkey.tutorials.beginner.projetTP;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Arrays;
import com.jme3.scene.Node;

import java.util.Random;

public class Asteroide {

	private final float minRadius = 5000;
	private final float maxRadius = 8000;
    private final int numAsteroids = 500;
	private String[] asteroidModels;
    private Spatial[] asteroidSpatials;

    // Constructeur
    public Asteroide(AssetManager assetManager,String[] asteroidModels) {
		this.asteroidModels = asteroidModels;
        asteroidSpatials = new Spatial[asteroidModels.length];
        for (int i = 0; i < asteroidModels.length; i++) {
            asteroidSpatials[i] = assetManager.loadModel(asteroidModels[i]);
    }
    }

    // Générer un nuage de points dans l'anneau et ajouter les Spatials
    public void generateAsteroids(Node noeud) {
        Random random = new Random();

        for (int i = 0; i < numAsteroids; i++) {
            // Calculer une position aléatoire dans l'anneau
            float distance = minRadius + random.nextFloat() * (maxRadius - minRadius); // Rayon entre min et max
            float angle = random.nextFloat() * 2 * (float) Math.PI; // Angle autour du centre

            // Calculer les coordonnées X et Z pour la position dans le plan
            float x = distance * (float) Math.cos(angle);
            float z = distance * (float) Math.sin(angle);

            // Ajouter une petite variation sur l'axe Y
            float y = (random.nextFloat() - 0.5f) * 100f; // Variation aléatoire entre -2.5 et 2.5

            // Positionner l'astéroïde à cette position
            Vector3f position = new Vector3f(x, y, z);

			String modelPath = asteroidModels[random.nextInt(asteroidModels.length)];
            int index = Arrays.asList(asteroidModels).indexOf(modelPath); // Trouver l'index
            Spatial asteroid = asteroidSpatials[index].clone();

            // Charger un modèle 3D pour représenter l'astéroïde, par exemple une sphère
            asteroid.setLocalTranslation(position);
			asteroid.setLocalScale(0.005f);
			noeud.attachChild(asteroid);
        }
    }
}
