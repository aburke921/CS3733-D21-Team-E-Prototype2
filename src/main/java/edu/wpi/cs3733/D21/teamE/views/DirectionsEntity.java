package edu.wpi.cs3733.D21.teamE.views;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Singleton class for interaction with the Google Maps Directions API
 */
public class DirectionsEntity {
    // API Key, loaded from .env on init
    private static String API_KEY = "";

    // API Context
    private static GeoApiContext geoContext;

    // Travel Mode, default is driving
    private TravelMode mode = TravelMode.DRIVING;

    // Destinations
    private static final String MAIN = "75 Francis St, Boston, MA 02115";
    private static final String LEFT = "80 Francis St, Boston, MA 02115";
    private static final String RIGHT = "15-51 New Whitney St, Boston, MA 02115";

    private static DirectionsEntity instance = null; // Singleton Instance

    /**
     * Singleton instance getter
     * @return The Instance
     */
    public static DirectionsEntity getInstance()
    {
        if (instance == null) {
            instance = new DirectionsEntity();
        }
        return instance;
    }

    /**
     * Constructor runs init, loads API Key and connects to API
     */
    private DirectionsEntity() {
        init();
    }

    /**
     * Sets the travel mode to be used for directions
     * @param mode The travel mode to set to
     */
    public void setMode(TravelMode mode) {
        instance.mode = mode;
    }

    /**
     * Gets the directions to/from BWH from/to the origin location
     * @param origin The origin location as a string
     * @param toBWH Whether it is to or from BWH
     * @return The directions as a list of string, first string is the URL, second is the header
     */
    public List<String> getDirections(String origin, Boolean toBWH) {
        try {
            DirectionsWrapper result = getDir(origin, toBWH);
            DirectionsLeg trip = result.getResult().routes[0].legs[0];
            List<String> listing = directionsListing(trip, toBWH);
            listing.add(0, getURL(result.getOrigin(),result.getDestination(),result.getMode()));
            return listing;
        } catch (IOException exception) {
            System.err.println("IO Exception: " + exception.getMessage());
        } catch (InterruptedException exception) {
            System.err.println("Interrupted Exception: " + exception.getMessage());
        } catch (ApiException exception) {
            System.err.println("API Exception: " + exception.getMessage());
        }
        return null;
        //TODO: handle API exceptions and inform the user correctly
    }

    /**
     * Gets the directions from the API
     * Handles switch to main BWH for non-driving directions as well as fastest between left and right parking lots
     * @param origin The origin location as a string
     * @param toBWH Whether it is to or from BWH
     * @return API given directions as a DirectionsResult
     * @throws IOException API exceptions
     * @throws InterruptedException API exceptions
     * @throws ApiException API exceptions
     */
    private DirectionsWrapper getDir(String origin, Boolean toBWH) throws IOException, InterruptedException, ApiException {
        DirectionsApiRequest request = new DirectionsApiRequest(geoContext);
        request.mode(instance.mode).departureTimeNow();
        DirectionsResult result;

        DirectionsWrapper wrapper = new DirectionsWrapper(mode);

        if (toBWH) {
            String BWH = MAIN;
            request.origin(origin);
            wrapper.setOrigin(origin);
            if (instance.mode == TravelMode.DRIVING) {
                request.destination(LEFT);
                DirectionsResult left = request.await();

                request = new DirectionsApiRequest(geoContext);
                request.origin(origin);
                request.mode(mode).departureTimeNow();
                request.destination(RIGHT);
                DirectionsResult right = request.await();

                long leftDur = left.routes[0].legs[0].duration.inSeconds;
                long rightDur = right.routes[0].legs[0].duration.inSeconds;

                if (leftDur < rightDur) {
                    result = left;
                    BWH = LEFT;
                } else {
                    result = right;
                    BWH = RIGHT;
                }

            } else {
                request.destination(MAIN);
                result = request.await();
            }

            wrapper.setDestination(BWH);
        } else {
            request.origin(MAIN);
            request.destination(origin);
            wrapper.setOrigin(MAIN);
            wrapper.setDestination(origin);
            result = request.await();
        }

        wrapper.setResult(result);

        return wrapper;
    }

    /**
     * Converts the API returned directions to a list of string that is helpful to the user
     * @param trip The specific path to make directions for (fastest trip to first result from Geocoding of origin string)
     * @param toBWH Whether it is to or from BWH
     * @return The directions as a list of string, first string is the header
     */
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

    /**
     * Makes the header for the dialog with relevant info for the user
     * @param trip The specific path to make directions for (fastest trip to first result from Geocoding of origin string)
     * @param toBWH Whether it is to or from BWH
     * @return The processed header as a String
     */
    private String getHeader(DirectionsLeg trip, boolean toBWH) {
        StringBuilder SB = new StringBuilder("Directions ");
        SB.append((toBWH ? "to" : "from")).append(" Brigham and Women's Hospital ");
        SB.append((toBWH ? ("from " + trip.startAddress) : ("to " + trip.endAddress)));
        SB.append("\n\nBy ").append(modeName(instance.mode));
        SB.append("\t\tDistance: ").append(trip.distance);
        SB.append("\t\tDuration: ").append(trip.duration);
        return SB.toString();
    }

    /**
     * Gets a human-readable representation of the travel mode
     * @param mode The travel mode
     * @return A readable String of the mode
     */
    private String modeName(TravelMode mode) {
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

    private String getURL(String origin, String destination, TravelMode mode) {
        StringBuilder URL = new StringBuilder("https://www.google.com/maps/embed/v1/directions?key=");
        URL.append(API_KEY);
        URL.append("&origin=").append(origin.trim().replaceAll(" ", "+").replaceAll(",", ""));
        URL.append("&destination=").append(destination.trim().replaceAll(" ", "+").replaceAll(",", ""));

        String modeName;
        switch (mode) {
            case BICYCLING:
                modeName = "bicycling";
                break;
            case WALKING:
                modeName = "walking";
                break;
            case TRANSIT:
                modeName = "transit";
                break;
            default:
                modeName = "driving";
                break;
        }
        URL.append("&mode=").append(modeName);

        return URL.toString();
    }

    /**
     * Init method
     * Loads the API Key from the `.env` file in the project root directory
     * Connects with the API
     */
    private static void init() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(".env"));
            String keyLine = reader.readLine();
            String key = keyLine.replace("MAPS_API_KEY=", "");
            API_KEY = key;
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println(".env not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        getGeoContext();
    }

    /**
     * API connection method
     * If no connection exists, create a new one with timeout support, uses API Key
     */
    private static void getGeoContext() {
        if(geoContext == null) {
            geoContext = new GeoApiContext.Builder()
                    .apiKey(API_KEY)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .queryRateLimit(5)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .build();
        }
    }

    /**
     * Closes the API connect
     * Called on app closure
     */
    public static void close() {
        if(geoContext != null) {
            geoContext.shutdown();
        }
    }

    /**
     * Private class to wrap directions and store origin and destination as well
     */
    private class DirectionsWrapper {
        private DirectionsResult result;
        private String origin;
        private String Destination;
        private TravelMode mode;

        public DirectionsWrapper(TravelMode mode) {
            this.mode = mode;
        }

        public DirectionsResult getResult() {
            return result;
        }

        public String getOrigin() {
            return origin;
        }

        public String getDestination() {
            return Destination;
        }

        public TravelMode getMode() {
            return mode;
        }

        public void setResult(DirectionsResult result) {
            this.result = result;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public void setDestination(String destination) {
            Destination = destination;
        }
    }
}


