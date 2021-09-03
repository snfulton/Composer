/*
 * File: Main.java
 * Author: djskrien
 * Date: 5/25/15
 */
package composer;

import composer.Command.Command;
import composer.Command.CommandWrapper;
import composer.Command.DiscreteCommand.*;
import composer.Manager.ClipboardManager;
import composer.Manager.CommandManager;
import composer.Manager.PaneManager;
import composer.MouseEventHandler.ReleaseEventHandler.ReleaseEventHandler;
import composer.MouseEventHandler.DragEventHandler.DragEventHandler;
import composer.MouseEventHandler.PressEventHandler.PanePressHandler;
import composer.MouseEventHandler.PressEventHandler.PressEventHandler;
import composer.MusicRectangle.MusicRectangle;
import composer.SaveLoad.FileSaver;
import composer.SaveLoad.FromXML;
import composer.SaveLoad.Loader;
import composer.SaveLoad.XMLLoader;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.*;
import java.util.*;
import java.util.List;
import javafx.geometry.Insets;

/**
 * This class starts up the Midi application for Project 2 and tests it.
 */
public class Main extends Application {

    //Radio Buttons and their Toggle Group
    @FXML
    private ToggleGroup togglegroup;
    @FXML
    private RadioButton inst_piano;
    @FXML
    private RadioButton inst_harpsicord;
    @FXML
    private RadioButton inst_marimba;
    @FXML
    private RadioButton inst_organ;
    @FXML
    private RadioButton inst_accordian;
    @FXML
    private RadioButton inst_guitar;
    @FXML
    private RadioButton inst_violin;
    @FXML
    private RadioButton inst_frenchhorn;
    @FXML
    private Button playButton, stopButton;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem stopMenuItem;
    @FXML
    private MenuItem playMenuItem;
    @FXML
    private MenuItem selectAllMenuItem;
    @FXML
    private MenuItem deleteAllMenuItem;
    @FXML
    private MenuItem groupMenuItem;
    @FXML
    private MenuItem ungroupMenuItem;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private Pane noteInputPane;

    //Objects for animation and playing

    private MidiPlayer player;

    //Data Structures for storing instruments, notes on the pane, and selected notes

    private List<Integer> instrumentList = Arrays.asList(0,6,12,20,21,26,40,60);
    private Boolean isPlaying;
    private PaneManager paneManager;
    private FileSaver fileSaver;
    private PressEventHandler pressEventHandler;
    private DragEventHandler dragEventHandler;
    private ReleaseEventHandler releaseEventHandler;
    private CommandManager commandManager;
    private ClipboardManager clipboardManager;
    private Stage primaryStage;


    /**
     * Loads the FXML files, calls setupRedLine and addCompBars, and shows the GUI to the user
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        player = new MidiPlayer(Constants.TICKS_PER_BEAT, Constants.BPM);
        isPlaying = false;
        commandManager = new CommandManager();
        this.clipboardManager = new ClipboardManager();
        clipboardManager.clearClipboard();

        //try to read in from FXML file
        BorderPane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Unable to load file \"Main.fxml\".\n" + e);
            System.exit(0);
        }


        //setup our Pane, redline, and dragRectangle
//        this.setUpDragRectangle();
        this.addCompBars();
        this.paneManager = new PaneManager(noteInputPane);
        paneManager.setupRedLine();
        pressEventHandler = new PanePressHandler(this.paneManager);

        //finish setting up our Stage and Scene
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.close();
            System.exit(0);
        });
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().addAll(this.getClass().getResource("Main.css").toExternalForm());
        primaryStage.setTitle("Music Composer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(Constants.NOTE_INPUT_PANE_HEIGHT / 2);
        primaryStage.setMinHeight(Constants.NOTE_INPUT_PANE_WIDTH / 2);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Adds horizontal gray bars to the pane
     */
    private void addCompBars() {
        for (int i = 0; i <= Constants.NUM_BARS; i++) {
            int xCord1 = 0;
            int xCord2 = 6000;
            int yCord = Constants.BAR_DISTANCE * i;
            Line toInsert = new Line(xCord1, yCord, xCord2, yCord);
            toInsert.setStroke(Color.GRAY);
            toInsert.setStrokeWidth(Constants.LINE_WIDTH);
            noteInputPane.getChildren().addAll(toInsert);
        }
    }


    /*
     *Is called when the pane is pressed by the mouse
     * It stops the player if it playing, and setups some booleans/data structures/objects that will be needed when we handle dragging and clicking
     */
    @FXML
    public void handleNotePaneMousePress(MouseEvent mouse) {
        System.out.println("pane press");
        if(isPlaying){
            this.handleStop();
            pressEventHandler.setWasPlaying(true);
        }
        pressEventHandler = paneManager.getPressEventHandler();
        pressEventHandler.handlePress(mouse);
        dragEventHandler = pressEventHandler.getDragEventHandler(mouse);
    }

