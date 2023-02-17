package com.example.juego_fx_m03;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    private Pane root;
    private ImageView avion;
    private ArrayList<ImageView> objetos;
    private Timeline timeline;
    private Random random;
    private int puntuacion;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Creamos la imagen del avión y la añadimos a la raíz
        avion = new ImageView(new Image(getClass().getResourceAsStream("assets/avion.png")));
        avion.setFitWidth(50);
        avion.setPreserveRatio(true);
        avion.relocate(50, scene.getHeight() / 2 - avion.getBoundsInLocal().getHeight() / 2);
        root.getChildren().add(avion);

        // Inicializamos el ArrayList de objetos
        objetos = new ArrayList<>();

        // Inicializamos el objeto Random
        random = new Random();

        // Creamos la línea de la puntuación
        Label labelPuntuacion = new Label("Puntuación: " + puntuacion);
        labelPuntuacion.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        labelPuntuacion.setTextFill(Color.WHITE);
        labelPuntuacion.relocate(10, 10);
        root.getChildren().add(labelPuntuacion);

        // Creamos el Timeline para generar objetos cada 2 segundos
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            ImageView objeto = crearObjeto();
            objetos.add(objeto);
            root.getChildren().add(objeto);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Creamos el bucle del juego
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) throws Exception {
                // Movemos los objetos hacia la izquierda
                for (ImageView objeto : objetos) {
                    objeto.relocate(objeto.getLayoutX() - 5, objeto.getLayoutY());

                    // Detectamos colisiones entre el avión y los objetos
                    if (objeto.getBoundsInParent().intersects(avion.getBoundsInParent())) {
                        gameOver();
                        stop();
                    }

                    // Eliminamos los objetos que salen de la pantalla
                    if (objeto.getLayoutX() < -objeto.getBoundsInLocal().getWidth()) {
                        root.getChildren().remove(objeto);
                        objetos.remove(objeto);
                    }
                }

                // Actualizamos la puntuación
                puntuacion++;
                labelPuntuacion.setText("Puntuación: " + puntuacion);
            }
        };
        gameLoop.start();

        // Permitimos mover el avión con las flechas del teclado
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if (avion.getLayoutY() > 0) {
                        avion.relocate(avion.getLayoutX(), avion.getLayoutY() - 10);
                    }
                    break;
                case DOWN:
                    if (avion.getLayoutY() < scene.getHeight() - avion.getBoundsInLocal().getHeight()) {
                        avion.relocate(avion.getLayoutX(), avion.getLayoutY() + 10);
                    }
                    break;
                case LEFT:
                    if (avion.getLayoutX() > 0) {
                        avion.relocate(avion.getLayoutX() - 10, avion.getLayoutY());
                    }
                    break;
                if (avion.getLayoutX() < scene.getWidth() - avion.getBoundsInLocal().getWidth()) {
                    avion.relocate(avion.getLayoutX() + 10, avion.getLayoutY());
                }
                break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para crear un nuevo objeto aleatorio
    private ImageView crearObjeto() {
        int tipo = random.nextInt(3) + 1;

        Image objetoImagen = new Image(getClass().getResourceAsStream("assets/objeto" + tipo + ".png"));
        ImageView objeto = new ImageView(objetoImagen);
        objeto.setFitWidth(50);
        objeto.setPreserveRatio(true);
        objeto.relocate(800, random.nextInt(550));

        return objeto;
    }

    // Método que se llama cuando el juego ha terminado
    private void gameOver() {
        timeline.stop();
        for (ImageView objeto : objetos) {
            root.getChildren().remove(objeto);
        }

        // Creamos la ventana de Game Over
        Label labelGameOver = new Label("GAME OVER");
        labelGameOver.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        labelGameOver.setTextFill(Color.WHITE);
        labelGameOver.relocate(200, 250);
        root.getChildren().add(labelGameOver);

        // Creamos el botón de Reiniciar
        Button botonReiniciar = new Button("Reiniciar");
        botonReiniciar.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        botonReiniciar.relocate(350, 350);
        botonReiniciar.setOnAction(event -> {
            root.getChildren().removeAll(labelGameOver, botonReiniciar);

            // Reiniciamos el juego
            puntuacion = 0;
            Label labelPuntuacion = new Label("Puntuación: " + puntuacion);
            labelPuntuacion.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            labelPuntuacion.setTextFill(Color.WHITE);
            labelPuntuacion.relocate(10, 10);
            root.getChildren().add(labelPuntuacion);

            timeline.play();
        });
        root.getChildren().add(botonReiniciar);
    }

    public static void main(String[] args) {
        launch(args);
    }


}