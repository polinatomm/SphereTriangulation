package sample;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import module.SphereModel;
import javafx.application.Platform;

public class Main extends Application {

    private static final double MAX_DISTANCE = -1500;
    private static final double MIN_DISTANCE = -100;
    final Group root = new Group();
    final Group axisGroup = new Group();
    final Group pyramidGroup = new Group();
    final Group sphereGroup = new Group();
    final Group world = new Group();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private module.SphereModel sphere = new SphereModel(50);

    private MeshView sphereMeshView;
    private final Xform lightXform = new Xform();

    private boolean isDynamicModeOff;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    Text iterationText = new Text("            Iteration :    " + 1);
    Text triText = new Text("           Triangles :   " + 4);
    Color sphereColor = new Color(0.144,0.077,0.22,0.3);


    private void buildScene() {
        root.getChildren().add(world);
    }

    private void buildCamera()  {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        final double cameraDistance = 400;

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-cameraDistance);
        cameraXform.ry.setAngle(0);
        cameraXform.rx.setAngle(0);

        lightXform.setTranslateZ(-cameraDistance);
        LightBase light = new PointLight();
        light.setLightOn(true);
        lightXform.getChildren().addAll(light);
        root.getChildren().add(lightXform);

        cameraXform.getChildren().add(lightXform);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private Button buttonBuilder(String name, Group group){
        Button button = new Button(name);
        button.setPrefSize(80, 20);
        button.setStyle("-fx-base: #99FFCC;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (group.isVisible()) {
                    group.setVisible(false);
                    button.setStyle("-fx-base: #FF9999;");
                }
                else {
                    group.setVisible(true);
                    button.setStyle("-fx-base: #99FFCC;");
                }
            }
        });
        return button;

    }

    private void secondWindow() {
        Group rootBorderPane = new Group();
        Scene scene2D = new Scene(rootBorderPane, 190, 210, Color.WHITE);

        //Menu
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(scene2D.widthProperty());

        Menu menuHelp = new Menu("Help");

        MenuItem work = new MenuItem("How to work");
        //TODO: добавить страницу как работать
        MenuItem about = new MenuItem("About");
        //TODO: добавить страницу щ продукте
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(actionEvent -> Platform.exit());

        menuHelp.getItems().addAll(work, about, exit);
        menuBar.getMenus().addAll(menuHelp);

        iterationText.setFont(Font.font (13));
        triText.setFont(Font.font (13));

        //BOX
        HBox rootHBox = new HBox();
        rootHBox.setSpacing(10);
        Button tunerSphereButton = buttonBuilder("Sphere", sphereGroup);
        Button tunerAxesButton = buttonBuilder("Axes", axisGroup);
        rootHBox.getChildren().addAll(tunerSphereButton, tunerAxesButton);

        Button stop = new Button("Stop");
        stop.setPrefSize(170, 20);
        stop.setStyle("-fx-base: #99FFCC;");
        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isDynamicModeOff) {
                    isDynamicModeOff = !isDynamicModeOff;
                    stop.setStyle("-fx-base: #99FFCC;");
                }
                else {
                    isDynamicModeOff = !isDynamicModeOff;
                    stop.setStyle("-fx-base: #FF9999;");
                }
            }
        });

        HBox rootHBox2 = new HBox();
        rootHBox2.setSpacing(10);

        Text sphereText = new Text("Sphere Color");
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.RED);
        colorPicker.setPrefSize(90, 25);
        sphereMeshView.setMaterial(new PhongMaterial(colorPicker.getValue()));
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sphereMeshView.setMaterial(new PhongMaterial(colorPicker.getValue()));
            }
        });
        rootHBox2.getChildren().addAll(sphereText, colorPicker);

        VBox rootVBoxText = new VBox();
        rootVBoxText.setPrefSize(170, 55);
        rootVBoxText.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #99FFCC;;");
        rootVBoxText.getChildren().addAll(iterationText, triText);

        VBox rootVBox = new VBox();
        rootVBox.setSpacing(10);
        rootVBox.setPadding(new Insets(30, 20, 0, 10));
        rootVBox.getChildren().addAll(rootVBoxText, rootHBox, stop, rootHBox2);

        rootBorderPane.getChildren().addAll(rootVBox, menuBar);

        Stage stage = new Stage();
        stage.setTitle("Controller");
        stage.getIcons().add(new Image("http://docs.huihoo.com/cgal/3.7/cgal-manual/Surface_mesher/sphere-surface.png"));
        stage.setScene(scene2D);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention!");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to exit?");
                alert.showAndWait();
                Platform.exit();
            }
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(root, 800, 600, true);
        buildScene();
        buildCamera();
        buildPyramid(50, false, true);

        buildAxes();
        buildSphere(50);

        scene.setFill(Color.WHITE);
        handleKeyboard(scene);
        handleMouse(scene);

        primaryStage.setTitle("Sphere Triangulation");
        primaryStage.getIcons().add(new Image("http://docs.huihoo.com/cgal/3.7/cgal-manual/Surface_mesher/sphere-surface.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention!");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to exit?");
                alert.showAndWait();
                Platform.exit();
            }
        });

        scene.setCamera(camera);
        secondWindow();
    }

    private void buildSphere(double R) {
        Sphere sphere = new Sphere();
        sphere.setRadius(R);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(sphereColor);

        sphere.setMaterial(redMaterial);

        sphereGroup.getChildren().addAll(sphere);
        world.getChildren().addAll(sphereGroup);
    }

    private void handleMouse(Scene scene) {

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 10.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });
        scene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            double currentDistance = camera.getTranslateZ();
            double newCameraDistance = currentDistance + delta;
            if (!isDynamicModeOff) {
                if (newCameraDistance > MAX_DISTANCE && newCameraDistance < MIN_DISTANCE) {

                    switch (Math.abs((int) newCameraDistance / 100)) {
                        case 1:
                            if (sphere.getNumberOfRefines() < 6) changeMeshViewApproaching();
                            break;
                        case 2:
                        case 3:
                            if (sphere.getNumberOfRefines() > 5) changeMeshViewDistancing();
                            else if (sphere.getNumberOfRefines() < 5) changeMeshViewApproaching();
                            break;
                        case 4:
                        case 5:
                            if (sphere.getNumberOfRefines() > 4) changeMeshViewDistancing();
                            else if (sphere.getNumberOfRefines() < 4) changeMeshViewApproaching();
                            break;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            if (sphere.getNumberOfRefines() > 3) changeMeshViewDistancing();
                            else if (sphere.getNumberOfRefines() < 3) changeMeshViewApproaching();
                            break;
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                            if (sphere.getNumberOfRefines() > 2) changeMeshViewDistancing();
                            else if (sphere.getNumberOfRefines() < 2) changeMeshViewApproaching();
                            break;
                        case 14:
                            if (sphere.getNumberOfRefines() > 1) changeMeshViewDistancing();
                            break;
                    }
                }
            }
            camera.setTranslateZ(newCameraDistance);
            lightXform.setTranslateZ(newCameraDistance);

        });

    }

    private void buildPyramid(float R, boolean ambient, boolean fill) {
        SphereModel sm = new SphereModel(R);

        sphereMeshView = new MeshView(sm.getMesh());
        sphereMeshView.setDrawMode(DrawMode.LINE);
        sphereMeshView.setCullFace(CullFace.BACK);
        pyramidGroup.getChildren().addAll(sphereMeshView);

        if(null != sphereColor)
        {
            PhongMaterial material = new PhongMaterial(sphereColor);
            sphereMeshView.setMaterial(material);
        }

        if (ambient)
        {
            AmbientLight light = new AmbientLight(Color.WHITE);
            light.getScope().add(sphereMeshView);
            pyramidGroup.getChildren().add(light);
        }
        if(fill) {
            sphereMeshView.setDrawMode(DrawMode.FILL);
        }

        world.getChildren().addAll(pyramidGroup);
    }

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {
            System.out.println("got event: " + event);
            switch (event.getCode()) {
                case Z:
                    if (event.isShiftDown()) {
                        cameraXform.ry.setAngle(0.0);
                        cameraXform.rx.setAngle(0.0);
                        camera.setTranslateZ(-400.0);
                    }
                    cameraXform2.t.setX(0.0);
                    cameraXform2.t.setY(0.0);
                    break;
                case X:
                    if (event.isControlDown()) {
                        if (axisGroup.isVisible()) {
                            axisGroup.setVisible(false);
                        }
                        else {
                            axisGroup.setVisible(true);
                        }
                    }
                    break;
                case S:
                    if (event.isControlDown()) {
                        if (sphereGroup.isVisible()) {
                            sphereGroup.setVisible(false);
                        }
                        else {
                            sphereGroup.setVisible(true);
                        }
                    }
                    break;
                case M:
                    if (event.isControlDown()) {
                        if (sphereMeshView.isVisible()) {
                            sphereMeshView.setVisible(false);
                        }
                        else {
                            sphereMeshView.setVisible(true);
                        }
                    }
                    break;
                case D:
                    if (event.isControlDown()) {
                        isDynamicModeOff = !isDynamicModeOff;
                    }
                    break;
                case E:
                    if (event.isControlDown()){
                        secondWindow();
                    }
            }
        });
    }

    private void changeMeshViewDistancing() {
       int n = sphere.unrefine();
       triText.setText("           Triangles :   " + n);
       iterationText.setText("            Iteration :    " + (sphere.getNumberOfRefines() + 1));
       sphereMeshView.setMesh(sphere.getMesh());
    }

    private void changeMeshViewApproaching() {
        int n = sphere.refine();
        triText.setText("           Triangles :   " + n);
        iterationText.setText("            Iteration :    " + (sphere.getNumberOfRefines() + 1));
        sphereMeshView.setMesh(sphere.getMesh());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
