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
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
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

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //establecemos la conexión con la BD
        conectaBD();

        //Creamos la escena
        Menu informes = new Menu("Informes");
        Menu ayuda = new Menu("Ayuda");
        Menu salir = new Menu("Salir");

        // create menuitems
        MenuItem m1 = new MenuItem("Listado Facturas");
        MenuItem m2 = new MenuItem("Ventas Totales");
        MenuItem m3 = new MenuItem("Facturas por Cliente");
        MenuItem m4 = new MenuItem("Subinforme Listado Facturas");

        // add menu items to menu
        informes.getItems().add(m1);
        informes.getItems().add(m2);
        informes.getItems().add(m3);
        informes.getItems().add(m4);

        //setOnActions
        m1.setOnAction(e -> {
            generaInformeFacturas();
        });
        m2.setOnAction(e -> {
            generaInformeVentasTotales();
        });
        m3.setOnAction(e -> {
            System.out.println("Menu Item 3 Selected");
        });
        m4.setOnAction(e -> {
            System.out.println("Menu Item 4 Selected");
        });

        // create a menubar
        MenuBar mb = new MenuBar();

        // add menu to menubar
        mb.getMenus().addAll(informes, ayuda, salir);

        // create a VBox
        VBox vb = new VBox(mb);

        // create a scene
        Scene sc = new Scene(vb, 815, 559);
        sc.getStylesheets().addAll(this.getClass().getResource("estilo.css").toExternalForm());
        // set the scene
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

    public static void main(String[] args) {
        launch(args);
    }

}
