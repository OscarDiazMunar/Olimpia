package com.oscar.olimpia.Modelo;

import java.io.Serializable;

/**
 * Created by Usuario on 22/08/2017.
 */

public class Usuario implements Serializable {
    private String nombre;
    private String cedula;
    private String dirreccion;
    private String celular;
    private String pais;
    private String ciudad;
    private String coordenadas;
    private String foto;

    public Usuario(String nombre, String cedula, String dirreccion, String celular, String pais, String ciudad, String coordenadas, String foto) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.dirreccion = dirreccion;
        this.celular = celular;
        this.pais = pais;
        this.ciudad = ciudad;
        this.coordenadas = coordenadas;
        this.foto = foto;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDirreccion() {
        return dirreccion;
    }

    public void setDirreccion(String dirreccion) {
        this.dirreccion = dirreccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", dirreccion='" + dirreccion + '\'' +
                ", celular='" + celular + '\'' +
                ", pais='" + pais + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", coordenadas='" + coordenadas + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
