package sample;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.*;
public class Main extends Application {
    private List<Note> notes= Arrays.asList(
            new Note("C",KeyCode.C,60),
            new Note("D",KeyCode.D,62),
            new Note("E",KeyCode.E,64),
            new Note("F",KeyCode.F,65),
            new Note("G",KeyCode.G,67),
            new Note("A",KeyCode.A,69),
            new Note("B",KeyCode.B,71),
            new Note("C",KeyCode.K,72),
            new Note("D",KeyCode.L,74),
            new Note("E",KeyCode.SEMICOLON,76),
            new Note("F",KeyCode.Q,78),
            new Note("G",KeyCode.W,80),
            new Note("A",KeyCode.R,82),
            new Note("B",KeyCode.T,84),
            new Note("C",KeyCode.Y,86)
    );

    private MidiChannel channel;
    private  HBox root=new HBox(15);
private Parent createContent(){
    loadChannel();


    root.setPrefSize(750,170);
    root.setBackground(new Background(new BackgroundFill(Color.BLACK,
            CornerRadii.EMPTY,null
            )));
notes.forEach(note -> {
    NoteView view=new NoteView(note);

    root.getChildren().addAll(view);

});

    return root;
}
private void loadChannel(){
    try{
        Synthesizer synth= MidiSystem.getSynthesizer();
    synth.open();
    synth.loadInstrument(synth.getDefaultSoundbank().getInstruments()[0]);
    channel=synth.getChannels()[0];
    }catch (MidiUnavailableException e){
        System.out.println("Cannot get synth");
        e.printStackTrace();
    }

}
    @Override
    public void start(Stage stage) throws Exception{
    Scene scene =new Scene(createContent());
    scene.setOnKeyPressed(e -> onKeyPress(e.getCode()));
        stage.setScene(scene);

        stage.show();
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 700, 575));
        //primaryStage.show();
    }
    private void onKeyPress(KeyCode key){
    root.getChildren()
            .stream()
            .map(view -> (NoteView) view)
            .filter(view -> view.note.key.equals(key))
            .forEach(view -> {
                FillTransition ft=new FillTransition(
                        Duration.seconds(0.17),
                        view.bg,
                        Color.WHITE,
                        Color.BLACK
                );
                ft.setCycleCount(2);
                ft.setAutoReverse(true);
                ft.play();
                channel.noteOn(view.note.number,150);
            });
    
    }

    private static class NoteView extends StackPane{
    private Note note;

private Rectangle bg=new Rectangle(50, 150, Color.WHITE);

    NoteView(Note note){

        this.note=note;

        bg.setStroke(Color.BLACK);
        getChildren().addAll(bg,new Text(note.name));

    }
    }










private static class Note{
    private String name;
    private KeyCode key;
    private int number;
    Note(String name, KeyCode key, int number){
        this.name=name;
        this.key=key;
        this.number=number;
    }
}

    public static void main(String[] args) {
      launch(args);
    }
}
