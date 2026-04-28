package modelos;

import java.time.LocalDate;

public class Temperatura {
    private String ciudad;
    private LocalDate fecha;
    private double valor;

    public Temperatura(String ciudad, LocalDate fecha, double valor) {
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
