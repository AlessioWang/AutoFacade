package FacadeGen.Unit;

import Geo.Cell;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class ClassUnit extends Unit  {

    public ClassUnit(Unit basicUnit){
        super(basicUnit.cells, basicUnit.index);
        function = CLASS;
    }

    public ClassUnit(List<Cell> cells, int index) {
        super(cells, index);
        function = CLASS;
    }

//    @Override
//    public void setPanel(PanelFactory factory, Integer style) {
//        this.panel = factory.createPanel(style);
//    }

}
