package com.example.sprintjava;

import com.example.components.Notifier;
import com.example.components.VisiblePasswordFieldSkin;
import com.example.dao.ClienteDAO;
import com.example.entity.Cliente;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

public class HelloController {
    @FXML
    private Rectangle rec1;
    @FXML
    private Rectangle rec2;
    @FXML
    private Rectangle rec3;
    @FXML
    private Button buttonGoogle;
    @FXML
    private Button signUpButton;
    @FXML
    private ImageView imageViewer;
    @FXML
    private Button switchLayoutButton;
    @FXML
    private TextField tfEmailLogin;
    @FXML
    private PasswordField tfPasswordLogin;
    @FXML
    private TextField tfEmailRegister;
    @FXML
    private PasswordField tfPasswordRegister;
    @FXML
    private TextField tfUsernameRegister;
    @FXML
    private Button eyeRegister;
    @FXML
    private Button eyeLogin;
    @FXML
    private AnchorPane ap;

    private final String[] imagePaths = {"/imgs/perfume1.png", "/imgs/perfume2.png", "/imgs/perfume3.png"};
    private int currentImageIndex = 0;
    private FadeTransition fadeTransition;
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        fadeTransition = new FadeTransition(Duration.seconds(2), imageViewer);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        Duration duration = Duration.seconds(4);
        KeyFrame keyFrame = new KeyFrame(duration, e -> changeImage());
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE); // Repete indefinidamente

        if (eyeLogin != null) {
            eyeLogin.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/imgs/closeEye.png")).toExternalForm())));
            VisiblePasswordFieldSkin skin = new VisiblePasswordFieldSkin(tfPasswordLogin, eyeLogin);
            tfPasswordLogin.setSkin(skin);
        } else if (eyeRegister != null) {
            eyeRegister.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/imgs/closeEye.png")).toExternalForm())));
            VisiblePasswordFieldSkin skin = new VisiblePasswordFieldSkin(tfPasswordRegister, eyeRegister);
            tfPasswordRegister.setSkin(skin);
        }
        timeline.play();
    }

    @FXML
    void LoginClicked() {
        String email = tfEmailLogin.getText();
        String senha = tfPasswordLogin.getText();

        Thread backgroundThread = new Thread(() -> {
            boolean loginSucesso = clienteDAO.verificarLogin(email, senha);

            Platform.runLater(() -> {
                if (loginSucesso) {
                    showNotification("Login bem Sucedido.", true);
                    navigateTo("mainClient.fxml");
                } else {
                    showNotification("Login Falhou.", false);
                }
            });
        });

        backgroundThread.start();
    }

    @FXML
    void RegisterClicked() {
        System.out.println("a");
        String nomeDeUsuario = tfUsernameRegister.getText();
        String email = tfEmailRegister.getText();
        String senha = tfPasswordRegister.getText();

        if (nomeDeUsuario.isBlank() || email.isBlank() || senha.isBlank()) {
            showNotification("Nome de usuário, email e senha são obrigatórios.", false);
            return;
        }

        if (!isValidEmail(email)) {
            showNotification("Email inválido. Digite um email válido.", false);
            return;
        }

        if (!isStrongPassword(senha)) {
            showNotification("Senha fraca. A senha deve ter pelo menos 8 caracteres.", false);
            return;
        }

        if (nomeDeUsuario.length() <= 7){
            showNotification("Seu nome de usuario deve ter pelomenos 8 caracteres.", false);
            return;
        }

        Thread backgroundThread = new Thread(() -> {
            if (clienteDAO.verificarNomeDeUsuarioExistente(nomeDeUsuario)) {
                showNotification("Nome de usuário já está em uso. Escolha outro nome de usuário.", false);
                return;
            }
            if (clienteDAO.verificarEmailExistente(email)) {
                showNotification("Email já está em uso. Escolha outro email.", false);
                return;
            }
            int insercaoSucesso = clienteDAO.save(new Cliente(nomeDeUsuario, email, senha));
            Platform.runLater(() -> {
                if (insercaoSucesso == 1) {
                    showNotification("Cadastro bem-sucedido", true);
                    changeToLogin();
                } else {
                    showNotification("Erro ao cadastrar o cliente", false);
                }
            });
        });
        backgroundThread.start();
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@([\\w-]+\\.)+[A-Za-z]{2,4}$");
    }
    private boolean isStrongPassword(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasUpperCase && hasLowerCase && hasDigit) {
                return true;
            }
        }

        return false;
    }

    @FXML
        void mouseEntered(MouseEvent event) {
        Node sourceNode = (Node) event.getSource();
        sourceNode.setStyle("-fx-text-fill: #027fe9;");
    }

    @FXML
    void mouseExited(MouseEvent event) {
        Node sourceNode = (Node) event.getSource();
        sourceNode.setStyle("-fx-text-fill:" + sourceNode.getId() + ";");
    }

    @FXML
    void ButtonGoogle() {
        buttonGoogle.setStyle("""
            transition: -fx-background-color 1s ease,
            -fx-scale-x 1s ease,
            -fx-scale-y 1s ease;
            -fx-scale-x: 1.03;
            -fx-scale-Y: 1.03;
        """);
    }

    @FXML
    void changeToRegister() {
        navigateTo("registerScreen.fxml");
    }

    @FXML
    void changeToLogin() {
        navigateTo("loginScreen.fxml");
    }

    @FXML
    void ButtonGoogleExited() {
        buttonGoogle.setStyle("""
            transition: -fx-background-color 1s ease,
            -fx-scale-x 1s ease,
            -fx-scale-y 1s ease;
            -fx-scale-x: 1;
            -fx-scale-Y: 1;
        """);
    }

    @FXML
    void signUpButton() {
        signUpButton.setStyle("""
            transition: -fx-background-color 1s ease,
            -fx-scale-x 1s ease,
            -fx-scale-y 1s ease;
            -fx-scale-x: 1.03;
            -fx-scale-Y: 1.03;
        """);
    }

    @FXML
    void signUpButtonExited() {
        signUpButton.setStyle("""
            transition: -fx-background-color 1s ease,
            -fx-scale-x 1s ease,
            -fx-scale-y 1s ease;
            -fx-scale-x: 1;
            -fx-scale-Y: 1;
        """);
    }

    @FXML
    void closeClicked() {
        Platform.exit();
    }

    private void changeImage() {
        currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResource(imagePaths[currentImageIndex])).toExternalForm());

        imageViewer.setImage(newImage);
        imageViewer.setOpacity(0.0);

        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        fadeTransition.playFromStart();

        switch (currentImageIndex) {
            case 0 -> {
                rec1.setFill(Color.WHITE);
                rec2.setFill(Color.rgb(194, 194, 199));
                rec3.setFill(Color.rgb(194, 194, 199));

                rec1.setWidth(36);
                rec2.setWidth(22);
                rec3.setWidth(22);

                rec1.setLayoutX(215);
                rec2.setLayoutX(255);
                rec3.setLayoutX(281);

                imageViewer.setFitHeight(437);
                imageViewer.setFitWidth(437);
                imageViewer.setY(0);
            }
            case 1 -> {
                rec1.setFill(Color.rgb(194, 194, 199));
                rec2.setFill(Color.WHITE);
                rec3.setFill(Color.rgb(194, 194, 199));

                rec1.setWidth(22);
                rec2.setWidth(36);
                rec3.setWidth(22);

                rec1.setLayoutX(215);
                rec2.setLayoutX(241);
                rec3.setLayoutX(281);

                imageViewer.setFitHeight(437);
                imageViewer.setFitWidth(437);
                imageViewer.setY(0);
            }
            case 2 -> {
                rec1.setFill(Color.rgb(194, 194, 199));
                rec2.setFill(Color.rgb(194, 194, 199));
                rec3.setFill(Color.WHITE);

                rec1.setWidth(22);
                rec2.setWidth(22);
                rec3.setWidth(36);

                rec1.setLayoutX(215);
                rec2.setLayoutX(241);
                rec3.setLayoutX(267);

                imageViewer.setFitHeight(585);
                imageViewer.setFitWidth(437);
                imageViewer.setY(-70);
            }
        }
    }

    private void navigateTo(String fxmlPath) {
        try {
            stage = (Stage) switchLayoutButton.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            scene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            scene.setOnMouseDragged(event -> {
                // Verifique se o mouse está nos primeiros 50 pixels (ajuste conforme necessário)
                if (event.getSceneY() <= 100) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            stage.setScene(scene);
            stage.setResizable(false);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IllegalStateException ignore){}
        catch (Exception e) {e.printStackTrace();}
    }

    @FXML
    private void productClicked(){
        abrirLinkNoNavegador("https://www.youtube.com");
    }
    @FXML
    private void storeClicked(){
        abrirLinkNoNavegador("https://www.youtube.com");
    }
    @FXML
    private void aboutClicked(){
        abrirLinkNoNavegador("https://www.youtube.com");
    }


    private void abrirLinkNoNavegador(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            // Lidar com erros, como a falta de suporte ao Desktop
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao abrir o link");
            alert.setHeaderText(null);
            alert.setContentText("Ocorreu um erro ao tentar abrir o link.");

            alert.showAndWait();
        }
    }

    private void showNotification(String message, boolean isSuccess) {
        Notifier notifier = new Notifier(message, isSuccess);
        notifier.show();
    }
}
