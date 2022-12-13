package facadeGen.Panel;

/**
 * @auther Alessio
 * @date 2022/12/13
 **/
public interface PanelBaseAble {
    private void init() {
        initInfo();
        initShape();
        initDir();
    }

    void initDir();

    void initShape();

    void initInfo();

}
