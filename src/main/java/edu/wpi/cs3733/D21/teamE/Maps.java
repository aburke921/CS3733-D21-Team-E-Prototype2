package edu.wpi.cs3733.D21.teamE;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Maps {
    private static String API_KEY = ""; //Loaded on INIT

    private static final String MAIN = "Brigham and Womens'";
    private static final String LEFT = "80 Francis St, Boston, MA 02115";
    private static final String RIGHT = "15-51 New Whitney St, Boston, MA 02115";
    private static GeoApiContext geoContext;

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        init();
        Scanner io = new Scanner(System.in);
        System.out.println("Enter Origin Location");
        String origin = io.nextLine();
        System.out.println("\nEnter Transit Method");
        String method = io.nextLine();
        System.out.println();

        method.toUpperCase();

        TravelMode mode = TravelMode.DRIVING;

        switch (method) {
            case "WALKING":
                mode = TravelMode.WALKING;
                break;

            case "BICYCLING":
                mode = TravelMode.BICYCLING;
                break;

            case "TRANSIT":
                mode = TravelMode.TRANSIT;
                break;

            default:
                mode = TravelMode.DRIVING;
                break;
        }

        DirectionsApiRequest request = new DirectionsApiRequest(geoContext);
        request.mode(mode).departureTimeNow();

        request.origin(origin);
        request.destination(MAIN);

        // TODO: If transit method is not driving, use main entrance as the destination
        // TODO: If transit is driving, use the closer parking lot

        DirectionsResult result = request.await();
        System.out.println("Distance: " + result.routes[0].legs[0].distance + "\t Duration: " + result.routes[0].legs[0].duration);
        System.out.println();
        for (DirectionsStep step : result.routes[0].legs[0].steps) {
            System.out.println(step.toString());
        }

        geoContext.shutdown();
    }

    private static void init(){
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("MAPS_API_KEY");
        getGeoContext();
    }

    private static GeoApiContext getGeoContext() {
        if(geoContext == null) {
            geoContext = new GeoApiContext.Builder()
                    .apiKey(API_KEY)
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .queryRateLimit(5)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .build();
        }
        return geoContext;
    }
}
