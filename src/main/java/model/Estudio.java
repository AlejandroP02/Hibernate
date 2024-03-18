package model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * Subclase que se encuentra dentro
 * de la clase Serie
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "estudio")
public class Estudio implements Serializable {
    /**
     * Identificador de la clase.
     */
    @Id
    protected int id;
    /**
     * Nombre del estudio.
     */
    @Column(name = "nombre", length = 50)
    protected String nombre;
    /**
     * Enlace del estudio.
     */
    @Column(name = "link", length = 100)
    protected String link;
    /**
     * Fecha en la que se creó el
     * estudio.
     */
    @Column(name = "fechaCreacion")
    protected LocalDate fechaCreacion;
    /**
     * Cantidad de series que pertenecen
     * al estudio.
     */
    @Column(name = "series")
    protected int series;
    /**
     * Constructor vació porque Hibernate lo solicita.
     */
    public Estudio() {
    }

    /**
     * Constructor de la clase Estudio, con todos los atributos
     * necesarios. El identificador es incremental por ello no
     * lo necesita como parámetro.
     * @param id Id del estudio.
     * @param nombre Nombre del estudio.
     * @param link Enlace de la página del estudio de la que se obtiene
     *             información en el programa.
     * @param fechaCreacion Fecha en la que se creó el estudio.
     * @param series Cantidad de series en las que ha trabajado el estudio.
     */
    public Estudio(int id, String nombre, String link, LocalDate fechaCreacion, int series) {
        super();
        this.id=id;
        this.nombre = nombre;
        this.link = link;
        this.fechaCreacion = fechaCreacion;
        this.series = series;
    }
    /**
     * Getter del identificador del estudio.
     * @return Devuelve el ID del estudio.
     */
    public int getId() {
        return id;
    }
    /**
     * Getter del nombre del estudio.
     * @return Retorna un String con el nombre del estudio.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Getter del enlace del estudio, se utiliza principalmente
     * para evitar entrar en páginas en las ya se ha entrado.
     * @return Retorna un String con el enlace del estudio.
     */
    public String getLink() {
        return link;
    }

    /**
     * Getter de la fecha de creación del estudio.
     * @return Retorna como LocalDate la fecha en la que se
     * creó el estudio.
     */
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * Getter del número de series del estudio.
     * @return Retorna en int el número de series que contiene
     * el estudio.
     */
    public int getSeries() {
        return series;
    }


    @Override
    public String toString() {
        return "Estudio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", link='" + link + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", series=" + series +
                '}';
    }
}
