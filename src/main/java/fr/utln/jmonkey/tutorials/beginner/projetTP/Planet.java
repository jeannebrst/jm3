package fr.utln.jmonkey.tutorials.beginner.projetTP;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.asset.AssetManager;
import java.util.*;

public class Planet {
	private String nom;
	private Geometry planete;
	private Node axePlanete;
	private Node orbitePlanete;
	private float taillePlanete;
	private float vitesseRevolution;
	private float vitesseRotation;
	private float demiPetitAxe;
	private float demiGrandAxe;
	private float angle = 0;
	private List<Planet> satellites;

	public Planet(AssetManager assetManager, String nom, float taillePlanete, float vitesseRevolution, float vitesseRotation, float demiPetitAxe, float demiGrandAxe) {
		this.nom = nom;
		this.taillePlanete = taillePlanete;
		this.vitesseRevolution = vitesseRevolution;
		this.vitesseRotation = vitesseRotation;
		this.demiPetitAxe = demiPetitAxe;
		this.demiGrandAxe = demiGrandAxe;

		initPlanete(assetManager);
		initAxes();
	}

	public Planet(AssetManager assetManager, float taillePlanete, float vitesseRotation) {
		this.taillePlanete = taillePlanete;
		this.vitesseRotation = vitesseRotation;

		Sphere sphere = new Sphere(32,32,taillePlanete);
		planete = new Geometry("planete", sphere);

		Material mat = new Material(assetManager,
		"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/Soleil.jpg"));
		planete.setMaterial(mat);
		planete.rotate(-FastMath.PI/2,0,0);

		axePlanete = new Node("axePlanete");
		axePlanete.setLocalTranslation(0,0,0);
		axePlanete.attachChild(planete);

		orbitePlanete = new Node("orbitePlanete");
		orbitePlanete.attachChild(axePlanete);
	}

	private void initPlanete(AssetManager assetManager) {
		Sphere sphere = new Sphere(32,32,taillePlanete);
		planete = new Geometry("planete", sphere);

		Material mat = new Material(assetManager,
		"Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/"+nom+".jpg"));
		planete.setMaterial(mat);
		planete.rotate(-FastMath.PI/2,0,0);
	}

	private void initAxes() {
		axePlanete = new Node("axePlanete");
		axePlanete.setLocalTranslation(0,0,0);
		axePlanete.attachChild(planete);

		orbitePlanete = new Node("orbitePlanete");
		orbitePlanete.attachChild(axePlanete);
	}

	public void addSatellites(Planet satellite) {
		if (satellites == null) {
			satellites = new ArrayList<>();
		}
		this.satellites.add(satellite);
		this.axePlanete.attachChild(satellite.axePlanete);
	}

	public void update(float tpf) {
		angle += vitesseRevolution*tpf*FastMath.TWO_PI;
		if (angle > FastMath.TWO_PI) {
			angle -= FastMath.TWO_PI;
		}
		float x = demiGrandAxe*FastMath.cos(angle);
		float z = demiPetitAxe*FastMath.sin(angle);

		axePlanete.setLocalTranslation(x,0,z);
	}

	public Node getOrbitePlanete() {
		return orbitePlanete;
	}

	public void setOrbitePlanete(Node orbitePlanete) {
		this.orbitePlanete = orbitePlanete;
	}

	public Geometry getPlanete() {
		return planete;
	}

	public void setPlanete(Geometry planete) {
		this.planete = planete;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getTaillePlanete() {
		return taillePlanete;
	}

	public void setTaillePlanete(float taillePlanete) {
		this.taillePlanete = taillePlanete;
	}

	
}
