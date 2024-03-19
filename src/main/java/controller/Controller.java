package controller;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

import javax.persistence.*;

/**
 * Esta clase controla las operaciones principales del programa,
 * incluida la lectura de datos desde archivos CSV,
 * la interacción con la base de datos y la gestión de las operaciones CRUD.
 */
public class Controller {
    /**
     * Lista que contiene los géneros extraídos del CSV.
     */
    private List<Genero> generos;
    /**
     * Lista que contiene los estudios extraídos del CSV.
     */
    private List<Estudio> estudios;
    /**
     * Lista que contiene las series extraídas del CSV.
     */
    private List<Serie> series;
    /**
     * Sirve para interactuar con la base de datos.
     */
    private EntityManagerFactory entityManagerFactory;
    /**
     * Sirve para leer lo que escribe el usuario.
     */
    private Scanner sc = new Scanner(System.in);
    /**
     * Instancia de serieController.
     */
    private SerieController serieController;
    /**
     * Instancia de estudioController.
     */
    private EstudioController estudioController;
    /**
     * Instancia de generoController.
     */
    private GeneroController generoController;

    /**
     * Constructor de la clase Controller.
     * @param entityManagerFactory el EntityManagerFactory para interactuar con la base de datos.
     * @throws CsvValidationException si ocurre un error de validación de CSV.
     * @throws IOException si ocurre un error de E/S durante la ejecución del programa.
     */
    public Controller(EntityManagerFactory entityManagerFactory) throws CsvValidationException, IOException {
        generos = new ArrayList<>();
        estudios = new ArrayList<>();
        series = new ArrayList<>();
        this.entityManagerFactory = entityManagerFactory;
        serieController = new SerieController(entityManagerFactory);
        estudioController = new EstudioController(entityManagerFactory);
        generoController = new GeneroController(entityManagerFactory);
        readEstudios();
        readGeneros();
        readSeries();
    }

    /**
     * Lee los géneros desde un archivo CSV.
     * @throws FileNotFoundException si no se encuentra el archivo.
     */
    public void readGeneros() throws FileNotFoundException {
        generos = new CsvToBeanBuilder<Genero>(new FileReader("src/main/resources/generos.csv"))
                .withType(Genero.class)
                .build()
                .parse();
    }

