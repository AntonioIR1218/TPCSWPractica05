package org.uv.TPCSWPractica05;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "empleados2")  // Nombre de la tabla en la base de datos
public class Empleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleados2_id_seq")
    @SequenceGenerator(name = "empleados2_id_seq", sequenceName = "empleados2_id_seq", allocationSize = 1)
    @Column(name = "id")  // Nombre de la columna en la base de datos
    private long clave;  // Atributo interno en Java se llama 'clave'
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "celular")
    private String celular;
    
    @Column
    private String direccion;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "depto_clave", referencedColumnName = "clave")
    @JsonBackReference  // Evita que se haga una referencia circular entre Empleado y Departamento
    private Departamentos depto;

    // Getters y Setters
    public long getClave() {
        return clave;
    }

    public void setClave(long clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Departamentos getDepto() {
        return depto;
    }

    public void setDepto(Departamentos depto) {
        this.depto = depto;
    }
}
