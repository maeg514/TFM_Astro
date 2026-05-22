package main.efficiencyOriented;

import main.GUI;

public class Main {
     public static void main(String[] args) {
        /*RKController rkController = new RKController();
        rkController.run("RK5");*/
        GUI app = new GUI();
        app.execute();
    }
}

//Terminar cambios tareas
//Printear con un poco más de sentido las cosas
//Implementar el JUnit para los test (No se si esto puede ir de la mano de lo que tienen Horizons)
/*
Se podría hacer una especie de menu que te deje ejecutar el runge kutta de forma bruta o con una configuración personalizada.
Dentro de los parámetros a elegir tenemos:
-Guardado
-Calidad de precisión
-Comparación con otros resultados
-Gráficos?

*/

/*

- Más flexibilidad a la hora de establecer el final de la integración. Paso de TT a UTC con el día Juliano. Seguir usando 0 para el inicio *****
	- Tal vez separar el método RK4 en varios para más claridad
	- Cuidado con la última iteración si se establece un punto final que requiera de un número de pasos de integración no entero ***************
	- Implementar correcciones relativistas, de achatamiento terrestre, y fuerzas no gravitatorias en Apophis **********************************
	- Revisión del código, mejorar organización, e ideas para optimizarlo por velocidad
	- Cálculo de efemérides desde un observador, en principio en el centro de la Tierra. Usar precesión de Laskar
	- Pruebas de precisión con el RK4 para Apophis, comparación con Horizons
	- Implementación del RK de orden 5: comparación con Horizons durante miles de años usando TDB
	- Trayectoria de Apophis y 2024 YR4 desde un observador en Madrid. Comparación con Horizons. Qué ocurre si se añaden asteroides con masa ?
	- Posibles ideas para probar la precisión del código - gráficos
	- Grafico de los errores en el cielo a lo largo del tiempo
	- Grafico de la curvatura en la trayectoria de apophis durante su tránsito cerca de la Tierra
	- Probar añadiendo asteroide con mas(top16 hace horizon)
	- corregir coordenadas astrométricas
	- probar metodo normand-prince (es un poco peor, probar cambiando el paso)
	- Comparar con fotometria, comprar discrepancias
	- TODO documentar todo y deprecar clases obsoletas
	- TODO probar añadiendo los 16 cuerpos extra que utiliza Horizons

	TODO quitar parámetro vagabundo step
	TODO Jlist JtextArea


- joptionPanel //PRimera opcion
- jMessageDialog
 */
 