package be.pxl.student;

import be.pxl.student.entity.Account;
import be.pxl.student.util.BudgetPlannerImporter;

public class BudgetPlanner {
    public static void main(String[] args) {
        String fileName = "src\\main\\resources\\account_payments.csv";
        BudgetPlannerImporter budgetPlannerImporter = new BudgetPlannerImporter(fileName);
        Account account = budgetPlannerImporter.deserialise();
        System.out.println(account.toString());
    }
}
