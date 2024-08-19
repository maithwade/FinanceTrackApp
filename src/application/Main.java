package application;
	
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {
	
	private TableView<BudgetItem> tableView = new TableView<>();
	private ObservableList<BudgetItem> budgetItems = FXCollections.observableArrayList();
	Set<Category> categories = new HashSet<>();	

	@Override
	public void start(Stage primaryStage) {
		try {
			Budget budget = new Budget(0, budgetItems, categories);
			
			initData(budget);
	        
			BorderPane root = new BorderPane();
			
	        Text totalBalanceLabel = new Text("Total Balance: $" + budget.calcBalance());
            totalBalanceLabel.setText("Total Balance: $" + String.format("%.2f", budget.calcBalance()));
	        Font font = Font.font("Arial", FontWeight.BOLD, 30);

	        totalBalanceLabel.setFont(font);
	        
	        TableColumn<BudgetItem, String> descriptionColumn = new TableColumn<>("Description");
	        descriptionColumn.setPrefWidth(200);
	        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
	        TableColumn<BudgetItem, Double> amountColumn = new TableColumn<>("Amount");
	        amountColumn.setCellValueFactory(cellData -> {
	            double amount = cellData.getValue().getAmount();
	            return new ReadOnlyObjectWrapper<Double>(amount);
	        });
	        
	        TableColumn<BudgetItem, String> dateColumn = new TableColumn<>("Date");
	        dateColumn.setCellValueFactory(cellData -> {
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
	        TableColumn<BudgetItem, String> categoryColumn = new TableColumn<>("Category");
	        categoryColumn.setCellValueFactory(cellData -> {
	            if (cellData.getValue() instanceof Expense) {
	                Expense expense = (Expense) cellData.getValue();
	                return new ReadOnlyStringWrapper(expense.getCategory().getCategory());
	            } else {
	                return new ReadOnlyStringWrapper(""); 
	            }
	        });	       
	        tableView.getColumns().addAll(descriptionColumn, amountColumn, dateColumn, categoryColumn);
	        tableView.setItems(FXCollections.observableArrayList(budget.getBudgetItems()));
	        tableView.setPrefWidth(600);
	        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	        Button addButton = new Button("Add Item");
	        addButton.setOnAction(e -> {
	            new AddItemForm(budget);
	        });
	        
	        Button addCategoryButton = new Button("Add Category");
	        addCategoryButton.setOnAction(e -> {
	            new AddCategoryForm(budget);
	        });
	        
	        Button addAlertButton = new Button("Add Budget Alert");
	        addAlertButton.setOnAction(e -> {
	        	new AddBudgetForm(budget);
	        });
	        
	        Button removeButton = new Button("Remove");
	        removeButton.setOnAction(e -> {
	        	new RemoveForm(budget);
	        });
	        
	        Button selectMonthButton = new Button("Select Month");
	        selectMonthButton.setOnAction(e -> {
	        	new MonthPage(budget);
	        });
            
	        budgetItems.addListener((ListChangeListener.Change<? extends BudgetItem> change) -> {
	            while (change.next()) {
	                if (change.wasAdded()) {
	                    tableView.getItems().addAll(change.getAddedSubList());
	                    String formattedBalance = String.format("%.2f", budget.calcBalance());
	                    totalBalanceLabel.setText("Total Balance: $" + formattedBalance);
	                }
	                if (change.wasRemoved()) {
	                    tableView.getItems().removeAll(change.getRemoved());
	                    String formattedBalance = String.format("%.2f", budget.calcBalance());
	    	            totalBalanceLabel.setText("Total Balance: $" + formattedBalance);
	                }
	            }
	        });
	        
	        HBox bottomBar = new HBox();
	        bottomBar.getChildren().addAll(addButton, addAlertButton, addCategoryButton, removeButton, selectMonthButton);
	        bottomBar.setSpacing(10);
	        bottomBar.setAlignment(Pos.CENTER);
	        bottomBar.setPadding(new Insets(10,0,0,0));
	        

	        HBox topBar = new HBox();
	        topBar.getChildren().add(totalBalanceLabel);
	        topBar.setAlignment(Pos.CENTER);
	        topBar.setPadding(new Insets(0,0,10,0));
	        root.setTop(topBar);
	        root.setCenter(tableView);
	        root.setBottom(bottomBar);
	        
	        
	        root.setPadding(new Insets(20));
	         

	        Scene scene = new Scene(root, 700, 700);
	        primaryStage.setScene(scene);
	        primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initData(Budget budget) {
		budget.addCategory(new Category("Food"));
		budget.addCategory(new Category("Shopping"));
		budget.addCategory(new Category("Entertainment"));
		budget.addCategory(new Category("Housing"));
		budget.addCategory(new Category("Transportation"));
		
	    for (int i = 0; i < 50; i++) {
	        double amount = Math.round((Math.random() * 90 + 10) * 100.0) / 100.0;

	        String[] descriptions = {"MBTA", "Uber", "Burgers", "Sushi", "Groceries", "Concert tickets", "Flights", "Amazon", "Rent"};
	        String description = descriptions[(int) (Math.random() * descriptions.length)];

	        LocalDateTime currentDateTime = LocalDateTime.now();
	        int randomMonth = (int) (Math.random() * 12) + 1;
	        int randomDay = (int) (Math.random() * 28) + 1;
	        LocalDateTime randomDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonth().minus(randomMonth), randomDay, 0, 0);
	        Date date = Date.from(randomDateTime.atZone(ZoneId.systemDefault()).toInstant());

	        Category category = new Category("");
	        switch (description) {
	        case "MBTA":
	        case "Flights":
	        case "Uber":
	        	category = new Category("Transportation");
	        	break;
	        case "Burgers":
	        case "Sushi":
	        case "Groceries":
	        	category = new Category("Food");
	        	break;
	        case "Concert tickets":
	        	category = new Category("Entertainment");
	        	break;
	        case "Amazon":
	        	category = new Category("Shopping");
	        	break;
	        case "Rent":
	        	category = new Category("Housing");
	        	break;
	        }
	        
	        Expense expense = new Expense(description, amount, date, category);
	        budget.addItem(expense);
	        
	        if(i % 10 == 0) {
		        Income income = new Income("income", amount, date);
		        budget.addItem(income);
	        }
	        
	    }
	}
	
	

	public static void main(String[] args) {
		launch(args);
	}
}
