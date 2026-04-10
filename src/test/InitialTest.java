package test;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InitialTest {

   @Test
    void testConUmbralPersonalizado() {
        double[] esperado = {1.0, 2.0, 3.0};
        double[] resultado = {1.2, 2.1, 2.8};

        double umbral = 0.15;

        for (int i = 0; i < esperado.length; i++) {
            double diferencia = Math.abs(esperado[i] - resultado[i]);

            assertTrue(
                    diferencia <= umbral,
                    "Error en posición " + i + ": diferencia = " + diferencia
            );
        }
    }
}

