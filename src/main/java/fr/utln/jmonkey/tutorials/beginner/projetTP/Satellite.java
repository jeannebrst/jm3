package fr.utln.jmonkey.tutorials.beginner.projetTP;

import com.jme3.math.FastMath;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.asset.AssetManager;
import java.util.*;
import com.jme3.math.Vector3f;

public class Satellite {
	private String nom;
	private Geometry satelliteGeo;
	private Planet parent;
	private float taille;
	private float demiPetitAxe;
	private float demiGrandAxe;
	private float vitesseRevolution;
	private float vitesseRotation;
	private float angle = 0;

	// public Satellite(AssetManager assetManager, String nom, Planet parent, float taille, float demiPetitAxe, float demiGrandAxe, float vitesseRevolution, float vitesseRotation) {
	// 	this.nom = nom;
	// 	this.parent = parent;
	// 	this.taille = taille;
	// 	this.demiPetitAxe = demiPetitAxe;
	// 	this.demiGrandAxe = demiGrandAxe;
	// 	this.vitesseRevolution = vitesseRevolution;
	// 	this.vitesseRotation = vitesseRotation;

	// 	initAxes();
	// 	initSatellite(assetManager);
	// }

	// private void initSatellite(AssetManager assetManager) {
	// 	Sphere sphere = new Sphere(32,32,taille);
	// 	satelliteGeo = new Geometry("planete", sphere);

	// 	Material mat = new Material(assetManager,
	// 	"Common/MatDefs/Light/Lighting.j3md");
	// 	mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/"+nom+".jpg"));
	// 	satelliteGeo.setMaterial(mat);
	// 	satelliteGeo.rotate(-FastMath.PI/2,0,0);
	// }

	// private void initAxes() {
	// 	Node axeSatellite = parent.getAxePlanete();
	// 	axeSatellite.setLocalTranslation(0,0,0);
	// 	axeSatellite.attachChild(satelliteGeo);

	// 	// orbitePlanete = new Node("orbitePlanete");
	// 	// orbitePlanete.attachChild(axePlanete);
	// }

	// public void update(float tpf) {
	// 	angle += vitesseRevolution * tpf * FastMath.TWO_PI;
	// 	if (angle > FastMath.TWO_PI) {
	// 		angle -= FastMath.TWO_PI;
	// 	}

	// 	float x = demiGrandAxe * FastMath.cos(angle);
	// 	float z = demiPetitAxe * FastMath.sin(angle);

	// 	// La Lune tourne autour de la Terre, et la Terre tourne autour du Soleil
	// 	Vector3f parentPos = parent.getPosition();
	// 	satelliteGeo.setLocalTranslation(parentPos.x + x, 0, parentPos.z + z);
	// }
}