package edu.software_testing;

import edu.software_testing.data_base.StaffDB;
import edu.software_testing.model.Department;
import edu.software_testing.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@SuppressWarnings("unchecked")
public class SceneController implements Initializable{

    @FXML
    private TextField nameTF;
    @FXML
    private TextField salaryTF;
    @FXML
    private DatePicker hireDP;
    @FXML
    private TextField searchTF;
    @FXML
    private TextField departmentNameTF;
    @FXML
    private Button addB;
    @FXML
    private Button addDepartmentB;
    @FXML
    private Button removeB;
    @FXML
    private Button removeDepartmentB;
    @FXML
    private Button searchB;
    @FXML
    private Button resetB;
    @FXML
    private TextArea logArea;
    @FXML
    private TableView staffView;
    @FXML
    private TableView departmentsView;

    private TableColumn IDcol;
    private TableColumn<Employee, String> nameCol;
    private TableColumn<Employee, Integer> salaryCol;
    private TableColumn hireDateCol;
    private TableColumn<Employee, Department> departmentCol;

    @FXML
    private ChoiceBox departmentBox;

    @FXML
    private void addButtonHandler() {

        if(!isAddFormValid()) {
            return;
        }
        String eName = nameTF.getText();
        int eSalary = Integer.parseInt(salaryTF.getText());
        LocalDate eHireDate = hireDP.getValue();

        Department eDepartment = (Department) departmentBox.getValue();
        Employee newEmployee = new Employee(eName, eSalary, eHireDate, eDepartment);

        StaffDB.add(newEmployee);
        staffView.setItems(StaffDB.getStaff());
    }
    @FXML
    private void removeButtonHandler() {
        if(StaffDB.getStaff().size() == 0) {
            logArea.appendText("Nothing to remove.\n");
            return;
        }
        ObservableList<Employee> toRemove = staffView.getSelectionModel().getSelectedItems();
        StaffDB.remove(toRemove);
        staffView.setItems(StaffDB.getStaff()/*staff*/);
        staffView.getSelectionModel().clearSelection();
        removeB.setDisable(true);
    }
    @FXML
    private void searchButtonHandler() {
        if(searchTF.getText().isEmpty()) {
            logArea.appendText("Enter a search query.\n");
            return;
        }
        resetB.setDisable(false);
        String query = searchTF.getText();
        staffView.setItems(search(query));
    }
    @FXML
    private void resetButtonHandler() {
        resetB.setDisable(true);
        staffView.setItems(StaffDB.getStaff());
    }

    private ObservableList<Employee> search(String query) {
        ObservableList<Employee> result = FXCollections.observableArrayList();
        for(Employee e: StaffDB.getStaff()) {
            if(query.equals("ID "+e.getId())) {
                result.add(e);
                continue;
            }
            if(e.getName().contains(query)) {
                result.add(e);
                continue;
            }
            if(e.getHireDate().toString().contains(query)) {
                result.add(e);
                continue;
            }
            if(e.getDepartment().toString().contains(query)) {
                result.add(e);
            }
        }
        return result;
    }

    private boolean isAddFormValid() {

        //name
        if(nameTF.getText().isEmpty()) {
            logArea.appendText("Enter name.\n");
            return false;
        }
        else if(!nameTF.getText().matches("[A-Za-z]+")) {
            logArea.appendText("Name must contain only english letters.\n");
            return false;
        }
        else if(nameTF.getText().length() > 45) {
            logArea.appendText("Name is too long.\n");
            return false;
        }
        //salary
        if(salaryTF.getText().isEmpty()) {
            logArea.appendText("Enter salary.\n");
            return false;
        }
        else if(!salaryTF.getText().matches("^[1-9]\\d*$")) {
            logArea.appendText("Salary must be positive integer.\n");
            return false;
        }
        else if (salaryTF.getText().length() > 5) {
            logArea.appendText("Salary is too big.\n");
            return false;
        }
        //hire date
        if(hireDP.getValue() == null) {
            logArea.appendText("Enter date.\n");
            return false;

        }
        //department
        if(departmentBox.getValue() == null) {
            logArea.appendText("Select department.\n");
            return false;
        }
        return true;
    }

    private boolean isAddDepartmentFormValid() {
        if(departmentNameTF.getText().isEmpty()) {
            logArea.appendText("Enter department name.\n");
            return false;
        }
        else if(!departmentNameTF.getText().matches("[A-Za-z]+")) {
            logArea.appendText("Department name must contain only english letters.\n");
            return false;
        }
        else if(departmentNameTF.getText().length() > 45) {
            logArea.appendText("Department name is too long.\n");
            return false;
        }

        return true;
    }

