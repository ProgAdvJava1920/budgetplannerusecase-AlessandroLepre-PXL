package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Util class to import csv file
 */
public class BudgetPlannerImporter {
    //reader gebruiken om de lijnen in te lezen
    //mapper gebruiken voor de lijnen
    //deze objecten in een list zetten dan
    private String fileName;
    private Account account = null;
    public static final String PATTER = "EEE MMM dd HH:mm:ss z yyyy";
    public BudgetPlannerImporter(String fileName){
        this.fileName = fileName;
    }

    public Account deserialise(){
        List<Payment> payments = new ArrayList<>();
        Path path = Paths.get(this.fileName);
        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line = reader.readLine();
            line = reader.readLine();
            this.account = createAccount(line.split(","));
            while (line != null){
                Payment payment = createPayment(line.split(","));
                payments.add(payment);
                line = reader.readLine();
            }
            reader.readLine();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        account.setPayments(payments);
        return account;
    }

    public Account createAccount(String[] line) {
        //Account name,Account IBAN,Counteraccount IBAN,Transaction date,Amount,Currency,Detail
        Account account = new Account();
        account.setName(line[0]);
        account.setIBAN(line[1]);
        return account;
    }

    public Payment createPayment(String[] line) throws ParseException {
        //Account name,Account IBAN,Counteraccount IBAN,Transaction date,Amount,Currency,Detail
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        float account = Float.parseFloat(line[4]);
        //LocalDateTime date = LocalDateTime.parse(line[3], dateTimeFormatter);
        String currancy = line[5];
        String detail = line[6];
        return new Payment(convertToDate(line[3]), account, currancy, detail);
    }

    public Date convertToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTER, Locale.US);
        return dateFormat.parse(dateString);
    }
}
