package fr.utln.jmonkey.tutorials.beginner.projetTP;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

public class Orbite {
    private Geometry orbiteGeo;
    private Node orbiteNode;
    private float demiGrandAxe;
    private float excentricite;

    public Orbite(AssetManager assetManager, float demiGrandAxe, float excentricite, String nom) {
        this.demiGrandAxe = demiGrandAxe;
        this.excentricite = excentricite;
        this.orbiteNode = new Node("Orbite");
        initOrbite(assetManager,nom);
    }

    private void initOrbite(AssetManager assetManager, String nom) {
        int segments = 256;
        Vector3f[] points = new Vector3f[segments + 1];
        float demiPetitAxe = demiGrandAxe * FastMath.sqrt(1 - excentricite * excentricite);
        float focalOffset = excentricite * demiGrandAxe;

        for (int i = 0; i <= segments; i++) {
            float angle = i * FastMath.TWO_PI / segments;
            float x = demiGrandAxe * FastMath.cos(angle) - focalOffset;
            float z = demiPetitAxe * FastMath.sin(angle);
            points[i] = new Vector3f(x, 0, z);
        }

        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.LineStrip);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(points));
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(generateIndices(segments)));
        mesh.updateBound();
        mesh.setStatic();

        orbiteGeo = new Geometry("OrbiteGeo", mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/"+nom+".jpg"));
        orbiteGeo.setMaterial(mat);

        orbiteNode.attachChild(orbiteGeo);
    }

    private int[] generateIndices(int segments) {
        int[] indices = new int[segments + 1];
        for (int i = 0; i <= segments; i++) {
            indices[i] = i;
        }
        return indices;
    }

    public Node getOrbiteNode() {
        return orbiteNode;
    }
}
