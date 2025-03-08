** Ejecutar 

- api/clientes
- 



** Instrucciones para ejecutar la aplicación

* Preparar la base de datos:

- Asegúrate de tener PostgreSQL instalado y en ejecución
- Crea una base de datos llamada "gestiondb"
- Los scripts schema.sql y data.sql se ejecutarán automáticamente al iniciar la aplicación


** Compilar y ejecutar la aplicación:
- bashCopymvn clean install
- mvn spring-boot:run

** Acceder a la API:

- La API estará disponible en http://localhost:8000


** Funcionalidades principales
- Gestión de Clientes

- Listar todos los clientes: GET /api/clientes
- Obtener un cliente por ID: GET /api/clientes/{id}
- Crear un nuevo cliente: POST /api/clientes
- Actualizar un cliente existente: PUT /api/clientes/{id}
- Eliminar un cliente: DELETE /api/clientes/{id}
- Buscar clientes por nombre o apellido: GET /api/clientes/search?query={texto}

** Gestión de Productos

- Listar todos los productos: GET /api/productos
- Obtener un producto por ID: GET /api/productos/{id}
- Crear un nuevo producto: POST /api/productos
- Actualizar un producto existente: PUT /api/productos/{id}
- Eliminar un producto: DELETE /api/productos/{id}
- Buscar productos por nombre: GET /api/productos/search?nombre={texto}
- Filtrar productos por categoría: GET /api/productos/categoria/{categoria}
- Filtrar productos por rango de precio: GET /api/productos/precio?min={min}&max={max}

** Postman

** Endpoints de Cliente
1. Obtener todos los clientes

- Método: GET
- URL: http://localhost:8000/api/clientes
- Headers: Content-Type: application/json
- Body: No requiere

2. Obtener un cliente por ID

- Método: GET
- URL: http://localhost:8000/api/clientes/1
- Headers: Content-Type: application/json
- Body: No requiere

3. Crear un nuevo cliente

- Método: POST
- URL: http://localhost:8000/api/clientes
- Headers: Content-Type: application/json
- Body:

jsonCopy{
"nombre": "Carlos",
"apellido": "Rodríguez",
"email": "carlos.rodriguez@example.com",
"telefono": "555-9876",
"direccion": "Avenida Principal 789"
}
4. Actualizar un cliente existente

- Método: PUT
- URL: http://localhost:8000/api/clientes/1
- Headers: Content-Type: application/json
Body:

jsonCopy{
"nombre": "Carlos",
"apellido": "Rodríguez López",
"email": "carlos.rodriguez@example.com",
"telefono": "555-9876",
"direccion": "Calle Nueva 123"
}

6. Eliminar un cliente

- Método: DELETE
- URL: http://localhost:8000/api/clientes/1
- Headers: Content-Type: application/json
- Body: No requiere

6. Buscar clientes

- Método: GET
- URL: http://localhost:8000/api/clientes/search?query=Carlos
- Headers: Content-Type: application/json
- Body: No requiere

** Endpoints de Producto
1. Obtener todos los productos

- Método: GET
- URL: http://localhost:8000/api/productos
- Headers: Content-Type: application/json
- Body: No requiere

2. Obtener un producto por ID

- Método: GET
- URL: http://localhost:8000/api/productos/1
- Headers: Content-Type: application/json
- Body: No requiere

3. Crear un nuevo producto

- Método: POST
- URL: http://localhost:8000/api/productos
- Headers: Content-Type: application/json
- Body:

jsonCopy{
"nombre": "Smartphone Samsung",
"descripcion": "Samsung Galaxy S23 - 256GB, 8GB RAM",
"precio": 799.99,
"stock": 25,
"categoria": "Electrónica"
}
4. Actualizar un producto existente

- Método: PUT
- URL: http://localhost:8000/api/productos/1
- Headers: Content-Type: application/json
- Body:

jsonCopy{
"nombre": "Smartphone Samsung",
"descripcion": "Samsung Galaxy S23 - 256GB, 8GB RAM, Negro",
"precio": 749.99,
"stock": 30,
"categoria": "Electrónica"
}
5. Eliminar un producto

- Método: DELETE
- URL: http://localhost:8000/api/productos/1
- Headers: Content-Type: application/json
- Body: No requiere

6. Buscar productos por nombre

- Método: GET
- URL: http://localhost:8000/api/productos/search?nombre=Samsung
- Headers: Content-Type: application/json
- Body: No requiere

7. Buscar productos por categoría

- Método: GET
- URL: http://localhost:8000/api/productos/categoria/Electrónica
- Headers: Content-Type: application/json
- Body: No requiere

8. Buscar productos por rango de precio

- Método: GET
- URL: http://localhost:8000/api/productos/precio?min=500&max=1000
- Headers: Content-Type: application/json
- Body: No requiere