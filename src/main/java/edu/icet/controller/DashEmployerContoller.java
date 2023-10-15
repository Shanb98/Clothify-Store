package edu.icet.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.db.DBConnection;
import edu.icet.entity.Employer;
import edu.icet.entity.Item;
import edu.icet.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashEmployerContoller implements Initializable {

    @FXML
    private JFXTextField txtEmpId;

    @FXML
    private JFXTextField txtEmpName;

    @FXML
    private JFXTextField txtEmpNic;

    @FXML
    private Button btnEmpPrint;

    @FXML
    private Button btnEmpClear;

    @FXML
    private Button btnEmpSave;

    @FXML
    private TableView<Employer> tblEmp;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colEmpTitle;

    @FXML
    private TableColumn<?, ?> colEmpName;

    @FXML
    private TableColumn<?, ?> colEmpNic;

    @FXML
    private TableColumn<?, ?> colEmpDob;

    @FXML
    private TableColumn<?, ?> colEmpAddress;

    @FXML
    private TableColumn<?, ?> colEmpContact;

    @FXML
    private TableColumn<?, ?> colEmpBankAcNo;

    @FXML
    private TableColumn<?, ?> colEmpBranch;

    @FXML
    private JFXTextField txtEmpAddress;

    @FXML
    private TextField txtSearchEmp;

    @FXML
    private Button btnEmpSearch;

    @FXML
    private JFXTextField txtEmpContact;

    @FXML
    private JFXTextField txtBankAcNo;

    @FXML
    private JFXTextField txtBranch;

    @FXML
    private DatePicker dateEmpDob;

    @FXML
    private JFXComboBox<String> selectEmpTitle;
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
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("EMPID"));
        colEmpTitle.setCellValueFactory(new PropertyValueFactory<>("EMPTitle"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("EMPName"));
        colEmpNic.setCellValueFactory(new PropertyValueFactory<>("EMPNic"));
        colEmpDob.setCellValueFactory(new PropertyValueFactory<>("EMPDob"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory<>("EMPAddress"));
        colEmpContact.setCellValueFactory(new PropertyValueFactory<>("EMPContact"));
        colEmpBankAcNo.setCellValueFactory(new PropertyValueFactory<>("EMPBankAcNo"));
        colEmpBranch.setCellValueFactory(new PropertyValueFactory<>("EMPBranch"));

        loadTable();



    }
    public void loadCmb(){
        ObservableList<String> obs = FXCollections.observableArrayList("Mr.", "Mrs.","Miss");
        selectEmpTitle.setItems(obs);
    }

    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT EMPID FROM Employer ORDER BY EMPID DESC LIMIT 1");
        return rst.next() ? rst.getString("EMPID") : null;
    }
    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("EMP\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(3)) + 1;
                String newSupplierId = String.format("EMP%04d", numericPart);
                txtEmpId.setText(newSupplierId);
            } else {
                txtEmpId.setText("EMP0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnEmpClear(ActionEvent event) {
        txtEmpId.clear();
        txtEmpName.clear();
        txtEmpNic.clear();
        txtEmpAddress.clear();
        dateEmpDob.getEditor().clear();
        txtEmpContact.clear();
        txtBankAcNo.clear();
        txtBranch.clear();
        selectEmpTitle.setValue(null);
        generateId();

    }

    @FXML
    void btnEmpPrint(ActionEvent event) {

    }

    @FXML
    void btnEmpSave(ActionEvent event) {
        Employer employer = new Employer(
                txtEmpId.getText(),
                txtEmpName.getText(),
                txtEmpNic.getText(),
                txtEmpAddress.getText(),
                dateEmpDob.getValue() != null ? dateEmpDob.getValue().toString() : null,
                txtEmpContact.getText(),
                txtBankAcNo.getText(),
                txtBranch.getText(),
                selectEmpTitle.getValue().toString()
        );

        try {
            boolean isAdded = CrudUtil.execute("INSERT INTO Employer (EMPID, EMPName, EMPNic, EMPAddress, EMPDob, EMPContact, EMPBankAcNo, EMPBranch, EMPTitle) VALUES(?,?,?,?,?,?,?,?,?)",
                    employer.getEMPID(),
                    employer.getEMPName(),
                    employer.getEMPNic(),
                    employer.getEMPAddress(),
                    employer.getEMPDob(),
                    employer.getEMPContact(),
                    employer.getEMPBankAcNo(),
                    employer.getEMPBranch(),
                    selectEmpTitle.getValue().toString()
            );

            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Employer Saved..!").show();
                txtEmpName.clear();
                txtEmpNic.clear();
                txtEmpAddress.clear();
                dateEmpDob.getEditor().clear();
                txtEmpContact.clear();
                txtBankAcNo.clear();
                txtBranch.clear();
                selectEmpTitle.setValue(null);
                loadTable();
                generateId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong..!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);        }
    }

    public void loadTable() {
        ObservableList<Employer> list = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Employer");
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                list.add(new Employer(
                        resultSet.getString("EMPID"),
                        resultSet.getString("EMPName"),
                        resultSet.getString("EMPNic"),
                        resultSet.getString("EMPAddress"),
                        resultSet.getString("EMPDob"),
                        resultSet.getString("EMPContact"),
                        resultSet.getString("EMPBankAcNo"),
                        resultSet.getString("EMPBranch"),
                        resultSet.getString("EMPTitle")
                ));
            }
            tblEmp.setItems(list);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnEmpSearch(ActionEvent event) {
        String id = txtSearchEmp.getText();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String SQL = "SELECT * FROM Employer WHERE EMPID = '" + id + "'";
            ResultSet rst = stm.executeQuery(SQL);

            if (rst.next()) {
                Employer employer = new Employer(
                        rst.getString("EMPID"),
                        rst.getString("EMPName"),
                        rst.getString("EMPNic"),
                        rst.getString("EMPAddress"),
                        rst.getString("EMPDob"),
                        rst.getString("EMPContact"),
                        rst.getString("EMPBankAcNo"),
                        rst.getString("EMPBranch"),
                        rst.getString("EMPTitle")

                );
                txtEmpId.setText(employer.getEMPID());
                txtEmpName.setText(employer.getEMPName());
                txtEmpNic.setText(employer.getEMPNic());
                txtEmpAddress.setText(employer.getEMPAddress());
                dateEmpDob.getEditor().setText(employer.getEMPDob()); // Set the date in the DatePicker
                txtEmpContact.setText(employer.getEMPContact());
                txtBankAcNo.setText(employer.getEMPBankAcNo());
                txtBranch.setText(employer.getEMPBranch());
                selectEmpTitle.setValue(employer.getEMPTitle()); // Set the title
            } else {
                new Alert(Alert.AlertType.ERROR, "Employer not found").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
