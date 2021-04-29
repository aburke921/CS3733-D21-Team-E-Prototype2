package edu.wpi.cs3733.D21.teamE;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Maps {
    private static GeoApiContext geoContext;

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = getGeoContext();

        Scanner io = new Scanner(System.in);
        System.out.println("Enter Origin Location");
        String origin = io.nextLine();

        System.out.println("\nEnter Destination Location");
        String dest = io.nextLine();
        System.out.println();

        DirectionsResult result = DirectionsApi.getDirections(context, origin, dest).await();
        for (DirectionsStep step : result.routes[0].legs[0].steps) {
            System.out.println(step.toString());
        }

        context.shutdown();
    }

    private static GeoApiContext getGeoContext() {
        if(geoContext == null) {
            geoContext = new GeoApiContext.Builder()
                    .apiKey("Insert API Key Here")
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .queryRateLimit(5)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .build();
        }
        return geoContext;
    }
}
