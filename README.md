# 🛒 Microservicios de Productos e Inventario

Este proyecto implementa un sistema de microservicios en **Spring Boot** para gestionar productos, inventario y compras.

---

## 📌 Arquitectura General

La solución se compone de dos microservicios principales:

- **MSProductos**  
  Maneja la información de los productos (crear, listar, obtener por ID).
- **MSInventario**  
  Administra la disponibilidad de inventario, gestiona compras y valida productos a través de MSProductos.

📡 Comunicación entre servicios: **HTTP + JSON API**  
🔑 Seguridad: **API Key** en cabecera de cada request  
⚙️ Tolerancia a fallos: timeout + reintentos básicos en llamadas REST

---

## 📂 Estructura del Proyecto
/msproductos
├── src/main/java/com/example/msproductos
│ ├── controller
│ ├── model
│ ├── repository
│ └── service
├── resources/application.yml
└── pom.xml

/msinventario
├── src/main/java/com/example/msinventario
│ ├── client
│ ├── controller
│ ├── dto
│ ├── entity
│ ├── repository
│ └── service
├── resources/application.yml
└── pom.xml

## 🚀 Ejecución del Proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tuusuario/microservicios-compras.git
   cd microservicios-compras

 2.Compilar y empaquetar cada servicio:
    cd msproductos
    mvn clean package -DskipTests
    cd ../msinventario
    mvn clean package -DskipTests

3.Ejecutar los microservicios:

    # MSProductos
    java -jar target/msproductos-0.0.1-SNAPSHOT.jar
    
    # MSInventario
    java -jar target/msinventario-0.0.1-SNAPSHOT.jar

    (Opcional) Levantar con Docker:

4. Levandar con docker opcional
   docker build -t msproductos ./msproductos
    docker run -p 8080:8080 msproductos
    
    docker build -t msinventario ./msinventario
    docker run -p 8081:8081 msinventario

5. Seguridad entre Microservicios
   Se implementa API Key en los headers HTTP para validar las llamadas entre servicios.

    Ejemplo de header:  x-api-key: mi-clave-secreta
    Las claves se configuran en application.yml.

6. Documentación API SProductos (http://localhost:8080/productos)
   POST /productos → Crear un nuevo producto
   Body ejemplo:
   {
      "nombre": "Laptop",
      "precio": 3500.0
    }
    GET /productos/{id} → Obtener un producto por ID
    GET /productos → Listar todos los productos


   7. Documentación API MSInventario (http://localhost:8081)
    
    GET /inventario/{productoId}
    Consulta la cantidad disponible de un producto.

    PUT /inventario/{productoId}
    Actualiza el stock de un producto.
    Body ejemplo:
    {
      "cantidad": 15
    }
        
    POST /compras
    Realiza una compra, validando disponibilidad.
    Body ejemplo:
    {
      "productoId": 1,
      "cantidad": 2
    }
   
    Response ejemplo:
    {
      "productoId": 1,
      "cantidadComprada": 2,
      "mensaje": "Compra realizada con éxito"
    }

7. Swagger/OpenAPI se habilita en ambos servicios:
    MSProductos → http://localhost:8080/swagger-ui.html
    MSInventario → http://localhost:8081/swagger-ui.html
