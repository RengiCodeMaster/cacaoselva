# CacaoSelva - Pruebas de la API con Postman

Esta guia explica como crear y ejecutar la coleccion **CacaoSelva - Pruebas API**
para comprobar el comportamiento de la API REST.

## Requisitos

1. La API debe estar ejecutandose en `http://localhost:5080`.
2. Tener Postman instalado.

## Crear la coleccion

1. Abrir Postman y crear una coleccion llamada:

   ```text
   CacaoSelva - Pruebas API
   ```

2. Crear una variable de coleccion:

   ```text
   baseUrl = http://localhost:5080
   ```

## Peticiones

| Codigo | Metodo | URL | Resultado esperado |
| ------ | ------ | --- | ------------------ |
| P01 | GET | `{{baseUrl}}/lotes` | 200 |
| P02 | GET | `{{baseUrl}}/lotes/1` | 200 |
| P03 | GET | `{{baseUrl}}/lotes/999` | 404 |
| P04A | GET | `{{baseUrl}}/lotes/abc` | 400 |
| P04B | GET | `{{baseUrl}}/lotes/0` | 400 |
| P05 | POST | `{{baseUrl}}/lotes` | 201 |
| P06 | PUT | `{{baseUrl}}/lotes/{{loteId}}` | 200 |
| P07 | DELETE | `{{baseUrl}}/lotes/{{loteId}}` | 204 |

Para P05 y P06 usar `Body > raw > JSON`:

```json
{
  "socio": "Socio de prueba",
  "pesoKg": 42.50,
  "estado": "PENDIENTE"
}
```

## Scripts de prueba

### P01 - Listar lotes

```javascript
pm.test("La respuesta es 200", function () {
    pm.response.to.have.status(200);
});

pm.test("La respuesta contiene lotes", function () {
    const datos = pm.response.json();

    pm.expect(datos).to.be.an("array");
    pm.expect(datos.length).to.be.greaterThan(0);
});
```

### P05 - Crear lote

```javascript
pm.test("El lote se crea", function () {
    pm.response.to.have.status(201);
    const lote = pm.response.json();
    pm.expect(lote.id).to.be.a("number");
    pm.collectionVariables.set("loteId", lote.id);
});
```

### P06 - Actualizar lote

```javascript
pm.test("El lote se actualiza", function () {
    pm.response.to.have.status(200);
    pm.expect(pm.response.json().id).to.eql(
        Number(pm.collectionVariables.get("loteId"))
    );
});
```

### P07 - Eliminar lote

```javascript
pm.test("El lote se elimina", function () {
    pm.response.to.have.status(204);
});
```

### P03 - Lote inexistente

```javascript
pm.test("Lote inexistente retorna 404", function () {
    pm.response.to.have.status(404);
});
```

### P04 - Identificador invalido

```javascript
pm.test("Identificador invalido retorna 400", function () {
    pm.response.to.have.status(400);
});
```

## Respuestas de referencia

### P01 - GET /lotes (200)

```json
[
  { "id": 1, "socio": "Ana Torres", "pesoKg": 120.5, "estado": "PENDIENTE" },
  { "id": 2, "socio": "Luis Mendoza", "pesoKg": 80, "estado": "LIQUIDADO" }
]
```

### P02 - GET /lotes/1 (200)

```json
{ "id": 1, "socio": "Ana Torres", "pesoKg": 120.5, "estado": "PENDIENTE" }
```

### P03 - GET /lotes/999 (404)

```json
{
  "status": 404,
  "message": "No existe el lote con id 999",
  "timestamp": "..."
}
```

### P04 - GET /lotes/abc y GET /lotes/0 (400)

```json
{
  "status": 400,
  "message": "El identificador del lote es invalido: abc",
  "timestamp": "..."
}
```

## Comprobacion de falla de conexion

1. Detener la API.
2. Ejecutar cualquier peticion en Postman.
3. Postman debe mostrar un error de conexion.
4. Reiniciar la API y volver a ejecutar: debe responder 200 nuevamente.
