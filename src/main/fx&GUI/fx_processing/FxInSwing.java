package fx_processing;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import processing.core.PApplet;
import processing.opengl.PSurfaceJOGL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * @program: Sample3D
 * @description: 将Processing和JavaFX整合进Swing窗口的类，主程序需继承该类，并设定窗口大小和JavaFX_GUI
 * @author: Naturalpowder
 * @create: 2021-03-15 11:41
 **/
public abstract class FxInSwing<T extends FXPApplet> {
    /**
     * Processing主程序
     */
    protected final T applet;
    /**
     * Processing画布
     */
    protected Component canvas;

    /**
     * 四周放置JavaFX的面板
     */
    protected JFXPanel top, bottom, right, left;

    /**
     * 主窗口
     */
    protected JFrame frame;
    /**
     * 窗口标题
     */
    private String title = "FX & Processing";
    /**
     * 图标文件路径
     */
    private String iconPath;
    /**
     * 是否等PApplet执行完再继续执行其他线程
     */
    private boolean invokeAndWait = false;

    static {
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
    }


    /**
     * 构造FXInSwing
     *
     * @param applet 继承FXPApplet类的实例
     */
    protected FxInSwing(T applet) {
        this.applet = applet;
    }

    /**
     * 初始化参数
     */
    private void initAndShowGUI() {
        initPanels();
        initIcon();
        settings();
        initCanvas();
        initLayout();
        runScene();
        initialize();
    }

    /**
     * 初始化Swing面板
     */
    private void initPanels() {
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        top = new JFXPanel();
        bottom = new JFXPanel();
        right = new JFXPanel();
        left = new JFXPanel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
//                    Runtime.getRuntime().exec("tskill dimGen");
                    String command = "cmd.exe /c c:\\windows\\system32\\taskkill /f /im  dimGen.exe";
                    Process proc = Runtime.getRuntime().exec(command);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                super.windowClosing(e);
            }
        });
    }

    /**
     * 初始化Processing画布
     */
    private void initCanvas() {
        if (invokeAndWait) {
            try {
                SwingUtilities.invokeAndWait(this::getCanvasFromProcessing);
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            SwingUtilities.invokeLater(this::getCanvasFromProcessing);
        }
    }

    /**
     * 从Processing获取画布内容
     */
    private void getCanvasFromProcessing() {
        PSurfaceJOGL ps = (PSurfaceJOGL) applet.initSurface(PApplet.P3D);
        ps.initOffscreen(applet);
        ps.setLocation(0, 0);
        // add canvas to JFrame (used as a Component)
        canvas = ps.getComponent();
        canvas.setSize(frame.getWidth(), frame.getHeight());
//        canvas.setSize(300,300);
        frame.add(canvas, BorderLayout.CENTER);
    }


    /**
     * 初始化窗口布局
     */
    private void initLayout() {
        frame.add(top, BorderLayout.NORTH);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.add(right, BorderLayout.EAST);
        frame.add(left, BorderLayout.WEST);
    }

    static class MyWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }


    /**
     * 设置窗口内各窗格内容
     */
    private void runScene() {
        Platform.runLater(() -> {
            setScene();
        });
    }

    private void setScene() {
        //主程序模块
        Scene scene_top = createTop();
        Scene scene_right = createRight();
        Scene scene_bottom = createBottom();
        Scene scene_left = createLeft();

        top.setScene(scene_top);
        bottom.setScene(scene_bottom);
        right.setScene(scene_right);
        left.setScene(scene_left);
    }

    /**
     * 设置窗口图标路径
     *
     * @param iconPath 图标文件路径
     */
    public void setIcon(String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * 初始化窗口图标
     */
    private void initIcon() {
        if (iconPath != null) {
            URL imgURL = getClass().getResource(iconPath);
            ImageIcon imageIcon = new ImageIcon(imgURL);
            frame.setIconImage(imageIcon.getImage());
        }
    }

    /**
     * 设置是否等Processing执行完再执行其他线程
     *
     * @param invokeAndWait True为等待，False为同时执行
     */
    public void setInvokeAndWait(boolean invokeAndWait) {
        this.invokeAndWait = invokeAndWait;
    }

    /**
     * 设定窗口大小
     */
    protected abstract void settings();

    /**
     * 设置左侧窗格场景
     *
     * @return 对应场景
     */
    protected abstract Scene createLeft();

    /**
     * 设置右侧窗格场景
     *
     * @return 对应场景
     */
    protected abstract Scene createRight();

    /**
     * 设置顶部窗格场景
     *
     * @return 对应场景
     */
    protected abstract Scene createTop();

    /**
     * 设置底部窗格场景
     *
     * @return 对应场景
     */
    protected abstract Scene createBottom();

    protected abstract void initialize();

    protected abstract void runAnotherFX();

    /**
     * 启动主程序
     *
     * @param title 窗口标题
     */
    public void launch(String title) {
        this.title = title;
        initAndShowGUI();
    }
}
