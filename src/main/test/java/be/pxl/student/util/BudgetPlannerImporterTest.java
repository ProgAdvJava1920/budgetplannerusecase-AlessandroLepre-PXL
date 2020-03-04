package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class BudgetPlannerImporterTest {
    String filename = "src\\main\\resources\\account_payments.csv";
    String line = "Jos,BE69771770897312,BE55513730204360,Tue Feb 18 18:26:54 CET 2020,3861.55,EUR,Perferendis id consectetur quis doloribus provident velit.";
    Account account = null;
    BudgetPlannerImporter importer = null;
    LocalDateTime date = null;
    DateTimeFormatter dateTimeFormatter = null;

    @BeforeEach
    public void init(){
        dateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        importer = new BudgetPlannerImporter(filename);
        account = importer.deserialise();
    }

    @Test
    public void createAccountShouldHaveCorrectName(){
        assertEquals("Jos", account.getName());
    }

    @Test
    public void createAccountShouldHaveCorrectIBAN(){
        assertEquals("BE69771770897312", account.getIBAN());
    }

    @Test
    public void createPaymentShouldHaveCorrectCurrency() throws ParseException {
        String[] splitLine = line.split(",");
        Payment payment = importer.createPayment(splitLine);
        assertEquals("EUR", payment.getCurrency());
    }

    @Test
    public void createPaymentShouldHaveCorrectAmount() throws ParseException {
        String[] splitLine = line.split(",");
        Payment payment = importer.createPayment(splitLine);
        assertEquals(3861.55, Math.round(payment.getAmount() * 100) / 100.0);
    }

    @Test
    public void createAccountShouldHaveCorrectDetail() throws ParseException {
        String[] splitLine = line.split(",");
        Payment payment = importer.createPayment(splitLine);
        assertEquals("Perferendis id consectetur quis doloribus provident velit.", payment.getDetail());
    }

    @Test
    public void createAccountShouldHaveCorrectDate() throws ParseException {
        String[] splitLine = line.split(",");
        Payment payment = importer.createPayment(splitLine);
        date = LocalDateTime.parse("Tue Feb 18 18:26:54 CET 2020" , dateTimeFormatter);
        assertEquals(date , payment.getDate());
    }
}