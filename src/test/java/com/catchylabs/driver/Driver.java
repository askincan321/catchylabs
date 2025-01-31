package com.catchylabs.driver;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.openqa.selenium.WebDriver;


import java.nio.file.FileSystems;
import java.util.concurrent.ConcurrentHashMap;

public class Driver {
    public static WebDriver driver;
    public static String browser = "chrome";
    public static String url = "https://catchylabs-webclient.testinium.com/signIn";
    public static String userDir = System.getProperty("user.dir");
    public static String slash = FileSystems.getDefault().getSeparator();
    public static ConcurrentHashMap<String,Object> TestMap;

    @BeforeScenario
    public void beforeScenario()
    {
       driver = Browsers.getWebDriver(browser);
       driver.manage().window().maximize();
       driver.get(url);
       TestMap = new ConcurrentHashMap<String, Object>();

    }
    @AfterScenario
    public void afterScenario()
    {
        if(driver!=null)
            driver.quit();
    }


}
