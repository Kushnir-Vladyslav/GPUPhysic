package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXThread extends Application {

    private WritableImage writableImage;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {

        writableImage = new WritableImage(GLOBAL_STATE.getScreenWidth(), GLOBAL_STATE.getScreenHeight());

        GLOBAL_STATE.Pixels = new int [GLOBAL_STATE.getScreenWidth() * GLOBAL_STATE.getScreenHeight()];
        GLOBAL_STATE.DepthBuffer = new float [GLOBAL_STATE.getScreenWidth() * GLOBAL_STATE.getScreenHeight()];

        imageView = new ImageView(writableImage);

        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, GLOBAL_STATE.getScreenWidth(), GLOBAL_STATE.getScreenHeight());

        primaryStage.setTitle("Simple render");
        primaryStage.setScene(scene);
        primaryStage.show();

        //обробники подій зміни розміру вікна
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            GLOBAL_STATE.setScreenWidth(newValue.intValue());
            updateImageSize();
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            GLOBAL_STATE.setScreenHeight(newValue.intValue());
            updateImageSize();
        });

        //Обробка натискання клавіш
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case UP, W -> GLOBAL_STATE.IsUp = true;
                case DOWN, S -> GLOBAL_STATE.IsDown = true;
                case LEFT, A -> GLOBAL_STATE.IsLeft = true;
                case RIGHT, D -> GLOBAL_STATE.IsRight = true;
            }
        });

        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case UP, W -> GLOBAL_STATE.IsUp = false;
                case DOWN, S -> GLOBAL_STATE.IsDown = false;
                case LEFT, A -> GLOBAL_STATE.IsLeft = false;
                case RIGHT, D -> GLOBAL_STATE.IsRight = false;
            }
        });

        //Обробка натискання лівої кнопки миші і руху
        scene.setOnMousePressed((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {

            }
        });

        scene.setOnMouseDragged((event) -> {
            if (event.isPrimaryButtonDown()) {

            }
        });


        //запуск таймеру для анімації
        AnimationTimer animationTimer = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                GLOBAL_STATE.Time += (float) (now - last) / 1000000000;

                updatePixels((float) (now - last) / 1000000000);

                writableImage.getPixelWriter().setPixels(0, 0,
                        GLOBAL_STATE.getScreenWidth(), GLOBAL_STATE.getScreenHeight(),
                        PixelFormat.getIntArgbInstance(), GLOBAL_STATE.Pixels, 0, GLOBAL_STATE.getScreenWidth());

                last = now;
            }
        };
        animationTimer.start();
    }

    //оновлення вікна після зміни розміру
    private void updateImageSize() {
        writableImage = new WritableImage( GLOBAL_STATE.getScreenWidth(), GLOBAL_STATE.getScreenHeight());
        GLOBAL_STATE.Pixels = new int[ GLOBAL_STATE.getScreenWidth() * GLOBAL_STATE.getScreenHeight()];
        GLOBAL_STATE.DepthBuffer = new float [GLOBAL_STATE.getScreenWidth() * GLOBAL_STATE.getScreenHeight()];
        imageView.setImage(writableImage);
    }

    //основна функція відрисовки
    private void updatePixels(float time)  {


    }

    public static void main(String[] args) {
        launch(args);
    }
}

