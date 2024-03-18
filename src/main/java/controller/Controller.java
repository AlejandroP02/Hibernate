package controller;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.*;


public class Controller {
    private List<Genero> generos;
    private List<Estudio> estudios;
    private List<Serie> series;
    private EntityManagerFactory entityManagerFactory;
    private Scanner sc = new Scanner(System.in);
    private SerieController serieController;
    private EstuidoController estuidoController;
    private GeneroController generoController;

    public Controller(EntityManagerFactory entityManagerFactory) throws CsvValidationException, IOException {
        generos = new ArrayList<>();
        estudios = new ArrayList<>();
        series = new ArrayList<>();
        this.entityManagerFactory = entityManagerFactory;
        serieController = new SerieController(entityManagerFactory);
        estuidoController = new EstuidoController(entityManagerFactory);
        generoController = new GeneroController(entityManagerFactory);
        readEstudios();
        readGeneros();
        readSeries();
    }

    public void readGeneros() throws FileNotFoundException {
        generos = new CsvToBeanBuilder<Genero>(new FileReader("src/main/resources/generos.csv"))
                .withType(Genero.class)
                .build()
                .parse();
    }

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
    public void crearTablas(){
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    }

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

    public void poblar(){
        generoController.addGeneros(generos);
        estuidoController.addEstudios(estudios);
        serieController.addSeries(series);
    }
    public List<Genero> getGeneros() {
        return generos;
    }

    public List<Estudio> getEstudios() {
        return estudios;
    }

    public List<Serie> getSeries() {
        return series;
    }


    /**
     * Lee un numero escrito por el usuario.
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
