package application;

import java.util.Date;

public class Expense extends BudgetItem {
	private Category category;

	public Expense(String description, double amount, Date date, Category category) {
		super(description, amount, date);
		this.category = category;
	}
	
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
