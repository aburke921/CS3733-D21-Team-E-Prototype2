package edu.wpi.cs3733.D21.teamE;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.Scanner;
import java.time.Instant;

public class DirectionsBoundary {
    public static void main(String[] args) {
        DirectionsController.init();

        Scanner io = new Scanner(System.in);
        System.out.println("Enter Origin Location");
        String origin = io.nextLine();
        System.out.println("\nEnter Transit Method");
        String method = io.nextLine();
        //System.out.println("\nEnter Desired Time of Arrival");
        //String arrivalTime = io.nextLine();
        // TODO: Figure out time scheduler
        System.out.println();

        switch (method.toUpperCase()) {
            case "WALKING":
                DirectionsController.setMode(TravelMode.WALKING);
                break;

            case "BICYCLING":
                DirectionsController.setMode(TravelMode.BICYCLING);
                break;

            case "TRANSIT":
                DirectionsController.setMode(TravelMode.TRANSIT);
                break;

            default:
                DirectionsController.setMode(TravelMode.DRIVING);
                break;
        }

        try {
            DirectionsResult result = DirectionsController.getDirections(origin);
            DirectionsLeg trip = result.routes[0].legs[0];
            System.out.println("From: " + trip.startAddress + " to " + trip.endAddress);
            System.out.println("Distance: " + trip.distance + "\tDuration: " + trip.duration + "\tDuration with Traffic: " + trip.durationInTraffic);
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (ApiException e) {
            System.err.println("API Exception: " + e.getMessage());
        }
    }
}
