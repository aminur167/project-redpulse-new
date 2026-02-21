package com.example.projectredpulsenew;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreatePost_Controller implements Initializable {

    @FXML private Label appNameLabel;

    @FXML private Button btnChat;
    @FXML private Button btnCreatePost;
    @FXML private Button btnLogOut;
    @FXML private Button btnNewsFeed;
    @FXML private Button btnHome;
    @FXML private Button btnNotifications;
    @FXML private Button btnProfile;
    @FXML private Button btnSettings;

    @FXML private VBox sidebar;
    @FXML private VBox sidebarButtons;

    @FXML private HBox sidebarHeader;
    @FXML private HBox topBar;
    @FXML private HBox topBarRight;

    // Form fields
    @FXML private TextField patientNameField;
    @FXML private ComboBox<String> bloodGroupField;
    @FXML private TextField unitNeededField;
    @FXML private TextField locationField;
    @FXML private DatePicker dateNeededField;
    @FXML private TextField phoneField;
    @FXML private TextArea notesArea;
    @FXML private Button submitBtn;
    @FXML private ComboBox<String> districtField;

    // File paths - আপনার original পাথ ব্যবহার করছি
    private final String postFilePath = "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\PostDetails.json";
    private final String userFilePath = "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\UserDetails.json";
    private final String notiFilePath = "D:\\project-redpulse-new\\src\\main\\resources\\com\\example\\projectredpulsenew\\Noti.json";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        districtField.setItems(FXCollections.observableArrayList(
                "Bagerhat", "Bandarban", "Barguna", "Barishal", "Bhola", "Bogura",
                "Brahmanbaria", "Chandpur", "Chapainawabganj", "Chattogram", "Chuadanga",
                "Cox's Bazar", "Cumilla", "Dhaka", "Dinajpur", "Faridpur", "Feni",
                "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jashore",
                "Jhalokati", "Jhenaidah", "Joypurhat", "Khagrachhari", "Khulna",
                "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat",
                "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar",
                "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narayanganj",
                "Narsingdi", "Natore", "Netrokona", "Nilphamari", "Noakhali", "Pabna",
                "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi",
                "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur",
                "Sirajganj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"
        ));
    }

    // ------------------- Navigation Buttons -------------------

    @FXML
    void CreatetoNews(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
        Stage stage = (Stage) btnNewsFeed.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoHome(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoNoti(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Notification.fxml"));
        Stage stage = (Stage) btnNotifications.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoChat(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Stage stage = (Stage) btnChat.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoProf(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Stage stage = (Stage) btnProfile.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void CreatetoStng(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = (Stage) btnSettings.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ------------------- Save Post to JSON -------------------

    private void savePostToJson(PostDetails post) {
        List<PostDetails> posts = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File file = new File(postFilePath);
            System.out.println("Writing to file at: " + file.getAbsolutePath());
            if (file.exists() && file.length() > 0) {
                try (Reader reader = new FileReader(file)) {
                    Type listType = new TypeToken<List<PostDetails>>() {}.getType();
                    List<PostDetails> existingPosts = gson.fromJson(reader, listType);
                    if (existingPosts != null) posts = existingPosts;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading existing posts: " + e.getMessage());
        }

        posts.add(post);

        try (Writer writer = new FileWriter(postFilePath)) {
            gson.toJson(posts, writer);
            System.out.println("Post saved successfully for user: " + post.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving post: " + e.getMessage());
        }
    }

    // ------------------- Send Notifications to Matching Blood Group Donors -------------------

    private void sendNotificationsToDonors(String bloodGroup, String posterName, String posterEmail,
                                           String posterPassword, String posterProfilePic) {
        try {
            Gson gson = new Gson();

            // Read all users from UserDetails.json
            File userFile = new File(userFilePath);
            if (!userFile.exists()) {
                System.out.println("ERROR: UserDetails.json file not found at: " + userFilePath);
                return;
            }

            Reader userReader = new FileReader(userFile);
            Type userListType = new TypeToken<List<User>>(){}.getType();
            List<User> allUsers = gson.fromJson(userReader, userListType);
            userReader.close();

            System.out.println("Total users found in UserDetails.json: " + allUsers.size());

            // Find users with matching blood group
            List<User> matchingDonors = new ArrayList<>();
            for (User user : allUsers) {
                if (user.getBloodGroup() != null && user.getBloodGroup().equals(bloodGroup)) {
                    // Don't send notification to the poster themselves
                    if (!user.getEmail().equals(posterEmail)) {
                        matchingDonors.add(user);
                    }
                }
            }

            if (matchingDonors.isEmpty()) {
                System.out.println("No other donors found with blood group: " + bloodGroup);
                return;
            }

            System.out.println("Found " + matchingDonors.size() + " donors with blood group: " + bloodGroup);

            // Read existing notifications
            File notiFile = new File(notiFilePath);
            List<NotificationDetails> notifications = new ArrayList<>();

            if (notiFile.exists() && notiFile.length() > 0) {
                Reader notiReader = new FileReader(notiFile);
                Type notiListType = new TypeToken<List<NotificationDetails>>(){}.getType();
                List<NotificationDetails> existingNotis = gson.fromJson(notiReader, notiListType);
                notiReader.close();
                if (existingNotis != null) {
                    notifications = existingNotis;
                }
            }

            // Create notification for each matching donor
            int notificationCount = 0;
            for (User donor : matchingDonors) {
                NotificationDetails notification = new NotificationDetails(
                        posterName,                          // clickerName (person who posted)
                        bloodGroup,                          // clickerBloodgroup (needed blood group)
                        "N/A",                               // clickerDistrict
                        "N/A",                               // clickerNumber
                        posterEmail,                         // clickerEmail
                        posterPassword,                      // clickerPassword
                        posterProfilePic,                    // clickerProfilePic
                        donor.getEmail(),                    // receiverEmail
                        donor.getPassword(),                 // receiverPassword
                        "request"                            // notiType
                );

                notifications.add(notification);
                notificationCount++;
            }

            // Save all notifications
            try (Writer writer = new FileWriter(notiFilePath)) {
                gson.toJson(notifications, writer);
                System.out.println("SUCCESS: Sent " + notificationCount + " notifications to " + bloodGroup + " donors");
                System.out.println("Notifications saved to: " + notiFilePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending notifications: " + e.getMessage());
        }
    }

    // ------------------- Submit Button -------------------

    @FXML
    public void submitPost(ActionEvent event) {
        try {
            String patient = patientNameField.getText();
            String blood = bloodGroupField.getValue();
            String unit = unitNeededField.getText();
            String loc = locationField.getText();
            String date = dateNeededField.getValue() != null ? dateNeededField.getValue().toString() : "";
            String phone = phoneField.getText();
            String district = districtField.getValue();
            String notes = notesArea.getText();

            // Validation
            if (patient.isEmpty()) {
                showAlert("Error", "Please Enter Patient Name!");
                return;
            }
            if (!isValidName(patient)) {
                showAlert("Error", "Patient Name must contain only letters and spaces (3-50 chars).");
                return;
            }

            if (blood == null || blood.isEmpty()) {
                showAlert("Error", "Please select a blood group.");
                return;
            }

            if (!isValidUnit(unit)) {
                showAlert("Error", "Unit Needed must be a positive number.");
                return;
            }

            if (!isValidLocation(loc)) {
                showAlert("Error", "Hospital name must have at least 3 characters.");
                return;
            }

            if (!isValidDate(dateNeededField)) {
                showAlert("Error", "Please select a valid date (today or future).");
                return;
            }

            if (phone.isEmpty()) {
                showAlert("Error", "Please Enter Phone Number!");
                return;
            }
            if (!isValidPhone(phone)) {
                showAlert("Error", "Phone must be 11 digits, Bangladesh format (01XXXXXXXXX).");
                return;
            }

            if (district == null || district.isEmpty()) {
                showAlert("Error", "Please select a district.");
                return;
            }

            if (!isValidNotes(notes)) {
                showAlert("Error", "Notes cannot exceed 200 characters.");
                return;
            }

            // Get current user's info
            String currentUserName = "Anonymous";
            String currentUserEmail = "N/A";
            String currentUserPassword = "N/A";
            String currentUserProfilePic = "N/A";

            try {
                User temp2 = LoginDetails.getUser();
                if (temp2 != null) {
                    if (temp2.getName() != null) currentUserName = temp2.getName();
                    if (temp2.getEmail() != null) currentUserEmail = temp2.getEmail();
                    if (temp2.getPassword() != null) currentUserPassword = temp2.getPassword();
                    if (temp2.getProfilePicPath() != null) currentUserProfilePic = temp2.getProfilePicPath();
                }
            } catch (Exception e) {
                System.out.println("Could not get user info: " + e.getMessage());
            }

            // Create post
            PostDetails post = new PostDetails(
                    currentUserName,
                    currentUserEmail,
                    currentUserPassword,
                    currentUserProfilePic,
                    patient,
                    blood,
                    unit,
                    loc,
                    date,
                    phone,
                    district,
                    notes
            );

            System.out.println("Creating post by: " + currentUserName);

            // Save the post
            savePostToJson(post);

            // Send notifications to all users with matching blood group
            sendNotificationsToDonors(blood, currentUserName, currentUserEmail, currentUserPassword, currentUserProfilePic);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Post Created!");
            alert.setContentText("Your blood donation request has been posted. Notifications sent to all " + blood + " donors.");
            alert.showAndWait();

            clearForm();

            // Navigate to newsfeed
            Parent root = FXMLLoader.load(getClass().getResource("Newsfeed.fxml"));
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to create post: " + e.getMessage());
            alert.show();
        }
    }

    private void clearForm() {
        patientNameField.clear();
        bloodGroupField.setValue(null);
        unitNeededField.clear();
        locationField.clear();
        dateNeededField.setValue(null);
        phoneField.clear();
        districtField.setValue(null);
        notesArea.clear();
    }

    // ================= VALIDATION HELPERS =================
    private boolean isValidName(String name) {
        if (name == null) return false;
        name = name.trim();
        String regex = "^[A-Za-z]+( [A-Za-z]+)*$";
        return name.matches(regex) && name.length() >= 3 && name.length() <= 50;
    }

    private boolean isValidUnit(String unit) {
        if (unit == null || unit.isEmpty()) return false;
        try {
            int u = Integer.parseInt(unit);
            return u > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLocation(String loc) {
        return loc != null && loc.length() >= 3;
    }

    private boolean isValidDate(DatePicker datePicker) {
        if (datePicker.getValue() == null) return false;
        return !datePicker.getValue().isBefore(java.time.LocalDate.now());
    }

    private boolean isValidNotes(String notes) {
        return notes.length() <= 200;
    }

    private boolean isValidPhone(String phone) {
        if (phone == null) return false;
        phone = phone.trim();
        String regex = "^01[3-9]\\d{8}$";
        return phone.matches(regex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void logout_c(ActionEvent event) throws Exception {
        chkLogin.setlogout();
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}