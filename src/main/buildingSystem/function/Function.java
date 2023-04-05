package function;

/**
 * Unit单元的预置功能
 *
 * @auther Alessio
 * @date 2023/2/24
 **/
public enum Function {

    Default("default"),

    ClassRoom("classroom"),

    Stair("stair"),

    Transport("transport"),

    Roof("roof"),

    InnerWall("innerWall"),

    Floor("floor"),

    Open("open"),

    Handrail("handrail"),

    Office("office"),

    Toilet("toilet");

    private String function;

    Function(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public static Function[] getAllFuncTypes() {
        return Function.values();
    }
}
