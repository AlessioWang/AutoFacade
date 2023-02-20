package Facade.facade.unit;



import Facade.facade.basic.BasicObject;
import Facade.facade.unit.styles.F_Example;
import Facade.facade.unit.styles.F_WindowArray;
import Facade.facade.unit.styles.S_ExtrudeIn;

public enum UnitStyles {
    LONG(F_Example.class),
    WIN(F_WindowArray.class),
    EXTRUDE(S_ExtrudeIn.class);

    Class<?extends BasicObject> classname;
    private UnitStyles(Class<?extends BasicObject> classname){
        this.classname = classname;
    }

    public Class<? extends BasicObject> getClassname() {
        return classname;
    }
}
