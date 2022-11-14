package facadeGen.Unit;

import facadeGen.Geo.Cell;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class OfficeUnit extends Unit  implements PanelSetAble {

    public OfficeUnit(List<Cell> cells, int index) {
        super(cells, index);
        function = OFFICE;
    }

//    @Override
//    public void setPanel(PanelFactory factory, Integer style) {
//        this.panel = factory.createPanel(style);
//    }

}
