
import java.io.IOException;
import java.sql.SQLException;

import com.opencsv.exceptions.CsvValidationException;
import view.Menu;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

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

    public static void main(String[] args) throws SQLException, IOException, CsvValidationException {
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
        Menu menu = new Menu(entityManagerFactory);
        menu.mainMenu();
    }
}