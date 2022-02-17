package FacadeGen.Unit;

import FacadeGen.Panel.PanelFactory;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public interface Panelable {
    public void setPanel(PanelFactory factory, Integer style);
}
