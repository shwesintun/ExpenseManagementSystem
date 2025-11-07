package co.jp.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"co.jp.expense.controller", "co.jp.expesne.service"})
public class ExpenseManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseManagementSystemApplication.class, args);
    }
}
