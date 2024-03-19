package view;

import com.opencsv.exceptions.CsvValidationException;
import controller.*;
import model.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Esta clase representa el menú de la aplicación.
 * Contiene métodos para mostrar el menú principal,
 * interactuar con las tablas de la base de datos,
 * y manejar las opciones seleccionadas por el usuario.
 */
public class Menu {
    /**
     * Sirve para mantener el programa activo.
     */
    private boolean continua =  true;
    /**
     * Controlador de la aplicación.
     */
    private Controller c;

    /**
     * Constructor de la clase Menu.
     * @param entityManagerFactory el EntityManagerFactory para interactuar con la base de datos.
     * @throws CsvValidationException si ocurre un error de validación de CSV.
     * @throws IOException si ocurre un error de E/S durante la ejecución del programa.
     */
    public Menu(EntityManagerFactory entityManagerFactory) throws CsvValidationException, IOException {
        super();
        c = new Controller(entityManagerFactory);
    }

    /**
     * Método que inicia el menú principal del programa.
     * @throws IOException si ocurre un error de E/S durante la ejecución del programa.
     * @throws SQLException si ocurre un error de SQL al interactuar con la base de datos.
     */
    public void mainMenu() throws IOException, SQLException {;

        while (continua){

            System.out.println(" \nMENU PRINCIPAL \n");

            System.out.println("1. Mostrar tablas");
            System.out.println("2. Borrar tabla");
            System.out.println("3. Borrar tablas");
            System.out.println("4. Crear tablas");
            System.out.println("5. Poblar tablas");
            System.out.println("6. Select con texto concreto");
            System.out.println("7. Select con condicion");
            System.out.println("8. Select elemento especifico");
            System.out.println("9. Update");
            System.out.println("10. Delete");
            System.out.println("11. Sortir");
            System.out.println("Esculli opció:");
            opcion(c.nextInt());
        }
    }

    /**
     * Método que devuelve el nombre de una tabla del menú.
     * @return el nombre de la tabla seleccionada por el usuario.
     */
    public String menuTable(){
        return (String) c.mostrarTablas().get(c.nextInt());
    }

    /**
     * Método que muestra el menú de opciones de condición.
     * @return la opción de condición seleccionada por el usuario.
     */
    public String menuCondicion(){
        System.out.println(" \nElige una condición \n");

        System.out.println("1. LIKE");
        System.out.println("2. =");
        System.out.println("3. <");
        System.out.println("4. >");
        return oCondicion(c.nextInt());
    }

    /**
     * Método que convierte el número de condición
     * seleccionado por el usuario en su representación textual.
     * @param o el número de condición seleccionado.
     * @return la representación textual de la condición.
     */
    public String oCondicion(int o){
        if(o==1){
            return "LIKE";
        }else if(o==2){
            return "=";
        }else if(o==3){
            return "<";
        }else if(o==4){
            return ">";
        }else {
            return "opcion invalida";
        }
    }

    /**
     * Método que muestra el menú de columnas para una tabla.
     * @param tabla el nombre de la tabla.
     * @return el nombre de la columna seleccionada por el usuario.
     */
    public String menuColumnas(String tabla){
        return (String) c.mostrarColumnas(tabla).get(c.nextInt());
    }

    /**
     * Método que maneja las opciones del menú.
     * @param o La opción seleccionada por el usuario.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
     */
    private void opcion(int o) throws SQLException, IOException {
        c.nextLine();
        if(o==1){
            c.mostrarTablas();
            System.out.println();
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==2){
            c.borrarTabla(menuTable());
            System.out.println();
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==3){
            c.borrarTablas();
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==4){
            c.crearTablas();
            System.out.println("Tablas creadas");
            System.out.println("Pulsa enter para continuar");
            c.nextLine();
        }else if(o==5){
            c.poblar();
            System.out.println("Tablas plobadas");
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==6){
            String tabla = menuTable();
            String columna = menuColumnas(tabla);
            System.out.println("Escribe que buscas");
            String text=c.next();
            if(tabla.equals("serie")){
                c.selectText(Serie.class, columna,text);
            }else if(tabla.equals("estudio")){
                c.selectText(Estudio.class, columna,text);
            }else if(tabla.equals("genero")){
                c.selectText(Genero.class, columna,text);
            }else {
                System.out.println("La tabla no contiene texto.");
            }
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==7){
            System.out.println("Elige una tabla");
            String tabla = menuTable();
            String condicion = menuCondicion();
            System.out.println("Elige una columna para la condición");
            String columna2 = menuColumnas(tabla);
            c.nextLine();
            System.out.println("Escribe el texto de la condición");
            String text = c.nextLine();
            System.out.println(tabla+", "+condicion+", "+columna2+", "+text);
            c.selectCondicion(tabla,condicion, columna2, text);
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==8){
            System.out.println("Elige una tabla");
            String tabla = menuTable();
            System.out.println("Escribe la id del elemento que buscas");
            int id = c.nextInt();
            c.select1(tabla, id);
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
            c.nextLine();
        }else if(o==9){
            System.out.println("Elige una tabla");
            String tabla = menuTable();
            System.out.println("Elige una columna");
            String columna = menuColumnas(tabla);
            String condicion = menuCondicion();
            c.nextLine();
            System.out.println("Escribe los cambios a realizar");
            String update=c.nextLine();
            System.out.println("Elige una columna para la condición");
            String columna2 = menuColumnas(tabla);
            c.nextLine();
            System.out.println("Escribe el texto de la condición");
            String text = c.nextLine();
            c.update(tabla, columna, condicion, update, columna2, text);
            System.out.println("Datos actualizados");
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==10){
            String tabla = menuTable();
            System.out.println("Introduce la id del elemento");
            int id = c.nextInt();
            c.eliminarEntidad(tabla, id);
            System.out.println("Datos borrados");
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==11){
            continua = false;
        }else{
            System.out.println("Opcion no valida");
        }
    }
}