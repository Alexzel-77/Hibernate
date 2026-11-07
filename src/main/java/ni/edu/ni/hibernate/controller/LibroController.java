package ni.edu.ni.hibernate.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.ni.hibernate.Libro;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class LibroController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtPrecio;

    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCargar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Libro> tblLibros;
    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, Double> colPrecio;

    private ObservableList<Libro> listaLibros;


    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");

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


        cargarLibros();
    }

    private void guardarLibro() {
        try {
            Libro libro = new Libro();
            libro.setTitulo(txtTitulo.getText());
            libro.setAutor(txtAutor.getText());
            libro.setAnio_publicacion(Integer.parseInt(txtAnio.getText()));
            libro.setPrecio(Double.parseDouble(txtPrecio.getText()));

            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(libro); // Guarda en la BD
            em.getTransaction().commit();
            em.close();

            limpiarCampos();
            cargarLibros(); // Refresca la tabla
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    private void modificarLibro() {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un libro de la tabla para modificar.");
            return;
        }

        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();


            Libro libroActualizar = em.find(Libro.class, libroSeleccionado.getId());
            libroActualizar.setTitulo(txtTitulo.getText());
            libroActualizar.setAutor(txtAutor.getText());
            libroActualizar.setAnio_publicacion(Integer.parseInt(txtAnio.getText()));
            libroActualizar.setPrecio(Double.parseDouble(txtPrecio.getText()));

            em.merge(libroActualizar);
            em.getTransaction().commit();
            em.close();

            limpiarCampos();
            cargarLibros();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo modificar: " + e.getMessage());
        }
    }

    private void eliminarLibro() {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un libro de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación eliminacion");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el libro seleccionado: " + libroSeleccionado.getTitulo() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            try {
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();

                Libro libroEliminar = em.find(Libro.class, libroSeleccionado.getId());
                em.remove(libroEliminar);

                em.getTransaction().commit();
                em.close();

                limpiarCampos();
                cargarLibros();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
            }
        }

        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();

            Libro libroEliminar = em.find(Libro.class, libroSeleccionado.getId());
            if (libroEliminar != null) {
                em.remove(libroEliminar); // Elimina de la BD
            }

            em.getTransaction().commit();
            em.close();

            limpiarCampos();
            cargarLibros();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void cargarLibros() {
        try {
            EntityManager em = emf.createEntityManager();
            // Consulta HQL para obtener todos los registros
            List<Libro> libros = em.createQuery("FROM Libro", Libro.class).getResultList();
            listaLibros.setAll(libros); // Actualiza el ObservableList de JavaFX
            em.close();
        } catch (Exception e) {
            mostrarAlerta("Error de Conexión", "No se pudieron cargar los datos: " + e.getMessage());
        }
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


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}