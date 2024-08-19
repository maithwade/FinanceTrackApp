package application;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MonthPage extends Stage {
	private PieChart pieChart = new PieChart();
	private TableView<BudgetItem> tableView = new TableView<>();
	

	public MonthPage(Budget budget) {
	        Stage monthStage = new Stage();
	
			ComboBox<String> monthComboBox = new ComboBox<>();
			monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			monthComboBox.setPromptText("Select A Month");
			
			TableColumn<BudgetItem, String> descriptionCol = new TableColumn<>("Description");
			descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
			
			descriptionCol.setCellFactory(column -> {
			    return new TableCell<BudgetItem, String>() {
			        @Override
			        protected void updateItem(String item, boolean empty) {
			            super.updateItem(item, empty);
			            if (item == null || empty) {
			                setText(null);
			            } else {
			                setText(item);
			                if (getTableRow().getItem() instanceof BudgetItem && ((BudgetItem) getTableRow().getItem()).getDescription().equals("Total")) {
			                    setStyle("-fx-font-weight: bold;");
			                } else {
			                    setStyle("");
			                }
			            }
			        }
			    };
			});
			
			TableColumn<BudgetItem, Double> amountCol = new TableColumn<>("Amount");
			amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
			
			amountCol.setCellFactory(column -> {
			    return new TableCell<BudgetItem, Double>() {
			        @Override
			        protected void updateItem(Double item, boolean empty) {
			            super.updateItem(item, empty);
			            if (item == null || empty) {
			                setText(null);
			            } else {
			                setText(String.format("%.2f", item));
			                if (getTableRow().getItem() instanceof BudgetItem && ((BudgetItem) getTableRow().getItem()).getDescription().equals("Total")) {
			                    setStyle("-fx-font-weight: bold;");
			                } else {
			                    setStyle("");
			                }
			            }
			        }
			    };
			});

			TableColumn<BudgetItem, String> dateCol = new TableColumn<>("Date");
			dateCol.setCellValueFactory(cellData -> {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	            Date date = cellData.getValue().getDate();
	            if (date != null) {
	                Instant instant = Instant.ofEpochMilli(date.getTime());
	                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	                String formattedDate = localDateTime.format(formatter);
	                return new ReadOnlyStringWrapper(formattedDate);
	            } else {
	                return new ReadOnlyStringWrapper("");
	            }
	        });
			
			TableColumn<BudgetItem, String> categoryCol = new TableColumn<>("Category");
			categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
			

			TableColumn<BudgetItem, String> categoryColumn = new TableColumn<>("Category");
	        categoryColumn.setCellValueFactory(cellData -> {
	            if (cellData.getValue() instanceof Expense) {
	                Expense expense = (Expense) cellData.getValue();
	                return new ReadOnlyStringWrapper(expense.getCategory().getCategory());
	            } else {
	                return new ReadOnlyStringWrapper(""); 
	            }
	        });	  
	        
			tableView.getColumns().addAll(descriptionCol, amountCol, dateCol, categoryColumn);
			tableView.setItems(FXCollections.observableArrayList());
	        tableView.setPrefWidth(600);
	        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
									
			monthComboBox.setOnAction(event -> {
				String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();
				if(selectedMonth != null) {
					int monthIndex = monthComboBox.getItems().indexOf(selectedMonth) + 1;
					showExpensesForMonth(monthIndex, budget);
				}
			});
			
			tableView.getItems().addListener(new ListChangeListener<BudgetItem>() {
			    @Override
			    public void onChanged(Change<? extends BudgetItem> change) {
			        updateTotal();
			        updatePieChart();
			        
			    }
			});
			
			VBox root = new VBox();
			root.setSpacing(10);
			root.setPadding(new Insets(10));
			root.getChildren().addAll(monthComboBox, tableView, pieChart);
			
			Scene scene = new Scene(root,600,600);
			monthStage.setScene(scene);
			monthStage.show();
						
	}	
	
	private void showExpensesForMonth(int month, Budget budget) {
	    if (budget != null) {
	        ArrayList<BudgetItem> expensesForMonth = new ArrayList<>();
	        for (BudgetItem item : budget.getBudgetItems()) {
	            LocalDateTime itemDateTime = LocalDateTime.ofInstant(item.getDate().toInstant(), ZoneId.systemDefault());
	            if (itemDateTime.getMonthValue() == month) {
	                expensesForMonth.add(item);
	            }
	        }
	        tableView.getItems().clear(); // Remove existing items (including total row)
	        tableView.setItems(FXCollections.observableArrayList(expensesForMonth));

	        updateTotal();
	        updatePieChart();
	    }
	}
	
	private void updateTotal() {		
		double total = 0;
	    for (BudgetItem budgetItem : tableView.getItems()) {
	        if (budgetItem instanceof Expense) {
	            total += budgetItem.getAmount();
	        } else if (budgetItem instanceof Income) {
	            total -= budgetItem.getAmount();
	        }
	    }
	    
	    String formattedTotal = String.format("%.2f", total);

	    BudgetItem totalRow = new BudgetItem("Total", Double.parseDouble(formattedTotal), null);
	    tableView.getItems().add(totalRow);
		
	}
	
	
	private void updatePieChart() {
		 pieChart.getData().clear();
		 Map<String, Double> categoryTotals = new HashMap<>();
		    
		    for (BudgetItem item : tableView.getItems()) {
		        if (item instanceof Expense) {
		            Expense expense = (Expense) item;
		            String category = expense.getCategory().getCategory();
		            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
		        } else if (item instanceof Income) {
		           
		        }
		    }
		    
		    for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
		        pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
		    }
	}
}