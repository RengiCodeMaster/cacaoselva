# Analisis de mejoras de CacaoSelva

## Situacion inicial

La API solo permitia consultar tres registros almacenados en memoria. Desktop
mostraba una tabla de solo lectura y el Monitor escribia la cantidad de pendientes
cada diez segundos, aunque el resultado no hubiera cambiado.

## Persistencia y API

Se incorporo un adaptador JPA que implementa el puerto `LoteRepository`. H2 en
modo archivo permite ejecutar la demostracion sin instalar servicios adicionales,
mientras que el perfil `postgres` permite conectarse a PostgreSQL. La API ofrece
CRUD completo y conserva sus reglas en casos de uso independientes de Spring.

## Desktop

El problema principal era que la interfaz solo consultaba datos. Se agrego un
puerto de comandos separado del puerto de consulta, manteniendo ISP y DIP. El
cliente ahora puede crear, actualizar y eliminar, filtrar resultados y mostrar
errores sin bloquear el hilo de JavaFX. Los detalles HTTP y JSON permanecen en
`infrastructure`.

## Monitor

El registro anterior generaba ruido porque repetia el mismo valor en cada ciclo.
El monitor ahora calcula total, pendientes y liquidados, y solo escribe cuando el
resumen cambia. Tambien detecta transiciones de caida y recuperacion, acepta un
intervalo configurable y libera el planificador durante el cierre de la JVM.

## Pruebas y riesgos

Las pruebas cubren casos de uso, validaciones, CRUD contra H2, comandos HTTP del
Desktop y comportamiento del Monitor. GitHub Actions ejecuta la suite completa en
cada cambio. La autenticacion, autorizacion y migraciones versionadas de esquema
quedan fuera del alcance academico actual.
