package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Comprobaciones {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%2710%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTORS%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271,9,20%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        String url2 = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27Apophis%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27" +
                "&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14" +
                "%27&STEP_SIZE=%271d%27&QUANTITIES=%271,9,20%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        /*String resultado = query(url);
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
                }else {
                    finished = true;
                }
            }
            if (inputLine.contains("$$SOE")) take = true;
        }
        in.close();
        return output.toString();
    }

    // X = -1.455656900545057E-04 Y = 1.751927427817715E-04 Z = 1.141817805459706E-04    JPL
    // X = -1.4500160886044E-4    Y = 1.7578322869904461E-4 Z = 1.1475726733700675E-4    POO
    // X = -1.4649900667473847E-4 Y = 1.7717332974365174E-4 Z = 1.1519435237661302E-4    EOO

    // Apophis | 129.72265        :  26.62301              JPL
    // Apophis | 129.5186956031062:  26.728963197811982    POO
    // Apophis | 129.58611425722697: 26.612975937764222    EOO

    // Pluto(Sun):
    // X = 22.39145277362250  Y = -24.60284945484394 Z = -14.42456582921362   JPL
    // X = 22.342215393477137 Y = -24.52871026222943 Z = -14.39150456821061   POO
    // X = 22.342215393477137 Y = -24.52871026222943 Z = -14.39150456821061   EOO

    // Apohphis(Sun):
    // X = -0.9175567641209890 Y = -0.3716242567082039 Z = -0.1610482740289097    JPL
    // X = -0.9667985251201395 Y = -0.2974911182695593 Z = -0.12799842656284177   POO
    // X = -0.9668000225179538 Y = -0.2974897281685147 Z = -0.12799798947780217   EOO


}
