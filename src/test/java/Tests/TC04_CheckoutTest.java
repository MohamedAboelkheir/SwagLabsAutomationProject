package Tests;

import Listeners.IInvokedMethodListenerClass;
import Listeners.ITestResultListenerClass;
import Pages.P01_LoginPage;
import Pages.P02_LandingPage;
import Pages.P03_CartPage;
import Pages.P04_CheckoutPage;
import Utilities.DataUtils;
import Utilities.LogsUtils;
import Utilities.Utility;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static DriverFactory.DriverFactory.*;
import static Utilities.DataUtils.getPropertyValue;

@Listeners({IInvokedMethodListenerClass.class, ITestResultListenerClass.class})
public class TC04_CheckoutTest {
    private final String USERNAME = DataUtils.getJsonData("validLogin", "username");
    private final String PASSWORD = DataUtils.getJsonData("validLogin", "password");
    private final String FIRSTNAME = DataUtils.getJsonData("information", "fName") + "-" + Utility.getTimeStamp();
    private final String LASTNAME = DataUtils.getJsonData("information", "lName") + "-" + Utility.getTimeStamp();
    private final String ZIPCODE = new Faker().number().digits(5);

    @BeforeMethod
    public void setup() throws IOException {
        setupDriver(getPropertyValue("environment", "Browser"));
        LogsUtils.info("Edge driver is opened");
        getDriver().get(getPropertyValue("environment", "BASE_URL"));
        LogsUtils.info("Page is redirected to the URL");
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void checkoutStepOneTC() throws IOException {
        //TODO:Login Steps
        new P01_LoginPage(getDriver())
                .enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickOnLoginButton();
        //TODO:Adding products Steps
        new P02_LandingPage(getDriver()).addRandomProducts(2, 6)
                .clickOnCartIcon();
        //TODO:Go to checkout page Steps
        new P03_CartPage(getDriver()).clickOnCheckoutButton();
        //TODO:Filling Information Steps
        new P04_CheckoutPage(getDriver()).fillingInformationForm(FIRSTNAME, LASTNAME, ZIPCODE)
                .clickOnContinueButton();
        LogsUtils.info(FIRSTNAME + " " + LASTNAME + " " + ZIPCODE);
        Assert.assertTrue(Utility.VerifyURL(getDriver(), getPropertyValue("environment", "CHECKOUT_URL")));
    }


    @AfterMethod
    public void quit() {
        quitDriver();
    }
}
