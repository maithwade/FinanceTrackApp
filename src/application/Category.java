package application;

public class Category {
    private String name;
    private double budgetLimit;
    private double categoryTotal;

    public Category(String name) {
        this.name = name;
    }

    public String getCategory() {
        return name;
    }

    public void setCategory(String name) {
        this.name = name;
    }
    
    public void setBudgetLimit(double limit) {
    	this.budgetLimit = limit;
    }
    
    public double getBudgetLimit() {
    	return this.budgetLimit;
    }
    
    public void setCategoryTotal(double total) {
    	this.categoryTotal = total;
    }
    
    public double getCategoryTotal() {
    	return this.categoryTotal;
    }
    
    public void resetBudgetLimit() {
    	this.budgetLimit = 0;
    }
    
    public boolean exceedsLimit() {
    	return this.categoryTotal > this.budgetLimit;
    }
    
    @Override
    public String toString() {
    	return this.name;
    }
}
