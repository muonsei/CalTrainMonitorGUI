package view;

import javafx.scene.control.Alert;

public interface I_AlertFactory {
	Alert createConfirmationAlert();
	Alert createErrorAlert();
	Alert createInformationAlert();
	Alert createWarningAlert();
}
