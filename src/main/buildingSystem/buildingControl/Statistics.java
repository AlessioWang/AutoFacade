package buildingControl;

import unit2Vol.Building;

/**
 * @auther Alessio
 * @date 2023/3/30
 **/
public class Statistics {

    private Building building;

    private int allPanelNumber;

    private int outPanelNumber;

    private int innerPanelNumber;

    private int floorPanelNumber;

    private int roofPanelNumber;

    private double allConcreteVol;

    private double outConVol;

    private double innerConVol;

    private double floorConVol;

    private double roofConVol;

    public Statistics(Building building) {
        this.building = building;

        syncData();
    }

    public Statistics() {

        syncData();
    }

    public void syncData() {
        updateAllNum();
        updateVol();
    }

    private void updateAllNum() {
        allPanelNumber = outPanelNumber + innerPanelNumber + floorPanelNumber + roofPanelNumber;
    }

    private void updateVol() {
        allConcreteVol = outConVol + innerConVol + floorConVol + roofConVol;
    }

    public int getOutPanelNumber() {
        return outPanelNumber;
    }

    public void setOutPanelNumber(int outPanelNumber) {
        this.outPanelNumber = outPanelNumber;
    }

    public int getInnerPanelNumber() {
        return innerPanelNumber;
    }

    public void setInnerPanelNumber(int innerPanelNumber) {
        this.innerPanelNumber = innerPanelNumber;
    }

    public int getFloorPanelNumber() {
        return floorPanelNumber;
    }

    public void setFloorPanelNumber(int floorPanelNumber) {
        this.floorPanelNumber = floorPanelNumber;
    }

    public int getRoofPanelNumber() {
        return roofPanelNumber;
    }

    public void setRoofPanelNumber(int roofPanelNumber) {
        this.roofPanelNumber = roofPanelNumber;
    }

    public double getAllConcreteVol() {
        return allConcreteVol;
    }

    public void setAllConcreteVol(double allConcreteVol) {
        this.allConcreteVol = allConcreteVol;
    }

    public double getOutConVol() {
        return outConVol;
    }

    public void setOutConVol(double outConVol) {
        this.outConVol = outConVol;
    }

    public double getInnerConVol() {
        return innerConVol;
    }

    public void setInnerConVol(double innerConVol) {
        this.innerConVol = innerConVol;
    }

    public double getFloorConVol() {
        return floorConVol;
    }

    public void setFloorConVol(double floorConVol) {
        this.floorConVol = floorConVol;
    }

    public double getRoofConVol() {
        return roofConVol;
    }

    public void setRoofConVol(double roofConVol) {
        this.roofConVol = roofConVol;
    }

    public Building getBuilding() {
        return building;
    }

    public int getAllPanelNumber() {
        return allPanelNumber;
    }
}
