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


public class DirectionsController {
    // API Key, loaded from .env on init
    private static String API_KEY = "";

    // API Context
    private static GeoApiContext geoContext;

    // Travel Mode, default is driving
    private TravelMode mode = TravelMode.DRIVING;

    // Destinations
    private static final String MAIN = "Brigham and Womens'";
    private static final String LEFT = "80 Francis St, Boston, MA 02115";
    private static final String RIGHT = "15-51 New Whitney St, Boston, MA 02115";

    private static DirectionsController instance = null;

    public static DirectionsController getInstance()
    {
        if (instance == null)
            instance = new DirectionsController();

        return instance;
    }

    private DirectionsController() {
        init();
    }

    public void setMode(TravelMode mode) {
        instance.mode = mode;
    }

    public List<String> getDirections(String origin, Boolean toBWH) {
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

    private DirectionsResult getDir(String origin, Boolean toBWH) throws IOException, InterruptedException, ApiException {
        DirectionsApiRequest request = new DirectionsApiRequest(geoContext);
        request.mode(mode).departureTimeNow();
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

    private List<String> directionsListing(DirectionsLeg trip, boolean toBWH) {
        ArrayList<String> directions = new ArrayList<>();
        directions.add(getHeader(trip, toBWH));
        // First String is header
        int stepNum = 1;
        for (DirectionsStep step: trip.steps) {
            String str = step.toString();
            str = str.replaceAll("<div style=\"font-size:0.9em\">", " "); // linebreak comp
            str = str.replaceAll("\\<.*?\\>", "");
            str = str.substring(str.indexOf("\"")+1);
            String dir = str.substring(0, str.indexOf("\""));
            dir = dir.replaceAll("&nbsp;", " ");
            String dirWithNum = (stepNum++) + ")\t " + dir;
            directions.add(dirWithNum);
        }

        return directions;
    }

    private String getHeader(DirectionsLeg trip, boolean toBWH) {
        StringBuilder SB = new StringBuilder("Directions ");
        SB.append((toBWH ? "to" : "from")).append(" Brigham and Women's Hospital ");
        SB.append((toBWH ? ("from " + trip.startAddress) : ("to " + trip.endAddress)));
        SB.append("\n\nBy ").append(modeName(instance.mode));
        SB.append("\t\tDistance: ").append(trip.distance);
        SB.append("\t\tDuration: ").append(trip.duration);
        return SB.toString();
    }

    private static String modeName(TravelMode mode) {
        String str;
        switch (mode) {
            case WALKING:
                str =  "Walking";
                break;
            case BICYCLING:
                str =  "Bicycling";
                break;
            case TRANSIT:
                str =  "Public Transit";
                break;
            default:
                str =  "Driving";
                break;
        };
        return str;
    }

    private static void init() {
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
