package Facade.manager;

import Facade.facade.basic.BasicObject;
import Facade.facade.basic.ControlPanel;
import archijson.ArchiJSON;
import com.google.gson.Gson;

import processing.core.PApplet;
import wblut.processing.WB_Render3D;

public abstract class Generator{
   BasicObject obj;
   ControlPanel panel;

   public Generator(PApplet app){
       initObject();
       if (Config.LOCAL_PANEL){
           panel = new ControlPanel(app,ControlPanel.Mode.Slider);
           panel.updatePanel(obj,obj.getClass().getName());
           System.out.println("panel created");
       }
   }
    public abstract void initObject();
    public abstract void receive(ArchiJSON archiJSON);
    public abstract ArchiJSON toArchiJSON(Gson gson);

    public void localDraw(WB_Render3D render3D){
        obj.draw(render3D);
        render3D.getHome().camera();
    }

}
