package client;

import igeo.*;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class IgeoTest {
    public static void main(String[] args) {
        IG.init();
        IG.open("C:\\Users\\Alessio\\Desktop\\tt.3dm");
        int num = IG.layer("geo").getSurfaceNum();
        System.out.println(num);
        ICurve curve = IG.layer("geo").getCurve(0);
        System.out.println(curve);
        IMesh mesh = IG.layer("geo").getMesh(0);
//        IMesh mesh = IG.layer("geo").mesh(0);
        System.out.println(mesh.edge(0));
        ISurface surface = IG.layer("geo").getSurface(0);
        System.out.println(surface);
    }

}
