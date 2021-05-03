package edu.wpi.cs3733.D21.teamE.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetsAndJava {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Google Sheets Example";
    private static String SPREADSHEET_ID = "1mHPadEEvNUn3CxcwiFVrmSPj0f5ioP3Q8_jVGnkiv7g";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("engineeringsoftware3733");

        return credential;
    }

    public static Sheets getSheetService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static int getRow(int appointmentID, String sheetName) throws IOException, GeneralSecurityException {
        sheetsService = getSheetService();
        int rowCounter = 0;
        String range = sheetName;

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

        List<List<Object>> values = response.getValues();

        for (List row : values) {
            rowCounter++;
            if (row.get(0).equals(appointmentID)){
                System.out.println("Row Num:" + rowCounter);
                break;
            }
        }
        return rowCounter - 1;
    }

    public static void deleteRow(int row, int sheetID) throws IOException, GeneralSecurityException {

        sheetsService = getSheetService();

        DeleteDimensionRequest deleteDateCell = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                        .setSheetId(sheetID)
                        .setDimension("ROWS")
                        .setStartIndex(row-1)
                        .setEndIndex(row)
                );

        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteDateCell));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
    }

    public static void addAppointmentToSheet(int appointmentID, String email, String firstName, String lastName, String doctor, String date, String sheetName) throws IOException, GeneralSecurityException {

        sheetsService = getSheetService();

        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(appointmentID, email, firstName, lastName, doctor, date)
                ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append((SPREADSHEET_ID), sheetName, appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

    }


    public static void deleteSheetData(int sheetID) throws GeneralSecurityException, IOException {
        sheetsService = getSheetService();

        DeleteDimensionRequest deleteDateCell = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                                .setSheetId(sheetID)
                                .setDimension("ROWS")
                                .setStartIndex(2)
                );

        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteDateCell));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
    }




}
