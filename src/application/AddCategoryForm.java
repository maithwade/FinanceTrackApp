package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddCategoryForm extends Stage {
	
	
	public AddCategoryForm(Budget budget) {		
			Stage addCategoryStage = new Stage();
			TextField categoryInput = new TextField();
			
			Button submitButton = new Button("Submit");
			
			VBox categoryLayout = new VBox();
			categoryLayout.getChildren().addAll(new Label("New Category Name: "), categoryInput, submitButton);
			categoryLayout.setPadding(new Insets(10));
			categoryLayout.setSpacing(5);
			categoryLayout.setAlignment(Pos.CENTER);
			
			submitButton.setOnAction(event -> {
				String categoryName = categoryInput.getText();
				if(!categoryName.isBlank()) {
					budget.getCategories().add(new Category(categoryName));
				}
				addCategoryStage.close();
			});
			
			Scene scene = new Scene(categoryLayout);
			addCategoryStage.setScene(scene);
			addCategoryStage.show();
	}
}
