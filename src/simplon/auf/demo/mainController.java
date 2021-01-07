/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplon.auf.demo;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author LENOVO
 */
public class mainController implements Initializable {
    
   
    @FXML
    private TextField inputId;
    @FXML
    private TextField inputTitle;
    @FXML
    private TextField inputAuthor;
    @FXML
    private TextField inputYear;
    @FXML
    private TextField inputPages;
    @FXML
    private TableView<Books> tableViewBooks;
    @FXML
    private TableColumn<Books, Integer> colId;
    @FXML
    private TableColumn<Books, String> colTitle;
    @FXML
    private TableColumn<Books, String> colAuthor;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private TableColumn<Books, Integer> colPages;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnInsert){
            insertRecord();
        }else if(event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteRecord();
        }

    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showBooks();
    }    
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
            System.out.println(conn);
            return conn;
        }catch(SQLException ex){
            System.out.println("Error: "+ ex);
            return null;
        }
    }
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM books";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()){
                books = new Books(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("year"), rs.getInt("pages"));
                bookList.add(books);
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }
        return bookList;
    }
    public void showBooks() {
        ObservableList<Books> list = getBooksList();
        colId.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));
        
        tableViewBooks.setItems(list);

    }
    private void insertRecord(){
        String query = "INSERT INTO books(title, author, year, pages) VALUES ('" + inputTitle.getText() + "','" + inputAuthor.getText() + "'," + inputYear.getText() + "," + inputPages.getText() + ")" ;
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
        String query = "UPDATE books SET title = '" + inputTitle.getText() +"' , author = '" + inputAuthor.getText() +"' , year =" + inputYear.getText() + ", pages =" + inputPages.getText() + "  WHERE id =" + inputId.getText() ;
        executeQuery(query); 
        showBooks();
    }
    private void deleteRecord(){
        String query = "DELETE FROM books WHERE id = " + inputId.getText()  ; 
        executeQuery(query);
        showBooks();
    }
    private void executeQuery(String query){
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }

    // @FXML
    //private void handleButtonAction(MouseEvent event) {
    //        Books book = tableViewBooks.getSelectionModel().getSelectedItem();
    //        inputId.setText(""+book.getId());
    //        inputTitle.setText(book.getTitle());
    //        inputAuthor.setText(book.getAuthor());
    //        inputYear.setText(""+book.getYear());
    //        inputPages.setText(""+book.getPages()); 
    //}
}
