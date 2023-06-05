package demo;


import fx_processing.FXPApplet;
import fx_processing.FxInSwing;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class FxMain extends FxInSwing<FXPApplet> {

    //程序运行基本参数
    int width = 2000;
    int height = 1200;

    LeftController leftController;
    RightController rightController;

    /**
     * 构造FXInSwing
     *
     * @param applet 继承FXPApplet类的实例
     */
    protected FxMain(FXPApplet applet) {
        super(applet);
    }

    /**
     * 主程序标准启动格式
     *
     * @param args args
     */
    public static void main(String[] args) {
        FxMain sample = new FxMain(new PickFx());
//        FxMain sample = new FxMain(new PickSY());
        sample.launch("Facade Creator");
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
        leftController = new LeftController((PickFx) applet);
//        leftController = new LeftController((PickSY) applet);
        leftController.setOnAction();
        Scene scene = new Scene(leftController);
        return scene;
    }

    @Override
    protected Scene createRight() {
        rightController = new RightController((PickFx) applet);
//        rightController = new RightController((PickSY) applet);
        rightController.setOnAction();
        Scene scene = new Scene(rightController);
        return scene;
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
