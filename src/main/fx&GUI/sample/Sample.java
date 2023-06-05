package sample;

import Main.LeftController;
import Main.PickFx;
import Main.RightController;
import fx_processing.FXPApplet;
import fx_processing.FxInSwing;
import javafx.scene.Scene;

/**
 * 带有GUI的程序框体标准类写法
 *
 * @auther Alessio
 * @date 2023/6/5
 **/
public class Sample extends FxInSwing<FXPApplet> {

    //程序运行基本参数
    int width = 2000;
    int height = 1200;

    //左右的GUI面板类
    private LeftController leftController;
    private RightController rightController;

    //你自己需要替换的主程序线程
    //需要需要集成FXPApplet
    static FXPApplet yourMainProcess;

    //程序运行框的标题
    private String title = "My Sample";

    /**
     * 构造FXInSwing
     *
     * @param applet 继承FXPApplet类的实例
     */
    protected Sample(FXPApplet applet) {
        super(applet);
    }

    public static void main(String[] args) {
        Sample sample = new Sample(yourMainProcess);
        sample.launch(sample.title);
    }

    @Override
    protected void settings() {
        frame.setSize(width, height);
    }

    /**
     * 初始化你自己的LeftController
     *
     * @return
     */
    @Override
    protected Scene createLeft() {
        //替换为你自己的controller
        leftController = new LeftController((PickFx) applet);
        leftController.setOnAction();
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
