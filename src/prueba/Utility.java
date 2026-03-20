package prueba;

public class Utility {

    public static double dateToJulianDay(int year, int month, int day, int hour, int minute, boolean julian) {
        if (month < 3) {
            year = year - 1;
            month = month + 12;
        }
        int a = year / 100;
        int b = 0;
        double c = (hour + minute / 60.0) / 24.0;
        if (!julian) b = 2 - a + a / 4;
        return (int) (365.25 * (year + 4716)) + (int) (30.6001 * (month + 1)) + day + b - 1524.5 + c;
    }

}
