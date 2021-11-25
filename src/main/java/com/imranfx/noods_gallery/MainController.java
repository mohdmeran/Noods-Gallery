/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.imranfx.noods_gallery;

import com.imranfx.noods_gallery.database.mysql;
import com.imranfx.noods_gallery.model.MyImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author black
 */
public class MainController implements Initializable {
    
    @FXML
    private BorderPane container;
    
    @FXML
    private Button btn_add;

    @FXML
    private Button btn_search;
    
    @FXML
    private Button btn_addCap;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_save;
    
    @FXML
    private Button btn_cancel;

    @FXML
    private TextArea caption;
    
    @FXML
    private VBox v_caption;
    
    @FXML
    private HBox hbox_btns;

    @FXML
    private GridPane grid;

    @FXML
    private TextField input_search;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ImageView view_focusImage;
    
    
    private ImageController currentFocusController;
    private MyListener myListener;
    private List<MyImage> images = new ArrayList<>();
    private int column = 0, row = 0;
    private mysql db;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = new mysql();
        
        File directory = new File(".\\StoreImages");
        if (! directory.exists()){
            directory.mkdir();
        }
        
        this.bindProperty();
        this.checkActionButton();
        
        images.addAll(initData());
        
        myListener = (ImageController imageController) -> {
            changeFocus(imageController);
        };
        
        if(images.isEmpty()) return;

        ImageController imageController = this.loadImage(images.get(0));
        this.currentFocusController = imageController;
        this.changeFocus(imageController);
        
        for(int i = 1; i < images.size(); i++) {
            this.loadImage(images.get(i));
        }
    }
    
    public void changeFocus(ImageController focusImageController) {
        try {
            MyImage focusImage = focusImageController.getImg();
            Image image = crop(new Image(new FileInputStream(focusImage.getImgSrc())));
            view_focusImage.setImage(image);
            caption.setText(focusImage.getCaption());
            currentFocusController = focusImageController;
            this.checkActionButton();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private List<MyImage> initData() {
        File imagesDirectory = new File(".\\StoreImages");
        
        File[] storedImages = imagesDirectory.listFiles((File dir, String name) -> {
            return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png");
        });
        
        List<MyImage> images = new ArrayList<>();
        
        for(File storedImage : storedImages) {
            images.add(this.createMyImage(storedImage.getAbsolutePath()));
        }

        return images;
    }
    
    private MyImage createMyImage(String absPath) {
        MyImage img = new MyImage();
        img.setName(absPath.substring(absPath.lastIndexOf("\\") + 1));
        img.setCaption("");
        img.setImgSrc(absPath);
        
        return img;
    }
    
    private Image crop(Image img) {
        double d = Math.min(img.getWidth(),img.getHeight());
        double x = (d-img.getWidth())/2;
        double y = (d-img.getHeight())/2;
        Canvas canvas = new Canvas(d, d);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(img, x, y);
        return canvas.snapshot(null, null);
    }
    
    private void bindProperty() {
        
        hbox_btns.visibleProperty().bind(view_focusImage.visibleProperty());
        caption.visibleProperty().bind(view_focusImage.visibleProperty());
        btn_addCap.managedProperty().bind(btn_addCap.visibleProperty());
        btn_edit.managedProperty().bind(btn_edit.visibleProperty());
        btn_save.managedProperty().bind(btn_save.visibleProperty());
        v_caption.managedProperty().bind(v_caption.visibleProperty());
        btn_cancel.managedProperty().bind(btn_cancel.visibleProperty());
        btn_cancel.visibleProperty().bind(btn_save.visibleProperty());
        caption.editableProperty().bind(btn_save.visibleProperty());
    }
    
    private void captionAddAble() {
        v_caption.setVisible(false);
        btn_addCap.setVisible(true);
        btn_edit.setVisible(false);
        btn_save.setVisible(false);
    }
    
    private void editAble() {
        v_caption.setVisible(true);
        btn_addCap.setVisible(false);
        btn_edit.setVisible(true);
        btn_save.setVisible(false);
    }
    
    @FXML
    void isEdit(ActionEvent event) {
        v_caption.setVisible(true);
        btn_addCap.setVisible(false);
        btn_edit.setVisible(false);
        btn_save.setVisible(true);
    }
    
    @FXML
    void isCancel(ActionEvent event) {
        this.caption.setText(this.currentFocusController.getImg().getCaption());
        this.checkActionButton();
    }
    
    @FXML
    void saveCaption() {
        this.currentFocusController.updateCaption(caption.getText());
        this.checkActionButton();
    }
    
    @FXML
    void addImage() {
        Stage stage = (Stage) container.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jpg and png only", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);
        if(selectedFile == null) return;
        
        List<MyImage> newImages = new ArrayList<>();
        
        ImageController imageController = null;
        
        for(int i = 0; i < selectedFile.size(); i++) {
            File file = selectedFile.get(i);
            String path = file.getAbsolutePath();
            
            //this will add new images into images array list, right now is not needed but if need then uncomment it
            // newImages.add(this.createMyImage(path));
            
            //copy image to saved folder
            try {
                Path savedFolder = Paths.get(".\\StoreImages\\" + file.getName());
                Files.copy(file.toPath(), savedFolder);
            } catch(IOException ex) {
                continue;
            }
            
            imageController = this.loadImage(this.createMyImage(path));
        }
        this.changeFocus(imageController);
        
        this.images.addAll(newImages);
    }
    
    private ImageController loadImage(MyImage img) {
        ImageController imageController = null;
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("image.fxml"));

            AnchorPane anchorPane = fxmlLoader.load();

            imageController = fxmlLoader.getController();
            imageController.setData(img, myListener);

            if(column >= 3) {
                column = 0;
                row++;
            }

            // set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            // set grid heigth
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);

            grid.add(anchorPane, column++, row);

            GridPane.setMargin(anchorPane, new Insets(10));
            
        } catch (IOException ex) {
            System.out.println("FAIL BROTHER FAIL");
        }
        
        return imageController;
    }
   
    private void checkActionButton() {
        // check to show which button
        if(this.currentFocusController == null) {
            view_focusImage.setVisible(false);
            return;
        }
        
        view_focusImage.setVisible(true);
        
        if(this.currentFocusController.getImg().getCaption().isEmpty()) {
            this.captionAddAble();
            return;
        }
        
        this.editAble();
    }
    
    
}
