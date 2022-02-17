package FacadeGen.Unit;

import FacadeGen.Cell;
import FacadeGen.Panel.PanelFactory;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class OfficeUnit extends Unit  implements Panelable {

    public OfficeUnit(List<Cell> cells, int index) {
        super(cells, index);
        function = OFFICE;
    }

    @Override
    public void setPanel(PanelFactory factory, Integer style) {
        this.panel = factory.createPanel(style);
    }

}
