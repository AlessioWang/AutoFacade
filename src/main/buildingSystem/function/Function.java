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
    Transport("transport");

    private String function;

    Function(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

}