package edu.wpi.cs3733.D21.teamE.views;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.text.WordUtils.wrap;


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

    public static String getMode() {
        switch (mode) {
            case DRIVING:
                return "Driving";
            case WALKING:
                return "Walking";
            case TRANSIT:
                return "Public Transit";
            case BICYCLING:
                return "Bicycling";
            default:
                return "ERROR";
        }
    }

    public static List<String> getDirections(String origin, Boolean toBWH) {
        try {
            DirectionsResult result = getDir(origin, toBWH);
            DirectionsLeg trip = result.routes[0].legs[0];
            List<String> listing = directionsListing(trip, toBWH);
            return listing;
        } catch (IOException exception) {
            System.err.println("IO Exception: " + exception.getMessage());
        } catch (InterruptedException exception) {
            System.err.println("Interrupted Exception: " + exception.getMessage());
        } catch (ApiException exception) {
            System.err.println("API Exception: " + exception.getMessage());
        }
        return null;
    }

    public static DirectionsResult getDir(String origin, Boolean toBWH) throws IOException, InterruptedException, ApiException {
        DirectionsApiRequest request = new DirectionsApiRequest(geoContext);
        request.mode(mode).departureTimeNow();
        //request.arrivalTime(Instant.parse("10:00 pm"));
        DirectionsResult result;

        if (toBWH) {
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

                result = ( leftDur < rightDur) ? (left) : (right);
            } else {
                request.destination(MAIN);
                result = request.await();
            }
        } else {
            request.origin(MAIN);
            request.destination(origin);
            request.mode(mode);
            result = request.await();
        }

        return result;
    }

    private static List<String> directionsListing(DirectionsLeg trip, boolean toBWH) {
        ArrayList<String> directions = new ArrayList<>();
        directions.add(getHeader(trip, toBWH));
        // First String is header
        for (DirectionsStep step: trip.steps) {
            String str = step.toString();
            str = str.replaceAll("\\<.*?\\>", "");
            str = str.substring(str.indexOf("\"")+1);
            String dir = str.substring(0, str.indexOf("\""));
            dir = dir.replaceAll("&nbsp;", " ");
            dir = wrap(dir, 100, null, false);
            directions.add(dir);
        }

        return directions;
    }

    private static String getHeader(DirectionsLeg trip, boolean toBWH) {
        StringBuilder SB = new StringBuilder("Directions ");
        SB.append((toBWH ? "To" : "From")).append(" Brigham and Women's Hospital ");
        SB.append((toBWH ? ("From " + trip.startAddress) : ("To " + trip.endAddress)));
        SB.append("\nBy ").append(mode);
        SB.append("\tDistance: ").append(trip.distance);
        SB.append("\tDuration: ").append(trip.duration);
        return SB.toString();
    }

    public static void init() {
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

    public static void close() {
        if(geoContext != null) {
            geoContext.shutdown();
        }
    }
}
