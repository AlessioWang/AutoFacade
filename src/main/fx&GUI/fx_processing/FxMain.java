package fx_processing;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class FxMain extends FxInSwing<NewMain> {

    //程序运行基本参数
    int width = 1800;
    int height = 1080;

    NewLeftController leftController;

    /**
     * 构造FXInSwing
     *
     * @param applet 继承FXPApplet类的实例
     */
    protected FxMain(NewMain applet) {
        super(applet);
    }

    /**
     * 主程序标准启动格式
     *
     * @param args args
     */
    public static void main(String[] args) {
        FxMain sample = new FxMain(new NewMain());
        sample.launch("MyFxTest");
    }

    @Override
    protected void settings() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int w = (int) primaryScreenBounds.getWidth();
        int h = (int) primaryScreenBounds.getHeight();

        frame.setSize(width, height);
    }

    @Override
    protected Scene createLeft() {
        leftController = new NewLeftController(applet);
        Scene scene = new Scene(leftController);
        return scene;
    }

    @Override
    protected Scene createRight() {
        return null;
    }

    @Override
    protected Scene createTop() {
        return null;
    }

    @Override
    protected Scene createBottom() {
        return null;
    }

    @Override
    protected void initialize() {
        synchronized (applet) {
            try {
                applet.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void runAnotherFX() {

    }
}
