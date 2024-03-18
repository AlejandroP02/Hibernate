package view;

import com.opencsv.exceptions.CsvValidationException;
import controller.*;
import model.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.sql.SQLException;

public class Menu {
    /**
     * Sirve para mantener el programa activo.
     */
    private boolean continua =  true;
    private Controller c;
    private SerieController serieController;
    private EstuidoController estuidoController;
    private GeneroController generoController;

    public Menu(EntityManagerFactory entityManagerFactory) throws CsvValidationException, IOException {
        super();
        c = new Controller(entityManagerFactory);
        serieController = new SerieController(entityManagerFactory);
        estuidoController = new EstuidoController(entityManagerFactory);
        generoController = new GeneroController(entityManagerFactory);
    }

    /**
     * Método que inicia el menú principal del programa ACB.
//     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
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

    public String menuTable(){
        return (String) c.mostrarTablas().get(c.nextInt());
    }

    public String menuColumnas(){
        return (String) c.mostrarColumnas(menuTable()).get(c.nextInt());
    }

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
        }/*else if(o==7){
            c.selectCondicion();
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }else if(o==8){
            c.selectElemento();
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
            c.nextLine();
        }else if(o==9){
            c.update();
            System.out.println("Datos actualizados");
            System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
            c.nextLine();
        }*/else if(o==10){
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