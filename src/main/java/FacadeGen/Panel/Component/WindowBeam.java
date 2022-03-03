package FacadeGen.Panel.Component;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class WindowBeam {


    public static final int VERTICAL = 0, HORIZON = 1;
    private int type;
    private double[] Pos;
    private double width;
    private double depth;
    private Window window;

    public WindowBeam(int type, double[] pos, double width, double depth) {
        this.type = type;
        Pos = pos;
        this.width = width;
        this.depth = depth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getPos() {
        return Pos;
    }

    public void setPos(int[] pos) {
        Pos = pos;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
