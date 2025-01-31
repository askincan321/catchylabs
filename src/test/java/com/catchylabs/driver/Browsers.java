package com.catchylabs.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Browsers {
    public static WebDriver getWebDriver(String browser)
    {
        WebDriver driver=null;
    switch(browser)
    {
        case "chrome":
            driver=new ChromeDriver();
            break;
        case "edge":
            driver=new EdgeDriver();
            break;
        case "firefox":
            driver=new FirefoxDriver();
            break;
        default:
            throw new NullPointerException("Tarayıcı Bulunamadı");

    }
    return driver;
    }
}
