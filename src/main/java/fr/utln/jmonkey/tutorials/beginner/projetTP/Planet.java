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
	private float distanceSoleil;
	private float vitesseRevolution;
	private float vitesseRotation;
	private float angleRevolution;
	private float angleRotation;
	private List<Planet> satellites;

	public Planet(AssetManager assetManager, String nom, float taillePlanet, float distanceSoleil, float vitesseRevolution, float vitesseRotation, float angleRevolution, float angleRotation) {
		this.nom = nom;
		this.taillePlanete = taillePlanet;
		this.distanceSoleil = distanceSoleil;
		this.vitesseRevolution = vitesseRevolution;
		this.vitesseRotation = vitesseRotation;
		this.angleRevolution = angleRevolution;
		this.angleRotation = angleRotation;

		initPlanete(assetManager);
		initAxes();
	}

	private void initPlanete(AssetManager assetManager) {
		Sphere sphere = new Sphere(32,32,taillePlanete);
		planete = new Geometry("planete", sphere);

		Material mat = new Material(assetManager,
		"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/"+nom+".jpg"));
		planete.setMaterial(mat);
		planete.rotate(-FastMath.PI/2,0,0);
	}

	private void initAxes() {
		axePlanete = new Node("axePlanete");
		axePlanete.setLocalTranslation(distanceSoleil,0,0);
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
		orbitePlanete.rotate(0,vitesseRevolution*tpf,0);
		axePlanete.rotate(0,vitesseRotation*tpf,0);

		// angleRevolution += vitesseRevolution*tpf;
		// angleRotation += vitesseRotation*tpf;
		
		// float x = (float) (distanceSoleil*Math.cos(angleRevolution));
		// float z = (float) (distanceSoleil*Math.sin(angleRevolution));
		// planete.setLocalTranslation(x,0,z);
		
		// planete.rotate(0,vitesseRotation*tpf,0);
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

	
}
