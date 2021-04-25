package util.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertiesHelper {

    public static String getProperty(String file, String property) throws IOException {
        InputStream input = FilePropertiesHelper.class.getClassLoader().getResourceAsStream(file);
        if(input == null){
            System.out.println("Error when opening the property file!");
            return "";
        }
        Properties prop = new Properties();
        prop.load(input);
        return prop.getProperty(property);
    }

    public static void setProperties(String file) throws IOException {
        InputStream input = FilePropertiesHelper.class.getClassLoader().getResourceAsStream(file);
        if(input == null){
            System.out.println("Error when opening the property file!");
        }
        Properties prop = new Properties();
        prop.load(input);
        prop.keySet().forEach(
                item -> System.setProperty(item.toString(), prop.getProperty(item.toString()))
        );
    }
}
