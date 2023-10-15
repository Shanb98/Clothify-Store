package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    void employeeBtn(ActionEvent event) {
        URL resource = this.getClass().getResource("/view/DashEmployer.fxml");
        assert resource != null;
        Parent load;
        try {
            load = FXMLLoader.load(resource);
            rootPane.getChildren().clear();
            rootPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void itemBtn(ActionEvent event) {
        URL resource = this.getClass().getResource("/view/DashItem.fxml");
        assert resource != null;
        Parent load;
        try {
            load = FXMLLoader.load(resource);
            rootPane.getChildren().clear();
            rootPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void orderBtn(ActionEvent event) {
        URL resource = this.getClass().getResource("/view/DashOrders.fxml");
        assert resource != null;
        Parent load;
        try {
            load = FXMLLoader.load(resource);
            rootPane.getChildren().clear();
            rootPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void orderDetailsBtn(ActionEvent event) {

    }

    @FXML
    void salesReportBtn(ActionEvent event) {

    }

    @FXML
    void salesReturnBtn(ActionEvent event) {

    }

    @FXML
    void supplierBtn(ActionEvent event) {
        URL resource = this.getClass().getResource("/view/DashSupplies.fxml");
        assert resource != null;
        Parent load;
        try {
            load = FXMLLoader.load(resource);
            rootPane.getChildren().clear();
            rootPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashBoard();

    }

    public void loadDashBoard() {
        URL resource = this.getClass().getResource("/view/DashSummery.fxml");
        assert resource != null;
        Parent load;
        try {
            load = FXMLLoader.load(resource);
            rootPane.getChildren().clear();
            rootPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
