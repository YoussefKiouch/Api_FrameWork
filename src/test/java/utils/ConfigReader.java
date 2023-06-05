package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            String path ="src/test/resources/application.properties";

            //File IO
            FileInputStream inputStream = new FileInputStream(path);

            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProperty(String Key){
        return properties.getProperty(Key).trim();
    }
}
