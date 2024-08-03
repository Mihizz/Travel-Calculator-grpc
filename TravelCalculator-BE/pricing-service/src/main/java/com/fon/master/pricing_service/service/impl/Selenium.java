package com.fon.master.pricing_service.service.impl;

import com.fon.master.pricing_service.exception.ResourceNotFoundException;
import com.fon.master.pricing_service.model.Currency;
import com.fon.master.pricing_service.model.Fuel;
import com.fon.master.pricing_service.repository.CurrencyRepository;
import org.openqa.selenium.NoSuchElementException;
import com.fon.master.pricing_service.repository.FuelTypeRepository;
import org.springframework.stereotype.Service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Objects;


@Service
public class Selenium {
    private WebDriver driver;

    private FuelTypeRepository fuelTypeRepository;

    private CurrencyRepository currencyRepository;

    public Selenium(FuelTypeRepository fuelTypeRepository, CurrencyRepository currencyRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.currencyRepository = currencyRepository;
    }

    public Selenium(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Selenium() {
    }

    private By getFuelXpathSerbia(String fuelTag){
        return By.xpath("//app-single-station[4]//p[contains(text(),'" + fuelTag + "'  )]/parent::div[@class='data-row']");
    }

    private By getFuelXpath(String country, String fuelTag) {
        return By.xpath("//div[h2[@class='card-title' and text()='" + country + "']]//p[contains(text(), '" + fuelTag + "')]/parent::div[@class='data-row']");
    }

    private By getElectricityXPath(String country){
        return By.xpath("//tr[td[2][strong[text()='" + country + "']]]/td[4]");
    }

    private By getLPGPriceXPath(String fuelTag) {
        return By.xpath("//table[1]//tr[td[contains(text(),'" + fuelTag + "')]]/td[2]");
    }

    private By getCurrencyXPath(String currencyTag) {
        return By.xpath("//table//tr[td[1][contains(text(),'" + currencyTag + "')]]/td[3]");
    }

    public double fuelPriceFromBenzinko(Fuel fuel){
        WebDriver driver = initializeDriver();
        String[] fuelTags = fuel.getFuelType().getFuelTags().split(", ");

        Currency currency = currencyRepository.findByCurrencyName("Euro").orElseThrow(() -> new ResourceNotFoundException("Currency", "name", "Euro"));
        double currencyPrice = currency.getExchangeRate();

        //Electricity in euro
        if(fuel.getFuelType().getFuelName().equals("Struja")){
            driver.get("https://switcher.ie/car-insurance/cost-of-charging-electric-car-europe/");

            By fuelXpath = getElectricityXPath(fuel.getCountry().getCountryNameEng());

            try{
                String fuelPrice = driver.findElement(fuelXpath).getText().substring(1);

                return Double.parseDouble(fuelPrice) * currencyPrice;
            }
            catch (NoSuchElementException e){
                //Croatia
                return 2.6 * currencyPrice;
            }

        // Montenegro LCG in Euro
        } else if (fuel.getCountry().getCountryNameEng().equals("Montenegro") && fuel.getFuelType().getFuelName().equals("Gas")){
            driver.get("https://autotraveler.ru/en/montenegro/trend-price-fuel-montenegro.html");

            for (String tag : fuelTags) {
                By fuelXpath = getLPGPriceXPath(tag);

                try{
                    String fuelPrice = driver.findElement(fuelXpath).getText();
                    return Double.valueOf(fuelPrice.substring(2)) * currencyPrice;
                }
                catch (NoSuchElementException e){
                     //Tag incorrect, trying another
                    System.out.println("Element not found: " + fuelXpath);
                }
            }
            return 0;
        }

        //Serbia
        if(fuel.getCountry().getCountryNameEng().equals("Serbia")){
            driver.get("https://benzinko.com");

            for (String tag : fuelTags) {
                By fuelXpath = getFuelXpathSerbia(tag);

                try{
                    String fuelAll = driver.findElement(fuelXpath).getText();
                    String[] lines = fuelAll.split("\\n");

                    return Double.valueOf(lines[1]);
                }
                catch (NoSuchElementException e){
                    // Tag incorrect, trying another
                    System.out.println("Element not found: " + fuelXpath);
                }
                return 0;
            }
        }

        //Other
        else{
            driver.get("https://benzinko.com/eu");

            for (String tag : fuelTags) {
                By fuelXpath = getFuelXpath(fuel.getCountry().getCountryNameEng(),tag);

                try{
                    String fuelAll = driver.findElement(fuelXpath).getText();
                    String[] lines = fuelAll.split("\\n");

                    return Double.valueOf(lines[1]);
                }
                catch (NoSuchElementException e){
                    // Tag incorrect, trying another
                    System.out.println("Element not found: " + fuelXpath);
                }
            }
        }
        return 0.00;
    }

    public double exchangeRateForCurrencies(String currencyTag){
        WebDriver driver = initializeDriver();

        driver.get("https://www.kursevra.com/");

        if(Objects.equals(currencyTag, "RSD")){
            return 1.00;
        }

        By currencyPath = getCurrencyXPath(currencyTag);
        try{
            String currencyPrice = driver.findElement(currencyPath).getText();

            System.out.println("Element not found: " + currencyPrice);

            return Double.parseDouble(currencyPrice);
        }
        catch (Exception e){
            return 0.00;
        }
    }

    @BeforeClass
    public WebDriver initializeDriver(){


        System.setProperty("webdriver.http.factory", "jdk-http-client");
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        return driver;
    }

    @AfterClass
    public void tearDownDriver(){
        driver.quit();
    }
}
