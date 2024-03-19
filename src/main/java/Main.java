
import java.io.IOException;
import java.sql.SQLException;

import com.opencsv.exceptions.CsvValidationException;
import view.Menu;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Esta clase representa el punto de entrada de la aplicación.
 * Inicializa el EntityManagerFactory y comienza el menú principal.
 */
public class Main {

    /**
     * Crea un objeto EntityManagerFactory.
     * @return EntityManagerFactory - el objeto EntityManagerFactory creado.
     * @throws ExceptionInInitializerError si no se puede crear el EntityManagerFactory.
     */
    public static EntityManagerFactory createEntityManagerFactory() {
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
     * El método principal de la aplicación.
     * @param args argumentos de línea de comandos.
     * @throws SQLException si se produce un error de acceso a la base de datos.
     * @throws IOException si se produce un error de E/S.
     * @throws CsvValidationException si se encuentran errores al analizar un archivo CSV.
     */
    public static void main(String[] args) throws SQLException, IOException, CsvValidationException {
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
        Menu menu = new Menu(entityManagerFactory);
        menu.mainMenu();
    }
}