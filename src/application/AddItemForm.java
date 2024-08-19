package application;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddItemForm extends Stage {

    public AddItemForm(Budget budget) {
        this.setTitle("Add New Item");

        TextField descriptionTextField = new TextField();
        TextField amountTextField = new TextField();
        DatePicker datePicker = new DatePicker();
        ComboBox<String> categoryComboBox = new ComboBox<>();
        RadioButton incomeRadio = new RadioButton("Income");
        RadioButton expenseRadio = new RadioButton("Expense");
        Button submitButton = new Button("Submit");

        for (Category category : budget.getCategories()) {
            categoryComboBox.getItems().add(category.getCategory());
        }

        ToggleGroup toggleGroup = new ToggleGroup();
        incomeRadio.setToggleGroup(toggleGroup);
        expenseRadio.setToggleGroup(toggleGroup);
     
        incomeRadio.setOnAction(event -> {
            categoryComboBox.setDisable(true);
        });

        expenseRadio.setOnAction(event -> {
            categoryComboBox.setDisable(false);
        });
        
        VBox layout = new VBox();
        layout.getChildren().addAll(new Label("Description:"), descriptionTextField,
                new Label("Amount:"), amountTextField,
                new Label("Date:"), datePicker,
                incomeRadio, expenseRadio,
                new Label("Category:"), categoryComboBox,
                submitButton);
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        
        Scene scene = new Scene(layout, 600, 400);
        this.setScene(scene);
        this.show();

        submitButton.setOnAction(e -> {
            String description = descriptionTextField.getText();
            String amountString = amountTextField.getText();
           
            String category = categoryComboBox.getValue();
            LocalDate selectedDate = datePicker.getValue();
            if(!description.isEmpty() && !amountString.isEmpty()) {            	
            	double amount = Double.parseDouble(amountString);
            	
            	Instant instant = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
            	Date date = Date.from(instant);
            	
            	BudgetItem newItem;
            	
            	if (incomeRadio.isSelected()) {
            		newItem = new Income(description, -amount, date);
            		budget.addItem(newItem);
            		
            	} else {
            		newItem = new Expense(description, +amount, date, new Category(category));
            		budget.addItem(newItem);
            		
            		for (Category c : budget.getCategories()) {
            			if (category.equals(c.getCategory())) {
            				c.setCategoryTotal(amount + c.getCategoryTotal());
            				if(c.getBudgetLimit() == 0) continue;
            				else if(c.exceedsLimit()) {
            					Alert alertPopup = new Alert(AlertType.WARNING);
            					alertPopup.initStyle(StageStyle.UTILITY);
            					alertPopup.setTitle("Budget Alert!");
            					String message = "You are exceeding the budget limit for " + c.getCategory() + ". The limit is $" + c.getBudgetLimit() + ".";
            					alertPopup.setContentText(message);
            					alertPopup.showAndWait();
            				}
            				break; 
            			}
            		}
            		
            	}
            }
            	
            this.close(); 
        });
    }

}
