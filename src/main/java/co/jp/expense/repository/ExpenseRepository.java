package co.jp.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.jp.expense.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

	@Query("SELECT e.year,e.month,SUM(e.cost) FROM Expense e GROUP BY e.year,e.month")
	public List<Object[]> getYearMonthSumCost();

	@Query("SELECT e.id,e.item,e.day,e.month,e.year,e.cost FROM Expense e WHERE e.year = ?1 and e.month = ?2 ORDER BY e.day")
	public List<Object[]> getMonthSumCostByMonth(Integer year, Integer month);

	@Query("SELECT sum(e.cost) FROM Expense e WHERE e.year = ?1 and e.month = ?2 GROUP BY e.year,e.month")
	public double getTotalCostByMonth(Integer year, Integer month);

	@Query("SELECT day,month,year FROM Expense e WHERE e.id = ?1 ")
	public List<Object[]> getDate(Integer id);

}