    @FXML
    private void addDepartmentButtonHandler() {
        if(!isAddDepartmentFormValid()) {
            return;
        }
        String dName = departmentNameTF.getText();
        Department newDepartment = new Department(dName);
        StaffDB.addDepartment(newDepartment);
        departmentsView.setItems(StaffDB.getDepartments());
        updateDepartmentsDisplay();
    }
    @FXML
    private void removeDepartmentButtonHandler() {
        if(StaffDB.getDepartments().size() == 0) {
            logArea.appendText("Nothing to remove.\n");
            return;
        }
        ObservableList<Department> toRemove = departmentsView.getSelectionModel().getSelectedItems();
        StaffDB.removeDepartment(toRemove);
        departmentsView.setItems(StaffDB.getDepartments());
        departmentsView.getSelectionModel().clearSelection();

        staffView.setItems(StaffDB.getStaff());
        removeDepartmentB.setDisable(true);

        /*departmentBox.setItems(StaffDB.getDepartments());
        if(departmentBox.getItems().size() > 0)
            departmentBox.setValue(departmentBox.getItems().get(0));
        departmentCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(StaffDB.getDepartments()));*/
        updateDepartmentsDisplay();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StaffDB.createSession();

        initStaffViewColumns();
        initDepartmensViewColumns();

        if(StaffDB.getStaff().size() == 0)
            fillDB();

        staffView.setItems(StaffDB.getStaff());
        departmentsView.setItems(StaffDB.getDepartments());
        updateDepartmentsDisplay();

        removeB.setDisable(true);
        removeDepartmentB.setDisable(true);
        resetB.setDisable(true);
    }

    private void initStaffViewColumns() {
        //Staff table view
        IDcol = (TableColumn) staffView.getColumns().get(0);
        IDcol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));

        nameCol = (TableColumn) staffView.getColumns().get(1);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setOnEditCommit(t -> {

            if(t.getNewValue().isEmpty() || !t.getNewValue().matches("[A-Za-z]+") || t.getNewValue().length() > 45) {
                logArea.appendText("Name is invalid.\n");
                t.getTableColumn().setVisible(false);
                t.getTableColumn().setVisible(true);
            }
            else {
                StaffDB.getSession().beginTransaction();
                t.getRowValue().setName(t.getNewValue());
                StaffDB.getSession().getTransaction().commit();
            }
        });

        salaryCol = (TableColumn) staffView.getColumns().get(2);
        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                if (object == null) {
                    return "" ;
                }
                else {
                    return object.toString() ;
                }
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.valueOf(string);
                }
                catch (NumberFormatException e) {
                    return null ;
                }
            }
        }));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setOnEditCommit(t -> {

            if(t.getNewValue() == null || t.getNewValue().toString().length() > 5) {
                logArea.appendText("Salary is invalid.\n");
                t.getTableColumn().setVisible(false);
                t.getTableColumn().setVisible(true);
            }
            else {
                StaffDB.getSession().beginTransaction();
                t.getRowValue().setSalary(t.getNewValue());
                StaffDB.getSession().getTransaction().commit();
            }
        });

        hireDateCol = (TableColumn) staffView.getColumns().get(3);
        hireDateCol.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("hireDate"));

        departmentCol = (TableColumn) staffView.getColumns().get(4);
        departmentCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(StaffDB.getDepartments()));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        departmentCol.setOnEditCommit(t -> {
            StaffDB.getSession().beginTransaction();
            t.getRowValue().setDepartment(t.getNewValue());
            StaffDB.getSession().getTransaction().commit();
        });

        staffView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        staffView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    removeB.setDisable(false);
                });
    }

    private void initDepartmensViewColumns() {
        // Departments table view
        TableColumn depIDCol = (TableColumn) departmentsView.getColumns().get(0);
        depIDCol.setCellValueFactory(new PropertyValueFactory<Department, Integer>("depId"));

        TableColumn<Department, String> depNameCol = (TableColumn) departmentsView.getColumns().get(1);
        depNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        depNameCol.setCellValueFactory(new PropertyValueFactory<>("depName"));
        depNameCol.setOnEditCommit(t -> {

            if(t.getNewValue().isEmpty() || !t.getNewValue().matches("[A-Za-z]+") || t.getNewValue().length() > 45) {
                logArea.appendText("Department name is invalid.\n");
                t.getTableColumn().setVisible(false);
                t.getTableColumn().setVisible(true);
            }
            else {
                StaffDB.getSession().beginTransaction();
                t.getRowValue().setDepName(t.getNewValue());
                StaffDB.getSession().getTransaction().commit();
                staffView.getItems().clear();
                staffView.setItems(StaffDB.getStaff());
                updateDepartmentsDisplay();
            }
        });

        departmentsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        departmentsView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    removeDepartmentB.setDisable(false);
                });
    }

    private void updateDepartmentsDisplay() {
        departmentBox.getItems().clear();
        departmentBox.setItems(StaffDB.getDepartments());
        if(departmentBox.getItems().size() > 0)
            departmentBox.setValue(departmentBox.getItems().get(0));
        departmentCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(StaffDB.getDepartments()));
    }

    private void fillDB() {

        StaffDB.addDepartment(new Department("IT"));
        StaffDB.addDepartment(new Department("Sales"));
        StaffDB.addDepartment(new Department("Import"));
        StaffDB.addDepartment(new Department("Export"));

        StaffDB.add(new Employee("Max", 4000, LocalDate.of(2017, 12, 30), StaffDB.getDepartments().get(0)));
        StaffDB.add(new Employee("Michael", 4200, LocalDate.of(2016, 10, 5), StaffDB.getDepartments().get(1)));
        StaffDB.add(new Employee("Helen", 7300, LocalDate.of(2017, 5, 1), StaffDB.getDepartments().get(2)));
        StaffDB.add(new Employee("Jack", 5900, LocalDate.of(2008, 3, 17), StaffDB.getDepartments().get(3)));
        StaffDB.add(new Employee("Nick", 3400, LocalDate.of(2005, 10, 5), StaffDB.getDepartments().get(0)));

    }
}
