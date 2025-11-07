package co.jp.expesne.service;

import co.jp.expense.entity.Expense;
import co.jp.expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAll() {
        List<Expense> expenseList = new ArrayList<>();
        List<Object[]> objectList = expenseRepository.getYearMonthSumCost();
        for (Object[] objects : objectList) {
            int year = (int) objects[0];
            int month = (int) objects[1];
            double sumCost = (double) objects[2];
            expenseList.add(createExpense(year, month, sumCost));
        }
        return expenseList;
    }

    public List<Expense> getMonthSumCostByDate(int year, int month) {
        List<Expense> expenseList = new ArrayList<>();
        List<Object[]> objectList = expenseRepository.getMonthSumCostByMonth(year, month);
        for (Object[] objects : objectList) {
            int id = (int) objects[0];
            String item = (String) objects[1];
            int day = (int) objects[2];
            int m = (int) objects[3];
            int y = (int) objects[4];
            double cost = (double) objects[5];
            expenseList.add(createExpense(id, item, day, m, y, cost));
        }
        return expenseList;
    }

    public double getTotalCostByMonth(int year, int month) {
        return expenseRepository.getTotalCostByMonth(year, month);
    }

    public Expense getDetail(int id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.orElse(null);
    }

    public void updateExpense(Expense expense) {
        try {
            expenseRepository.save(expense);
        } catch (Exception e) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        }
    }


    public String getDate(int id) {
        String date = "";
        try {
            List<Object[]> objectList = expenseRepository.getDate(id);
            for (Object[] objects : objectList) {
                int day = (int) objects[0];
                int month = (int) objects[1];
                int year = (int) objects[2];
                date = createFormattedDate(year, month, day);
            }
        } catch (Exception e) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        }
        return date;
    }

    private Expense createExpense(int year, int month, double cost) {
        Expense expense = new Expense();
        expense.setYear(year);
        expense.setMonth(month);
        expense.setCost(cost);
        return expense;
    }

    private Expense createExpense(int id, String item, int day, int month, int year, double cost) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setItem(item);
        expense.setDay(day);
        expense.setMonth(month);
        expense.setYear(year);
        expense.setCost(cost);
        return expense;
    }

    private String createFormattedDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    public void createExpense(Expense expense) {
        // Set the day, month, and year components of the expense
        LocalDate localDate = LocalDate.parse(expense.getInputdate());
        expense.setDay(localDate.getDayOfMonth());
        expense.setMonth(localDate.getMonthValue());
        expense.setYear(localDate.getYear());

        // Save the expense to the repository
        expenseRepository.save(expense);
    }

}
