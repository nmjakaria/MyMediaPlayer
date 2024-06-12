package mymediaplayer;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author NM Jakaria
 */
public class FXMLDocumentController implements Initializable {
    
    private String path;
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider progressBar;
    @FXML
    private Slider volumeSlider;
    private List<String> extensions;
    
    public void openFileMethod(ActionEvent event){
        extensions= Arrays.asList("*.mp4","*.3gp","*.mkv","*.MP4","*.MKV","*.3GP","*.flv","*.wmv");
        FileChooser fileChooser = new FileChooser();   
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select file",extensions);
        try{
            fileChooser.getExtensionFilters().add(filter);
            fileChooser.setTitle("Select File to Open");
            File file = fileChooser.showOpenDialog(null);
            path = file.toURI().toString();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            DoubleProperty widthProperty = mediaView.fitWidthProperty();
            DoubleProperty heigthProperty = mediaView.fitHeightProperty();
            
            widthProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
            heigthProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    progressBar.setValue(newValue.toSeconds());
                }
            });
            
            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            });
            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            });
            
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });
            
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
            
            mediaPlayer.play();
        }
    }
    
    public void paly(ActionEvent event){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
    public void pause(ActionEvent event){
        mediaPlayer.pause();
    }
    public void stop(ActionEvent event){
        mediaPlayer.stop();
    }
    public void backForward(ActionEvent event){
        mediaPlayer.setRate(0.5);
    }
    public void fastForward(ActionEvent event){
        mediaPlayer.setRate(2.0);
    }
    public void skip10(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }
    public void back10(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }
    @FXML
    private void closeVideo(ActionEvent event){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

}
