/**
 * Created by YaHeng on 2017-05-30.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;



public class Main extends Application
{

    @Override
    public void start(final Stage primaryStage) throws IOException
    {
        final File products = new File("./products.txt");
        final File sales = new File("./Sales.txt");
        BufferedReader inProducts = new BufferedReader(new InputStreamReader(new FileInputStream(products)));
        BufferedWriter out = new BufferedWriter(new FileWriter(sales));
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(12);
        gridPane.setVgap(12);
        gridPane.setPadding(new Insets(8,8,8,8));
        Scene scene = new Scene(gridPane, 980,720);
        primaryStage.setTitle("JA Sales Helper 1.0.00");
        primaryStage.setScene(scene);

        Button saveFileButton = new Button("Save Sales File");
        gridPane.add(saveFileButton,7,5);
        Button openFileButton = new Button("Open Sales File");
        gridPane.add(openFileButton,7,4);

        Button addSaleButton = new Button("Add Sale");
        gridPane.add(addSaleButton,8,5);


        Label Product = new Label("Product: ");
        gridPane.add(Product,0,1);
        Button addProductButton = new Button("Add Product");
        gridPane.add(addProductButton,2,1);
        final ArrayList<Product> productsList = new ArrayList<Product>();
        final HashMap<String,Double> productMap = new HashMap<String, Double>();
        ArrayList<String> productsNames = new ArrayList<String>();
        String line;
        while ((line = inProducts.readLine()) != null)
        {
            String[] parts = line.split(" ");
            String productName = parts[0];
            double productPrice = Double.parseDouble(parts[1]);

            Product toAdd = new Product(productName,productPrice);

            productsList.add(toAdd);
            productMap.put(productName,productPrice);
            productsNames.add(productName);
        }

        final ChoiceBox Products = new ChoiceBox();
        Products.getItems().addAll(productsNames);
        gridPane.add(Products,1,1);
        Label methodPaid = new Label("Method Paid: ");
        gridPane.add(methodPaid, 0, 3);

        Label quantity = new Label("Quantity: ");
        gridPane.add(quantity,0,2);
        final TextField quantityTextField = new TextField();
        gridPane.add(quantityTextField,1,2);


        ToggleGroup methodPaidButtons = new ToggleGroup();
        final RadioButton methodPaidCredit = new RadioButton("Credit Card");
        final RadioButton methodPaidDebit = new RadioButton("Debit Card");
        final RadioButton methodPaidCash = new RadioButton("Cash");
        methodPaidCash.setToggleGroup(methodPaidButtons);
        methodPaidCredit.setToggleGroup(methodPaidButtons);
        methodPaidDebit.setToggleGroup(methodPaidButtons);
        gridPane.add(methodPaidCash,1,3);
        gridPane.add(methodPaidCredit,2,3);
        gridPane.add(methodPaidDebit,3,3);


        Label isThisAnOrder = new Label("Is this an order? ");
        gridPane.add(isThisAnOrder,0,4);
        final ToggleGroup isOrderToggle = new ToggleGroup();
        final RadioButton isOrderTrue = new RadioButton("Yes");
        gridPane.add(isOrderTrue,1,4);
        final RadioButton isOrderFalse = new RadioButton("No");
        gridPane.add(isOrderFalse,2,4);
        isOrderFalse.setToggleGroup(isOrderToggle);
        isOrderTrue.setToggleGroup(isOrderToggle);


        Label Date = new Label("Date: (HH:mm:ss DD/MM/YYYY)");
        gridPane.add(Date,0,5);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        java.util.Date date = new Date();
        Text dateText = new Text(dateFormat.format(date));
        gridPane.add(dateText,1,5);
        System.out.println(dateFormat.format(date));


        Label customerName = new Label("Customer Name: ");
        gridPane.add(customerName,0,6);
        final TextField customerNameTextField = new TextField();
        //String customerNameString = customerNameTextField.getText();
        gridPane.add(customerNameTextField,1,6);


        Label customerEmail = new Label("Customer Email (if order): ");
        gridPane.add(customerEmail,0,7);
        final TextField customerEmailTextField = new TextField();
        gridPane.add(customerEmailTextField,1,7);


        Label customerPhone = new Label("Customer Phone Number (if order): ");
        gridPane.add(customerPhone,0,8);
        final TextField customerPhoneTextField = new TextField();
        gridPane.add(customerPhoneTextField,1,8);


        primaryStage.show();



        saveFileButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                File dest = fileChooser.showSaveDialog(primaryStage);

            }
        });

        final ArrayList<Sale> tracker = new ArrayList<Sale>();

        addSaleButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String customerName = customerNameTextField.getText();
                String customerEmail = customerEmailTextField.getText();
                String customerPhone = customerPhoneTextField.getText();
                Customer newCustomer = new Customer(customerName,customerPhone,customerEmail);
                Product product = new Product(Products.getSelectionModel().getSelectedItem().toString(), productMap.get(Products.getSelectionModel().getSelectedItem().toString())) ;
                int quantity = Integer.parseInt(quantityTextField.getText());
                Sale newSale = new Sale(product,quantity,newCustomer);
                tracker.add(newSale);
                DateFormat productDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                Date productDate = new Date();
                String date = productDateFormat.format(productDate);
                String toggled = "";
                String methodPaid = "";


                if (isOrderFalse.isSelected())
                    toggled = "N";
                else if (isOrderTrue.isSelected())
                    toggled = "Y";

                if (methodPaidCash.isSelected())
                    methodPaid = "Cash";
                else if (methodPaidCredit.isSelected())
                    methodPaid = "Credit";
                else if (methodPaidDebit.isSelected())
                    methodPaid = "Debit";


                try
                {
                    BufferedWriter out = new BufferedWriter(new FileWriter(sales,true));
                    System.out.println(date + " " + newSale.getProduct().getName() + " " + newSale.getProduct().getCost() + " "
                            + newSale.getQuantity() + " " + toggled + " " + methodPaid + " " + newSale.getRevenue() + " "   + newSale.getCustomer().getName()
                            + " " + newSale.getCustomer().getEmail() + " " + newSale.getCustomer().getPhoneNumber()
                    );
                    out.write(date + " " + newSale.getProduct().getName() + " " + newSale.getProduct().getCost() + " "
                    + newSale.getQuantity() + " " + toggled + " " + methodPaid + " " + newSale.getRevenue() + " "   + newSale.getCustomer().getName()
                    + " " + newSale.getCustomer().getEmail() + " " + newSale.getCustomer().getPhoneNumber()
                    );
                    out.newLine();
                    out.close();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
        });

        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                final Stage newProduct = new Stage();
                GridPane newProductPane = new GridPane();
                newProductPane.setAlignment(Pos.CENTER);
                newProductPane.setPadding(new Insets(6,6,6,6));
                newProductPane.setHgap(4);
                newProductPane.setVgap(4);
                Scene newProductScene = new Scene(newProductPane, 400,400);

                Label productName = new Label("Product Name: ");
                newProductPane.add(productName,0,1);
                final TextField productNameField = new TextField();
                newProductPane.add(productNameField,1,1);

                Label productPrice = new Label("Product Sales Price: ");
                newProductPane.add(productPrice,0,2);
                final TextField productPriceField = new TextField();
                newProductPane.add(productPriceField,1,2);

                final Button addProduct = new Button("Add Product");
                newProductPane.add(addProduct,0,3);
                final Button cancelButton = new Button("Cancel");
                newProductPane.add(cancelButton,0,4);
                newProduct.setTitle("Add Product");
                newProduct.setScene(newProductScene);

                addProduct.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        String name = productNameField.getText();
                        double price = Double.parseDouble(productPriceField.getText());
                        Product newProduct = new Product(name,price);
                        try{
                            BufferedReader in2 = new BufferedReader(new FileReader(products));
                            BufferedWriter out1 = new BufferedWriter(new FileWriter(products,true));
                            out1.write(newProduct.getName() + " " + newProduct.getCost());
                            out1.newLine();
                            out1.close();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        System.out.println("New Product Added");
                        Stage temp = (Stage) addProduct.getScene().getWindow();
                        temp.close();

                    }
                });

                cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        Stage temp =(Stage) cancelButton.getScene().getWindow();
                        temp.close();
                    }
                });

                newProduct.show();
            }
        });



    }


    public static void main(String[] args) throws IOException
    {
        launch(args);
    }
}
