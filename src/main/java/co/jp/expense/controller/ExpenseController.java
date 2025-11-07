package co.jp.expense.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.jp.expense.entity.Expense;
import co.jp.expesne.service.ExpenseService;
import jakarta.validation.Valid;

@Controller
public class ExpenseController {
	@Autowired
	private ExpenseService expenseService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("expenseList", expenseService.getAll());
		return "expenseIndex";
	}

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("form", new Expense());
		model.addAttribute("inputdate", getCurrentFormattedDate());
		return "expenseAdd";
	}

	@PostMapping("/add")
	public String addExpense(Model model, @ModelAttribute("form") @Valid Expense expense, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {

			model.addAttribute("inputdate", getCurrentFormattedDate());
			return "expenseAdd";
		}

		Expense newExpense = new Expense();
		newExpense.setItem(expense.getItem());
		newExpense.setInputdate(expense.getInputdate());
		newExpense.setCost(expense.getCost());

		expenseService.createExpense(newExpense);

		return "redirect:/detail/" + newExpense.getYear() + "/" + newExpense.getMonth();
	}

	@GetMapping("/detail/{year}/{month}")
	public String detail(Model model, @PathVariable int year, @PathVariable int month) {
		model.addAttribute("expenseList", expenseService.getMonthSumCostByDate(year, month));
		model.addAttribute("cost", expenseService.getTotalCostByMonth(year, month));
		return "expenseDetail";
	}

	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable int id) {
		model.addAttribute("detail", expenseService.getDetail(id));
		model.addAttribute("inputdate", expenseService.getDate(id));
		return "expenseUpdate";
	}

	@PostMapping("/updateConfirm")
	public String updateConfirm(Model model, @ModelAttribute("form") @Valid Expense expense,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "expenseUpdate";
		}
		this.expenseService.updateExpense(expense);

		model.addAttribute("inputdate", expenseService.getDate(expense.getId()));
		updateExpenseWithFormattedDate(expense);
		expenseService.updateExpense(expense);
		model.addAttribute("expenseList", expenseService.getMonthSumCostByDate(expense.getYear(), expense.getMonth()));
		model.addAttribute("cost", expenseService.getTotalCostByMonth(expense.getYear(), expense.getMonth()));
		return "expenseDetail";
	}

	// Helper method to update expense's year, month, and day
	public void updateExpenseWithFormattedDate1(Expense expense) {
		String inputDate = expense.getInputdate();
		expense.setDay(Integer.parseInt(inputDate.substring(8, 10)));
		expense.setMonth(Integer.parseInt(inputDate.substring(5, 7)));
		expense.setYear(Integer.parseInt(inputDate.substring(0, 4)));
	}

	private String getCurrentFormattedDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(new Date());
	}

	public void updateExpenseWithFormattedDate(Expense expense) {
		String inputDate = expense.getInputdate();
		expense.setDay(Integer.parseInt(inputDate.substring(8, 10)));
		expense.setMonth(Integer.parseInt(inputDate.substring(5, 7)));
		expense.setYear(Integer.parseInt(inputDate.substring(0, 4)));
	}
}
