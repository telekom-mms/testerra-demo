/*
 * Testerra
 *
 * (C) 2023, Martin Großmann, Deutsche Telekom MMS GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.testerra.demo.cdp.tests;

import eu.tsystems.mms.tic.testframework.pageobjects.UiElementFinder;
import eu.tsystems.mms.tic.testframework.testing.ChromeDevToolsProvider;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.events.ConsoleEvent;
import org.openqa.selenium.devtools.v118.log.Log;
import org.openqa.selenium.devtools.v118.log.model.LogEntry;
import org.openqa.selenium.devtools.v118.network.Network;
import org.openqa.selenium.devtools.v118.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v118.network.model.ResponseReceived;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created on 2023-11-16
 *
 * @author mgn
 */
public class ChromeDevToolsDemoTest extends AbstractTest implements ChromeDevToolsProvider {

    @Test
    public void test_CDP_BasicAuthentication() {
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(webDriver);

        // Credentials are used for all further website calls in your webdriver session
        CHROME_DEV_TOOLS.setBasicAuthentication(webDriver, UsernameAndPassword.of("admin", "admin"));

        webDriver.get("https://the-internet.herokuapp.com/basic_auth");
        uiElementFinder.find(By.tagName("p")).assertThat().text().isContaining("Congratulations");
    }

    @Test
    public void test_CDP_GeoLocation() {
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(webDriver);

        CHROME_DEV_TOOLS.setGeoLocation(
                webDriver,
                52.52084,   // latitude
                13.40943,          // longitude
                1);                // accuracy

        webDriver.get("https://my-location.org/");
        uiElementFinder.find(By.xpath("//button[@aria-label = 'Consent']")).click();
        uiElementFinder.find(By.id("latitude")).assertThat().text().isContaining("52.52084");
        uiElementFinder.find(By.id("longitude")).assertThat().text().isContaining("13.40943");
    }

    @Test
    public void test_CDP_LogListener_JsLogs() throws MalformedURLException {
        DesktopWebDriverRequest request = new DesktopWebDriverRequest();
        request.setBaseUrl("https://www.selenium.dev/selenium/web/bidi/logEntryAdded.html");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);
        DevTools devTools = CHROME_DEV_TOOLS.getRawDevTools(webDriver);

        // Create a list for console events
        List<ConsoleEvent> consoleEvents = new ArrayList<>();
        // Create a consumer and add them to a listener
        Consumer<ConsoleEvent> addEntry = consoleEvents::add;
        devTools.getDomains().events().addConsoleListener(addEntry);

        UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(webDriver);
        uiElementFinder.find(By.id("consoleLog")).click();
        uiElementFinder.find(By.id("consoleError")).click();

        consoleEvents.forEach(event ->
                log().info(
                        "JS_LOGS: {} {} - {}",
                        event.getTimestamp(),
                        event.getType(),
                        event.getMessages().toString())
        );
    }

    @Test
    public void test_CDP_LogListener_JsExceptions() throws MalformedURLException {
        DesktopWebDriverRequest request = new DesktopWebDriverRequest();
        request.setBaseUrl("https://www.selenium.dev/selenium/web/bidi/logEntryAdded.html");
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver(request);
        DevTools devTools = CHROME_DEV_TOOLS.getRawDevTools(webDriver);

        // Create a list for JS exceptions
        List<JavascriptException> jsExceptionsList = new ArrayList<>();
        Consumer<JavascriptException> addEntry = jsExceptionsList::add;
        devTools.getDomains().events().addJavascriptExceptionListener(addEntry);

        UiElementFinder uiElementFinder = UI_ELEMENT_FINDER_FACTORY.create(webDriver);
        uiElementFinder.find(By.id("jsException")).click();
        uiElementFinder.find(By.id("logWithStacktrace")).click();

        jsExceptionsList.forEach(jsException ->
                log().info(
                        "JS_EXCEPTION: {} {}",
                        jsException.getMessage(),
                        jsException.getSystemInformation()
                ));
    }

    // Be careful to the imports!
    @Test
    public void test_CDP_LogListener_BrokenImages() {
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        DevTools devTools = CHROME_DEV_TOOLS.getRawDevTools(webDriver);
        devTools.send(Log.enable());

        List<LogEntry> logEntries = new ArrayList<>();
        Consumer<LogEntry> addedLog = logEntries::add;
        devTools.addListener(Log.entryAdded(), addedLog);

        webDriver.get("http://the-internet.herokuapp.com/broken_images");
        TimerUtils.sleep(1000);     // Short wait to get delayed logs

        logEntries.forEach(logEntry ->
                log().info(
                        "LOG_ENTRY: {} {} {} - {} ({})",
                        logEntry.getTimestamp(),
                        logEntry.getLevel(),
                        logEntry.getSource(),
                        logEntry.getText(),
                        logEntry.getUrl()
                )
        );
    }

    @Test
    public void test_CDP_NetworkListener() {
        WebDriver webDriver = WEB_DRIVER_MANAGER.getWebDriver();
        DevTools devTools = CHROME_DEV_TOOLS.getRawDevTools(webDriver);
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Create lists for requests and responses
        List<ResponseReceived> responseList = new ArrayList<>();
        List<RequestWillBeSent> requestList = new ArrayList<>();

        devTools.addListener(Network.responseReceived(), responseList::add);
        devTools.addListener(Network.requestWillBeSent(), requestList::add);

        webDriver.get("https://the-internet.herokuapp.com/broken_images");

        requestList.forEach(request ->
                log().info(
                        "Request: {} {} - {}",
                        request.getRequestId().toString(),
                        request.getRequest().getMethod(),
                        request.getRequest().getUrl()
                )
        );

        responseList.forEach(response ->
                log().info(
                        "Response: {} {} - {}",
                        response.getRequestId().toString(),
                        response.getResponse().getStatus(),
                        response.getResponse().getStatusText()
                )
        );
    }

}
