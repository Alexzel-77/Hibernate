package ni.edu.ni.hibernate.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.ni.hibernate.Libro;

public class LibroController {

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtPrecio;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCargar;
    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Libro> tblLibros;
    @FXML
    private TableColumn<Libro, Long> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, Integer> colAnio;
    @FXML
    private TableColumn<Libro, Double> colPrecio;

    private ObservableList<Libro> listaLibros;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio_publicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        listaLibros = FXCollections.observableArrayList();
        tblLibros.setItems(listaLibros);

        btnGuardar.setOnAction(event -> guardarLibro());
        btnModificar.setOnAction(event -> modificarLibro());
        btnEliminar.setOnAction(event -> eliminarLibro());
        btnCargar.setOnAction(event -> cargarLibros());
        btnLimpiar.setOnAction(event -> limpiarCampos());

        tblLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarLibroSeleccionado(newSelection);
            }
        });
    }

    private void guardarLibro() {
    }

    private void modificarLibro() {
    }

    private void eliminarLibro() {
    }

    private void cargarLibros() {
    }

    private void limpiarCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        tblLibros.getSelectionModel().clearSelection();
    }

    private void mostrarLibroSeleccionado(Libro libro) {
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio_publicacion()));
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
    }
}
