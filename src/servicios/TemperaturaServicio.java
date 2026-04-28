package servicios;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import modelos.Temperatura;

public class TemperaturaServicio {

        public static List<Temperatura> getDatos(String nombreArchivo) {
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                        var lineas = Files.lines(Paths.get(nombreArchivo));

                        return lineas.skip(1)
                                        .map(linea -> linea.split(","))
                                        .map(textos -> new Temperatura(
                                                        textos[0].trim(),
                                                        LocalDate.parse(textos[1].trim(), formatoFecha),
                                                        Double.parseDouble(textos[2].trim())))
                                        .collect(Collectors.toList());

                } catch (Exception e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                }
        }

        public static List<String> getCiudades(List<Temperatura> temperaturas) {
                return temperaturas.stream()
                                .map(Temperatura::getCiudad)
                                .distinct()
                                .sorted()
                                .collect(Collectors.toList());
        }

        public static List<Temperatura> filtrar(List<Temperatura> temperaturas,
                        String ciudad, LocalDate desde, LocalDate hasta) {
                return temperaturas.stream()
                                .filter(t -> t.getCiudad().equals(ciudad) &&
                                                !t.getFecha().isBefore(desde) &&
                                                !t.getFecha().isAfter(hasta))
                                .collect(Collectors.toList());
        }

        public static Map<LocalDate, Double> getDatosGrafica(List<Temperatura> temperaturas) {
                return temperaturas.stream()
                                .collect(Collectors.toMap(Temperatura::getFecha, Temperatura::getValor));
        }

        public static Map<String, Double> getPromedioPorCiudad(List<Temperatura> temperaturas) {
                return temperaturas.stream()
                                .collect(Collectors.groupingBy(
                                                Temperatura::getCiudad,
                                                Collectors.averagingDouble(Temperatura::getValor)));
        }

        public static String getCiudadMasCalurosa(List<Temperatura> temperaturas) {
                return getPromedioPorCiudad(temperaturas).entrySet().stream()
                                .max(Comparator.comparingDouble(Map.Entry::getValue))
                                .map(Map.Entry::getKey)
                                .orElse("n/a");
        }

        public static String getCiudadMenosFria(List<Temperatura> temperaturas) {
                return getPromedioPorCiudad(temperaturas).entrySet().stream()
                                .min(Comparator.comparingDouble(Map.Entry::getValue))
                                .map(Map.Entry::getKey)
                                .orElse("n/a");
        }

        public static Map<String, Double> getPromedioPorCiudadEnRango(List<Temperatura> temperaturas,
                        LocalDate desde, LocalDate hasta) {
                return temperaturas.stream()
                                .filter(t -> !t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta))
                                .collect(Collectors.groupingBy(
                                                Temperatura::getCiudad,
                                                Collectors.averagingDouble(Temperatura::getValor)));
        }
}
