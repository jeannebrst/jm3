package fr.utln.jmonkey.tutorials.beginner.projetTP;


import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.util.LinkedList;

public class Orbite {
    private static final int MAX_POINTS = 500; // Nombre max de points pour la trace
    private LinkedList<Vector3f> positions;
    private Mesh orbitMesh;
    private Geometry orbitGeom;
    private Node parentNode;
    
    public Orbite(Node parentNode, Material material) {
        this.positions = new LinkedList<>();
        this.parentNode = parentNode;
        
        // Création du mesh
        orbitMesh = new Mesh();
        orbitMesh.setMode(Mesh.Mode.LineStrip);
        orbitGeom = new Geometry("Orbit", orbitMesh);
        orbitGeom.setMaterial(material);
        
        parentNode.attachChild(orbitGeom);
    }
    
    public void updateOrb(Vector3f planetPosition) {
        positions.add(new Vector3f(planetPosition));
        if (positions.size() > MAX_POINTS) {
            positions.removeFirst(); // Supprime les anciens points pour éviter surcharge
        }
        
        updateMesh();
    }
    
    private void updateMesh() {
        Vector3f[] pointsArray = positions.toArray(new Vector3f[0]);
        orbitMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(pointsArray));
        orbitMesh.updateBound();
        orbitMesh.updateCounts();
    }
}
