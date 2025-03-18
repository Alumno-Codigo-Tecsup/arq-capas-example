package com.gestionapp; // Ajusta este paquete según tu estructura

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoolConexionesExcesoTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testExcesoPoolConexiones() throws Exception {
        // 1. Primero determinar el tamaño actual del pool
        int maxPoolSize = determinarTamanoPool();
        System.out.println("Tamaño máximo del pool detectado: " + maxPoolSize);

        // 2. Reservar conexiones para forzar la saturación del pool
        List<Connection> conexionesReservadas = new ArrayList<>();
        try {
            // Reservar la mayoría de las conexiones disponibles
            for (int i = 4; i < maxPoolSize - 1; i++) {
                Connection conn = dataSource.getConnection();
                // Ejecutar una consulta para mantener la conexión activa
                try (var stmt = conn.createStatement()) {
                    stmt.execute("SELECT 1");
                }
                conexionesReservadas.add(conn);
                System.out.println("Conexión " + (i+1) + " reservada");
            }

            // 3. Ahora lanzar múltiples peticiones concurrentes que excederán el pool
            int numPeticionesExcedentes = maxPoolSize * 2; // El doble del tamaño del pool

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            CountDownLatch latch = new CountDownLatch(numPeticionesExcedentes);
            AtomicInteger peticionesExitosas = new AtomicInteger(0);
            AtomicInteger peticionesFallidas = new AtomicInteger(0);
            AtomicInteger peticionesTimeout = new AtomicInteger(0);
            List<Exception> excepciones = new ArrayList<>();

            // Usar un pool con timeout reducido para detectar problemas de conexión
            ExecutorService executor = Executors.newFixedThreadPool(numPeticionesExcedentes);

            // 4. Lanzar las peticiones concurrentemente
            for (int i = 0; i < numPeticionesExcedentes; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        String requestBody = "{\n" +
                                " \"nombre\": \"Test Grecia-" + index + "\",\n" +
                                " \"descripcion\": \"Samsung Galaxy S23 - 256GB, 8GB RAM\",\n" +
                                " \"precio\": 799.99,\n" +
                                " \"stock\": 25,\n" +
                                " \"categoria\": \"Electrónica\"\n" +
                                "}";

                        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

                        // Configurar timeout más corto para detectar problemas de conexión
                        org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                                new org.springframework.http.client.SimpleClientHttpRequestFactory();
                        factory.setConnectTimeout(500);
                        factory.setReadTimeout(2000);
                        TestRestTemplate clienteConTimeout = new TestRestTemplate();
                        clienteConTimeout.getRestTemplate().setRequestFactory(factory);

                        // seteo del al url
                        String url = "http://localhost:" + 8000 + "/api/productos";

                        System.out.println("Iniciando petición " + index);
                        ResponseEntity<String> response = clienteConTimeout.postForEntity(url, request, String.class);

                        if (response.getStatusCode().is2xxSuccessful()) {
                            peticionesExitosas.incrementAndGet();
                            System.out.println("Petición " + index + " exitosa");
                        } else {
                            peticionesFallidas.incrementAndGet();
                            System.out.println("Petición " + index + " fallida: " + response.getStatusCode());
                        }
                    } catch (Exception e) {
                        if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                            peticionesTimeout.incrementAndGet();
                            System.out.println("Petición " + index + " timeout");
                        } else {
                            peticionesFallidas.incrementAndGet();
                            System.out.println("Petición " + index + " error: " + e.getMessage());
                        }
                        synchronized (excepciones) {
                            excepciones.add(e);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            // 5. Esperar a que todas las peticiones terminen o hasta un tiempo límite
            boolean todasTerminaron = latch.await(30, TimeUnit.SECONDS);

            // 6. Mostrar resultados
            System.out.println("Resultados del test de exceso de pool:");
            System.out.println("Peticiones exitosas: " + peticionesExitosas.get());
            System.out.println("Peticiones fallidas: " + peticionesFallidas.get());
            System.out.println("Peticiones con timeout: " + peticionesTimeout.get());

            if (!excepciones.isEmpty()) {
                System.out.println("Tipos de excepciones encontradas:");
                excepciones.stream()
                        .map(e -> e.getClass().getSimpleName() + ": " + e.getMessage())
                        .distinct()
                        .forEach(System.out::println);
            }

            // 7. Verificaciones básicas
            assertTrue(todasTerminaron, "Todas las peticiones deberían haber terminado en el tiempo límite");
            assertTrue(peticionesExitosas.get() + peticionesFallidas.get() + peticionesTimeout.get() == numPeticionesExcedentes,
                    "El número total de peticiones procesadas debería ser igual al número de peticiones enviadas");

            // 8. Verificación de que algunos requests fallaron o tuvieron timeout debido al exceso del pool
            // Si el pool está configurado correctamente con un límite, deberían haber fallos o timeouts
            assertTrue(peticionesFallidas.get() > 0 || peticionesTimeout.get() > 0,
                    "Debería haber peticiones fallidas o con timeout cuando se excede el pool");

        } finally {
            // 9. Liberar las conexiones reservadas
            for (Connection conn : conexionesReservadas) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Ignorar errores al cerrar
                }
            }
            System.out.println("Conexiones liberadas: " + conexionesReservadas.size());
        }
    }

    private int determinarTamanoPool() {
        // Tratar de determinar el tamaño del pool basado en la implementación
        if (dataSource instanceof com.zaxxer.hikari.HikariDataSource hikariDS) {
            return hikariDS.getMaximumPoolSize();
        }

        // Si no podemos determinar el tamaño, asumimos un valor por defecto
        return 10; // Valor típico por defecto en Spring Boot
    }
}