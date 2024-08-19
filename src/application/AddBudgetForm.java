package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddBudgetForm {
	
	public AddBudgetForm(Budget budget) {
        Stage addAlertStage = new Stage();
        addAlertStage.setTitle("Add Alert");
        Button submitButton = new Button("Submit");
        
        ComboBox<String> categoryComboBox = new ComboBox<>();
        TextField budgetLimit = new TextField();
        	
        for (Category category : budget.getCategories()) {
             categoryComboBox.getItems().add(category.getCategory());
        }

        VBox alertLayout = new VBox();
        alertLayout.getChildren().addAll(new Label("Limit amount: "), budgetLimit, new Label("Category: "), categoryComboBox, submitButton);
        alertLayout.setPadding(new Insets(10));
        alertLayout.setSpacing(5);
        alertLayout.setAlignment(Pos.CENTER);
        
        submitButton.setOnAction(event -> {
        	if(!budgetLimit.getText().isEmpty()) {
        		double amount = Double.parseDouble(budgetLimit.getText());
                String category = categoryComboBox.getValue();
                for(Category c : budget.getCategories()) {
                	if(category.equals(c.getCategory())) {
    	                c.setBudgetLimit(amount);
                	}
                }
        	}
            addAlertStage.close();
        });

        Scene scene = new Scene(alertLayout);
        addAlertStage.setScene(scene);
        addAlertStage.show();
	}
}
