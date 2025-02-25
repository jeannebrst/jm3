package fr.utln.jmonkey.tutorials.beginner.projetTP;


import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import java.util.*;

import java.nio.FloatBuffer;

public class Planet {
	private String nom;
	private Geometry planete;
	private Node axePlanete;
	private Node orbitePlanete;
	private Node anneaux;
	private float taillePlanete;
	private float vitesseRevolution;
	private float vitesseRotation;
	private float demiGrandAxe;
	private float angle = 0;
	private float excentricite;
	private List<Planet> satellites;

	public Planet(AssetManager assetManager, String nom, float taillePlanete, float vitesseRevolution, float vitesseRotation, float demiGrandAxe, float excentricite) {
		this.nom = nom;
		this.taillePlanete = taillePlanete;
		this.vitesseRevolution = vitesseRevolution;
		this.vitesseRotation = vitesseRotation;
		this.excentricite = excentricite;
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
		sphere.setTextureMode(Sphere.TextureMode.Projected);
		planete = new Geometry("planete", sphere);

		Material mat = new Material(assetManager,
		"Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/"+nom+".jpg"));
		planete.setMaterial(mat);
		planete.rotate(-FastMath.PI/2,0,0);
	}

	private void initAxes() {
		float focalOffset = excentricite*demiGrandAxe;
		axePlanete = new Node("axePlanete");
		axePlanete.setLocalTranslation(0,0,0);
		axePlanete.attachChild(planete);

		orbitePlanete = new Node("orbitePlanete");
		orbitePlanete.setLocalTranslation(-focalOffset,0,0);
		orbitePlanete.attachChild(axePlanete);
	}

	public void addSatellites(Planet satellite) {
		if (satellites == null) {
			satellites = new ArrayList<>();
		}
		this.satellites.add(satellite);
		this.axePlanete.attachChild(satellite.axePlanete);
	}

	public void addRings(AssetManager assetManager, String nomTexture) {
			Mesh ringMesh = new Mesh();
			int numPoints = 800000;
			FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(numPoints * 3);
	
			// float innerRingRadius = taillePlanete + 10f;
			// float outerRingRadius = taillePlanete + 60f;

			float innerRingRadius = 66;
			float outerRingRadius = 120;
	
			for (int i = 0; i < numPoints; i++) {
				float angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
				float radius = innerRingRadius + FastMath.nextRandomFloat() * (outerRingRadius - innerRingRadius);
				float x = FastMath.cos(angle) * radius;
				float y = 0;
				float z = FastMath.sin(angle) * radius;
				vertexBuffer.put(x).put(y).put(z);
			}
	
			ringMesh.setBuffer(VertexBuffer.Type.Position, 3, vertexBuffer);
			ringMesh.setMode(Mesh.Mode.Points);
			ringMesh.updateBound();
			ringMesh.updateCounts();
	
			Geometry ringGeo = new Geometry("Saturn Rings", ringMesh);
			Material ringMat = new Material(assetManager,
			"Common/MatDefs/Light/Lighting.j3md");
			ringMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/"+nomTexture+".png"));
			ringGeo.setMaterial(ringMat);
	
			anneaux = new Node("anneaux");
			anneaux.attachChild(ringGeo);
			axePlanete.attachChild(anneaux);
		}

	public void update(float tpf) {
		angle += vitesseRevolution*tpf*FastMath.TWO_PI;
		if (angle > FastMath.TWO_PI) {
			angle -= FastMath.TWO_PI;
		}
		float x = demiGrandAxe*FastMath.cos(angle);
		float z = demiGrandAxe*FastMath.sqrt(1-excentricite*excentricite)*FastMath.sin(angle);

		axePlanete.setLocalTranslation(x,0,z);

		axePlanete.rotate(0,vitesseRotation*tpf,0);
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

	public List<Planet> getSatellites() {
		return satellites;
	}

	public Node getAxePlanete() {
		return axePlanete;
	}

	public float getExcentricite() {
		return excentricite;
	}

	public float getGrandAxe() {
		return demiGrandAxe;
	}
}
