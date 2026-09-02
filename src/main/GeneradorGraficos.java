package main;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.AnnotationText;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GeneradorGraficos {

    public static void main(String[] args) throws IOException {

        boolean apophis = false;

        double[] x = getX(apophis);

        double[] y = getY(apophis);

/*
        List<String> etiquetas = Arrays.asList(
                "15:38", "16:08", "16:38", "17:08", "17:38", "18:08", "18:38", "19:08",
                "19:38", "20:08", "20:38", "21:08", "21:38", "22:08", "22:38", "23:08", "23:38",
                "00:08", "00:38", "01:08", "01:38", "02:08", "02:38", "03:08", "03:38"
        );
        */


        List<String> etiquetas = Arrays.asList(
                "20:24", "20:54", "21:24", "21:54", "22:24", "22:54", "23:24", "23:54", "00:24", "00:54", "01:24", "01:54", "02:24", "02:54",
                "03:24", "03:54", "04:24", "04:54", "05:24", "05:54", "06:24", "06:54", "07:24", "07:54", "08:24", "08:54", "09:24", "09:54",
                "10:24", "10:54", "11:24", "11:54", "12:24", "12:54", "13:24", "13:54", "14:24", "14:54", "15:24", "15:54", "16:24", "16:54",
                "17:24", "17:54", "18:24", "18:54", "19:24", "19:54", "20:24"
        );


        double minX = Arrays.stream(x).min().getAsDouble();
        double maxX = Arrays.stream(x).max().getAsDouble();
        double minY = Arrays.stream(y).min().getAsDouble();
        double maxY = Arrays.stream(y).max().getAsDouble();
        double centroX = (minX + maxX) / 2.0;
        double centroY = (minY + maxY) / 2.0;
        double rango = Math.max(maxX - minX, maxY - minY);
        rango *= 1.05;
        double xMin = centroX - rango / 2.0;
        double xMax = centroX + rango / 2.0;
        double yMin = centroY - rango / 2.0;
        double yMax = centroY + rango / 2.0;


        double l = 1398.3809523809523 - 101.6190476190476;
        double km_per_pixel = rango/l;
        System.out.println(km_per_pixel);
        double screen_radio = Constants.EARTH_RADIUS/km_per_pixel;
        System.out.println(screen_radio);


        XYChart chart = new XYChartBuilder().width(1500).height(1500).build();

        chart.getStyler().setXAxisMin(xMin);
        chart.getStyler().setXAxisMax(xMax);
        chart.getStyler().setYAxisMin(yMin);
        chart.getStyler().setYAxisMax(yMax);


        chart.getStyler().setChartBackgroundColor(Color.BLACK);
        chart.getStyler().setPlotBackgroundColor(Color.BLACK);
        chart.getStyler().setXAxisTicksVisible(false);
        chart.getStyler().setYAxisTicksVisible(false);
        chart.getStyler().setXAxisTitleVisible(false);
        chart.getStyler().setYAxisTitleVisible(false);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setSeriesColors(new Color[]{Color.WHITE});


        XYSeries serie = chart.addSeries("Datos", x, y);

        serie.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        serie.setMarker(SeriesMarkers.CIRCLE);
        serie.setMarkerColor(Color.WHITE);
        serie.setMarker(SeriesMarkers.CIRCLE);


        chart.getStyler().setAnnotationTextFontColor(Constants.colorText);
        chart.getStyler().setAnnotationTextFont(new Font("Arial", Font.BOLD, 21));
        chart.getStyler().setAnnotationTextPanelFontColor(Color.WHITE);

        for (int i = 0; i < x.length; i++) {
            if (i % 2 == 0) {
                chart.addAnnotation(new AnnotationText(etiquetas.get(i), x[i] - 0.0001, y[i] - 0.00001, false));//2024 YR4
                //chart.addAnnotation(new AnnotationText(etiquetas.get(i), x[i] - 0.00002, y[i] + 0.00002, false));//Apophis
            }
        }


        double[] xCentro = {0.0};
        double[] yCentro = {0.0};

        XYSeries centro = chart.addSeries("Centro", xCentro, yCentro);
        centro.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        centro.setMarker(SeriesMarkers.CIRCLE);
        centro.setMarkerColor(Color.BLUE);


        String nombreSVG = "preuba2024.svg";
        VectorGraphicsEncoder.saveVectorGraphic(chart, nombreSVG, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
        System.out.println("Gráfica guardada como " + nombreSVG);
    }

    //Apophis
    //En el gráfico cada pixel son 1.31918244 UA = 197.3468840888 km
    //La Tierra entonces con un radio 6371 km debería tener un radio de 32.32 píxeles, 32 redondeando

    //2024 YR4
    //En el gráfico cada pixel son 5.3814209290896075E-6 UA
    //La Tierra entonces con un radio 6371 km debería tener un radio de 7.922667701008438 píxeles, 8 redondeando

    //La relación distancia/pixel se calcula utilizando el eje con mayor tamaño


    public static double[] getX(boolean apophis) {
        if (apophis) {
            /*
            double[] x = {
                    -0.001510789400544943, -0.0014584938278843218, -0.0014060979771784998, -0.0013535924980789815, -0.0013009666914939322, -0.0012482082476830136, -0.0011953029039304397,
                    -0.001142234009653631, -0.0010889819781508825, -0.0010355235591974488, -9.818308791940122E-4, -9.278701666249889E-4, -8.736000032288915E-4, -8.189689029711733E-4,
                    -7.639119094061675E-4, -7.083456889427353E-4, -6.521613388962288E-4, -5.95213666317429E-4, -5.373049666742569E-4, -4.781605391247501E-4, -4.173931524246166E-4,
                    -3.544588567777618E-4, -2.8863335484663466E-4, -2.1912480479324703E-4, -1.4555506625535397E-4, -6.869258725283878E-5, 9.682648027586893E-6, 8.798505205398488E-5,
                    1.654450100875282E-4, 2.418695145750549E-4, 3.1730839086785423E-4, 3.9188175884774523E-4, 4.6571496504810295E-4, 5.389188524542732E-4, 6.115862242972447E-4,
                    6.837933829519915E-4, 7.556028733722853E-4, 8.270661491412978E-4, 8.98225806633568E-4, 9.691173861855473E-4, 0.001039770786834593, 0.0011102113573882022,
                    0.001180460761276536, 0.001250537656482642, 0.0013204582195278647, 0.0013902365743354572, 0.0014598851361448117, 0.0015294148745913505, 0.0015988355372120733
            };
            */
            return new double[]{
                     -8.736000032288915E-4, -8.189689029711733E-4, -7.639119094061675E-4, -7.083456889427353E-4, -6.521613388962288E-4,
                    -5.95213666317429E-4, -5.373049666742569E-4, -4.781605391247501E-4, -4.173931524246166E-4, -3.544588567777618E-4,
                    -2.8863335484663466E-4, -2.1912480479324703E-4, -1.4555506625535397E-4, -6.869258725283878E-5, 9.682648027586893E-6,
                    8.798505205398488E-5, 1.654450100875282E-4, 2.418695145750549E-4, 3.1730839086785423E-4, 3.9188175884774523E-4,
                    4.6571496504810295E-4, 5.389188524542732E-4, 6.115862242972447E-4, 6.837933829519915E-4, 7.556028733722853E-4
            };
        } else {
            return new double[]{
                    1.1060743860891176E-4, 3.63518549170716E-5, -3.7903429547458956E-5, -1.1215796102184705E-4, -1.8641118315690375E-4, -2.6066241967859227E-4, -3.349108372329712E-4,
                    -4.091554173059758E-4, -4.833949211824895E-4, -5.576278318537298E-4, -6.31852301643282E-4, -7.060660897782872E-4, -7.80266474774291E-4, -8.544501725017833E-4,
                    -9.286132480646886E-4, -0.0010027510128772371, -0.0010768579477315743, -0.0011509276538611557, -0.0012249528406772098, -0.0012989254089645769, -0.0013728366444028667,
                    -0.0014466775282120092, -0.001520439195541179, -0.0015941134945005736, -0.0016676935613302302, -0.0017411743447261624, -0.001814552967235275, -0.001887828843772893,
                    -0.0019610035760509043, -0.002034080652148329, -0.002107065015259882, -0.002179962615859704, -0.0022527799996101827, -0.0023255239494531788, -0.0023982011763388073,
                    -0.0024708177164868006, -0.002543375395369127, -0.002615842753978595, -0.002688125026602081, -0.0027602707369675655, -0.002832347289498882, -0.0029043763468203088,
                    -0.0029763669961501843, -0.0030483245553374616, -0.003120252788686495, -0.0031921546323706307, -0.003264032505703243, -0.003335888468519191, -0.0034077242989978274
            };
        }
    }

    public static double[] getY(boolean apophis) {
        if (apophis) {
            /*
            double[] y = {
                    -6.782518471886156E-4, -6.431623708635925E-4, -6.080285819424214E-4, -5.728473124434474E-4, -5.376150628219056E-4, -5.023279652469625E-4, -4.6698173529263753E-4,
                    -4.3157161969836766E-4, -3.960923490381463E-4, -3.605380848307749E-4, -3.2490237571058955E-4, -2.891781444204211E-4, -2.533577190430014E-4, -2.1743296822929192E-4,
                    -1.8139564850061518E-4, -1.4523814537276136E-4, -1.0895500520580192E-4, -7.2546059133205E-5, -3.6022785350586783E-5, 5.785315927431256E-7, 3.716920704871507E-5,
                    7.35505076892018E-5, 1.0930032556349945E-4, 1.435987331973032E-4, 1.7515922115907134E-4, 2.0272736598625496E-4, 2.260142536618659E-4, 2.4577012432402956E-4,
                    2.630170889039718E-4, 2.7856545345239514E-4, 2.929569049679448E-4, 3.065390573729898E-4, 3.195369421291061E-4, 3.321000537092056E-4, 3.443307413747987E-4,
                    3.5630117038876197E-4, 3.680636232214485E-4, 3.7965691417701786E-4, 3.9111049253875985E-4, 4.0244714199955123E-4, 4.1368480127357277E-4, 4.2483781921648367E-4,
                    4.359178425987764E-4, 4.4693445596810655E-4, 4.5789564983289566E-4, 4.688081732760474E-4, 4.796778034864002E-4, 4.905095538374482E-4, 5.013078413886318E-4
            };
             */
            return new double[]{
                    -2.533577190430014E-4, -2.1743296822929192E-4, -1.8139564850061518E-4, -1.4523814537276136E-4, -1.0895500520580192E-4,
                    -7.2546059133205E-5, -3.6022785350586783E-5, 5.785315927431256E-7, 3.716920704871507E-5, 7.35505076892018E-5,
                    1.0930032556349945E-4, 1.435987331973032E-4, 1.7515922115907134E-4, 2.0272736598625496E-4, 2.260142536618659E-4,
                    2.4577012432402956E-4, 2.630170889039718E-4, 2.7856545345239514E-4, 2.929569049679448E-4, 3.065390573729898E-4,
                    3.195369421291061E-4, 3.321000537092056E-4, 3.443307413747987E-4, 3.5630117038876197E-4, 3.680636232214485E-4
            };
        } else {
            return new double[]{
                    -0.004131812801857038, -0.003994208482919537, -0.003856582862448721, -0.0037189342671284376, -0.003581260867492553, -0.003443560653777822, -0.003305831439167628,
                    -0.0031680708357031406, -0.0030302762315858045, -0.0028924447991802715, -0.002754573480603262, -0.0026166589818743535, -0.0024786978078927824, -0.002340686288748395,
                    -0.002202620632602237, -0.00206449704175804, -0.0019263118456546158, -0.0017880616862584464, -0.0016497437865730102, -0.0015113562399061697, -0.0013728983257920158,
                    -0.001234370839603427, -0.001095776321731634, -9.571191482689034E-4, -8.184054573022737E-4, -6.796428362368934E-4, -5.408398288979654E-4, -4.020053667644907E-4,
                    -2.6314816610284453E-4, -1.2427620440091935E-4, 1.4603649384548056E-5, 1.5348591138697465E-4, 2.923666986666129E-4, 4.3124399351035514E-4, 5.701183320109271E-4,
                    7.089948439631399E-4, 8.478900931302435E-4, 9.86846568576949E-4, 0.0011258486270266932, 0.0012648099156629122, 0.0014037309295142908, 0.001542621890829543,
                    0.0016814884045986966, 0.0018203337280445764, 0.0019591600345271187, 0.002097968927077809, 0.0022367616898848475, 0.0023755394173742284, 0.0025143030594113602
            };
        }
    }
}



