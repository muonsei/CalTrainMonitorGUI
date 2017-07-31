package view;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class CustomAlert extends Alert implements I_CustomAlert {

	public CustomAlert(AlertType alertType) {
		super(alertType);
		this.setTitle("CalTrainII");
		this.setHeaderText(null);
		this.customize();
	}

	@Override
	public void customize() {
		DialogPane dialogPane = this.getDialogPane();
		dialogPane.getStylesheets().add(
				   getClass().getResource("/fxml/dialog.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");
	}

}