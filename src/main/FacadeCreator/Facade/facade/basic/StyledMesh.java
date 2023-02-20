package Facade.facade.basic;

import processing.opengl.PGraphicsOpenGL;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.util.List;

public class StyledMesh extends StyledGeometry {
    HE_Mesh mesh;
    Material material;
    int strokeColor = 0;

    public StyledMesh(Material material) {
        this.material = material;
        if (material == Material.Glass)
            transparent = true;
    }

    public StyledMesh(Material material, int strokeColor) {
        this.material = material;
        this.strokeColor = strokeColor;
        if (material == Material.Glass)
            transparent = true;
    }

    public StyledMesh add(HE_Mesh other) {
        if (this.mesh == null) {
            this.mesh = other.copy();

        } else
            this.mesh.add(other);
        this.mesh.setFaceColor(material.getColor());
        return this;
    }

    public StyledMesh addAll(List<HE_Mesh> meshList) {
        if (meshList.size() == 0)
            return this;
        if (this.mesh == null)
            this.mesh = meshList.get(0).copy();
        else
            this.mesh.add(meshList.get(0));
        for (int i = 1; i < meshList.size(); i++) {
            this.mesh.add(meshList.get(i));
        }
        this.mesh.setFaceColor(material.getColor());
        return this;
    }

    public void setMesh(HE_Mesh mesh) {
        this.mesh = mesh;
    }

    public HE_Mesh getMesh() {
        return mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    @Override
    public void draw(WB_Render3D render3D) {
        PGraphicsOpenGL home = render3D.getHome();
        home.pushStyle();
        home.fill(0);
        home.noStroke();
        render3D.drawFacesFC(mesh);
        if (strokeColor >= 0)
            home.stroke(strokeColor);
        home.noFill();
        render3D.drawEdges(mesh);
        render3D.drawFaceNormals(mesh, 200);
        home.popStyle();
    }
}
