package com.example.projectredpulsenew;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.Reader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class Notification_Controller {

    @FXML
    private Label appNameLabel;

    @FXML
    private Button btnChat, btnCreatePost, btnLogOut, btnNewsFeed, btnHome, btnNotifications, btnProfile, btnSettings;

    @FXML
    private VBox sidebar, sidebarButtons, notificationContainer;

    @FXML
    private HBox sidebarHeader, topBar, topBarRight;

    // 🔴 NEW: Store cards and their time labels for real-time update
    private Map<HBox, Label> notificationTimeLabels = new HashMap<>();
    private Timeline timeUpdateTimeline;
    private Timeline refreshTimeline;

    // ================== NAVIGATION BUTTONS ==================
    @FXML
    void NotitoNews(ActionEvent event) throws Exception {
        stopAllTimers(); // Cleanup before navigation
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoHome(ActionEvent event) throws Exception {
        stopAllTimers();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoCreate(ActionEvent event) throws Exception {
        stopAllTimers();
        Parent root = FXMLLoader.load(getClass().getResource("CreatePost.fxml"));
        Stage stage = (Stage) btnCreatePost.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoChat(ActionEvent event) throws Exception {
        stopAllTimers();
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Stage stage = (Stage) btnChat.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoProf(ActionEvent event) throws Exception {
        stopAllTimers();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void NotitoStng(ActionEvent event) throws Exception {
        stopAllTimers();
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void logout_noti(ActionEvent event) throws Exception {
        stopAllTimers();
        chkLogin.setlogout();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ================== INITIALIZE ==================
    @FXML
    public void initialize() {
        loadNotifications();
        startRealTimeUpdates();
        startTimeLabelsUpdater();
    }

    // 🔴 NEW: Start periodic refresh (every 10 seconds)
    private void startRealTimeUpdates() {
        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> refreshNotifications())
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // 🔴 NEW: Start time labels updater (every 1 second)
    private void startTimeLabelsUpdater() {
        timeUpdateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateAllTimeLabels())
        );
        timeUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        timeUpdateTimeline.play();
    }

    // 🔴 NEW: Refresh notifications (called periodically)
    private void refreshNotifications() {
        Platform.runLater(() -> {
            // Save current scroll position if needed
            loadNotifications();
        });
    }

    // 🔴 NEW: Update all time labels
    private void updateAllTimeLabels() {
        for (Map.Entry<HBox, Label> entry : notificationTimeLabels.entrySet()) {
            HBox card = entry.getKey();
            Label timeLabel = entry.getValue();

            // Get timestamp from card's user data
            if (card.getUserData() instanceof Long) {
                long timestamp = (Long) card.getUserData();
                updateTimeLabel(timeLabel, timestamp);
            }
        }
    }

    // 🔴 NEW: Format time like Facebook
    private void updateTimeLabel(Label timeLabel, long timestamp) {
        long currentTime = System.currentTimeMillis();
        long diffSeconds = (currentTime - timestamp) / 1000;

        if (diffSeconds < 60) {
            timeLabel.setText("Just now");
        } else if (diffSeconds < 3600) {
            long minutes = diffSeconds / 60;
            timeLabel.setText(minutes + "m");
        } else if (diffSeconds < 86400) {
            long hours = diffSeconds / 3600;
            timeLabel.setText(hours + "h");
        } else if (diffSeconds < 604800) { // 7 days
            long days = diffSeconds / 86400;
            timeLabel.setText(days + "d");
        } else {
            long weeks = diffSeconds / 604800;
            timeLabel.setText(weeks + "w");
        }
    }

    // 🔴 NEW: Stop all timers (cleanup)
    private void stopAllTimers() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        if (timeUpdateTimeline != null) {
            timeUpdateTimeline.stop();
        }
        notificationTimeLabels.clear();
    }

    // ================== LOAD NOTIFICATIONS ==================
    private void loadNotifications() {
        try {
            Gson gson = new Gson();
            String path = "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\Noti.json";
            Reader reader = new FileReader(path);

            Type listType = new TypeToken<List<NotificationDetails>>(){}.getType();
            List<NotificationDetails> notiList = gson.fromJson(reader, listType);

            User Cur_user = LoginDetails.getUser();
            String currentEmail = Cur_user.getEmail();
            String currentPass = Cur_user.getPassword();

            notificationContainer.getChildren().clear(); // Clean previous
            notificationTimeLabels.clear(); // 🔴 Clear old entries

            for (NotificationDetails n : notiList) {
                if (n.getReceiverEmail().equals(currentEmail)
                        && n.getReceiverPassword().equals(currentPass)) {

                    HBox card = createNotificationCard(n);
                    notificationContainer.getChildren().add(card);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading notifications: " + e.getMessage());
        }
    }

    // ================== CREATE DYNAMIC CARD ==================
    private HBox createNotificationCard(NotificationDetails n) {

        HBox card = new HBox(15);
        card.setMaxWidth(900);
        card.getStyleClass().add("noti-card");

        // 🔴 Store timestamp in card's user data
        long timestamp = n.getTimestamp();
        if (timestamp == 0) {
            timestamp = System.currentTimeMillis(); // fallback
        }
        card.setUserData(timestamp);

        // Icon Background - Circle with profile pic or default
        Circle avatar = new Circle(25);
        avatar.setStroke(Color.WHITE);
        avatar.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        try {
            String picPath = n.getClickerProfilePic();
            File picFile = (picPath != null && !picPath.isEmpty()) ? new File(picPath) : null;

            if (picFile != null && picFile.exists()) {
                Image img = new Image("file:" + picFile.getAbsolutePath(), false);
                avatar.setFill(new ImagePattern(img));
            } else {
                File defaultFile = new File("D:\\project-redpulse-new\\files\\default.jpg");
                if (defaultFile.exists()) {
                    Image img = new Image("file:" + defaultFile.getAbsolutePath(), false);
                    avatar.setFill(new ImagePattern(img));
                } else {
                    avatar.setFill(Color.web("#cccccc"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            avatar.setFill(Color.web("#cccccc"));
        }

        StackPane iconBg = new StackPane(avatar);
        iconBg.setPrefSize(50, 50);

        Label title = new Label();
        VBox infoBox = new VBox(2);

        if (n.getNotiType().equalsIgnoreCase("interested")) {
            title.setText(n.getClickerName() + " showed interest in your post");

            Label emailLabel = new Label("Donor's email: " + n.getClickerEmail());
            Label numberLabel = new Label("Donor's number: " + n.getClickerNumber());
            Label bloodLabel = new Label("Donor's Blood group: " + n.getClickerBloodgroup());
            Label districtLabel = new Label("Donor's District: " + n.getClickerDistrict());

            emailLabel.getStyleClass().add("noti-subtitle");
            numberLabel.getStyleClass().add("noti-subtitle");
            bloodLabel.getStyleClass().add("noti-subtitle");
            districtLabel.getStyleClass().add("noti-subtitle");

            infoBox.getChildren().addAll(emailLabel, numberLabel, bloodLabel, districtLabel);

        } else if (n.getNotiType().equalsIgnoreCase("request")) {
            title.setText("New blood request posted");
        }

        title.getStyleClass().add("noti-title");

        VBox textBox = new VBox(4, title, infoBox);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        // 🔴 Time Label - will be updated in real-time
        Label time = new Label();
        time.getStyleClass().add("noti-time");
        updateTimeLabel(time, timestamp); // Initial update

        // 🔴 Store this label for real-time updates
        notificationTimeLabels.put(card, time);

        card.getChildren().addAll(iconBg, textBox, time);

        return card;
    }

    // 🔴 Optional: Clean up when scene changes
    @FXML
    public void cleanup() {
        stopAllTimers();
    }
}