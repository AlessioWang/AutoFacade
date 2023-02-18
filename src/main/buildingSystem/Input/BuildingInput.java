package Input;

import Tools.DxfReader.DXFImporter;

/**
 * 从dxf文件初始化建筑信息
 *
 * @auther Alessio
 * @date 2023/2/18
 **/
public class BuildingInput {
    private final String path;
    private DXFImporter importer;

    public BuildingInput(String path) {
        this.path = path;

    }


}
