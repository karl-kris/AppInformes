/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package appinformes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Kyle7
 */
public class AppInformes extends Application {

    public static Connection conexion = null;
    public static int ID = 0;

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        conectaBD();

        Menu informes = new Menu("Informes");
        Menu ayuda = new Menu("Ayuda");
        Menu salir = new Menu("Salir");

        MenuItem m1 = new MenuItem("Listado Facturas");
        MenuItem m2 = new MenuItem("Ventas Totales");
        MenuItem m3 = new MenuItem("Facturas por Cliente");
        MenuItem m4 = new MenuItem("Subinforme Listado Facturas");

        informes.getItems().add(m1);
        informes.getItems().add(m2);
        informes.getItems().add(m3);
        informes.getItems().add(m4);

        TextInputDialog textoID = new TextInputDialog();
        textoID.setHeaderText("Introduce el ID del cliente.");
        TextField id = textoID.getEditor();

        m1.setOnAction(e -> {
            generaInformeFacturas();
        });
        m2.setOnAction(e -> {
            generaInformeVentasTotales();
        });
        m3.setOnAction(e -> {
            Optional<String> action1 = textoID.showAndWait();
            if (action1.isPresent()) {
                if (id.getText() != null) {
                    generaFacturaCliente(id.getText());
                } else {
                    System.out.println("No se pudo crear el Jasper Report");
                }
            }
        });
        m4.setOnAction(e -> {
            generaFacturasSubinforme();
        });

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(informes, ayuda, salir);

        VBox vb = new VBox(mb);

        Scene sc = new Scene(vb, 815, 559);
        sc.getStylesheets().addAll(this.getClass().getResource("estilo.css").toExternalForm());
        primaryStage.setScene(sc);

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        try {

            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/sampledb");
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar la conexion a la BD");
        }

    }

    public void conectaBD() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost/sampledb";
        String usuario = "SA";
        String clave = "";

        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }

    }

    public void generaInformeFacturas() {

        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Factura.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaInformeVentasTotales() {

        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Ventas_totales.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaFacturaCliente(String id) {

        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Factura_Cliente.jasper"));
            //Map de parámetros
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            int idAddress = Integer.parseInt(id);
            parametros.put("ADDRESSID", idAddress);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaFacturasSubinforme() {

        try {
            JasperReport sub = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturaSub.jasper"));
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Factura_Subinforme.jasper"));
//            JasperReport jr = JasperCompileManager.compileReport("Factura_Subinforme.jasper");
//            JasperReport sub = JasperCompileManager.compileReport("FacturaSub.jasper");

            //Map de parámetros
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("subReportParameter", sub);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