    /**
     * This event is called when the mouse clicks the pane (so when it releases a drag, or presses down)
     * It handles a variety of cases, using booleans that have been set at other stages, to determine what the final
     * behavior on-screen should be
     * Lastly, it resets all the booleans/objects used in handling user-interactions back to their pre-user interaction values
     */
    @FXML
    public void handleNotePaneMouseClick(MouseEvent mouse) {
        System.out.println("pane click");
    }


//    /**
//     * Removes all notes from everywhere they are used so that they can be garbage collected
//     */
//    @FXML
//    public void deleteAllNoteRect() {
//        player.clear();
//        paneManager.clearAllCollections();
//    }

    /**
     * Handles all the logic having to do with dragging the mouse in the note selection pane
     * If the mouse is dragged on a note it moves all selected notes
     * If the mouse is dragged on the right edge of a note it elongates all selected notes
     * If the mouse is dragged on the note pane it creates a selection box
     * @param mouse: MouseEvent which corresponds to the mouse input
     */
    @FXML
    public void handleNotePaneMouseDrag(MouseEvent mouse) {
        dragEventHandler.handleDrag(mouse);
        pressEventHandler.setWasDragged(true);
    }



    /**
     * Handles updating every selected notes data after they have been moved.
     * @param mouse: MouseEvent which corresponds to the mouse input
     */
    @FXML
    public void handleNotePaneMouseRelease(MouseEvent mouse){
        releaseEventHandler = pressEventHandler.getReleaseEventHandler();
        int channel = Integer.parseInt((String)togglegroup.getSelectedToggle().getUserData()); //pull the channel number from the selected radio button
        Paint rectangleColor = ((RadioButton)togglegroup.getSelectedToggle()).getTextFill(); //pull the colors from the the radio buttons text

        Command newCommand = releaseEventHandler.handleRelease(mouse, channel, rectangleColor);
        commandManager.addCommand(newCommand);
        paneManager.resetPressHandler();
    }


    /*
    * FXML helper, Resets the animation and midi-player to its starting state then plays the composition.
    */
    @FXML
    public void handlePlay() {
            paneManager.stopRedLine();
            player.stop();
            player.clear();
            loadInstrumentsToPlayer();
            Iterator<MusicRectangle> it = paneManager.allIterator();
            while(it.hasNext()){
                System.out.println("adding");
                MusicRectangle toAdd = it.next();
                AddToPlayer addToPlayer = new AddToPlayer(player, toAdd);
                addToPlayer.execute();
            }
            System.out.println(paneManager.selSize());
            player.play();
            paneManager.animateTimeline();
            isPlaying = true;
    }


    /**
     * Adds all the rectangleNoteList to the midi-player from the rectangleNote ArrayList
     */

    /**
    * Loads all the instrument values to their respective channels in the player
    */
    private void loadInstrumentsToPlayer(){
        ListIterator<Integer> iterator = instrumentList.listIterator();
        while(iterator.hasNext()) {
            player.addProgramChange(iterator.nextIndex(), iterator.next(), 0, 0);
        }
    }


    /*
     * stops the movement of the redLine, stops the player, and removes the redLine from the pane if it is on there.
     * Sets the boolean isPlaying to false.
     */
    @FXML
    public void handleStop() {
            isPlaying = false;
            player.stop();
            paneManager.stopRedLine();
    }

    /*
     * Closing the scene closes the entire application
     */
    @FXML
    public void handleExit() {
        System.exit(0);
    }

    /*
     * Closing the scene closes the entire application
     */
    @FXML
    public void selectAll(){
        Iterator<MusicRectangle> it = paneManager.allIterator();
        Collection<MusicRectangle> toSel = new HashSet<>();
            while(it.hasNext()){
            toSel.add(it.next());
        }
        SelectCommand selectCommand = new SelectCommand(paneManager, toSel);
        selectCommand.execute();
        commandManager.addCommand(selectCommand);
    }

    /*
     * Closing the scene closes the entire application
     */
    @FXML
    public void deleteAll() {
        this.handleStop();
        Iterator<MusicRectangle> it = paneManager.allIterator();
        Collection<MusicRectangle> toDel = new HashSet<>();
        while(it.hasNext()){
            toDel.add(it.next());
        }
        DeleteCommand deleteCommand = new DeleteCommand(paneManager, toDel);
        deleteCommand.execute();
        commandManager.addCommand(deleteCommand);
    }

    @FXML
    public void deleteSelected() {
        this.handleStop();
        Iterator<MusicRectangle> it = paneManager.selIterator();
        Collection<MusicRectangle> toDel = new HashSet<>();
        while(it.hasNext()){
            toDel.add(it.next());
        }
        DeleteCommand deleteCommand = new DeleteCommand(paneManager, toDel);
        deleteCommand.execute();
        commandManager.addCommand(deleteCommand);
    }

