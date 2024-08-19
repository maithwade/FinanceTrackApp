package application;

import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemoveForm {

	public RemoveForm(Budget budget) {
        Stage removeStage = new Stage();
        
        ComboBox<String> categoryComboBox = new ComboBox<>();
        Button removeCategory = new Button("Remove category");
        
        ComboBox<String> limitComboBox = new ComboBox<>();
        Button reset = new Button("Reset limit");
        
        ComboBox<String> itemComboBox = new ComboBox<>();
        Button removeItem = new Button("Remove item");

        
        for (Category category : budget.getCategories()) {
             categoryComboBox.getItems().add(category.getCategory());
             limitComboBox.getItems().add(category.getCategory());
        }
        
        for(BudgetItem item : budget.getBudgetItems()) {
        	itemComboBox.getItems().add(item.getDescription());
        }
        
        VBox removeCategoryLayout = new VBox();
        removeCategoryLayout.setPadding(new Insets(10));
        removeCategoryLayout.setSpacing(5);
        removeCategoryLayout.setAlignment(Pos.CENTER);
        removeCategoryLayout.getChildren().addAll(new Label("Category: "), categoryComboBox, removeCategory);
        
        VBox resetLimitLayout = new VBox();
        resetLimitLayout.setPadding(new Insets(10));
        resetLimitLayout.setSpacing(5);
        resetLimitLayout.setAlignment(Pos.CENTER);
        resetLimitLayout.getChildren().addAll(new Label("Category: "), limitComboBox, reset);
        
        VBox removeItemLayout = new VBox();
        removeItemLayout.setPadding(new Insets(10));
        removeItemLayout.setSpacing(5);
        removeItemLayout.setAlignment(Pos.CENTER);
        removeItemLayout.getChildren().addAll(new Label("Item: "), itemComboBox, removeItem);
        
        HBox removeLayout = new HBox();
        removeLayout.getChildren().addAll(removeItemLayout, removeCategoryLayout, resetLimitLayout);
        
        removeCategory.setOnAction(cateogoryEvent -> {
            String categoryName = categoryComboBox.getValue();
            
            Iterator<Category> iter = budget.getCategories().iterator();
            while (iter.hasNext()) {
            	if(categoryName == null) break;
            	if(categoryName.equals(iter.next().getCategory())) {
	                iter.remove();
	                break;
            	}
            }
            
        });
        
        reset.setOnAction(resetEvent -> {
            String categoryName = limitComboBox.getValue();
            
            for(Category c : budget.getCategories()) {
            	if(categoryName == null) break;
            	if(categoryName.equals(c.getCategory())) {
	                c.resetBudgetLimit();
            	}
            }
        });
        
        removeItem.setOnAction(removeItemEvent -> {
            String itemName = itemComboBox.getValue();
            Iterator<BudgetItem> iterator = budget.getBudgetItems().iterator();
            while (iterator.hasNext()) {
                BudgetItem item = iterator.next();
                if (itemName.equals(item.getDescription()) && itemName != null) {
                    iterator.remove(); 
                    break;
                }
            }
        });
        
        Scene scene = new Scene(removeLayout);
        removeStage.setScene(scene);
        removeStage.show();
	}
}
