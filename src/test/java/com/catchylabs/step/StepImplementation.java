package com.catchylabs.step;

import com.catchylabs.driver.Driver;
import com.catchylabs.methods.Methods;
import com.catchylabs.methods.MethodsUtil;
import com.thoughtworks.gauge.Step;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.ArrayList;

import static com.catchylabs.driver.Driver.driver;

public class StepImplementation {

    Methods methods;
    MethodsUtil methodsUtil;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public StepImplementation() {

        methods = new Methods();
        methodsUtil = new MethodsUtil();
    }

    @Step("<key> elementine tıkla")
    public void clickElement(String key) {

        methods.clickElement(methods.getBy(key));
    }

    @Step("<key> elementine çift tıkla")
    public void doubleClickElementWithAction(String key) {

        methods.doubleClickElementWithAction(methods.getBy(key), true);
    }

    @Step("<key> elementine tıkla <notClickByCoordinate> with staleElement")
    public void clickElementForStaleElement(String key, boolean notClickByCoordinate) {

        methods.clickElementForStaleElement(methods.getBy(key), notClickByCoordinate);
    }

    @Step("<key> elementine js ile tıkla")
    public void clickElementJs(String key) {

        methods.clickElementJs(methods.getBy(key));
    }

    @Step("Shadow root cookie kapatılır")
    public void shadowRootClickElementWithJs(){

        JavascriptExecutor jse=(JavascriptExecutor)driver;
        WebElement element =(WebElement)jse.executeScript("return document.querySelector('body > efilli-layout-dynamic').shadowRoot.querySelector('#eefe4907-e404-4da1-923b-7787d076df08')");
        ((JavascriptExecutor)driver).executeScript("arguments[0].click()",element);
    }

    @Step("<key> elementine koordinat <x> x ve <y> y ile tıkla")
    public void clickByWebElementCoordinate(String key, int x, int y) {

        methods.clickByWebElementCoordinate(methods.getBy(key), x, y);
    }

    @Step("<key> elementine js ile tıkla <notClickByCoordinate>")
    public void clickElementJs(String key, boolean notClickByCoordinate) {

        methods.clickElementJs(methods.getBy(key), notClickByCoordinate);
    }

    @Step("<key> elementine <text> değerini yaz")
    public void sendKeysElement(String key, String text) {

        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;
        methods.sendKeys(methods.getBy(key), text);
    }

    @Step("<key> elementine <text> değerini yaz ve entere bas")
    public void sendKeysElementAndEnter(String key, String text) {
        text = text.endsWith("KeyValue") ?
                Driver.TestMap.get(text).toString() : text;
        methods.sendKeys(methods.getBy(key), text);
        waitBySeconds(1);
        methods.sendKeysWithKeys(methods.getBy(key), "ENTER");
    }

    @Step("<key> elementi görünürse, <element> elementine <text> değerini yaz ve entere bas")
    public void sendKeysElementAndEnterIfIsVisible(String key, String element, String text) {
        methods.enterAndSendElementIsVisible(key, element, text);
    }

    @Step("<key> elementi görünürse, <element> elementine <text> değerini yaz.")
    public void sendElementIfIsVisible(String key, String element, String text) {
        methods.sendElementIfIsVisible(key, element, text);
    }


    @Step("<key> elementi görünürse, <element> elementine <text> değerini yaz ve <clics> elementine tıkla")
    public void sendTextAndClickElementIfVisibleStep(String key, String element, String text, String clicks){
        methods.sendTextAndClickElementIfVisible(key,element,text,clicks);
    }

    @Step("<key> elementi görünürse, önce <element> elementine, sonra <value> degerine tikla.")
    public void clickTwoElementsIfIsVisible(String key, String element, String value) {
        methods.removeProductIsVisible(key, element);
        waitPageLoadCompleteJs();
        waitByMilliSeconds(200);
        methods.removeProductIsVisible(value, value);
    }

    @Step("<key> elementine <fileUpload> dosya yolunu yaz")
    public void sendKeysFileUpload(String key, String fileUpload) {

        methods.sendKeys(methods.getBy(key), System.getProperty("user.dir") + fileUpload);
    }

    @Step("<key> elementine js ile <text> değerini yaz")
    public void sendKeysElementWithJsAndBackSpace(String key, String text) {

        methods.waitBySeconds(1);
        methods.jsExecuteScript("arguments[0].value = \"" + text + "\";"
                , false, methods.findElement(methods.getBy(key)));
        methods.waitByMilliSeconds(500);
        methods.sendKeysWithKeys(methods.getBy(key), "BACK_SPACE");
        methods.waitByMilliSeconds(500);
        methods.sendKeys(methods.getBy(key), text.substring(text.length() - 1));
    }

