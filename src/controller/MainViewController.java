package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import model.Station;
import model.Train;
import view.AlertFactory;
import view.CustomAlert;

public class MainViewController {
	
	private AlertFactory alertFactory;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane backgroundAnchor;

    @FXML
    private TextField trainPassengerCapTextField;

    @FXML
    private TextField passengerCountTextField;

    @FXML
    private ComboBox<String> passengerSourceDropdown;

    @FXML
    private ComboBox<String> passengerDestinationDropdown;
    
    @FXML
    private Button spawnTrainButton;

    @FXML
    private Button spawnPassengerButton;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private AnchorPane statusTabBackgroundAnchor;
    
    @FXML
    private AnchorPane spawnTabBackgroundAnchor;
    
    @FXML
    private AnchorPane trainViewAnchor;
    
    @FXML
    private ScrollPane trainStatusScrollPane;

    @FXML
    private ScrollPane stationStatusScrollPane;
    
    @FXML
    private Button stopSimulationButton;
    
    @FXML
    private GridPane trainStatusGridPane;
    
    @FXML
    private GridPane stationStatusGridPane;
    
    // Station
    private Label[][] stationStatusLabels;
    private StringProperty[][] stationStatusProperties;
    
    // Train
    private Label[][] trainStatusLabels;
    private StringProperty[][] trainStatusProperties;
    
    // Train sprite
    private Label[] trainSprite;
    private TranslateTransition[] transition;
    
    /* NON-GUI ATTRIBS */
    
    private Station[] stationArray = new Station[8];
	private int x, y, trainsSpawned;


    @FXML
    void initialize() {
        assert backgroundAnchor != null : "fx:id=\"backgroundAnchor\" was not injected: check your FXML file 'MainView.fxml'.";
        assert trainPassengerCapTextField != null : "fx:id=\"trainPassengerCapTextField\" was not injected: check your FXML file 'MainView.fxml'.";
        assert passengerCountTextField != null : "fx:id=\"passengerCountTextField\" was not injected: check your FXML file 'MainView.fxml'.";
        assert passengerSourceDropdown != null : "fx:id=\"passengerSourceDropdown\" was not injected: check your FXML file 'MainView.fxml'.";
        assert passengerDestinationDropdown != null : "fx:id=\"passengerDestinationDropdown\" was not injected: check your FXML file 'MainView.fxml'.";
        assert spawnTrainButton != null : "fx:id=\"spawnTrainButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert spawnPassengerButton != null : "fx:id=\"spawnPassengerButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert statusTabBackgroundAnchor != null : "fx:id=\"statusTabBackgroundAnchor\" was not injected: check your FXML file 'MainView.fxml'.";
        assert spawnTabBackgroundAnchor != null : "fx:id=\"spawnTabBackgroundAnchor\" was not injected: check your FXML file 'MainView.fxml'.";
        assert trainViewAnchor != null : "fx:id=\"trainViewAnchor\" was not injected: check your FXML file 'MainView.fxml'.";
        assert trainStatusScrollPane != null : "fx:id=\"trainStatusScrollPane\" was not injected: check your FXML file 'MainView.fxml'.";
        assert stationStatusScrollPane != null : "fx:id=\"stationStatusScrollPane\" was not injected: check your FXML file 'MainView.fxml'.";
        assert stopSimulationButton != null : "fx:id=\"stopSimulationButton\" was not injected: check your FXML file 'MainView.fxml'.";
        assert trainStatusGridPane != null : "fx:id=\"trainStatusGridPane\" was not injected: check your FXML file 'MainView.fxml'.";
        assert stationStatusGridPane != null : "fx:id=\"stationStatusGridPane\" was not injected: check your FXML file 'MainView.fxml'.";
        
        alertFactory = new AlertFactory();
        
        passengerSourceDropdown.getItems().removeAll(passengerSourceDropdown.getItems());
        passengerSourceDropdown.getItems().addAll("Station 1", "Station 2", "Station 3",
        		"Station 4", "Station 5", "Station 6", "Station 7", "Station 8");
        passengerSourceDropdown.getSelectionModel().select("Station 1");
        
        passengerDestinationDropdown.getItems().removeAll(passengerDestinationDropdown.getItems());
        passengerDestinationDropdown.getItems().addAll("Station 1", "Station 2", "Station 3",
        		"Station 4", "Station 5", "Station 6", "Station 7", "Station 8");
        passengerDestinationDropdown.getSelectionModel().select("Station 1");
        
        /*--------------------------------------------------*
         *                  SPAWN STATIONS
         *--------------------------------------------------*/
        
        for (x = 0; x < 8; x++) { // instantiate new stations
			stationArray[x] = new Station(x, this);
		}
		
		for (x = 0; x < 7; x++) // set next station for station 0-6
			stationArray[x].setNextStop(stationArray[x + 1]);
		
		stationArray[7].setNextStop(stationArray[0]);
		
		for (x = 1; x < 8; x++) // set previous station for station 1-7
			stationArray[x].setPreviousStop(stationArray[x - 1]);
		
		stationArray[0].setPreviousStop(stationArray[7]);
        
		/*--------------------------------------------------*
         *           INITIALIZE OTHER VARIABLES
         *--------------------------------------------------*/
		trainsSpawned = 0;
		
		stationStatusLabels = new Label[8][3];
		stationStatusProperties = new StringProperty[8][3];
		trainStatusLabels = new Label[15][5];
		trainStatusProperties = new StringProperty[15][5];
		
		// Stations
    	for (x = 0; x < 8; x++) {
    		for (y = 0; y < 3; y++) { // don't include 0
    			stationStatusLabels[x][y] = new Label();
    			stationStatusProperties[x][y] = new SimpleStringProperty("-");
    			stationStatusLabels[x][y].textProperty().bind(stationStatusProperties[x][y]);
    			stationStatusGridPane.add(stationStatusLabels[x][y], y, x);
    		}
		}
    	
    	// Trains
    	for (x = 0; x < 15; x++) {
    		for (y = 0; y < 5; y++) {
    			trainStatusLabels[x][y] = new Label();
    			trainStatusProperties[x][y] = new SimpleStringProperty("-");
    			trainStatusLabels[x][y].textProperty().bind(trainStatusProperties[x][y]);
    			trainStatusGridPane.add(trainStatusLabels[x][y], y, x);
    		}
    	}
    	
    	initializeOtherLabels();
    	
    	/*--------------------------------------------------*
         *                INITIALIZE SPRITES
         *--------------------------------------------------*/
    	trainSprite = new Label[15];
    	for (x = 0; x < 15; x++) {
    		trainSprite[x] = new Label("" + (x+1));
    		trainSprite[x].setStyle("-fx-background-color: #005895; "
    				+ "-fx-text-fill: #FFFFFF; -fx-padding: 10px;");
    		trainSprite[x].setLayoutX(40);
    		trainSprite[x].setLayoutY(52);
    		trainSprite[x].setVisible(false);
    		trainViewAnchor.getChildren().add(trainSprite[x]);
    	}
    	
    	// Transitions
    	transition = new TranslateTransition[15];
    	for (x = 0; x < 15; x++) {
    		transition[x] = new TranslateTransition();
    		transition[x].setNode(trainSprite[x]);
    		transition[x].setDuration(Duration.millis(500)); 
    		transition[x].toXProperty().set(0);
    		transition[x].toYProperty().set(0);
    	}
    }
    
