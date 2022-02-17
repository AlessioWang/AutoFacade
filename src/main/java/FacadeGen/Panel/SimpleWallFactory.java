package FacadeGen.Panel;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2021/12/16
 **/
public class SimpleWallFactory implements PanelFactory{

    private Map<Integer, SimpleWallPnl> repo = new HashMap<Integer, SimpleWallPnl>();

    @Override
    public SimpleWallPnl createPanel(Integer style) {
        SimpleWallPnl panel = repo.get(style);
        if (panel == null) {
            panel = new SimpleWallPnl(style);
            repo.put(style, panel);
            return panel;
        }
        return panel;
    }


}
