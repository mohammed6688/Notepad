import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;


public class MainClass extends Application{
    MenuItem newFile;
    MenuItem openFile;
    MenuItem saveFile;
    MenuItem exitFile;
    MenuItem undo;
    MenuItem cut;
    MenuItem copy;
    MenuItem past;
    MenuItem delete;
    MenuItem selectAll;
    MenuItem about;
    BorderPane pane;
    Menu file;
    Menu edit;
    Menu help;
    MenuBar menuBar;
    TextArea textField;

    @Override
    public void init() throws Exception {

        menuBar=new MenuBar();
        file=new Menu("File");
        edit=new Menu("Edit");
        help=new Menu("Help");

        newFile =new MenuItem("New",new ImageView(new Image(getClass().getResourceAsStream("plus_sign.png"))));
        openFile =new MenuItem("Open",new ImageView(new Image(getClass().getResourceAsStream("open_folder.png"))));
        saveFile =new MenuItem("Save",new ImageView(new Image(getClass().getResourceAsStream("diskette.png"))));
        exitFile =new MenuItem("Exit",new ImageView(new Image(getClass().getResourceAsStream("exit.png"))));

        newFile.setAccelerator(KeyCombination.keyCombination("Ctrl+n"));
        openFile.setAccelerator(KeyCombination.keyCombination("Ctrl+o"));
        saveFile.setAccelerator(KeyCombination.keyCombination("Ctrl+s"));
        exitFile.setAccelerator(KeyCombination.keyCombination("Ctrl+q"));


        undo =new MenuItem("Undo",new ImageView(new Image(getClass().getResourceAsStream("undo.png"))));
        cut =new MenuItem("Cut",new ImageView(new Image(getClass().getResourceAsStream("cut.png"))));
        copy =new MenuItem("Copy",new ImageView(new Image(getClass().getResourceAsStream("copy.png"))));
        past =new MenuItem("Past",new ImageView(new Image(getClass().getResourceAsStream("paste.png"))));
        delete =new MenuItem("Delete",new ImageView(new Image(getClass().getResourceAsStream("remove.png"))));
        selectAll =new MenuItem("Select All",new ImageView(new Image(getClass().getResourceAsStream("selection.png"))));
        about =new MenuItem("About Notepad",new ImageView(new Image(getClass().getResourceAsStream("about.png"))));

        file.getItems().addAll(newFile,openFile,saveFile,new SeparatorMenuItem(),exitFile);
        edit.getItems().addAll(undo,cut,copy,past,delete,new SeparatorMenuItem(),selectAll);
        help.getItems().addAll(about);

        textField=new TextArea();

        Text text=new Text("Copy right reserved for Mohammed Ashraf");

        menuBar.getMenus().addAll(file,edit,help);
        pane= new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(textField);
        pane.setBottom(text);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            primaryStage.setTitle("*FX NotBad");
        });

        cut.addEventHandler(ActionEvent.ACTION, e -> {
            textField.cut();
        });

        copy.addEventHandler(ActionEvent.ACTION, e -> {
            textField.copy();

        });

        undo.addEventHandler(ActionEvent.ACTION, e -> {
            textField.undo();
        });

        delete.addEventHandler(ActionEvent.ACTION, e -> {
            IndexRange i =textField.getSelection();
            textField.deleteText(i.getStart(),i.getEnd());
        });

        selectAll.addEventHandler(ActionEvent.ACTION, e -> {
            textField.selectAll();
        });

        past.addEventHandler(ActionEvent.ACTION, e -> {
            textField.paste();
        });

        exitFile.addEventHandler(ActionEvent.ACTION, e -> {
            primaryStage.close();
        });
        help.addEventHandler(ActionEvent.ACTION, e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("About");
            alert.setHeaderText("About Dialog");
            alert.setContentText("Developer name: Mohammed Ashraf");
            alert.showAndWait();
        });

        saveFile.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                saveFile(primaryStage);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            primaryStage.setTitle("FX NotBad");
        });

        openFile.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                openFile(primaryStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        newFile.addEventHandler(ActionEvent.ACTION, e -> {
            if (textField.getText().equals("")){

            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("FX NoteBad");
                alert.setHeaderText("Alert");
                alert.setContentText("Do you want to save changes?");

                ButtonType save = new ButtonType("Save");
                ButtonType dontSave = new ButtonType("Don't Save");
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(save, dontSave, cancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == save){
                    try {
                        saveFile(primaryStage);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (result.get() == dontSave) {
                    // ... user chose "Two"
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });

        Scene scene=new Scene(pane,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FX NotBad");
        primaryStage.show();
    }

    private void openFile(Stage mainStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            textField.clear();
            DataInputStream dataInputStream =new DataInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
            while (dataInputStream.available()>0){
                textField.appendText(dataInputStream.readUTF());
            }
        }
    }

    private void saveFile(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.txt"));
        File selectedFile =fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
            System.out.println(textField.getText());

            DataOutputStream dataOutputStream=new DataOutputStream(new FileOutputStream(selectedFile.getAbsolutePath()));
            dataOutputStream.writeUTF(textField.getText());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