    @FXML
    public void group() {
        if(! this.canGroup()){
            return;
        }
        Iterator<MusicRectangle> it = paneManager.selIterator();
        HashSet<MusicRectangle> toGroup = new HashSet<>();
        while(it.hasNext()){
            toGroup.add(it.next());
        }
        CommandWrapper commandWrapper = new CommandWrapper();
        GroupCommand groupCommand = new GroupCommand(paneManager, toGroup);
        groupCommand.execute();
        SelectCommand selectCommand = new SelectCommand(this.paneManager, groupCommand.getGesture());
        selectCommand.execute();
        commandWrapper.add(groupCommand);
        commandWrapper.add(selectCommand);
        commandManager.addCommand(commandWrapper);
    }

    @FXML
    public void ungroup(){
        if(! this.canUngroup()){
            return;
        }
        Iterator<MusicRectangle> it = paneManager.selIterator();
        MusicRectangle toUngroup = it.next();
        UngroupCommand ungroupCommand = new UngroupCommand(paneManager, toUngroup);
        ungroupCommand.execute();
        commandManager.addCommand(ungroupCommand);
    }

    public boolean canGroup(){
        return paneManager.selSize() > 1;
    }

    public boolean canUngroup(){
        return paneManager.selSize() == 1;
    }

    @FXML
    public void setDoAbilities() {
        if (commandManager.isUndoEmpty()) {
            undoMenuItem.setDisable(true);
        }
        else if (!commandManager.isUndoEmpty()) {
            undoMenuItem.setDisable(false);
        }
        if (commandManager.isRedoEmpty()) {
            redoMenuItem.setDisable(true);
        }
        else if (!commandManager.isRedoEmpty()) {
            redoMenuItem.setDisable(false);
        }
    }

    @FXML
    public void undo() {
        commandManager.undo();
    }

    @FXML
    public void redo() throws FromXML.XMLParsingException {
        commandManager.redo();
    }

    @FXML
    public void openAbout() throws FileNotFoundException {
        Stage stage = new Stage();
        //Reading the contents of a text file.
        InputStream inputStream = new FileInputStream("composer/AboutText.txt");
        Scanner sc = new Scanner(inputStream);
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(" "+sc.nextLine()+"\n");
        }
        //Creating a text object
        Text text = new Text(10.0, 25.0, sb.toString());
        text.setFont(Font.font(20));
        //Wrapping the text
        text.setWrappingWidth(585);
        Group root = new Group(text);
        Scene scene = new Scene(new Group(root), 595, 150, Color.WHITE);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void createNew(){
        deleteAll();
    }

    @FXML
    public void openComp(){

        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Invalid File");
        errorMessage.setContentText("The file you have tried to load is invalid. Please try another file.");

        Loader loader;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        while(true) {
            File fileToOpen = fileChooser.showOpenDialog(this.primaryStage);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            if (fileToOpen != null) {
                try {
                    loader = new XMLLoader(fileToOpen, this.paneManager);
                    this.deleteAll();
                    loader.load();
                    break;
                } catch (Exception e) {
                    errorMessage.showAndWait();
                }
            }
            else{
                break;
            }
        }
        commandManager.clear();
        Iterator<MusicRectangle> it = this.paneManager.allIterator();
        while(it.hasNext()){
            System.out.println(it.next().getClass());
        }
    return;
    }

    @FXML
    public void save(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Path");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML","*.xml"));
        File selectedSavePath = fileChooser.showSaveDialog(stage);
        if (selectedSavePath != null) {
            try {
                fileSaver = new FileSaver(selectedSavePath, paneManager);
                fileSaver.Save();
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
        System.out.println(selectedSavePath);
    }

    @FXML
    public void saveAs(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Path");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML","*.xml"));
        File selectedSavePath = fileChooser.showSaveDialog(stage);
        if (selectedSavePath != null) {
            try {
                fileSaver = new FileSaver(selectedSavePath, paneManager);
                fileSaver.Save();
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
        System.out.println(selectedSavePath);
    }

    @FXML
    public void cut() throws ParserConfigurationException, TransformerException {
        Iterator<MusicRectangle> it = paneManager.selIterator();
        HashSet<MusicRectangle> toCut = new HashSet<>();
        while(it.hasNext()){
            toCut.add(it.next());
        }
        //Copy
        this.copy();
        //Delete
        DeleteCommand deleteCommand = new DeleteCommand(paneManager, toCut);
        deleteCommand.execute();
        commandManager.addCommand(deleteCommand);
    }

    @FXML
    public void copy() throws ParserConfigurationException, TransformerException {
        Iterator<MusicRectangle> it = paneManager.selIterator();
        HashSet<MusicRectangle> toCopy = new HashSet<>();
        while(it.hasNext()){
            toCopy.add(it.next());
        }
        //Copy
        clipboardManager.addToClipboard(toCopy);
    }

    @FXML
    public void paste() throws IOException, SAXException, ParserConfigurationException, FromXML.XMLParsingException {
        Document clipboardContents = clipboardManager.getClipboardContent();
        if (clipboardContents != null) {
            PasteCommand pasteCommand = new PasteCommand(paneManager, clipboardContents);
            pasteCommand.execute();
            commandManager.addCommand(pasteCommand);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
