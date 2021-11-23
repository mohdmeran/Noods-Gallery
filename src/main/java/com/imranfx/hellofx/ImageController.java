/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.imranfx.hellofx;

import com.imranfx.hellofx.model.MyImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author black
 */
public class ImageController {
    
    @FXML
    private AnchorPane img_card;

    @FXML
    private ImageView img_love;

    @FXML
    private ImageView img_picture;
    
    @FXML
    void click(MouseEvent event) {
        myListener.onClickListener(this);
    }
    
    private MyListener myListener;
    private MyImage img;

    public MyImage getImg() {
        return img;
    }
    
    public void setData(MyImage img, MyListener myListener) {
        try {
            this.img = img;
            this.myListener = myListener;
            
            Image image = crop(new Image(new FileInputStream(img.getImgSrc())));
            img_picture.setImage(image);
            
            updateCaption();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void updateCaption() {
        if(img.getCaption().isEmpty()) {
            img_love.setVisible(false);
            return;
        }
        
        img_love.setVisible(true);
    }
    
    protected void updateCaption(String newCap) {
        img.setCaption(newCap);
        this.updateCaption();
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
    
}
