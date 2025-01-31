package com.catchylabs.methods;

import com.catchylabs.driver.Driver;
import com.catchylabs.helper.ElementHelper;
import com.catchylabs.helper.StoreHelper;
import com.catchylabs.model.ElementInfo;
import com.google.common.base.Splitter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import static com.catchylabs.step.StepImplementation.clearTurkishChars;
import static com.catchylabs.step.StepImplementation.round;
import static org.junit.jupiter.api.Assertions.*;

public class Methods {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    WebDriver driver;
    FluentWait<WebDriver> wait;
    JsMethods jsMethods;
    MethodsUtil methodsUtil;
    ActionMethods actionMethods;
    long waitElementTimeout = 20;
    long pollingEveryValue =300;


    public Methods(){

        this.driver = Driver.driver;
        wait = setFluentWait(waitElementTimeout);
        jsMethods = new JsMethods(driver);
        actionMethods = new ActionMethods(driver);
        methodsUtil = new MethodsUtil();
    }


    public FluentWait<WebDriver> setFluentWait(long timeout){

        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingEveryValue))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public ElementInfo getElementInfo(String key){

        return StoreHelper.INSTANCE.findElementInfoByKey(key);
    }

    public boolean containsKeyInElementInfoMap(String key){

        return StoreHelper.INSTANCE.containsKey(key);
    }

    public void duplicateKeyControlInElementInfoMap(boolean printAllData){

        StoreHelper.INSTANCE.duplicateKeyControl(printAllData);
    }

    public void createElementInfo(String key, String value, String type){

        ElementInfo elementInfo = new ElementInfo();
        elementInfo.setKey(key);
        elementInfo.setValue(value);
        elementInfo.setType(type);
        StoreHelper.INSTANCE.addElementInfoByKey(key,elementInfo);
    }

    public WebDriver getDriver(){

        return driver;
    }

    public By getBy(String key){

        By by = ElementHelper.getElementInfoToBy(getElementInfo(key));
        logger.info(key + " elementi " + by.toString() + " by değerine sahip");
        return by;
    }

    public List<String> getByValueAndSelectorType(By by){

        List<String> list = new ArrayList<String>();
        String[] values = by.toString().split(": ",2);
        list.add(values[1].trim());
        list.add(getSelectorTypeName(values[0].replace("By.","").trim()));
        return list;
    }

    public Object jsExecuteScript(String script, boolean isScriptAsync, Object... args){

        return isScriptAsync ? jsMethods.jsExecuteAsyncScript(script, args) : jsMethods.jsExecuteScript(script, args);
    }


    public JsMethods getJsMethods(){

        return jsMethods;
    }

    public ActionMethods getActionMethods(){

        return actionMethods;
    }

    public Boolean isElementEnabled(By by){

        return isElementEnabled(by,10);
    }

    public Boolean isElementEnabled(By by, int loopCount){

        for (int i = 0; i < loopCount; i++) {
            try {
                if (driver.findElement(by).isDisplayed() && driver.findElement(by).isEnabled()) {
                    return true;
                }
            }catch (Exception e){
            }
            waitByMilliSeconds(400);
        }
        return false;
    }

    public void clickElementForStaleElement(By by, boolean notClickByCoordinate){

        try {
            clickElement(by, notClickByCoordinate);
        } catch (StaleElementReferenceException e) {
            waitByMilliSeconds(400);
            waitUntilWithoutStaleElement(by);
            clickElement(by, notClickByCoordinate);
        }
    }

    public void waitUntilWithoutStaleElement(By by){

        waitUntilWithoutStaleElement(by,20);
    }

    public void waitUntilWithoutStaleElement(By by, long timeout){

        setFluentWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(findElement(by))));
    }

    public WebElement findElement(By by){

        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement findElementWithoutWait(By by){

        return driver.findElement(by);
    }

    public List<WebElement> findElements(By by){

        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public List<WebElement> findElementsWithOutError(By by){

        List<WebElement> list = new ArrayList<>();
        try {
            list.addAll(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<WebElement> findElementsWithoutWait(By by){

        return driver.findElements(by);
    }

    public void clickElement(By by){


            findElement(by).click();

        logger.info("Elemente tıklandı.");
    }

    public void clickElement(By by, boolean notClickByCoordinate){


            findElement(by).click();

        logger.info("Elemente tıklandı.");
    }

    public void clearElement(By by){

        findElement(by).clear();
        logger.info("Elementin text alanı temizlendi.");
    }

    public void clearElementWithBackSpace(By by, String value){

        clearElement(by);
        waitByMilliSeconds(100);
        sendKeys(by,value);
        waitByMilliSeconds(100);
        sendKeysWithKeys(by,"BACK_SPACE");
        waitByMilliSeconds(100);
    }

    public Dimension getSize(By by){

        return findElement(by).getSize();
    }

    public Point getLocation(By by){

        return findElement(by).getLocation();
    }

    public Rectangle getRect(By by){

        return findElement(by).getRect();
    }

    public void sendKeys(By by, String text){

        findElement(by).sendKeys(text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public void sendKeysWithKeys(By by, String text){

        findElement(by).sendKeys(Keys.valueOf(text));
    }

    public void sendKeysWithNumpad(By by, String text){

        WebElement webElement = findElement(by);
        char[] textArray = text.toCharArray();
        for(int i = 0; i < textArray.length; i++){

            webElement.sendKeys(Keys.valueOf("NUMPAD" + String.valueOf(textArray[i])));
        }
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public void sendKeysJs(By by, String text, String type){

        jsMethods.sendKeys(findElementForJs(by,type), text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public String getText(By by){

        return findElement(by).getText();
    }

    public String getTextContentJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"textContent");
    }

    public String getInnerTextJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"innerText");
    }

    public String getOuterTextJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"outerText");
    }

    public void mouseOverJs(By by, String type){

        jsMethods.mouseOver(findElementForJs(by,type));
        logger.info("mouseover " + by);
    }

    public void mouseOutJs(By by, String type){

        jsMethods.mouseOut(findElementForJs(by,type));
        logger.info("mouseout " + by);
    }

    public String getAttribute(By by, String attribute){

        return findElement(by).getAttribute(attribute);
    }

    public String getAttributeJs(By by, String attribute, String type){

        return jsMethods.getAttribute(findElementForJs(by,type), attribute);
    }

    public String getValueJs(By by, String type){

        return jsMethods.getValue(findElementForJs(by,type));
    }

    public String getCssValue(By by, String attribute){

        return findElement(by).getCssValue(attribute);
    }

    public String getHexCssValue(By by, String attribute){

        return Color.fromString(getCssValue(by, attribute)).asHex();
    }

    public String getCssValueJs(By by, String attribute, String type){

        return jsMethods.getCssValue(findElementForJs(by,type), attribute);
    }

    public String getHexCssValueJs(By by, String attribute, String type){

        return Color.fromString(getCssValueJs(by, attribute, type)).asHex();
    }

    public String getPageSource(){

        return driver.getPageSource();
    }

    public String getCurrentUrl(){

        return driver.getCurrentUrl();
    }

    public void openNewTabJs(String url){

        jsMethods.openNewTab(url);
        logger.info("Yeni tab açılıyor..." + " Url: " + url);
    }

    public void acceptAlert(){

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    public List<String> listTabs(){
        List<String> list = new ArrayList<String>();
        for (String window: driver.getWindowHandles()){
            list.add(window);
        }
        return list;
    }

    public void close(){

        driver.close();
    }

    public void switchTab(int tabNumber){

        driver.switchTo().window(listTabs().get(tabNumber));
    }

    public void switchFrame(int frameNumber){

        driver.switchTo().frame(frameNumber);
    }

    public void switchFrame(String frameName){

        driver.switchTo().frame(frameName);
    }

    public void switchFrameWithKey(By by){

        WebElement webElement = findElement(by);
        driver.switchTo().frame(webElement);
    }

    public void switchParentFrame(){

        driver.switchTo().parentFrame();
    }

    public void switchDefaultContent(){

        driver.switchTo().defaultContent();
    }

    public void navigateTo(String url){

        driver.navigate().to(url);
    }

    public void navigateToBack(){

        driver.navigate().back();
    }

    public void navigateToForward(){

        driver.navigate().forward();
    }

    public void navigateToRefresh(){

        driver.navigate().refresh();
    }

    public Set<Cookie> getCookies(){

        return driver.manage().getCookies();
    }

    public void deleteAllCookies(){

        driver.manage().deleteAllCookies();
    }

    public Select getSelect(By by){

        return new Select(findElement(by));
    }

    public void selectByValue(By by, String value){

        getSelect(by).selectByValue(value);
    }

    public void selectByVisibleText(By by, String text){

        getSelect(by).selectByVisibleText(text);
    }

    public void selectByIndex(By by, int index){

        getSelect(by).selectByIndex(index);
    }

    public List<WebElement> getSelectOptions(By by){

        return getSelect(by).getOptions();
    }

    public WebElement getFirstSelectedOption(By by){

        return getSelect(by).getFirstSelectedOption();
    }

    public List<WebElement> getAllSelectedOptions(By by){

        return getSelect(by).getAllSelectedOptions();
    }

    public void selectByIndexJs(By by, int index, String type){

        jsMethods.selectWithIndex(findElementForJs(by,type), index);
    }

    public void selectByTextJs(By by, String text, String type){

        jsMethods.selectWithText(findElementForJs(by,type), text);
    }

    public void selectByValueJs(By by, String value, String type){

        jsMethods.selectWithValue(findElementForJs(by,type), value);
    }

    public int getSelectedOptionIndexJs(By by, String type){

        return jsMethods.getSelectedOptionIndex(findElementForJs(by,type));
    }

    public String getSelectedOptionTextJs(By by, String type){

        return jsMethods.getSelectedOptionText(findElementForJs(by,type));
    }

    public String getSelectedOptionValueJs(By by, String type){

        return jsMethods.getSelectedOptionValue(findElementForJs(by,type));
    }

    public void scrollElementJs(By by, String type){

        jsMethods.scrollElement(findElementForJs(by,type));
    }


    public void scrollElementCenterJs(By by,String type){

        jsMethods.scrollElementCenter(findElementForJs(by,type));
    }

    public void focusElementJs(By by){

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        jsMethods.focusElement(webElement);
    }

    public void jsExecutorWithBy(String script, By by){

        jsMethods.jsExecuteScript(script, findElementForJs(by,"3"));
    }

    public void waitByMilliSeconds(long milliSeconds){

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds){

        waitByMilliSeconds(seconds*1000);
    }

    public boolean isElementClickable(By by, long timeout){

        try {
            setFluentWait(timeout).until(ExpectedConditions.elementToBeClickable(by));
            logger.info("true");
            return true;
        }
        catch (Exception e) {
            logger.info("false" + " " + e.getMessage());
            return false;
        }
    }

    public void checkElementClickable(By by, long timeout){

        assertTrue(isElementClickable(by, timeout),by.toString() + " elementi tıklanabilir değil.");
    }

    public void checkElementClickable(By by){
        checkElementClickable(by,20);
    }

    public boolean isElementVisible(By by, long timeout){

        try {
            setFluentWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
            logger.info("true");
            return true;
        } catch (Exception e) {
            logger.info("false" + " " + e.getMessage());
            return false;
        }
    }

    public void checkElementVisible(By by, long timeout) {

        assertTrue(isElementVisible(by, timeout), by.toString() + " elementi görüntülenemedi.");
    }

    public void checkElementVisible(By by){
        checkElementVisible(by,20);
    }

    public boolean isElementInVisible(By by, long timeout){

        try {
            setFluentWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(by));
            logger.info("true");
            return true;
        } catch (Exception e) {
            logger.info("false" + " " + e.getMessage());
            return false;
        }
    }

    public void checkElementInVisible(By by, long timeout) {

        assertTrue(isElementInVisible(by, timeout),by.toString() + " elementi görünür");
    }

    public void checkElementInVisible(By by) {

        checkElementInVisible(by,20);
    }

    public boolean isElementLocated(By by, long timeout){

        try {
            setFluentWait(timeout).until(ExpectedConditions.presenceOfElementLocated(by));
            logger.info("true");
            return true;
        } catch (Exception e) {
            logger.info("false" + " " + e.getMessage());
            return false;
        }
    }

    public void checkElementLocated(By by, long timeout) {

        assertTrue(isElementVisible(by, timeout),by.toString() + " element located error.");
    }

    public void checkElementLocated(By by){
        checkElementLocated(by,20);
    }

    public void hoverElementAction(By by, boolean isScrollElement) {

        WebElement webElement = findElementForJs(by,"1");
        if(isScrollElement){
            jsMethods.scrollElement(webElement);
        }
        actionMethods.hoverElement(webElement);
    }

    public void moveAndClickElement(By by, boolean isScrollElement) {

        WebElement webElement = findElementForJs(by,"1");
        if(isScrollElement){
            jsMethods.scrollElement(webElement);
        }
        actionMethods.moveAndClickElement(webElement);
    }

    public void clickElementWithAction(By by, boolean isScrollElement){

        WebElement webElement = findElementForJs(by,"1");
        if(isScrollElement){
            jsMethods.scrollElement(webElement);
        }
        actionMethods.clickElement(webElement);
    }

    public void doubleClickElementWithAction(By by, boolean isScrollElement){

        WebElement webElement = findElementForJs(by,"1");
        if(isScrollElement){
            jsMethods.scrollElement(webElement);
        }
        actionMethods.doubleClickElement(webElement);
    }

    public void whileElementIsNotVisibleTurnBack(String key, String element){
        while (isElementInVisible(getBy(key),1)){
            navigateToBack();
            waitPageLoadCompleteJs();
            waitByMilliSeconds(100);
            randomSelect(element);
            waitPageLoadCompleteJs();
            logger.info("Element görünür olmadığı için geri dönüldü");
        }

        logger.info("Element görünür");
    }

    public void selectAction(By by, int optionIndex, boolean isScrollElement){

        WebElement webElement = findElementForJs(by,"1");
        if(isScrollElement){
            jsMethods.scrollElement(webElement);
        }
        actionMethods.select(webElement, optionIndex);
    }

    // 1 loop 400 ms
    public boolean isImageLoadingJs(By by, int loopCount){

        boolean isImageLoading = false;
        try {
            isImageLoading = jsMethods.jsImageLoading(findElementForJs(by,"1"), loopCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isImageLoading;
    }

    public void waitPageLoadCompleteJs() {

        jsMethods.waitPageLoadComplete(setFluentWait(20));
    }

    public void waitForAngularLoadJs() {

        jsMethods.waitForAngularLoad(setFluentWait(10));
    }

    public void waitJQueryCompleteJs() {

        jsMethods.waitJQueryComplete(setFluentWait(10));
    }

    public void waitPageScrollingCompleteJs() {

        jsMethods.waitPageScrollingComplete(setFluentWait(10));
    }

    public void stopPageLoadJs() {

        jsMethods.stopPageLoad();
    }

    public boolean doesUrl(String url, int count, String condition){

        int againCount = 0;
        boolean isUrl = false;
        String takenUrl = "";
        logger.info("Beklenen url: " + url);
        while (!isUrl) {
            waitByMilliSeconds(250);
            if (againCount == count) {
                System.err.println("Expected url " + url + " doesn't equal current url " + takenUrl);
                logger.info("Alınan url: " + takenUrl);
                return false;
            }
            takenUrl = driver.getCurrentUrl();
            if (takenUrl != null) {
                isUrl = conditionValueControl(url,takenUrl,condition);
            }
            againCount++;
        }
        logger.info("Alınan url: " + takenUrl);
        return true;
    }

    public void focusToElementJs(By by){

        WebElement webElement = findElementForJs(by,"3");
        waitByMilliSeconds(1000);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(1000);
    }

    public void clickElementJs(By by){

        jsMethods.clickByElement(findElementForJs(by,"3"));
    }

    public void clickElementJs(By by, boolean notClickByCoordinate){

        jsMethods.clickByElement(findElementForJs(by,"3"), notClickByCoordinate);
    }

    public void clickByCoordinateJs(int x, int y){

        jsMethods.clickByCoordinate(x, y);
    }

    public void clickByWebElementCoordinate(By by){

        jsMethods.clickByWebElementCoordinate(findElementForJs(by,"3"));
    }

    public void clickByWebElementCoordinate(By by, int x, int y){

        jsMethods.clickByWebElementCoordinate(findElementForJs(by,"3"), x, y);
    }

    public void focusAndClickElementJs(By by){

        WebElement webElement = findElement(by);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.focusElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.scrollElementCenter(webElement);
        waitByMilliSeconds(1000);
        jsMethods.clickMouseEvent(webElement);
        waitByMilliSeconds(100);
    }

    public boolean doesAttributeValue(By by, String attribute, String value, String condition, int count){

        int againCount = 0;
        boolean attributeCondition = false;
        String actualAttributeValue = "";
        logger.info("Beklenen değer: " + attribute + " " + condition + " " + value);
        while (!attributeCondition) {
            waitByMilliSeconds(400);
            if (againCount == count) {
                logger.info("Alınan değer: " + actualAttributeValue);
                return false;
            }
            actualAttributeValue = findElement(by).getAttribute(attribute).trim();
            if (actualAttributeValue != null) {
                attributeCondition = conditionValueControl(value, actualAttributeValue, condition);
            }
            againCount++;
        }
        logger.info("Alınan değer: " + actualAttributeValue);
        return true;
    }

    public void checkElementExistWithUrl(By by, int elementControlCount, int repeatCount, String url, String errorMessage) {

        boolean isElementVisible = false;
        int countAgain = 0;
        int elementCount;
        while (!isElementVisible) {
            waitByMilliSeconds(400);
            if (countAgain == repeatCount*elementControlCount) {
                fail(errorMessage);
                break;
            }
            elementCount = driver.findElements(by)
                    .size();
            if (elementCount != 0) {
                isElementVisible = true;
            }
            if(countAgain % elementControlCount == 0) {
                driver.navigate().to(url);
                doesUrl(url,15,"contain");
            }
            countAgain++;
        }
    }

    public void checkElementExist(By by, int loopCount){

        assertTrue(doesElementExist(by,loopCount),"Element bulunamadı");
    }

    public boolean doesElementExist(By by, int loopCount){

        logger.info("Element " + by.toString() + " by değerine sahip");
        int countAgain = 0;
        int elementCount;
        while (true) {
            if (countAgain == loopCount) {
                return false;
            }
            elementCount = driver.findElements(by).size();
            if (elementCount != 0) {
                return true;
            }
            waitByMilliSeconds(250);
            countAgain++;
        }
    }

    public boolean doesElementNotExist(By by, int loopCount) {

        boolean isElementInvisible = false;
        int countAgain = 0;
        int elementCount;
        while (!isElementInvisible) {
            if (countAgain == loopCount) {
                return false;
            }
            elementCount = driver.findElements(by).size();
            if (elementCount == 0) {
                isElementInvisible = true;
            }
            waitByMilliSeconds(250);
            countAgain++;
        }
        return true;
    }

    public By getByWithKeySetValue(String key, String value){

        ElementInfo elementInfo = getElementInfo(key);
        String getValue = elementInfo.getValue();
        String type = elementInfo.getType();
        logger.info(value);
        String[] arrayValue = Splitter.on("!!").splitToList(value).toArray(new String[0]);
        String newValue = String.format(getValue, arrayValue);
        return ElementHelper.getElementInfoToBy(newValue,type);
    }

    public void keyValueChangerMethodWithNewElement(String key, String newKey, String value, String splitValue){

        ElementInfo elementInfo = getElementInfo(key);
        String getValue = elementInfo.getValue();
        String type = elementInfo.getType();
        logger.info(value);
        String[] arrayValue = Splitter.on(splitValue).splitToList(value).toArray(new String[0]);
        String newValue = String.format(getValue, arrayValue);
        logger.info(newValue);
        createElementInfo(newKey,newValue,type);
    }

    public String getKeyValueChangerStringBuilder(String value, String splitValue, String mapKeySuffix){

        if(value.contains(mapKeySuffix)) {
            String[] values = Splitter.on(splitValue).splitToList(value).toArray(new String[0]);
            StringBuilder stringBuilder = new StringBuilder();
            int valuesLength = values.length;
            for (int i = 0; i < valuesLength; i++) {
                String text = values[i];
                if (text.endsWith(mapKeySuffix)) {
                    text = Driver.TestMap.get(text).toString();
                }
                stringBuilder.append(text);
                if (i != valuesLength - 1) {
                    stringBuilder.append(splitValue);
                }
            }
            value = stringBuilder.toString();
        }
        return value;
    }

    public WebElement findElementForJs(By by, String type){

        WebElement webElement = null;
        switch (type){
            case "1":
                webElement = findElement(by);
                break;
            case "2":
                webElement = findElementWithoutWait(by);
                break;
            case "3":
                List<String> byValueList = getByValueAndSelectorType(by);
                webElement = jsMethods.findElement(byValueList.get(0),byValueList.get(1));
                break;
            default:
                fail("type hatalı");
        }
        return webElement;
    }

    public List<WebElement> findElementsForJs(By by, String type){

        List<WebElement> webElementList = null;
        switch (type){
            case "1":
                webElementList = findElements(by);
                break;
            case "2":
                webElementList = findElementsWithoutWait(by);
                break;
            case "3":
                List<String> byValueList = getByValueAndSelectorType(by);
                webElementList = jsMethods.findElements(byValueList.get(0),byValueList.get(1));
                break;
            default:
                fail("type hatalı");
        }
        return webElementList;
    }

    private String getSelectorTypeName(String type){

        String selectorType = "";
        switch (type) {

            case "id":
                selectorType = "id";
                break;

            case "name":
                selectorType = "name";
                break;

            case "className":
                selectorType = "class";
                break;

            case "cssSelector":
                selectorType = "css";
                break;

            case "xpath":
                selectorType = "xpath";
                break;

            default:
                fail("HATA");
                break;
        }
        return selectorType;
    }

    private boolean conditionValueControl(String expectedValue, String actualValue,String condition){

        boolean result = false;
        switch (condition){
            case "equal":
                result = actualValue.equals(expectedValue);
                break;
            case "contain":
                result = actualValue.contains(expectedValue);
                break;
            case "startWith":
                result = actualValue.startsWith(expectedValue);
                break;
            case "endWith":
                result = actualValue.endsWith(expectedValue);
                break;
            case "notEqual":
                result = !actualValue.equals(expectedValue);
                break;
            case "notContain":
                result = !actualValue.contains(expectedValue);
                break;
            case "notStartWith":
                result = !actualValue.startsWith(expectedValue);
                break;
            case "notEndWith":
                result = !actualValue.endsWith(expectedValue);
                break;
            default:
                fail("hatali durum: " + condition);
        }
        return result;
    }
    public void randomSelect(String key){
        waitByMilliSeconds(50);
        checkElementVisible(getBy(key),20);
        List<WebElement> selectRand = findElements(getBy(key));
        int rand = (int) (Math.random() * selectRand.size()+1);
        if(rand==0) {
            rand++;
            logger.info("------------------");
            logger.info("Random olarak " + rand + ". İndex seçildi");
            selectRand.get(rand).click();
        }
            else{
        rand--;
        logger.info("------------------");
        logger.info("Random olarak "+rand+". İndex seçildi");
        waitByMilliSeconds(50);
        selectRand.get(rand).click();
    }}
    public void jsRandomSelect(String key){
        waitByMilliSeconds(50);
        List<WebElement> selectRand = findElements(getBy(key));
        int rand = (int) (Math.random() * selectRand.size()+1);
        if(rand==0) {
            rand++;
            logger.info("------------------");
            logger.info("Random olarak " + rand + ". İndex seçildi");
            waitByMilliSeconds(50);
            jsMethods.jsDriver.executeScript("arguments[selectRand.get(rand)].click();", selectRand.get(rand));

        }
        else{
            rand--;
            logger.info("------------------");
            logger.info("Random olarak "+rand+". İndex seçildi");
            waitByMilliSeconds(50);
            jsMethods.jsDriver.executeScript("arguments[selectRand.get(rand)].click();", selectRand.get(rand));
        }}

    public void controlPrice(String key, String value, String element, String deger) {
        String actualText = getText(getBy(key));
        String expectedText = getText(getBy(value));
        String cargoPrice=getText(getBy(element));
        String urunAdet=getText(getBy(deger));
        cargoPrice=(clearTurkishChars(cargoPrice));
        System.out.println(cargoPrice);
        System.out.println(urunAdet);
        waitBySeconds(2);
        String rsActual=actualText.replace("TL","");
        String rActual=rsActual.replace(",",".");
        String rsExpected=expectedText.replace("TL","");
        String rExpected=rsExpected.replace(",",".");



        if ("UCRETSIZ".compareTo(cargoPrice) == 0) {
            float fActualNew =Float.parseFloat(rActual);
            float fExpectedNew =Float.parseFloat(rExpected);
            float fUrunAdetNew =Float.parseFloat(urunAdet);
            waitByMilliSeconds(200);
            fActualNew *= fUrunAdetNew;
            if(fActualNew>1000){
                rActual.replace(".","");
            }
            else{
                return;
            }
            fActualNew =Float.parseFloat(rActual);
            assertEquals(fExpectedNew, fActualNew, "Text değerleri eşit değil");
            System.out.println("Beklenen text: " + fExpectedNew);
            System.out.println("Alınan text: " + fActualNew);
            System.out.println("Text değerleri eşit");
        }
        else{
            float fActualNew =Float.parseFloat(rActual);
            float fExpectedNew =Float.parseFloat(rExpected);
            float fUrunAdetNew =Float.parseFloat(urunAdet);
            String rsCargoPrice=cargoPrice.replace("TL","");
            String rCargoPrice=rsCargoPrice.replace(",",".");
            float fCargoPriceNew= Float.parseFloat(rCargoPrice);
            fActualNew+=fCargoPriceNew;
            System.out.println("Kargo Fiyatı: " + fCargoPriceNew);
            System.out.println("Alınan text: " + fExpectedNew);
            System.out.println("Beklenen text: " + round(fActualNew,2));
            assertEquals(fExpectedNew, round(fActualNew,2), "Text değerleri eşit değil");
            System.out.println("Text değerleri eşit");

        }}
    public void removeProductIsVisible(String key, String element){
        if (isElementVisible(getBy(key),1)){
            waitByMilliSeconds(100);
            checkElementClickable(getBy(element),20);
            clickElement(getBy(element));
            logger.info("Element görünür");
        }
        else {
            logger.info("Element görünür değil");
        }
    }

    public void ifThereIsNoStockAddAnother(String key,String element,String kategori,String product,String size,String button){
        if(isElementVisible(getBy(key),1)){
            //Mevcut ürün bulunmamaktadır pop-up'ı kapatılır.
            checkElementVisible(getBy(element),60);
            checkElementClickable(getBy(element),60);
            clickElement(getBy(element));
            //ürün listelemeye gidilir.
            waitByMilliSeconds(200);
            checkElementClickable(getBy(kategori),60);
            clickElement(getBy(kategori));
            waitPageLoadCompleteJs();
            waitByMilliSeconds(400);
            //Random ürün seçilir.
            checkElementVisible(getBy(product),60);
            checkElementClickable(getBy(product),60);
            randomSelect(product);

            waitPageLoadCompleteJs();
            waitByMilliSeconds(400);
            //Random beden seçilir.
            checkElementVisible(getBy(size),60);
            checkElementClickable(getBy(size),60);
            randomSelect(size);

            waitPageLoadCompleteJs();
            waitByMilliSeconds(400);
            //Sepete eklenir.
            checkElementVisible(getBy(button),60);
            checkElementClickable(getBy(button),60);
            clickElement(getBy(button));
        }
        else{
            logger.info("Üründe stok mevcut, sepete eklendi.");
        }
    }

    public void bcSelectProductIsVisible(String key, String element){
        while(isElementInVisible(getBy(key),1)){
            randomSelect(element);
            logger.info("Bc'ye uygun ürün bedeni aranıyor.");
        }
        logger.info("Bc'ye uygun ürün bedeni bulundu.");
    }
    public void ifVisibleSelectRandom(String key, String element){
        if (isElementVisible(getBy(key),1)){
            waitPageLoadCompleteJs();
            checkElementClickable(getBy(element),20);
            randomSelect(element);
            logger.info("Görünür elemente random seçim yapıldı.");
        }
        else{
        logger.info("Element görünür değil.");
    }}

    public void invisibleNavigator(String key){
        if (isElementInVisible(getBy(key),1)){
            waitByMilliSeconds(500);
            navigateToRefresh();
            logger.info("Element görünür olmadığı için sayfa yenilendi...");
        }
        else {
            logger.info("Element görünür");
        }
    }

    public void enterAndSendElementIsVisible(String key, String element, String text){
    if (isElementVisible(getBy(key),1)) {
        text = text.endsWith("KeyValue") ?
                Driver.TestMap.get(text).toString() : text;
        sendKeys(getBy(element), text);
        waitByMilliSeconds(500);
        sendKeysWithKeys(getBy(key), "ENTER");
    }
        else{
        System.out.println("...");
    }
}
    public void sendElementIfIsVisible(String key, String element, String text){
        if (isElementVisible(getBy(key),1)) {
            text = text.endsWith("KeyValue") ?
                    Driver.TestMap.get(text).toString() : text;
            waitPageLoadCompleteJs();
            checkElementClickable(getBy(element),10);
            sendKeys(getBy(element), text);
            waitByMilliSeconds(500);
        }
        else{
            System.out.println("...");
        }
    }
    public void sendTextAndClickElementIfVisible(String key, String element, String text, String clicks){
        if(isElementVisible(getBy(key),1)){
            text = text.endsWith("KeyValue") ?
                    Driver.TestMap.get(text).toString() : text;
            sendKeys(getBy(element), text);
            waitByMilliSeconds(500);
            clickElement(getBy(clicks));
        }
        else{
            System.out.println("...");
        }
    }
    public void enterSendElementIsVisible(String key, String text){
        try{
            text = text.endsWith("KeyValue") ?
                    Driver.TestMap.get(text).toString() : text;
            sendKeys(getBy(key), text);
            sendKeysWithKeys(getBy(key), "ENTER");
        }
        catch(Exception e){
            logger.info("Otomasyon testi Prod ortamda test başlatılıyor.");
        }
    }
    public void elementGorunurseTekrarGirisYap(String key, String text, String value, String element){
        if(isElementVisible(getBy(key),20)){
            checkElementClickable(getBy(element));
            waitByMilliSeconds(100);
            sendKeys(getBy(text),"testotomasyon02@gmail.com");
            sendKeys(getBy(value),"Atlas123");
            waitByMilliSeconds(100);
            clickElement(getBy(element));
            logger.info("İkinci defa giriş yapıldı");
        }
        else {
            logger.info("İlk denemede giriş yapıldı");
        }


    }

    public void ifElementVisibleClickAndControl(String key, String element, String control){
        if(isElementVisible(getBy(key),1)){
            clickElement(getBy(element));
            waitByMilliSeconds(2500);
            checkElementVisible(getBy(control),1);
        }
        else {
            logger.info("Element görülemedi");
        }
    }
}
