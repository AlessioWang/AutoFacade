package client.entity;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class WallPanelEntity {
    private int w_index;
    private String panel_style;
    private double posX;
    private double posY;
    private double posZ;
    private double dirX;
    private double dirY;
    private double dirZ;

    public int getW_index() {
        return w_index;
    }

    public void setW_index(int w_index) {
        this.w_index = w_index;
    }

    public String getPanel_style() {
        return panel_style;
    }

    public void setPanel_style(String panel_style) {
        this.panel_style = panel_style;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getDirX() {
        return dirX;
    }

    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    public double getDirZ() {
        return dirZ;
    }

    public void setDirZ(double dirZ) {
        this.dirZ = dirZ;
    }

    @Override
    public String toString() {
        return "WallPanel{" +
                "index=" + w_index +
                ", style='" + panel_style + '\'' +
                ", pos: " + posX +
                ", " + posY +
                ", " + posZ +
                ", dir: " + dirX +
                ", " + dirY +
                ", " + dirZ +
                '}';
    }
}
