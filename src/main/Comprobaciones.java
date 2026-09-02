package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comprobaciones {
    public static final Map<String, String> objects = new HashMap<>();


    public static void main(String[] args) throws IOException, URISyntaxException {
        addObjects();

        //List<String> bodyList = List.of("1", "2", "4", "5", "6", "7", "8", "9", "10", "Apophis", "301"); //DES=20099942
        //List<String> bodyList = List.of("1", "2", "4", "5", "6", "7", "8", "9", "10", "DES=54509621", "301");
        List<String> bodyList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "301");


        //writeIntoFile("2024YR4_JPL", bodyList, "2032-12-22%2008:24:00%20UTC", "2032-12-23", "1d");
        writeIntoFile("JPL_Errores2000", bodyList, "JD%201721424%20UTC", "JD%201721424.5", "1d");

/*
        String url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27Apophis%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTORS%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271,9,20%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String url2 = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27Apophis%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String url3 = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27DES=20099942%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String url4 = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%271%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTOR%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500@10%27&ANG_FORMAT=%27DEG%27&START_TIME=%27A.D.%200800-01-01%2012:00:00%20UTC%27&STOP_TIME=%27A.D.%200800-01-02%2000:00:00" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String url5 = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%271%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTOR%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500@10%27&ANG_FORMAT=%27DEG%27&START_TIME=%27JD%202013258%20UTC%27&STOP_TIME=%27JD%202013258.5" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String resultado = query(url5);
        System.out.println(resultado);*/


    }

    /**
     * Returns the output of an HTTP query
     *
     * @param url The URL to query
     * @return Output from a HTTP query
     * @throws IOException        File system errors
     * @throws URISyntaxException If the URL is incorrect
     */
    public static String query(String url) throws IOException, URISyntaxException {
        URL curl = (new URI(url)).toURL();
        HttpURLConnection http = (HttpURLConnection) curl.openConnection();
        http.setConnectTimeout(30000);
        InputStream stream = http.getInputStream();
        return getText(stream);
    }

    /**
     * Converts an input stream into a String, and closes the stream
     *
     * @param is Input stream
     * @return The text read from the provided stream
     * @throws IOException File system errors
     */
    public static String getText(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String inputLine;

        StringBuilder output = new StringBuilder(1000);
        boolean take = false;
        boolean finished = false;
        while ((inputLine = in.readLine()) != null && !finished) {
            if (take) {
                if (!inputLine.contains("$$EOE")) {
                    output.append(inputLine).append("\n");
                } else {
                    finished = true;
                }
            }
            if (inputLine.contains("$$SOE")) take = true;
        }
        in.close();
        /*String outputforChop = output.toString();
        String[] trozos = outputforChop.split("\\s+");
        return trozos[trozos.length - 2] + "," + trozos[trozos.length - 1];*/
        String lineaXYZ = output.substring(output.indexOf("X =")).split("\n")[0];

        return lineaXYZ.replace("X =", "").replace("Y =", "").replace("Z =", "").trim().replaceAll("\\s+", ",");
    }


    /**
     * Saves in a file the result of the HTTP query for the selected bodies and chosen options for start, end and step size.
     * @param filename Name of the file.
     * @param objectList List of objects to be queried.
     * @param startTime String containing the start time with the next format: "2029-04-13%2021:38:00%20UTC"
     * @param endTime String containing the end time with the next format: "2029-04-14"
     * @param stepSize String containing the step size with one of this two formats: "1d" / "50"
     */
    public static void writeIntoFile(String filename, List<String> objectList, String startTime, String endTime, String stepSize) {
        File directory = new File("src/main/java/resources");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File archivo = new File(directory, filename);

        try {
            FileWriter writer = new FileWriter(archivo,true);

            for (String object : objectList) {
                //startime formato: "2029-04-13%2021:38:00%20UTC
                // endtime formato: "2029-04-14"
                // stepsize formato: "1d" / "50"

                String urlObserver = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27" + object + "%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTOR%27" +
                        "&OUT_UNITS=%27AU-D%27&CENTER=%27500@10%27&ANG_FORMAT=%27DEG%27&START_TIME=%27" + startTime + "%27&STOP_TIME=%27" + endTime +
                        "%27&STEP_SIZE=%27" + stepSize + "%27&QUANTITIES=%271%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";

                String texto = query(urlObserver);
                String objectName = objects.get(object);
                writer.write("1, " + objectName + ", " + texto);
                writer.write(System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds objects to the hashmap where ids for the fetching of HorizonsJPL.
     */
    public static void addObjects() {
        objects.put("1", "Mercury");
        objects.put("2", "Venus");
        objects.put("3", "Earth");
        objects.put("4", "Mars");
        objects.put("5", "Jupiter");
        objects.put("6", "Saturn");
        objects.put("7", "Uranus");
        objects.put("8", "Neptune");
        objects.put("9", "Pluto");
        objects.put("10", "Sun");
        objects.put("Apophis", "Apophis");
        objects.put("DES=54509621", "2024 YR4");
        objects.put("301", "Moon");
    }

}
