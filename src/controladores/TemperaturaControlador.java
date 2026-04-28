package controladores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class TemperaturaControlador {

    public static void graficar(JPanel pnlGrafica,
            List<Temperatura> temperaturas,
            String ciudad,
            LocalDate desde, LocalDate hasta) {

        Map<String, Double> promedios = TemperaturaServicio.getPromedioPorCiudadEnRango(temperaturas, desde, hasta);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var entrada : promedios.entrySet()) {
            dataset.addValue(entrada.getValue(), "Promedio (°C)", entrada.getKey());
        }

        JFreeChart graficador = ChartFactory.createBarChart(
                "Promedio de temperatura por ciudad (" + desde + " a " + hasta + ")",
                "Ciudad",
                "Temperatura (°C)",
                dataset);

        ChartPanel pnlGraficador = new ChartPanel(graficador);
        pnlGraficador.setPreferredSize(new Dimension(500, 270));

        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public static void getEstadisticas(JPanel pnlEstadisticas,
            List<Temperatura> temperaturas,
            String ciudad,
            LocalDate desde, LocalDate hasta) {

        List<Temperatura> datosFiltrados = temperaturas.stream()
                .filter(t -> !t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta))
                .collect(Collectors.toList());

        String masCalurosa = datosFiltrados.stream()
                .max(Comparator.comparingDouble(Temperatura::getValor))
                .map(t -> t.getCiudad() + String.format("  (%.2f °C)", t.getValor()))
                .orElse("N/A");

        String menosFria = datosFiltrados.stream()
                .min(Comparator.comparingDouble(Temperatura::getValor))
                .map(t -> t.getCiudad() + String.format("  (%.2f °C)", t.getValor()))
                .orElse("N/A");

        pnlEstadisticas.removeAll();
        pnlEstadisticas.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.gridx = 0;
        pnlEstadisticas.add(new JLabel("Ciudad más calurosa:"), gbc);
        gbc.gridx = 1;
        pnlEstadisticas.add(new JLabel(masCalurosa), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        pnlEstadisticas.add(new JLabel("Ciudad más fría:"), gbc);
        gbc.gridx = 1;
        pnlEstadisticas.add(new JLabel(menosFria), gbc);

        pnlEstadisticas.revalidate();
        pnlEstadisticas.repaint();
    }
}