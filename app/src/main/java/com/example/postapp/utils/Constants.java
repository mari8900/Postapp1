package com.example.postapp.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    // max 14 days to keep parcel at the postal office
    public static int appointmentDays = 14;

    public static List<String> minutes = Arrays.asList("00", "10", "20", "30", "40", "50");
    public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";

    public static String[] opList = new String[] {"Oficiul Postal 7",
            "Oficiul Postal 23",
            "Oficiul Postal 38",
            "Oficiul Postal 44",
            "Oficiul Postal 56",
            "Oficiul Postal 67",
            "Oficiul Postal 79",
            "Oficiul Postal 84"};

    public static String[] addressList = new String[] {"Sos. Giurgiului 119",
            "Str Romancierilor 1",
            "Str. Teiul Doamnei 19",
            "Str. Gheorghe Sincai 2",
            "Calea Crangasi 23",
            "Calea Plevnei 46-48",
            "Calea Mosilor 314",
            "Splaiul Independentei 290"};

    public static String databaseReference = "postapp-ad2d5-default-rtdb";
}
