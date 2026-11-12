package ni.edu.ni.hibernate.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.ni.hibernate.Categoria;
import ni.edu.ni.hibernate.dao.CategoriaDAO;
import java.util.List;

public class CategoriaController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<Categoria> tblCategorias;
    @FXML private TableColumn<Categoria, Long> colId;
    @FXML private TableColumn<Categoria, String> colNombre;
    @FXML private TableColumn<Categoria, String> colDescripcion;

    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblCategorias.setItems(listaCategorias);
        cargarCategorias();
    }

    @FXML
    public void guardarCategoria() {
        Categoria nueva = new Categoria();
        nueva.setNombre(txtNombre.getText());
        nueva.setDescripcion(txtDescripcion.getText());

        categoriaDAO.guardar(nueva);
        cargarCategorias();

        txtNombre.clear();
        txtDescripcion.clear();
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaDAO.obtenerTodas();
        listaCategorias.setAll(categorias);
    }
}