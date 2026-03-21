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
    static void main() throws IOException, URISyntaxException {
        String url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27Apophis%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27VECTOR%27&OUT_UNITS=%27AU-D%27&CENTER=%27500%27&ANG_FORMAT=%27DEG%27&START_TIME=%272029-04-13%2021:38:00%20UTC%27&STOP_TIME=%272029-04-14%27&STEP_SIZE=%271d%27&QUANTITIES=%271,9,20%27&ECLIP=%27J2000%27&VEC_CORR=%27NONE%27&OBJ_DATA=%27NO%27&REF_PLANE=%27FRAME%27";
        System.out.println(query(url));
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
        while ((inputLine = in.readLine()) != null) {
            output.append(inputLine + "\n");
            //output.append(inputLine + Util.getLineSeparator());
        }
        in.close();
        return output.toString();
    }
}
