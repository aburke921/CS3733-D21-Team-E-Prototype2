package edu.wpi.cs3733.D21.teamE;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class DirectionsController {
    // API Key, loaded from .env on init
    private static String API_KEY = "";

    // API Context
    private static GeoApiContext geoContext;

    // Travel Mode, default is driving
    private static TravelMode mode = TravelMode.DRIVING;

    // Time Instance (can be departure or arrival time)
    private static Instant time;

    // Destinations
    private static final String MAIN = "Brigham and Womens'";
    private static final String LEFT = "80 Francis St, Boston, MA 02115";
    private static final String RIGHT = "15-51 New Whitney St, Boston, MA 02115";

    public static void setMode(TravelMode mode) {
        DirectionsController.mode = mode;
    }

    public static DirectionsResult getDirections(String origin) throws IOException, InterruptedException, ApiException {
        DirectionsApiRequest request = new DirectionsApiRequest(geoContext);
        request.mode(mode).departureTimeNow();
        //request.arrivalTime(Instant.parse("10:00 pm"));
        DirectionsResult result;

        request.origin(origin);
        if (mode == TravelMode.DRIVING) {
            request.destination(LEFT);
            DirectionsResult left = request.await();

            request = new DirectionsApiRequest(geoContext);
            request.origin(origin);
            request.mode(mode).departureTimeNow();
            request.destination(RIGHT);
            DirectionsResult right = request.await();

            long leftDur = left.routes[0].legs[0].duration.inSeconds;
            long rightDur = right.routes[0].legs[0].duration.inSeconds;

            System.out.println("Left Dur: " + leftDur + "\tRight Dur: " + rightDur + "\tBest Dur: " + Math.min(leftDur, rightDur));

            result = ( leftDur < rightDur) ? (left) : (right);
        } else {
            request.destination(MAIN);
            result = request.await();
        }

        // TODO: add close method that is called when page is switched
        geoContext.shutdown();

        return result;
    }

    public static void init(){
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("MAPS_API_KEY");
        getGeoContext();
    }

    private static void getGeoContext() {
        if(geoContext == null) {
            geoContext = new GeoApiContext.Builder()
                    .apiKey(API_KEY)
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .queryRateLimit(5)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .build();
        }
    }
}
