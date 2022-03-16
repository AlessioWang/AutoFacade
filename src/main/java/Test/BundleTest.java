package Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @auther Alessio
 * @date 2022/3/15
 **/
public class BundleTest {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("Property/test");

        String name = bundle.getString("name");
        String no = bundle.getString("no");
        String width = bundle.getString("width");

        System.out.println(name + no + width );
    }

}
