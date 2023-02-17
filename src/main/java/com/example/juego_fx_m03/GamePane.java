package com.example.juego_fx_m03;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePane extends Pane {

    private ImageView avion;
    private double avionX;
    private double avionY;
    private double avionSpeed;

    private List<ImageView> objetos;
    private double objetosSpeed;

    public GamePane() {
        // Cargamos la imagen del avión
        Image avionImage = new Image(getClass().getResourceAsStream("assets/avion.png"));
        avion = new ImageView(avionImage);

        // Establecemos la posición y velocidad inicial del avión
        avionX = 50;
        avionY = 300;
        avionSpeed = 5;

        // Añadimos el avión al panel de juego
        getChildren().add(avion);

        // Creamos una lista para los objetos que se mueven
        objetos = new ArrayList<>();

        // Establecemos la velocidad inicial de los objetos que se mueven
        objetosSpeed = 3;

        // Creamos un nuevo objeto cada 2 segundos
        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 2_000_000_000) {
                    // Creamos un nuevo objeto
                    ImageView objeto = crearObjeto();

                    // Añadimos el objeto a la lista y al panel de juego
                    objetos.add(objeto);
                    getChildren().add(objeto);

                    // Actualizamos el tiempo del último objeto creado
                    lastUpdate = now;
                }

                // Movemos los objetos hacia la izquierda
                for (ImageView objeto : objetos) {
                    objeto.setX(objeto.getX() - objetosSpeed);

                    // Si el objeto choca con el avión, mostramos un mensaje de Game Over
                    if (objeto.getBoundsInParent().intersects(avion.getBoundsInParent())) {
                        System.out.println("Game Over!");
                    }
                }
            }
        }.start();

        // Añadimos un EventHandler para detectar cuando se presiona una tecla
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                avionY -= avionSpeed;
            } else if (event.getCode() == KeyCode.DOWN) {
                avionY += avionSpeed;
            } else if (event.getCode() == KeyCode.LEFT) {
                avionX -= avionSpeed;
            } else if (event.getCode() == KeyCode.RIGHT) {
                avionX += avionSpeed;
            }

            // Actualizamos la posición del avión
            avion.relocate(avionX, avionY);
        });

        // Hacemos que el panel de juego tenga el foco para que pueda recibir eventos de teclado
        setFocusTraversable(true);
    }

    // Método para crear un nuevo objeto aleatorio
    private ImageView crearObjeto() {
        Random random = new Random();
        int tipo = random.nextInt(3) + 1;

        Image objetoImage = null;

        switch (tipo) {
            case 1:
                objetoImage = new Image(getClass().getResourceAsStream("assets/objeto1.png"));
                break;
            case 2:
                objetoImage = new Image(getClass().getResourceAsStream("assets/objeto2.png"));
                break;
            case 3:
                objetoImage = new Image(getClass().getResourceAsStream("assets/objeto3.png"));
                break;
        }

        ImageView objeto = new ImageView(objetoImage);
        objeto.setFitWidth(50);
        objeto.setPreserveRatio(true);

        // Establecemos una posición aleatoria para el objeto
        double minY = 0;
        double maxY = getHeight() - objeto.getBoundsInLocal().getHeight();
        double posX = getWidth();
        double posY = random.nextDouble() * (maxY - minY) + minY;

        objeto.relocate(posX, posY);

        return objeto;
    }
}