    private void initializeOtherLabels() {
    	// Stations
    	for (x = 0; x < 8; x++) {
    		stationStatusProperties[x][0].setValue((x+1) + "");
    	}
    		
    	// Trains
    	for (x = 0; x < 15; x++) {
    		trainStatusProperties[x][0].setValue((x+1) + "");
    	}
    }
    
    @FXML
    private void spawnTrainAction(ActionEvent event) {
    	int trainCap;
    	
    	if (trainsSpawned < 15) {
    		try{
    	    	trainCap = Integer.parseInt(trainPassengerCapTextField.getText());
    	    	if (trainCap > 0) {
    	    		CustomAlert warning = alertFactory.createInformationAlert();
                	warning.setContentText("Spawned a train with a capacity of " +
                			trainCap + " passengers.");
                	warning.setTitle("Spawning a train");
                	warning.showAndWait();
                	trainSprite[trainsSpawned].setVisible(true);
                	trainsSpawned ++;
        	    	stationArray[0].spawnTrain(trainCap); // CREATES TRAIN HERE
    	    	}
    	    	else {
    	    		CustomAlert error = alertFactory.createErrorAlert();
            		error.setContentText("Cannot spawn a train without any seats.");
            		error.setTitle("Error in spawning a train");
            		error.showAndWait();
    	    	}
    	    } catch(NumberFormatException nfe) {
        		CustomAlert error = alertFactory.createErrorAlert();
        		error.setContentText("Passenger capacity only accepts integer values.");
        		error.setTitle("Error in spawning a train");
        		error.showAndWait();
        	}	
    	}
    	
    	else {
    		CustomAlert error = alertFactory.createErrorAlert();
    		error.setContentText("Only up to 15 trains can be spawned during runtime.");
    		error.setTitle("Error in spawning a train");
    		error.showAndWait();
    	}
    	
    	trainPassengerCapTextField.clear();
    }
    
