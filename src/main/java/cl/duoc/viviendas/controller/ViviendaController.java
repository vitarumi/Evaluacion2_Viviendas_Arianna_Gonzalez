package cl.duoc.viviendas.controller;

import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import cl.duoc.viviendas.model.Vivienda;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleDoubleProperty;

public class ViviendaController {

    // Selector
    @FXML private ComboBox<String> cmbTipo;

    // Cajas de texto
    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtPiso;
    @FXML private TextField txtHabitaciones;

    // Casillas de verificación
    @FXML private CheckBox chkPatio;
    @FXML private CheckBox chkGastoComun;
    @FXML private CheckBox chkEstacionamiento;

    // Mensajes y Tabla
    @FXML private Label lblMensaje;
    @FXML private TableView<Vivienda> tablaViviendas;

    // Se asigna las columnas de la tabla
    @FXML private TableColumn<Vivienda, String> colTipo;
    @FXML private TableColumn<Vivienda, String> colCodigo;
    @FXML private TableColumn<Vivienda, Double> colSuperficie;
    @FXML private TableColumn<Vivienda, Integer> colHabitaciones;
    @FXML private TableColumn<Vivienda, Double> colCosto;

    // Se inicia el menu
    @FXML public void initialize() {
        // opciones para el menu despegable
        cmbTipo.getItems().addAll("Casa", "Departamento");

        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        colHabitaciones.setCellValueFactory(new PropertyValueFactory<>("habitaciones"));

        // Pra calcular el coste
        colCosto.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().calcularCostoArriendo()).asObject()
        );
    }



    @FXML public void alSeleccionarTipo() {
        String tipo = cmbTipo.getValue();
        if (tipo != null) {
            if (tipo.equals("Casa")) {
                // Para habilitar y seleccionar las opciones de la casa
                chkPatio.setDisable(false);
                // Se bloquea lo demás y limpia si puso alguna opcion
                txtPiso.setDisable(true);
                txtPiso.clear();
                chkGastoComun.setDisable(true);
                chkGastoComun.setSelected(false);
                chkEstacionamiento.setDisable(true);
                chkEstacionamiento.setSelected(false);

            } else if (tipo.equals("Departamento")) {
                // Habilita lo del Departamento
                txtPiso.setDisable(false);
                chkGastoComun.setDisable(false);
                chkEstacionamiento.setDisable(false);
                // Bloquea y limpia lo de la Casa
                chkPatio.setDisable(true);
                chkPatio.setSelected(false);
            }
        }

    }

    // Metodo para el botón Registrar
    @FXML public void registrarVivienda() {

        try {
            // Validación de que se seleccione algo en el ComboBox
            String tipo = cmbTipo.getValue();
            if (tipo == null) throw new Exception("Seleccione Casa o Departamento.");

            // Para obtener los datos escritos por el usuario que son texto
            String codigo = txtCodigo.getText();

            // Para obtener los datos escritos por el usuario que son numeros
            double superficie = Double.parseDouble(txtSuperficie.getText());
            int habitaciones = Integer.parseInt(txtHabitaciones.getText());

            // Para leer la casillas de verdadero o falso
            boolean tienePatio = chkPatio.isSelected();
            boolean gastoComunAlDia = chkGastoComun.isSelected();
            boolean tieneEstacionamiento = chkEstacionamiento.isSelected();

            if(tipo.equals("Departamento")) {

                int piso = Integer.parseInt(txtPiso.getText());

                Departamento nuevoDepto = new Departamento(codigo, superficie,habitaciones, piso, gastoComunAlDia);

                if (tieneEstacionamiento) nuevoDepto.asignarEstacionamiento();

                tablaViviendas.getItems().add(nuevoDepto); // Se envia a la tabla

            }   else if (tipo.equals("Casa")) {
                Casa nuevaCasa = new Casa(codigo, superficie, habitaciones, tienePatio);

                tablaViviendas.getItems().add(nuevaCasa); // Se envia a la tabla
            }

            // Mensaje de éxito
            lblMensaje.setText("¡Datos ingresados correctamente!");
            lblMensaje.setStyle("-fx-text-fill: green;");

            // Validación por si se ingresan letras en vez de numeros en la Superficie
        } catch (NumberFormatException e) {
            lblMensaje.setText("Error: La superficie, piso y habitaciones deben ser números.");
            lblMensaje.setStyle("-fx-text-fill: red;");


            // Validación para la regla de negocio
        } catch (IllegalArgumentException e) {
            lblMensaje.setText("Error: " + e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");

            // Validación extra para atrapar cualquier otro error
        } catch (Exception e) {
            lblMensaje.setText("Error: Por favor complete todos los campos.");
            lblMensaje.setStyle("-fx-text-fill: red;");
    }
    }

    // Metodo para el botón Eliminar
    @FXML
    public void eliminarVivienda() {
        Vivienda seleccionada = tablaViviendas.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            tablaViviendas.getItems().remove(seleccionada);
            lblMensaje.setText("Vivienda eliminada correctamente.");
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            lblMensaje.setText("Error: Seleccione una vivienda de la tabla para eliminar.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

    }
}
