package fx_processing;

import processing.core.PApplet;
import processing.core.PSurface;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class FXPApplet extends PApplet {

    /**
     * 重写initSurface方法，可直接调用生成PSurface而无需使用runSketch()方法
     *
     * @param renderer 渲染器类型，例：P3D
     * @return PSurface，用于对接Swing
     */
    public PSurface initSurface(String renderer) {
        this.g = makeGraphics(sketchWidth(), sketchHeight(), renderer, sketchPath(), true);
        this.surface = this.g.createSurface();
        if (this.g.displayable()) {
            this.surface.initFrame(this);
            this.surface.setTitle(this.getClass().getSimpleName());
        } else {
            this.surface.initOffscreen(this);
        }
        this.surface.setResizable(false);
        this.surface.startThread();
        return this.surface;
    }
}

