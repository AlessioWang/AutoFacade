package Facade.facade.basic;

import java.awt.*;

public enum Material{
    White(0xFFFFFFFF),
    Black(0xFF000000),
    LightGray(0xFFc3c3c3),
    MidGray(0xFF969696),
    DarkGray(0xFF545454),
    Glass(0x107c99b3);

    private int color;

    private Material(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public static int RGBtoInt(int... rgba) {
        switch (rgba.length) {
            case 1:
                return new Color(rgba[0]).getRGB();
            case 2:
                return new Color(rgba[0], rgba[0], rgba[0], rgba[1]).getRGB();
            case 3:
                return new Color(rgba[0], rgba[1], rgba[2]).getRGB();
            case 4:
                return new Color(rgba[0], rgba[1], rgba[2], rgba[3]).getRGB();
            default:
                return Color.RED.getRGB();
        }
    }
}

