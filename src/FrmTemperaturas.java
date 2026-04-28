import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import controladores.TemperaturaControlador;
import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class FrmTemperaturas extends JFrame {

    private JComboBox cmbCiudad;
    private DateChooserCombo dccDesde, dccHasta;
    private JTabbedPane tpTemperaturas;
    private JPanel pnlGrafica;
    private JPanel pnlEstadisticas;

    private List<Temperatura> datos;

    public FrmTemperaturas() {

        setTitle("Temperaturas de Colombia");
        setSize(700, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        JButton btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Grafica Temperatura vs Fecha");
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGraficarClick();
            }
        });
        tb.add(btnGraficar);

        JButton btnCalcularEstadisticas = new JButton();
        btnCalcularEstadisticas.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnCalcularEstadisticas.setToolTipText("Estadísticas de la ciudad seleccionada");
        btnCalcularEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCalcularEstadisticasClick();
            }
        });
        tb.add(btnCalcularEstadisticas);

        JPanel pnlContenedor = new JPanel();
        pnlContenedor.setLayout(new BoxLayout(pnlContenedor, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        //JLabel lblCiudad = new JLabel("Ciudad");
        //lblCiudad.setBounds(10, 10, 60, 25);
        //pnlDatosProceso.add(lblCiudad);

        //cmbCiudad = new JComboBox();
        //cmbCiudad.setBounds(75, 10, 120, 25);
        //pnlDatosProceso.add(cmbCiudad);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(205, 10, 110, 25);
        pnlDatosProceso.add(dccDesde);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(325, 10, 110, 25);
        pnlDatosProceso.add(dccHasta);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlEstadisticas = new JPanel();
        JScrollPane spEstadisticas = new JScrollPane(pnlEstadisticas);

        tpTemperaturas = new JTabbedPane();
        tpTemperaturas.addTab("Gráfica", spGrafica);
        tpTemperaturas.addTab("Estadísticas", spEstadisticas);

        pnlContenedor.add(pnlDatosProceso);
        pnlContenedor.add(tpTemperaturas);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlContenedor, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Temperaturas colombia.csv";
        datos = TemperaturaServicio.getDatos(nombreArchivo);
        //var ciudades = TemperaturaServicio.getCiudades(datos);
        //cmbCiudad.setModel(new DefaultComboBoxModel(ciudades.toArray()));
    }
    

    private void btnGraficarClick() {
        //if (cmbCiudad.getSelectedIndex() >= 0) {
           // String ciudad = (String) cmbCiudad.getSelectedItem();
            LocalDate desde = dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate hasta = dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            tpTemperaturas.setSelectedIndex(0);
            TemperaturaControlador.graficar(pnlGrafica, datos, null, desde, hasta);
        //}
    }

    private void btnCalcularEstadisticasClick() {
        //if (cmbCiudad.getSelectedIndex() >= 0) {
          //  String ciudad = (String) cmbCiudad.getSelectedItem();
            LocalDate desde = dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate hasta = dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            tpTemperaturas.setSelectedIndex(1);
            TemperaturaControlador.getEstadisticas(pnlEstadisticas, datos, null, desde, hasta);
     //   }
    }
}