    @Step("<key> elementine <text> sayı değerini yaz")
    public void sendKeysElementWithNumpad(String key, String text) {

        methods.sendKeysWithNumpad(methods.getBy(key), text);
    }

    @Step("<url> adresine git")
    public void navigateTo(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        methods.navigateTo(url);
    }

    @Step("Şu anki url <url> ile aynı mı")
    public void doesUrlEqual(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, 80, "equal"), "Beklenen url, sayfa url ine eşit değil");
    }

    @Step("Şu anki url <url> içeriyor mu")
    public void doesUrlContain(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, 80, "contain"), "Beklenen url, sayfa url ine eşit değil");
    }

    @Step("Şu anki url <url> ile başlıyor mu")
    public void doesUrlStartWith(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, 80, "startWith"), "Beklenen url, sayfa url ine eşit değil");
    }

    @Step("Şu anki url <url> ile sonlanıyor mu")
    public void doesUrlEndWith(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, 80, "endWith"), "Beklenen url, sayfa url ine eşit değil");
    }

    @Step("Şu anki url <url> ile farklı mı")
    public void checkUrlDifferent(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, 80, "notEqual"), url + " sayfasından başka bir sayfaya geçiş sağlanamadı.");
    }

    @Step("<key> elementinin değerini temizle")
    public void clearElement(String key) {

        methods.clearElement(methods.getBy(key));
    }

    @Step("<key> elementinin değerini back space kullanarak temizle <value>")
    public void clearElementWithBackSpace(String key, String value) {

        methods.clearElementWithBackSpace(methods.getBy(key), value);
    }

    @Step("<seconds> saniye bekle")
    public void waitBySeconds(long seconds) {

        methods.waitBySeconds(seconds);
    }

    @Step("<milliseconds> milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {

        methods.waitByMilliSeconds(milliseconds);
    }

    @Step("<key> elementinin görünür olması kontrol edilir")
    public void checkElementVisible(String key) {

        checkElementVisible(key, 60);
    }

    @Step("<key> elementinin var olduğu kontrol edilir")
    public void checkElementExist(String key) {

        assertTrue(methods.doesElementExist(methods.getBy(key), 80), "");
    }

    @Step("<key> elementinin var olmadığı kontrol edilir")
    public void checkElementNotExist(String key) {

        assertTrue(methods.doesElementNotExist(methods.getBy(key), 80), "");
    }

    @Step("<key> elementinin görünür olmadığı kontrol edilir")
    public void checkElementInVisible(String key) {

        checkElementInVisible(key, 20);
    }

    @Step("<key> elementinin konumunu aldığı kontrol edilir")
    public void checkElementLocated(String key) {

        checkElementLocated(key, 20);
    }

    @Step("<key> elementinin konumunu aldığı kontrol edilir <timeout>")
    public void checkElementLocated(String key, long timeout) {

        methods.checkElementLocated(methods.getBy(key), timeout);
    }

    @Step("<key> elementinin görünür olması kontrol edilir <timeout>")
    public void checkElementVisible(String key, long timeout) {

        methods.checkElementVisible(methods.getBy(key), timeout);
    }

    @Step("<key> elementinin görünür olmadığı kontrol edilir <timeout>")
    public void checkElementInVisible(String key, long timeout) {

        methods.checkElementInVisible(methods.getBy(key), timeout);
    }

    @Step("<key> elementinin tıklanabilir olması kontrol edilir")
    public void checkElementClickable(String key) {

        checkElementClickable(key, 20);
    }

    @Step("<key> elementinin tıklanabilir olması kontrol edilir <timeout>")
    public void checkElementClickable(String key, long timeout) {

        methods.checkElementClickable(methods.getBy(key), timeout);
    }

    @Step("<key> elementinin text değerini <mapKey> keyinde tut <trim>")
    public void getElementTextAndSave(String key, String mapKey, boolean trim) {

        String text = methods.getText(methods.getBy(key));
        text = trim ? text.trim() : text;
        logger.info(text);
        Driver.TestMap.put(mapKey, text);
    }

    @Step("<key> elementinin <attribute> attribute değerini <mapKey> keyinde tut <trim>")
    public void getElementAttributeAndSave(String key, String attribute, String mapKey, boolean trim) {

        String value = methods.getAttribute(methods.getBy(key), attribute);
        value = trim ? value.trim() : value;
        logger.info(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step({"<key> indirimli fiyatı toplam <expectedText> fiyatına <element> kargo fiyat ve adet <deger> adet dahil eşit mi",
            "get text <key> element and control <expectedText> with cargo price <element>, <deger>"})
    public void controlElementTextsWithKargoPrice(String key, String value, String element, String deger) {
        methods.controlPrice(key, value, element, deger);
    }

    public static String clearTurkishChars(String str) {
        String ret = str;
        char[] turkishChars = new char[]{0x131, 0x120, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
        char[] englishChars = new char[]{'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            ret = ret.replaceAll(new String(new char[]{turkishChars[i]}), new String(new char[]{englishChars[i]}));
        }
        return ret;
    }


    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Step({"<key> elementinin text değeri <expectedText> değerine eşit mi",
            "get text <key> element and control <expectedText>"})
    public void controlElementText(String key, String expectedText) {

        expectedText = expectedText.endsWith("KeyValue") ? Driver.TestMap.get(expectedText).toString() : expectedText;
        //methods.waitByMilliSeconds(250);
        String actualText = methods.getText(methods.getBy(key))
                .replace("\r", "").replace("\n", "").trim();
        System.out.println("Beklenen text: " + expectedText);
        System.out.println("Alınan text: " + actualText);
        assertEquals(expectedText, actualText, "Text değerleri eşit değil");
        System.out.println("Text değerleri eşit");
    }

    @Step({"<key> elementinin text değeri <expectedText> değerini içeriyor mu",
            "get text <key> element and control contains <expectedText>"})
    public void controlElementTextContain(String key, String expectedText) {

        expectedText = expectedText.endsWith("KeyValue") ? Driver.TestMap.get(expectedText).toString() : expectedText;
        //methods.waitByMilliSeconds(250);
        String actualText = methods.getText(methods.getBy(key))
                .replace("\r", "").replace("\n", "").trim();
        System.out.println("Beklenen text: " + expectedText);
        System.out.println("Alınan text: " + actualText);
        assertTrue(actualText.contains(expectedText), "Text değerleri eşit değil");
        System.out.println("Text değerleri eşit");
    }

    @Step("<key> elementinin <attribute> niteliği <expectedValue> değerine eşit mi")
    public void checkElementAttribute(String key, String attribute, String expectedValue) {

        expectedValue = expectedValue.endsWith("KeyValue") ? Driver.TestMap.get(expectedValue).toString() : expectedValue;
        String attributeValue = methods.getAttribute(methods.getBy(key), attribute);
        assertNotNull(attributeValue, "Elementin değeri bulunamadi");
        System.out.println("expectedValue: " + expectedValue);
        System.out.println("actualValue: " + attributeValue);
        assertEquals(expectedValue, attributeValue, "Elementin değeri eslesmedi");
    }

    @Step("<key> elementinin <attribute> niteliği <expectedValue> değerini içeriyor mu")
    public void checkElementAttributeContains(String key, String attribute, String expectedValue) {

        expectedValue = expectedValue.endsWith("KeyValue") ? Driver.TestMap.get(expectedValue).toString() : expectedValue;
        String attributeValue = methods.getAttribute(methods.getBy(key), attribute);
        assertNotNull(attributeValue, "Elementin değeri bulunamadi");
        System.out.println("expectedValue: " + expectedValue);
        System.out.println("actualValue: " + attributeValue);
        assertTrue(attributeValue.contains(expectedValue)
                , expectedValue + " elementin değeriyle " + attributeValue + " eslesmedi");
    }

    @Step("<key> mouseover element")
    public void mouseOver(String key) {

        methods.mouseOverJs(methods.getBy(key), "1");
    }

    @Step("<key> mouseout element")
    public void mouseOut(String key) {

        methods.mouseOutJs(methods.getBy(key), "1");
    }

    @Step("<key> hover element")
    public void hoverElementAction(String key) {

        methods.hoverElementAction(methods.getBy(key), true);
    }

    @Step("<key> locatorli butonu üzerinde bekle ve <element> locatorlı kategoriye tıkla")
    public void hoverToTumkategorilerAndClickAyakkabi(String key, String element) throws InterruptedException {
        WebElement tumKategorilerBtn = driver.findElement(methods.getBy(key));
        Actions action = new Actions(driver);
        action.moveToElement(tumKategorilerBtn).perform();
        Thread.sleep(700);
        methods.clickElement(methods.getBy(element));
    }

    @Step("<key> hover element <isScrollElement>")
    public void hoverElementAction(String key, boolean isScrollElement) {

        methods.hoverElementAction(methods.getBy(key), isScrollElement);
    }

    @Step("<key> hover and click element")
    public void moveAndClickElement(String key) {

        methods.moveAndClickElement(methods.getBy(key), true);
    }

    @Step("<key> scroll element")
    public void scrollElementJs(String key) {

        methods.scrollElementJs(methods.getBy(key), "3");
    }

    @Step("<key> scroll element center")
    public void scrollElementCenterJs(String key) {

        methods.scrollElementCenterJs(methods.getBy(key), "3");
    }

    @Step("<key> focus element")
    public void focusElement(String key) {

        methods.focusElementJs(methods.getBy(key));
    }

    @Step("<key> select element by index <index>")
    public void selectElementByIndex(String key, int index) {

        methods.selectByIndex(methods.getBy(key), index);
    }

    @Step("<key> select element by value <value>")
    public void selectElementByValue(String key, String value) {

        methods.selectByValue(methods.getBy(key), value);
    }

    @Step("<key> select element by text <text>")
    public void selectElementByText(String key, String text) {

        methods.selectByVisibleText(methods.getBy(key), text);
    }

    @Step("<key> select element by index <index> js")
    public void selectElementByIndexJs(String key, int index) {

        methods.selectByIndexJs(methods.getBy(key), index, "3");
    }

    @Step("<key> select element by value <value> js")
    public void selectElementByValueJs(String key, String value) {

        methods.selectByValueJs(methods.getBy(key), value, "3");
    }

    @Step("<key> select element by text <text> js")
    public void selectElementByTextJs(String key, String text) {

        methods.selectByTextJs(methods.getBy(key), text, "3");
    }

    @Step("Open new tab <url>")
    public void openNewTabJs(String url) {

        methods.openNewTabJs(url);
    }

    @Step("Switch tab <switchTabNumber>")
    public void switchTab(int switchTabNumber) {

        methods.switchTab(switchTabNumber);
    }

    @Step("<key> switch frame element")
    public void switchFrameWithKey(String key) {
        methods.switchFrameWithKey(methods.getBy(key));
    }

    @Step("Switch default content")
    public void switchDefaultContent() {
        methods.switchDefaultContent();
    }

    @Step("Switch parent frame")
    public void switchParentFrame() {
        methods.switchParentFrame();
    }

    @Step("Navigate to refresh")
    public void navigateToRefresh() {

        methods.navigateToRefresh();
    }

    @Step("Navigate to back")
    public void navigateToBack() {

        methods.navigateToBack();
    }

    @Step("Navigate to forward")
    public void navigateToForward() {

        methods.navigateToForward();
    }

    @Step("Get Cookies")
    public void getCookies() {

        logger.info("cookies: " + methods.getCookies().toString());
    }

    @Step("Delete All Cookies")
    public void deleteAllCookies() {

        methods.deleteAllCookies();
    }

    @Step("Get User Agent")
    public void getUserAgent() {

        logger.info("userAgent: " + methods.jsExecuteScript("return navigator.userAgent;", false).toString());
    }

    @Step("Get page source")
    public void getPageSource() {

        logger.info("pageSource: " + methods.getPageSource());
    }

    @Step("Get performance logs <logContainText>")
    public void getPerformanceLogs(String logContainText) {

        LogEntries les = driver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry le : les) {
            //if(le.getMessage().contains("\"method\":\"Network.responseReceived\"")) {
            if (le.getMessage().contains(logContainText)) {
                System.out.println(le.getMessage());
            }
        }
    }

    @Step("Get Navigator Webdriver")
    public void getNavigatorWebdriver() {

        Object object = methods.jsExecuteScript("return navigator.webdriver;", false);
        logger.info("NavigatorWebdriver: " + (object == null ? "null" : object.toString()));
    }

    @Step("Get Navigator Webdriver Json")
    public void getNavigatorWebdriverJson() {

        Object object = methods.jsExecuteScript("return Object.defineProperty(navigator, 'webdriver', { get: () => undefined });", false);
        logger.info("NavigatorWebdriverJson: " + (object == null ? "null" : object.toString()));
    }


    @Step("Sekmeyi kapat")
    public void closeTab() {
        methods.close();

    }

    @Step("Tablar kontrol edilir")
    public void tabControl() {
        for (int i = 0; i < 20; i++) {
            methods.waitByMilliSeconds(400);
            if (methods.listTabs().size() > 1) {
                break;
            }
        }
    }

    @Step("<key> elementi için <deltaY> deltaY <offsetX> offsetX ve <offsetY> offsetY değerleriyle mouse tekerleğini kaydır")
    public void mouseWheel(String key, int deltaY, int offsetX, int offsetY) {

        methods.getJsMethods().wheelElement(methods.findElement(methods.getBy(key)), deltaY, offsetX, offsetY);
    }

    @Step("<key> elementi için <deltaY> deltaY <offsetX> offsetX ve <offsetY> offsetY değerleriyle mouse tekerleğini kaydır 2")
    public void mouseWheelSimple(String key, int deltaY, int offsetX, int offsetY) {

        methods.getJsMethods().wheelElementSimple(methods.findElement(methods.getBy(key)), deltaY, offsetX, offsetY);
    }

    @Step("waitPageLoadCompleteJs")
    public void waitPageLoadCompleteJs() {

        methods.waitPageLoadCompleteJs();
    }

    @Step("Open new tab <url> and go to keyValue")
    public void openNewTabJsAndGoToUrlKeyValue(String url) {

        url = url.endsWith("KeyValue") ? Driver.TestMap.get(url).toString() : url;
        methods.openNewTabJs(url);
    }

    @Step("Açılan yeni taba geçilir.")
    public void goToNewTab() {
        waitPageLoadCompleteJs();
        waitByMilliSeconds(50);
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        waitPageLoadCompleteJs();
        waitByMilliSeconds(50);
        // driver.close();
    }

    @Step("<key> elementinin değerini <value> değeriyle değiştirerek <newKey> elementini oluştur")
    public void keyValueChangerMethod(String key, String value, String newKey) {

        methods.keyValueChangerMethodWithNewElement(key, newKey, methods
                .getKeyValueChangerStringBuilder(value, "|!", "KeyValue"), "|!");
    }

    @Step("<value> değeri <replaceValues> degerlerini temizle ve <mapKey> değerinde tut <trim>")
    public void replaceText(String value, String replaceValues, String mapKey, boolean trim) {

        value = value.endsWith("KeyValue") ? Driver.TestMap.get(value).toString() : value;
        String[] splitValues = replaceValues.split("\\?!");
        for (String splitValue : splitValues) {
            if (!splitValue.equals("")) {
                value = value.replace(splitValue, "");
            }
        }
        value = trim ? value.trim() : value;
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> degerini <mapKey> keyinde tut")
    public void saveData(String value, String mapKey) {

        value = value.endsWith("KeyValue") ? Driver.TestMap.get(value).toString() : value;
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<key> listesinden random olarak seçim yapılır")
    public void random_Select(String key) {

        methods.randomSelect(key);
    }

    @Step("<key> listesinden random olarak js ile seçim yapılır")
    public void jsRandom_Select(String key) {

        methods.jsRandomSelect(key);
    }

    @Step("<key> elementi görünürse, <element> elementine tıkla")
    public void removeIfIsVisible(String key, String element) {
        methods.removeProductIsVisible(key, element);
    }

    @Step("<key> istenilen miktarda ürün bulunmamaktadır görünürse, <element> pop-up'ı kapatılır. <kategori> elementli kategoriye gidilir, <product> locator'lı random ürün seçilir, <size> random beden seçilip <button> sepete eklenir.")
    public void stepIfThereIsNoStockAddAnother(String key, String element, String kategori, String product, String size, String button){
        methods.ifThereIsNoStockAddAnother(key,element,kategori,product,size,button);
    }
    @Step("<key> elementi varsa, elemente <text> textini yaz ve entera bas -> ortam için")
    public void haveKeySendClick(String key, String element) {
        methods.enterSendElementIsVisible(key, element);
    }

    @Step("<key> elementi görünür değilse, random <element> seçime devam edilir.")
    public void bcUrunBedenBul(String key, String element) {
        methods.bcSelectProductIsVisible(key, element);
    }


    @Step("<key> elementi görünürse, <element> listesinden random seçim yapılır.")
    public void randomIfVisible(String key, String element) {
        methods.ifVisibleSelectRandom(key, element);
    }

    @Step("<key> elementi görünür değilse, sayfayı yenile")
    public void removeIfIsVisible(String key) {
        methods.invisibleNavigator(key);
    }

    @Step("<key> elementi görünür değilse element bulunana kadar bir sayfa geri dönülür ve tekrar random <element> seçilir.")
    public void turnBackAndSelectRandomItems(String key, String element){
        methods.whileElementIsNotVisibleTurnBack(key, element);
    }

    @Step("<key> elementi text'inde bulunan fiyat değerini ayıkla.")
    public void sayisalDegeriAyikla(String key) {
        String OSNAMES = methods.findElement(methods.getBy(key)).getText();
        String[] parts = OSNAMES.split(" ");
        String OS = parts[0];
        OS = OS.replace(",", ".");
        kargo_ucretsiz_fiyat = Float.parseFloat(OS);
        System.out.println(kargo_ucretsiz_fiyat);
    }

    public float kargo_ucretsiz_fiyat; //ürün detaydaki kargo ücretini toplama için float olarak tutar
    public float sepetteki_kargo_ucretsiz_fiyat; //sepetteki kargo ücretini toplama için float olarak tutar

    @Step("Ürün detaydaki kargo ücretsiz fiyat aralığının sepetteki <key> indirimli fiyatının, <element> genel toplam kargo fiyatı ve <value> sepet kargo fiyatıile uyuştuğu kontrol edilir.")
    public void dene(String key, String element, String value) {
        System.out.println(kargo_ucretsiz_fiyat);
        String actualText = methods.findElement(methods.getBy(key)).getText();
        String rsActual = actualText.replace("TL", "");
        String rActual = rsActual.replace(",", ".");
        float fActualNew = Float.parseFloat(rActual);
        System.out.println("Ürünün İndirimli Fiyatı: " + fActualNew);
        String cargoPrice = methods.findElement(methods.getBy(element)).getText();

        if (fActualNew >= kargo_ucretsiz_fiyat) {
            System.out.println("Beklenen: ÜCRETSİZ");
            System.out.println("Gerçekleşen: " + cargoPrice);
            assertEquals("ÜCRETSİZ", cargoPrice);
            System.out.println("Beklenen: Kargo Bedava");
            System.out.println("Gerçekleşen: " + methods.findElement(methods.getBy(value)).getText());
            assertEquals("Kargo Bedava", methods.findElement(methods.getBy(value)).getText());
        } else {
            assertNotEquals("ÜCRETSİZ", cargoPrice);
            System.out.println(kargo_ucretsiz_fiyat);
            System.out.println("Kargo ücretsiz limiti :" + kargo_ucretsiz_fiyat
                    + " sağlanmadığı için " + cargoPrice + " kargo ücreti uygulanmıştır.");
            String OSNAMES = methods.findElement(methods.getBy(value)).getText();
            String[] parts = OSNAMES.split(" ");
            String OS = parts[0];
            OS = OS.replace(",", ".");
            sepetteki_kargo_ucretsiz_fiyat = Float.parseFloat(OS);
            System.out.println(sepetteki_kargo_ucretsiz_fiyat);
            assertEquals(kargo_ucretsiz_fiyat, sepetteki_kargo_ucretsiz_fiyat);
        }
    }

    @Step("<key> iframe'e girilir.")
    public void paymentWithSmsOnay(String key) {

        driver.switchTo().frame(driver.findElement(methods.getBy(key)));
    }

    @Step("<key> iframe'i görünürse, iframe'e girilir.")
    public void paymentWithSmsOnayIframeIfVisible(String key) {
        if (methods.isElementVisible(methods.getBy(key), 1)) {
            driver.switchTo().frame(driver.findElement(methods.getBy(key)));
        }
        logger.info("iframe görünür değil.");
    }

    @Step("iframe'den çıkılır.")
    public void paymentWithSmsOnay() {
        switchParentFrame();
    }
    @Step("<key> elementinin text değerini tut")
    public void textTut(String key) {
        methods.getText(methods.getBy(key));
    }

    @Step("<key> elementi görünürse, <text> id ve <value> şifre input alanları ile tekrar giriş yap ve <element> butonuna tıkla")
    public void ifElementVisibleLoginAgain(String key, String text, String value, String element){
        methods.elementGorunurseTekrarGirisYap(key, text,value,element);
    }

    @Step("<key> elementine <text> sayısını numpadle yaz")
    public void sendKeysNumpad(String key, String text) {

        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;
        methods.sendKeysWithNumpad(methods.getBy(key), text);
    }

    @Step("<key> elementi görünürse <element> elementine tıkla ve <control> elementini kontrol et")
    public void ifElementVisibleClickAndControlStep(String key, String element, String control){

        methods.ifElementVisibleClickAndControl(key, element,control);
    }
    }