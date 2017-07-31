package model;

/**
 * Created by Mark Gavin on 7/17/2017.
 */
public class Passenger extends Thread {

    private Station currentStation;
    private Station destinationStation;
    private int destinationStationNo;
    private Train inside;
    private final int passengerNo;
    public static int passengersSpawned;

    public Passenger(int x, Station current, Station destination) {
        currentStation = current;
        destinationStation = destination;
        destinationStationNo = x;
        passengersSpawned++;
        passengerNo = passengersSpawned;
        this.start();
    }

    public void departTrain(int stationNum)//resets the train
    {
            inside = null;
            System.out.println("Passenger " + passengerNo + 
                " departed In Station " + (stationNum+2)  + ".");
    }

    public boolean checkDepart(int stationNum)//checks if this is destination station
    {
        if(destinationStationNo == stationNum + 1)
            return true;
        else
            return false;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public Train getTrainInside() {
        return inside;
    }

    public int getpassengerNo() {
        return passengerNo;
    }

    public void setTrain(Train x) {
    	inside = x;
    }

    public void run() {
        currentStation.station_wait_for_train(this);
        this.setTrain(currentStation.getCurrentlyLoading());
        currentStation.waiting.remove(this);
    }

}