    @FXML
    private void spawnPassengerAction(ActionEvent event) {
    	int source = passengerSourceDropdown.getSelectionModel().getSelectedIndex();
    	int destination = passengerDestinationDropdown.getSelectionModel().getSelectedIndex();
    	int passengerCount = 0;
    	try {
    		passengerCount = Integer.parseInt(passengerCountTextField.getText());
    		if (source == destination) {
    			CustomAlert error = alertFactory.createErrorAlert();
        		error.setContentText("Source station cannot be the same as destination.");
        		error.setTitle("Error in spawning passengers");
        		error.showAndWait();
    		}
    		else {
    			CustomAlert warning = alertFactory.createInformationAlert();
            	warning.setContentText("Spawned " + passengerCount + 
            			" passengers in Station " + (source + 1) + 
            			" to be dropped off at Station " + (destination + 1) + ".");
            	warning.setTitle("Spawning passengers");
            	warning.showAndWait();
            	
            	while (passengerCount > 0) {
            		stationArray[source].spawnPassenger(destination, stationArray[source], stationArray[destination]);
            		passengerCount--;
            	}
    		}
    	} catch(NumberFormatException nfe) {
    		CustomAlert error = alertFactory.createErrorAlert();
    		error.setContentText("Passenger count only accepts integer values.");
    		error.setTitle("Error in spawning passengers");
    		error.showAndWait();
    	}
        	
        // Reset values
    	passengerSourceDropdown.getSelectionModel().select("Station 1");
    	passengerDestinationDropdown.getSelectionModel().select("Station 1");
    	passengerCountTextField.clear();
    }
    
    @FXML
    private void stopSimulationAction(ActionEvent event) {
    	CustomAlert warning = alertFactory.createInformationAlert();
    	warning.setContentText("Simulation ended.");
    	warning.setTitle("CalTrainII");
    	warning.showAndWait();
    }
    
    public void updateTrainPassengers(Train t) {
    	int trainNo, passengerCount, seatsLeft;
    	
    	trainNo = t.getTrainNo() - 1;
    	passengerCount = t.getNumberofPassengers();
    	seatsLeft = t.countFreeSeats();
    	trainStatusProperties[trainNo][1].setValue(passengerCount + "");
    	trainStatusProperties[trainNo][2].setValue(seatsLeft + "");
    }
    
    public void updateTrainLocation(int trainNo, int stationNo) {
    	trainNo -= 1;
    	trainStatusProperties[trainNo][3].setValue(stationNo + "");
    }
    
    public void updateTrainStatus(int trainNo, String status) {
    	trainNo -= 1;
    	trainStatusProperties[trainNo][4].setValue(status);
    }
    
    public void updateStationWaiting(int stationNo, int passengerWaitCount) {
    	stationNo -= 1;
    	stationStatusProperties[stationNo][1].setValue(passengerWaitCount + "");
    }
    
    public void updateStationLoading(int stationNo, int trainNo) {
    	stationNo -= 1;
		stationStatusProperties[stationNo][2].setValue(trainNo + "");
    }
    
    public void updateStationLoading(int stationNo, String message) {
    	stationNo -= 1;
		stationStatusProperties[stationNo][2].setValue("-");
    }
    
    public void moveTrainSprite(int stationNo, int trainNo) {
    	double xpos, ypos;
    	trainNo -= 1;
    	xpos = trainSprite[trainNo].getTranslateX();
    	ypos = trainSprite[trainNo].getTranslateY();
    	System.out.println("trainNo = " + trainNo + "; x = " + xpos + "; y = " + ypos);
		
    	switch(stationNo) {
    		case 2:
    		case 3: 
    			System.out.println("!!! Going right !!!");
    			transition[trainNo].setToX(xpos + 175);
    			transition[trainNo].play();
    			break;
    		case 4:
    		case 5:
    			System.out.println("!!! Going down !!!");
    			transition[trainNo].setToY(ypos + 182);
    			transition[trainNo].play();
    			break;
    		case 6:
    		case 7:
    			System.out.println("!!! Going left !!!");
    			transition[trainNo].setToX(xpos - 175);
    			transition[trainNo].play();
    			break;
    		case 8:
    		case 1:
    			System.out.println("!!! Going up !!!");
    			transition[trainNo].setToY(ypos - 182);
    			transition[trainNo].play();
    			break;
    	}
    }
}
