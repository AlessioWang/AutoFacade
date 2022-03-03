package FacadeGen.Panel;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public abstract class Panel {

    public static final int SIMPLE_WALL = 0, WINDOW_WALL = 1, HANDRAIL_WALL = 2;
    private int style;





    public Panel(int style) {
        this.style = style;
    }




}
