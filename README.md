# JPAMagazinesAnnotations

# Programa de Gestión de Series

Este proyecto es un programa de gestión de series desarrollado en Java. Permite interactuar con una base de datos para realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) en las entidades Serie, Estudio y Género. Además, puede leer datos desde archivos CSV para poblar la base de datos y realizar consultas específicas.

## Funcionalidades

- Crear, leer, actualizar y eliminar series, estudios y géneros en la base de datos.
- Leer datos desde archivos CSV para poblar la base de datos.
- Realizar consultas específicas en la base de datos, como buscar texto en una columna o filtrar registros según una condición.
- Mostrar información sobre las tablas disponibles en la base de datos.

## Requisitos

- Java Development Kit (JDK) 8 o superior.
- Apache Maven para compilar y ejecutar el proyecto.
- Una base de datos PostgreSQL (u otro compatible con JPA) para almacenar los datos.

## Configuración

1. Clona el repositorio a tu máquina local.
2. Asegúrate de tener configurada una base de datos PostgreSQL y ajusta la configuración de conexión en el archivo `persistence.xml`.
3. Ejecuta el comando `mvn clean package` en el directorio raíz del proyecto para compilar el código y generar el archivo JAR.
4. Ejecuta el programa con el comando `java -jar target/nombre-del-archivo.jar`.

## Uso

El programa muestra un menú principal con varias opciones, incluyendo crear, leer, actualizar y eliminar registros, así como realizar consultas específicas y mostrar información sobre las tablas disponibles en la base de datos. Sigue las instrucciones en pantalla para interactuar con el programa.


## Licencia

Este proyecto está bajo la Licencia MIT. Para más detalles, consulta el archivo [LICENSE](LICENSE).


