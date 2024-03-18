package model;

import javax.persistence.*;

/**
 * Subclase que se encuentra dentro
 * de la clase Serie
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "genero")
public class Genero {
    /**
     * Identificador de la clase.
     */
    @Id
    protected int id;
    /**
     * Nombre del género.
     */
    @Column(name = "nombre", length = 50)
    protected String nombre;
    /**
     * Enlace del género.
     */
    @Column(name = "link", length = 100)
    protected String link;
    /**
     * Descripción o explicación del
     * género, algunos géneros no
     * contienen descripción.
     */
    @Column(name = "descripcion", length = 2000)
    protected String descripcion;
    /**
     * Cantidad de series que pertenecen
     * al género.
     */
    @Column(name = "series")
    protected int series;
    /**
     * Constructor vació porque Hibernate lo solicita.
     */
    public Genero() {
    }

    /**
     * Constructor de la clase Género, con todos los atributos
     * necesarios. El identificador es incremental por ello no
     * lo necesita como parámetro.
     * @param id Id del genero.
     * @param nombre Nombre del género.
     * @param link Enlace de la página del género de la que se obtiene
     *             información en el programa.
     * @param descripcion Descripción o explicación de que significa el
     *                    género.
     * @param series Cantidad de series que pertenecen al género.
     */
    public Genero(int id, String nombre, String link, String descripcion, int series) {
        this.id=id;
        this.nombre = nombre;
        this.link = link;
        this.descripcion = descripcion;
        this.series = series;
    }
    /**
     * Getter del identificador del género.
     * @return Devuelve el ID del género.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter del enlace del género, se utiliza principalmente
     * para evitar entrar en páginas en las ya se ha entrado.
     * @return Retorna un String con el enlace del género.
     */
    public String getLink() {
        return link;
    }

    /**
     * Getter del nombre del género.
     * @return Retorna un String con el nombre del género.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter de la explicación del género. Algunos géneros son tan
     * conocidos que no tienen explicación.
     * @return Retorna en forma de String una explicación de que
     * significa el género.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Getter del número de series del género.
     * @return Retorna en int el número de series que contiene
     * el género.
     */
    public int getSeries() {
        return series;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", link='" + link + '\'' +
                ", descripcion='" + descripcion.substring(0, Math.min(descripcion.length(), 50)) + '\'' +
                ", series=" + series +
                '}';
    }
}
