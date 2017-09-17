package application;
	
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	
	private File selectedFolder = null;
	private String fileName = "books";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Button buttonBrowse = new Button("Browse");
			Button buttonConvert = new Button("Convert");
			HBox hBox = new HBox();
			hBox.getChildren().add(buttonBrowse);
			hBox.getChildren().add(buttonConvert);
			buttonBrowse.setOnAction(e -> browse());
			buttonConvert.setOnAction(e -> {
				try {
					convert();
				} catch (ConfigurationException | SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			hBox.setAlignment(Pos.CENTER);
			root.setCenter(hBox);
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("/resources/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void browse() {
		System.out.println("Browse");
		JFrame parentFrame = new JFrame();
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setDialogTitle("Specify a folder destination");   
    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	//int userSelection = fileChooser.showSaveDialog(parentFrame);
    	int userSelection = fileChooser.showDialog(parentFrame, "Choose");
    	if (userSelection == JFileChooser.APPROVE_OPTION) {
    		System.out.println("DA");
    		selectedFolder = fileChooser.getSelectedFile();
    	}
	}
	
	public void convert() throws ConfigurationException, SAXException {
		System.out.println("Convert");
		if (selectedFolder != null) {
			StreamSource xmlSource = new StreamSource(getClass().getResourceAsStream("/resources/listofbooks.xml"));
			String filePath = selectedFolder + "\\" + fileName + ".pdf";
			System.out.println(filePath);
			ConvertDatabaseToPDF.FOPConvert(xmlSource, filePath);
		} else {
			System.out.println("Please choose your folder destination first");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
