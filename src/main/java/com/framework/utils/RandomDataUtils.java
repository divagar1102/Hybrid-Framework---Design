package com.framework.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomDataUtils - generates realistic fake test data using JavaFaker.
 * Thread-safe; each call uses a new Faker instance where needed.
 */
public class RandomDataUtils {

    private static final Logger log = LogManager.getLogger(RandomDataUtils.class);
    private static final Faker faker = new Faker(Locale.ENGLISH);

    private RandomDataUtils() {}

    // ─── Person ────────────────────────────────────────────────────────────────

    public static String getFirstName()      { return faker.name().firstName(); }
    public static String getLastName()       { return faker.name().lastName(); }
    public static String getFullName()       { return faker.name().fullName(); }
    public static String getUsername()       { return faker.name().username(); }

    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    public static String getEmail(String firstName, String lastName) {
        return (firstName + "." + lastName + "@" + faker.internet().domainName()).toLowerCase();
    }

    public static String getPhoneNumber()    { return faker.phoneNumber().cellPhone(); }
    public static String getDateOfBirth()    { return faker.date().birthday(18, 70).toString(); }

    // ─── Address ───────────────────────────────────────────────────────────────

    public static String getStreetAddress()  { return faker.address().streetAddress(); }
    public static String getCity()           { return faker.address().city(); }
    public static String getState()          { return faker.address().state(); }
    public static String getZipCode()        { return faker.address().zipCode(); }
    public static String getCountry()        { return faker.address().country(); }
    public static String getFullAddress()    { return faker.address().fullAddress(); }

    // ─── Company ───────────────────────────────────────────────────────────────

    public static String getCompanyName()    { return faker.company().name(); }
    public static String getJobTitle()       { return faker.job().title(); }
    public static String getDepartment()     { return faker.commerce().department(); }

    // ─── Internet / Auth ───────────────────────────────────────────────────────

    public static String getPassword() {
        return faker.internet().password(10, 16, true, true);
    }

    public static String getPassword(int minLen, int maxLen) {
        return faker.internet().password(minLen, maxLen, true, true);
    }

    public static String getUrl()            { return faker.internet().url(); }
    public static String getIpAddress()      { return faker.internet().ipV4Address(); }
    public static String getMacAddress()     { return faker.internet().macAddress(); }

    // ─── Text ──────────────────────────────────────────────────────────────────

    public static String getWord()           { return faker.lorem().word(); }
    public static String getSentence()       { return faker.lorem().sentence(); }
    public static String getParagraph()      { return faker.lorem().paragraph(); }
    public static String getWords(int count) { return faker.lorem().words(count).toString(); }

    // ─── Numbers ───────────────────────────────────────────────────────────────

    public static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static long getRandomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static double getRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static String getRandomDigits(int count) {
        return faker.number().digits(count);
    }

    // ─── Finance ───────────────────────────────────────────────────────────────

    public static String getCreditCardNumber() { return faker.finance().creditCard(); }
    public static String getIban()             { return faker.finance().iban(); }
    public static String getCurrency()         { return faker.currency().name(); }

    // ─── Dates ─────────────────────────────────────────────────────────────────

    public static String getFutureDate(int daysAhead) {
        return LocalDate.now().plusDays(daysAhead).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String getPastDate(int daysBehind) {
        return LocalDate.now().minusDays(daysBehind).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    // ─── IDs / Tokens ──────────────────────────────────────────────────────────

    public static String getUUID()           { return UUID.randomUUID().toString(); }

    public static String getAlphanumeric(int length) {
        return faker.regexify("[a-zA-Z0-9]{" + length + "}");
    }

    public static String getAlphabetic(int length) {
        return faker.regexify("[a-zA-Z]{" + length + "}");
    }

    public static String getNumeric(int length) {
        return faker.regexify("[0-9]{" + length + "}");
    }

    // ─── Products / Commerce ───────────────────────────────────────────────────

    public static String getProductName()    { return faker.commerce().productName(); }
    public static String getProductPrice()   { return faker.commerce().price(); }
    public static String getColor()          { return faker.color().name(); }
}
