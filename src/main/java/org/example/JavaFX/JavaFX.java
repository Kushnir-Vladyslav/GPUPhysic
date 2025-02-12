package org.example.JavaFX;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.Event.*;
import org.example.Event.EventDataStructs.MousePosition;
import org.example.Event.MouseEvent.*;
import org.example.Event.WindowResizeEvent.WindowHeightResizeEvent;
import org.example.Event.WindowResizeEvent.WindowWidthResizeEvent;
import org.example.OpenCL.OpenCL;
import org.example.Structs.Canvas;


import static org.example.JavaFX.Window.*;

public class JavaFX extends Application {

    private WritableImage writableImage;
    private ImageView imageView;

    private AnimationTimer animationTimer;

    private boolean isRun = true;

    private OpenCL openClTask;
    private Thread openClThread;

    private Window window;
    
    @Override
    public void start(Stage primaryStage) {

        window = Window.getInstance();
        
        createOpenClThread();

        writableImage = new WritableImage(Canvas.getCanvasWidth(), Canvas.getCanvasHeight());

        getInstance().pixels = new int [window.getScreenWidth() * window.getScreenHeight()];

        imageView = new ImageView(writableImage);
        imageView.setFitWidth(window.getScreenWidth());
        imageView.setFitHeight(window.getScreenHeight());

        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, window.getScreenWidth(), window.getScreenHeight());

        primaryStage.setTitle("Simple render");
        primaryStage.setScene(scene);
        primaryStage.show();

        //обробники подій зміни розміру вікна
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            final WindowWidthResizeEvent windowWidthResizeEvent =
                    EventManager.
                    getInstance().
                    getEvent(WindowWidthResizeEvent.EVENT_NAME);

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowWidthResizeEvent.invoke(newValue.intValue());
                updateImageSize();
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            final WindowHeightResizeEvent windowHeightResizeEvent =
                    EventManager.
                            getInstance().
                            getEvent(WindowHeightResizeEvent.EVENT_NAME);

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                windowHeightResizeEvent.invoke(newValue.intValue());
                updateImageSize();
            }
        });

        //Обробка натискання клавіш
        scene.setOnKeyPressed((event) -> {
//            switch (event.getCode()) {
//                case UP, W -> IsUp = true;
//                case DOWN, S -> IsDown = true;
//                case LEFT, A -> IsLeft = true;
//                case RIGHT, D -> IsRight = true;
//            }
        });

        scene.setOnKeyReleased((event) -> {
//            switch (event.getCode()) {
//                case UP, W -> IsUp = false;
//                case DOWN, S -> IsDown = false;
//                case LEFT, A -> IsLeft = false;
//                case RIGHT, D -> IsRight = false;
//            }
        });


        //Обробка натискання лівої/правої кнопки миші
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            final RightMousePressEvent rightMousePressEvent = EventManager.
                    getInstance().
                    getEvent(RightMousePressEvent.EVENT_NAME);

            final LeftMousePressEvent leftMousePressEvent = EventManager.
                    getInstance().
                    getEvent(LeftMousePressEvent.EVENT_NAME);

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    leftMousePressEvent.invoke(new MousePosition(
                            (float) mouseEvent.getX() / window.getScreenWidth() * Canvas.getCanvasWidth(),
                            (float) mouseEvent.getY() / window.getScreenHeight() * Canvas.getCanvasHeight())
                    );
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    rightMousePressEvent.invoke(new MousePosition(
                            (float) mouseEvent.getX() / window.getScreenWidth() * Canvas.getCanvasWidth(),
                            (float) mouseEvent.getY() / window.getScreenHeight() * Canvas.getCanvasHeight())
                    );
                }
            }
        });



        //Обробка перреміщення мишки
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            final MoveMouseEvent moveMouseEvent = EventManager.
                    getInstance().
                    getEvent(MoveMouseEvent.EVENT_NAME);

            @Override
            public void handle(MouseEvent mouseEvent) {
                moveMouseEvent.invoke(new MousePosition(
                        (float) mouseEvent.getX() / window.getScreenWidth() * Canvas.getCanvasWidth(),
                        (float) mouseEvent.getY() / window.getScreenHeight() * Canvas.getCanvasHeight())
                );
            }
        });

        //обробка відпускання мишки
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            final RightMouseReleaseEvent rightMouseReleaseEvent = EventManager.
                    getInstance().
                    getEvent(RightMouseReleaseEvent.EVENT_NAME);

            final LeftMouseReleaseEvent leftMouseReleaseEvent = EventManager.
                    getInstance().
                    getEvent(LeftMouseReleaseEvent.EVENT_NAME);

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    leftMouseReleaseEvent.invoke(new MousePosition(
                            (float) mouseEvent.getX() / window.getScreenWidth() * Canvas.getCanvasWidth(),
                            (float) mouseEvent.getY() / window.getScreenHeight() * Canvas.getCanvasHeight())
                    );
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    rightMouseReleaseEvent.invoke(new MousePosition(
                            (float) mouseEvent.getX() / window.getScreenWidth() * Canvas.getCanvasWidth(),
                            (float) mouseEvent.getY() / window.getScreenHeight() * Canvas.getCanvasHeight())
                    );
                }
            }
        });

        //закриття вікна
        primaryStage.setOnCloseRequest(event -> {
            isRun = false;
            animationTimer.stop();
            closeOpenSlThread();
        });

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

                updatePixels((float) (now - last) / 1000000000);

                writableImage.getPixelWriter().setPixels(0, 0,
                        Canvas.getCanvasWidth(), Canvas.getCanvasHeight(),
                        PixelFormat.getIntArgbInstance(), window.pixels, 0, Canvas.getCanvasWidth());

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
    }

    public void createOpenClThread () {
        openClTask = new OpenCL();
        openClThread = new Thread(openClTask);
        openClThread.setDaemon(true);
        openClThread.start();
    }

    public void closeOpenSlThread () {
        if (openClTask != null) {
            openClTask.cancel();
            openClThread.interrupt();
            openClTask = null;
        }
    }

    //оновлення вікна після зміни розміру
    private void updateImageSize() {
//        writableImage = new WritableImage( window.getScreenWidth(), window.getScreenHeight());
//        Pixels = new int[ window.getScreenWidth() * window.getScreenHeight()];
//        DepthBuffer = new float [window.getScreenWidth() * window.getScreenHeight()];
//        imageView.setImage(writableImage);
        imageView.setFitWidth(window.getScreenWidth());
        imageView.setFitHeight(window.getScreenHeight());
    }

    //основна функція відрисовки
    private void updatePixels(float time)  {
        openClTask.read();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