    /**
     * Lee los estudios desde un archivo CSV.
     * @throws IOException si ocurre un error de E/S durante la lectura del archivo.
     * @throws CsvValidationException si ocurre un error de validación de CSV.
     */
    public void readEstudios() throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/estudios.csv"));
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null) {
            int id = Integer.parseInt(record[0]);
            String nombre = record[1];
            String link = record[2];
            LocalDate fecha;
            fecha = record[3].equals("null")? null:LocalDate.parse(record[3]);
            int num_series = Integer.parseInt(record[4]);
            estudios.add(new Estudio(id, nombre, link, fecha, num_series));
        }
        reader.close();
    }

    /**
     * Lee las series desde un archivo CSV.
     * @throws IOException si ocurre un error de E/S durante la lectura del archivo.
     * @throws CsvValidationException si ocurre un error de validación de CSV.
     */
    public void readSeries() throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/series.csv"));
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null) {
            int id = Integer.parseInt(record[0]);
            String titulo = record[1];
            String imagen = record[2];
            String tipo = record[3];
            int episodios = Integer.parseInt(record[4]);
            String estado = record[5];
            LocalDate fechaEstreno;
            fechaEstreno= record[6].equals("null")? null : LocalDate.parse(record[6]);
            String licencia = record[7];
            String a = record[8].replaceFirst("\\[", "").replaceFirst("]","");
            a=a.replaceAll(" ", "");
            String[] ids= a.split(",");
            ArrayList<Estudio> estudios1 = new ArrayList<>();
            if (!ids[0].equals("")){
                for (int x=0;x<ids.length;x++){
                    estudios1.add(estudios.get(Integer.parseInt(ids[x])-1));
                }
            }
            String src = record[9];
            a = record[10].replaceFirst("\\[", "").replaceFirst("]","");
            a=a.replaceAll(" ", "");
            ids= a.split(",");
            ArrayList<Genero> generos1 = new ArrayList<>();
            for (int x=0;x<ids.length;x++){
                generos1.add(generos.get(Integer.parseInt(ids[x])-1));
            }
            float duracion = Float.parseFloat(record[11]);
            String descripcion = record[12];

            series.add(new Serie(id, titulo, imagen, tipo, episodios,
                    estado, fechaEstreno, licencia, estudios1, src, generos1,
                    duracion, descripcion));
        }
        reader.close();
    }

    /**
     * Muestra las tablas disponibles en la base de datos.
     * @return una lista de nombres de tablas disponibles.
     */
    public List<Object> mostrarTablas(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa
            Query query = entityManager.createNativeQuery("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname='public'");
            List<Object> results = query.getResultList();

            // Iterar sobre los resultados y mostrarlos
            for (int x=0;x<results.size();x++) {
                System.out.println(x+" "+results.get(x));
            }

            // Commit de la transacción
            entityManager.getTransaction().commit();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra las columnas de una tabla específica.
     * @param tabla el nombre de la tabla.
     * @return una lista de nombres de columnas de la tabla especificada.
     */
    public List<Object> mostrarColumnas(String tabla){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa
            Query query = entityManager.createNativeQuery("SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"+tabla+"'");
            List<Object> results = query.getResultList();

            // Iterar sobre los resultados y mostrarlos
            for (int x=0;x<results.size();x++) {
                System.out.println(x+" "+results.get(x));
            }

            // Commit de la transacción
            entityManager.getTransaction().commit();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Realiza una consulta para buscar texto específico en una columna de una entidad.
     * @param entityClass la clase de la entidad.
     * @param columna el nombre de la columna en la que se realizará la búsqueda.
     * @param text el texto a buscar.
     * @param <T> el tipo genérico de la entidad.
     */
    public <T> void selectText(Class<T> entityClass, String columna, String text) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        // Utilizamos parámetros en la consulta JPQL para evitar inyección de SQL
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + columna + " LIKE :text";

        // Creamos la consulta con parámetros
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        query.setParameter("text", "%" + text + "%"); // Aquí agregamos el parámetro

        // Ejecutamos la consulta
        List<T> objects = query.getResultList();

        for (T object : objects) {
            System.out.println(object.toString());
        }

        entityManager.getTransaction().commit();
    }

    /**
     * Realiza una consulta para buscar registros que cumplan una condición específica en una tabla de la base de datos.
     * @param tabla     el nombre de la tabla en la que se realizará la búsqueda.
     * @param condicion la condición a aplicar en la búsqueda (por ejemplo, "LIKE", "=", "<").
     * @param columna2  el nombre de la columna en la que se aplicará la condición.
     * @param text      el texto que se comparará con los registros en la columna.
     */
    public void selectCondicion(String tabla, String condicion, String columna2, String text){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa
            Query query;
            if (condicion.equals("LIKE")) {
                query = entityManager.createNativeQuery("SELECT * FROM " + tabla + " WHERE " + columna2 + " " + condicion + " '%" + text + "%'");
            } else {
                query = entityManager.createNativeQuery("SELECT * FROM " + tabla + " WHERE " + columna2 + " " + condicion + " ?");
                query.setParameter(1, text);
            }

            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()) {
                for (Object[] result : results) {
                    for (Object a: result){
                        if (a==null)a="null";
                        if (a.toString().length()>200) System.out.print("\""+a.toString().substring(0, 50)+"\", ");
                        else System.out.print("\""+a +"\", ");
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No se encontraron resultados.");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Realiza una consulta para obtener un registro específico
     * de una tabla en la base de datos.
     * @param tabla el nombre de la tabla.
     * @param id    el identificador del registro a buscar.
     */
    public void select1(String tabla, int id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa
            Query query = entityManager.createNativeQuery("SELECT * FROM " + tabla + " WHERE id = "+id);

            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()) {
                for (Object[] result : results) {
                    for (Object a: result){
                        if (a==null)a="null";
                        if (a.toString().length()>200) System.out.print("\""+a.toString().substring(0, 50)+"\", ");
                        else System.out.print("\""+a +"\", ");
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No se encontraron resultados.");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina un registro de una tabla en la base de datos.
     * @param tabla el nombre de la tabla.
     * @param id    el identificador del registro a eliminar.
     */
    public void eliminarEntidad(String tabla,int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Cargar la entidad que se desea eliminar
            if (tabla.equals("serie")){
                Serie serie = entityManager.find(Serie.class, id);

                // Removemos la relación entre la Serie y el Género
                if (serie != null) {
                    serie.getGeneros().removeAll(serie.getGeneros());
                    serie.getEstudios().removeAll(serie.getEstudios());
                    entityManager.merge(serie);
                }
                entityManager.remove(serie);
            }else if(tabla.equals("estudio")){
                Estudio estudio = entityManager.find(Estudio.class, id);
                entityManager.remove(estudio);
            }else if(tabla.equals("genero")){
                Genero genero = entityManager.find(Genero.class, id);
                entityManager.remove(genero);
            }

            // Verificar si la entidad existe antes de eliminarla

            System.out.println("La entidad ha sido eliminada.");


            transaction.commit();
        } finally {

        }
    }

    /**
     * Actualiza registros en una tabla de la base de datos.
     * @param tabla    el nombre de la tabla en la que se actualizarán los registros.
     * @param columna  el nombre de la columna que se actualizará.
     * @param condicion la condición para aplicar la actualización (por ejemplo, "LIKE", "=", "<").
     * @param update   el nuevo valor que se asignará a la columna.
     * @param columa2  el nombre de la columna en la que se aplicará la condición.
     * @param text     el texto que se comparará con los registros en la columna de condición.
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL.
     */
    public void update(String tabla, String columna, String condicion, String update, String columa2, String text) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa
            Query query = entityManager.createNativeQuery("UPDATE "+tabla+" SET "+columna+" = '"+update+"' WHERE "+columa2+" "+condicion+" "+text);;
            if (condicion.equals("LIKE")) query = entityManager.createNativeQuery("UPDATE "+tabla+" SET "+columna+" = '"+update+"' WHERE "+columa2+" "+condicion+" '%"+text+"%'");
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina una tabla completa de la base de datos.
     * @param tabla el nombre de la tabla a eliminar.
     */
    public void borrarTabla(String tabla){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            // Comenzar una transacción
            entityManager.getTransaction().begin();

            // Ejecutar la consulta SQL nativa para eliminar la tabla
            Query query = entityManager.createNativeQuery("DROP TABLE "+tabla+" CASCADE");
            int rowCount = query.executeUpdate();
            System.out.println("Tabla eliminada correctamente.");
            // Commit de la transacción
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina todas las tablas de la base de datos.
     */
    public void borrarTablas(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createNativeQuery("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname='public'");
        List<Object> results = query.getResultList();

        try {
            // Comenzar una transacción
            entityManager.getTransaction().begin();
            // Ejecutar la consulta SQL nativa para eliminar la tabla
            for (Object result:results){
                query = entityManager.createNativeQuery("DROP TABLE "+result+" CASCADE");
                query.executeUpdate();
            }
            System.out.println("Tablas eliminadas correctamente.");

            // Commit de la transacción
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea las tablas en la base de datos.
     */
    public void crearTablas(){
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    }

    /**
     * Crea un objeto EntityManagerFactory.
     * @return EntityManagerFactory - el objeto EntityManagerFactory creado.
     * @throws ExceptionInInitializerError si no se puede crear el EntityManagerFactory.
     */
    public EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory emf;
        try {
            emf = Persistence.createEntityManagerFactory("JPAMagazines");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return emf;
    }

    /**
     * Puebla la base de datos con los datos
     * leídos de los archivos CSV.
     */
    public void poblar(){
        generoController.addGeneros(generos);
        estudioController.addEstudios(estudios);
        serieController.addSeries(series);
    }
    public List<Genero> getGeneros() {
        return generos;
    }

    /**
     * Lee un número escrito por el usuario.
     * @return Un int escrito por el usuario.
     */
    public int nextInt(){
        return sc.nextInt();
    }
    /**
     * Le una palabra escrita por el usuario.
     * @return Un Strign escrito por el usuario.
     */
    public String next(){
        return sc.next();
    }
    /**
     * Le una cadena de texto escrita por el usuario.
     * @return Un String escrito por el usuario.
     */
    public String nextLine(){
        return sc.nextLine();
    }
}
