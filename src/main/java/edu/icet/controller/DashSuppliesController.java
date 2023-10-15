package edu.icet.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.db.DBConnection;
import edu.icet.entity.Item;
import edu.icet.entity.Supplier;
import edu.icet.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashSuppliesController implements Initializable  {

    @FXML
    private JFXTextField txtSupId;
    @FXML
    private JFXTextField txtSupName;
    @FXML
    private JFXComboBox<String> selectTitle;
    @FXML
    private JFXTextField txtSupContact;
    @FXML
    private JFXTextField txtSupCompany;

    @FXML
    private TableView<Supplier> tbpSupplier;
    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableColumn<?, ?> colSupTitle;

    @FXML
    private TableColumn<?, ?> ColSupName;

    @FXML
    private TableColumn<?, ?> colSupCompany;

    @FXML
    private TableColumn<?, ?> colSupContact;

    @FXML
    private TableColumn<?, ?> colSupOption;
    @FXML
    private TextField txtSearchSup;
    @FXML
    private TableView<Item> tblSupItem;

    @FXML
    private TableColumn<?, ?> colSupItemCode;

    @FXML
    private TableColumn<?, ?> colSupDescription;

    @FXML
    private TableColumn<?, ?> colSupQty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCmb();
        generateId();

        colSupId.setCellValueFactory(new PropertyValueFactory<>("supID"));
        colSupTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        ColSupName.setCellValueFactory(new PropertyValueFactory<>("supName"));
        colSupCompany.setCellValueFactory(new PropertyValueFactory<>("supCompany"));
        colSupContact.setCellValueFactory(new PropertyValueFactory<>("supContactNumber"));
        loadTable();


    }
    public void loadItemTable(String id) {
        ObservableList<Item> list = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE supplier_id = '" + id + "'");

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                list.add(new Item(
                        resultSet.getString("item_id"),
                        resultSet.getString("description"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("buying_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getString("type"),
                        resultSet.getString("size"),
                        resultSet.getDouble("profit"),
                        resultSet.getString("supplier_id")
                ));
            }
            tblSupItem.setItems(list);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTable() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier");
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                list.add(new Supplier(
                        resultSet.getString("id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("contact_number"),
                        resultSet.getString("company"),
                        resultSet.getString("title")
                ));
            }
            tbpSupplier.setItems(list);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchSup(ActionEvent event) {
        colSupItemCode.setCellValueFactory(new PropertyValueFactory<>("item_id"));
        colSupDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSupQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        String id = txtSearchSup.getText();
        loadItemTable(id);
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String SQL = "SELECT * FROM supplier WHERE id = '" + id + "'";
            ResultSet rst = stm.executeQuery(SQL);

            if (rst.next()) {
                Supplier supplier = new Supplier(
                        rst.getString("id"),
                        rst.getString("supplier_name"),
                        rst.getString("contact_number"),
                        rst.getString("company"),
                        rst.getString("title")
                );
                txtSupId.setText(supplier.getSupID());
                txtSupName.setText(supplier.getSupName());
                selectTitle.setValue(supplier.getTitle());
                txtSupContact.setText(supplier.getSupContactNumber());
                txtSupCompany.setText(supplier.getSupCompany());
            } else {
                new Alert(Alert.AlertType.ERROR, "Supplier not found").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadCmb(){
        ObservableList<String> obs = FXCollections.observableArrayList("Mr.", "Mrs.","Miss");
        selectTitle.setItems(obs);
    }

    @FXML
    void btnAddStock(ActionEvent event) {
        Supplier supplier = new Supplier(txtSupId.getText(), txtSupName.getText(), txtSupContact.getText(), txtSupCompany.getText(),selectTitle.getValue().toString());

        try {
            boolean isAdded = CrudUtil.execute("INSERT INTO supplier VALUES(?,?,?,?,?)",
                    supplier.getSupID(),
                    supplier.getTitle(),
                    supplier.getSupName(),
                    supplier.getSupContactNumber(),
                    supplier.getSupCompany());

            if(isAdded){
                new Alert(Alert.AlertType.INFORMATION,"Supplier Saved..!").show();
                txtSupName.clear();
                txtSupContact.clear();
                txtSupCompany.clear();
                selectTitle.setValue(null);
                loadTable();
                generateId();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong..!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnClear(ActionEvent event) {
        txtSupName.clear();
        txtSupContact.clear();
        txtSupCompany.clear();
        selectTitle.setValue(null);
        generateId();
    }

    @FXML
    void btnPrint(ActionEvent event) {

    }
    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT id FROM supplier ORDER BY id DESC LIMIT 1");
        return rst.next() ? rst.getString("id") : null;
    }
    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("SUP\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(3)) + 1;
                String newSupplierId = String.format("SUP%04d", numericPart);
                txtSupId.setText(newSupplierId);
            } else {
                txtSupId.setText("SUP0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}