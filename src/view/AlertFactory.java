package view;
import javafx.scene.control.Alert.AlertType;

public class AlertFactory implements I_AlertFactory {

	@Override
	public CustomAlert createConfirmationAlert() {
		return new CustomAlert(AlertType.CONFIRMATION);
	}

	@Override
	public CustomAlert createErrorAlert() {
		return new CustomAlert(AlertType.ERROR);
	}

	@Override
	public CustomAlert createInformationAlert() {
		return new CustomAlert(AlertType.INFORMATION);
	}

	@Override
	public CustomAlert createWarningAlert() {
		return new CustomAlert(AlertType.WARNING);
	}
}
