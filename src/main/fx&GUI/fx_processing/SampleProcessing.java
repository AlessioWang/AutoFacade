package fx_processing;

import guo_cam.CameraController;
import processing.core.PApplet;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class SampleProcessing  extends FXPApplet  {
    private CameraController camera;

    @Override
    public void settings() {
        size(800, 600, P3D);
    }

    @Override
    public void setup() {
        camera = new CameraController(this);
    }

    @Override
    public void draw() {
        background(255);
        camera.drawSystem(1000);
        box(100);
    }

    public CameraController getCamera() {
        return camera;
    }

    public static void main(String[] args) {
        PApplet.main(SampleProcessing.class.getName());
    }
}
