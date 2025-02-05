package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.EventManager.AddParticlesEvent;
import org.example.EventManager.EventManager;
import org.example.EventManager.EventStructs.NewParticle;


import static org.example.GLOBAL_STATE.*;

public class JavaFX extends Application {

    private WritableImage writableImage;
    private ImageView imageView;

    private AnimationTimer animationTimer;

    private boolean isRun = true;

//    private final OpenCL oCL = new OpenCL();

    @Override
    public void start(Stage primaryStage) {
        createOpenClThread();

        writableImage = new WritableImage(WorkZoneWidth, WorkZoneHeight);

        Pixels = new int [getScreenWidth() * getScreenHeight()];
        DepthBuffer = new float [getScreenWidth() * getScreenHeight()];

        imageView = new ImageView(writableImage);
        imageView.setFitWidth(getScreenWidth());
        imageView.setFitHeight(getScreenHeight());

        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, getScreenWidth(), getScreenHeight());

        primaryStage.setTitle("Simple render");
        primaryStage.setScene(scene);
        primaryStage.show();

        //обробники подій зміни розміру вікна
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            setScreenWidth(newValue.intValue());
            updateImageSize();
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            setScreenHeight(newValue.intValue());
            updateImageSize();
        });

        //Обробка натискання клавіш
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case UP, W -> IsUp = true;
                case DOWN, S -> IsDown = true;
                case LEFT, A -> IsLeft = true;
                case RIGHT, D -> IsRight = true;
            }
        });

        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case UP, W -> IsUp = false;
                case DOWN, S -> IsDown = false;
                case LEFT, A -> IsLeft = false;
                case RIGHT, D -> IsRight = false;
            }
        });


        //Обробка натискання лівої/правої кнопки миші
//        scene.setOnMousePressed((event) -> {
//            if (event.getButton() == MouseButton.PRIMARY) {
//                cursorPosition.setCursorPosition(
//                        (float) event.getX() / getScreenWidth() * WorkZoneWidth,
//                        (float) event.getY() / getScreenHeight() * WorkZoneWidth);
//            } else if (event.getButton() == MouseButton.SECONDARY) {
//
//                AddParticlesEvent APD = EventManager.
//                        getEventManager().
//                        getEvent(AddParticlesEvent.EVENT_NAME);
//
//                APD.invoke(new NewParticle(
//                        (float) event.getX() / getScreenWidth() * WorkZoneWidth,
//                        (float) event.getY() / getScreenHeight() * WorkZoneWidth,
//                        1
//                        )
//                );
//            }
//        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            AddParticlesEvent APD = EventManager.
                    getEventManager().
                    getEvent(AddParticlesEvent.EVENT_NAME);

            @Override
            public void handle(MouseEvent mouseEvent) {
                APD.invoke(new NewParticle(
                                (float) mouseEvent.getX() / getScreenWidth() * WorkZoneWidth,
                                (float) mouseEvent.getY() / getScreenHeight() * WorkZoneWidth,
                                1
                        )
                );
            }
        });



        //Обробка перреміщення мишки
        scene.setOnMouseDragged((event) -> {
            if (event.isPrimaryButtonDown()) {
                cursorPosition.setCursorPosition(
                        (float) event.getX() / getScreenWidth() * WorkZoneWidth,
                        (float) event.getY() / getScreenHeight() * WorkZoneWidth);
            }
        });

        //обробка відпускання мишки
        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                cursorPosition.inactivateCursor();
            }
        });

        //закриття вікна
        primaryStage.setOnCloseRequest(event -> {
            isRun = false;
            animationTimer.stop();
            closeOpenSlThread();
//            oCL.destroy();
        });

//        System.out.println("ok");
//        System.out.println(Pixels[1]);
//
//        for (int i = 0; i < Pixels.length; i++) {
//            System.out.println(i + " " + Pixels[i]);
//        }

        //запуск таймеру для анімації
         animationTimer = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if(!isRun) {
                    return;
                }
                if (last == 0) {
                    last = now;
                    return;
                }

                Time += (float) (now - last) / 1000000000;

                updatePixels((float) (now - last) / 1000000000);


                writableImage.getPixelWriter().setPixels(0, 0,
                        WorkZoneWidth, WorkZoneHeight,
                        PixelFormat.getIntArgbInstance(), Pixels, 0, WorkZoneWidth);

                last = now;
            }
        };
        animationTimer.start();
    }

    //при закритті вікна
    @Override
    public void stop() {
        isRun = false;
        animationTimer.stop();
        closeOpenSlThread();
//        oCL.destroy();
    }

    public void createOpenClThread () {
        OpenClTask = new OpenCL();
        OpenClThread = new Thread(OpenClTask);
        OpenClThread.setDaemon(true);
        OpenClThread.start();
    }

    public void closeOpenSlThread () {
        if (OpenClTask != null) {
            OpenClTask.cancel();
            OpenClThread.interrupt();
            OpenClTask = null;
        }
    }

    //оновлення вікна після зміни розміру
    private void updateImageSize() {
//        writableImage = new WritableImage( getScreenWidth(), getScreenHeight());
//        Pixels = new int[ getScreenWidth() * getScreenHeight()];
//        DepthBuffer = new float [getScreenWidth() * getScreenHeight()];
//        imageView.setImage(writableImage);
        imageView.setFitWidth(getScreenWidth());
        imageView.setFitHeight(getScreenHeight());
    }

    //основна функція відрисовки
    private void updatePixels(float time)  {
        OpenClTask.read();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

