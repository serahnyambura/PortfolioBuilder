package com.project.one1.portfoliobuilder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;

public class PortfolioBuilder extends Application {

    private Stage primaryStage;
    private Color accentColor;
    private String biographyText;
    private String picturePath;
    private DatePicker birthdayPicker;
    private TextArea experienceTextArea;
    private TextArea educationTextArea;
    private TextArea projectsTextArea;
    private TextField twitterHandleField;
    private TextField fontField;
    private ImageView selectedImageView;
    private Text biographyContentText;
    private TextField skillsTextField;
    private TextField linkedinProfileField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Portfolio Builder");

        GridPane grid = createInputGrid();

        Scene scene = new Scene(grid, 1100, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Button uploadPictureButton = createButton("Upload Picture", e -> {
            picturePath = chooseFile(fileChooser);
            if (picturePath != null) {
                selectedImageView.setImage(new Image("file:" + picturePath));
                setBorderColor(accentColor);
            }
        });

        TextField nameField = createTextField("Name:");
        birthdayPicker = new DatePicker();
        ColorPicker accentColorPicker = new ColorPicker();

        FileChooser bioFileChooser = new FileChooser();
        bioFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Button uploadBioButton = createButton("Upload Biography File", e -> {
            this.biographyText = readTextFile(chooseFile(bioFileChooser));
            if (biographyText != null) {
                biographyContentText.setText(this.biographyText);
            }
        });

        experienceTextArea = createTextArea("Current Experience:");
        educationTextArea = createTextArea("Education:");
        projectsTextArea = createTextArea("Projects Handled:");
        twitterHandleField = createTextField("Twitter Handle:");
        fontField = createTextField("Font Selection:");
        skillsTextField = createTextField("Skills:");
        linkedinProfileField = createTextField("LinkedIn Profile:");

        selectedImageView = new ImageView();
        selectedImageView.setFitWidth(150);
        selectedImageView.setFitHeight(150);
        grid.add(selectedImageView, 2, 0, 1, 3);

        biographyContentText = new Text();
        biographyContentText.setWrappingWidth(300); // Adjust the width as needed
        grid.add(biographyContentText, 2, 4, 1, 3);

        Button submitButton = createButton("Generate Form", e -> generatePortfolio());
        Button clearButton = createButton("Clear Form", e -> clearForm());

        grid.add(new Label("Upload Picture:"), 0, 0);
        grid.add(uploadPictureButton, 1, 0);

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Birthday:"), 0, 2);
        grid.add(birthdayPicker, 1, 2);

        grid.add(new Label("Accent Color:"), 0, 3);
        grid.add(accentColorPicker, 1, 3);

        grid.add(new Label("Upload Biography File:"), 0, 4);
        grid.add(uploadBioButton, 1, 4);

        grid.add(new Label("Current Experience:"), 0, 5);
        grid.add(experienceTextArea, 1, 5);

        grid.add(new Label("Education:"), 0, 6);
        grid.add(educationTextArea, 1, 6);

        grid.add(new Label("Projects Handled:"), 0, 7);
        grid.add(projectsTextArea, 1, 7);

        grid.add(new Label("Twitter Handle:"), 0, 8);
        grid.add(twitterHandleField, 1, 8);

        grid.add(new Label("Font Selection:"), 0, 9);
        grid.add(fontField, 1, 9);

        grid.add(new Label("Skills:"), 0, 10);
        grid.add(skillsTextField, 1, 10);

        grid.add(new Label("LinkedIn Profile:"), 0, 11);
        grid.add(linkedinProfileField, 1, 11);

        grid.add(submitButton, 0, 12);
        grid.add(clearButton, 1, 12);

        return grid;
    }

    private Button createButton(String text, javafx.event.EventHandler action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        return textField;
    }

    private TextArea createTextArea(String prompt) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(prompt);
        return textArea;
    }

    private String chooseFile(FileChooser fileChooser) {
        File file = fileChooser.showOpenDialog(primaryStage);
        return file != null ? file.getAbsolutePath() : null;
    }

    private String readTextFile(String filePath) {
        try {
            return filePath != null ? new String(Files.readAllBytes(Paths.get(filePath))) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePortfolio() {
        Stage portfolioStage = new Stage();
        portfolioStage.setTitle("Generated Portfolio");

        GridPane portfolioGrid = createPortfolioGrid();

        Scene portfolioScene = new Scene(portfolioGrid, 600, 800);
        portfolioStage.setScene(portfolioScene);

        portfolioStage.show();

        exportAsPDF(portfolioGrid);
    }

    private GridPane createPortfolioGrid() {
        GridPane portfolioGrid = new GridPane();
        portfolioGrid.setPadding(new Insets(20, 20, 20, 20));
        portfolioGrid.setVgap(10);
        portfolioGrid.setHgap(10);

        Image image = new Image("file:" + picturePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        setBorderColor(accentColor);
        portfolioGrid.add(imageView, 0, 0, 3, 1);

        LocalDate birthday = birthdayPicker.getValue();
        int age = Period.between(birthday, LocalDate.now()).getYears();
        Label ageLabel = createLabel("Age: " + age + " years");
        portfolioGrid.add(ageLabel, 0, 1, 2, 1);

        ImageView zodiacIcon = getZodiacSignIcon(birthday.getMonthValue(), birthday.getDayOfMonth());
        portfolioGrid.add(zodiacIcon, 2, 1);

        Label bioTextLabel = createLabel(biographyText);
        bioTextLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 12;");
        portfolioGrid.add(bioTextLabel, 0, 2, 3, 1);

        Label experienceLabel = createLabel("Current Experience:");
        experienceLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 14; -fx-font-weight: bold;");
        portfolioGrid.add(experienceLabel, 0, 3, 3, 1);

        Label experienceTextLabel = createLabel(experienceTextArea.getText());
        experienceTextLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 12;");
        portfolioGrid.add(experienceTextLabel, 0, 4, 3, 1);

        Label educationLabel = createLabel("Education:");
        educationLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 14; -fx-font-weight: bold;");
        portfolioGrid.add(educationLabel, 0, 5, 3, 1);

        Label educationTextLabel = createLabel(educationTextArea.getText());
        educationTextLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 12;");
        portfolioGrid.add(educationTextLabel, 0, 6, 3, 1);

        Label projectsLabel = createLabel("Projects Handled:");
        projectsLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 14; -fx-font-weight: bold;");
        portfolioGrid.add(projectsLabel, 0, 7, 3, 1);

        Label projectsTextLabel = createLabel(projectsTextArea.getText());
        projectsTextLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 12;");
        portfolioGrid.add(projectsTextLabel, 0, 8, 3, 1);

        Label skillsLabel = createLabel("Skills:");
        skillsLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 14; -fx-font-weight: bold;");
        portfolioGrid.add(skillsLabel, 0, 9, 3, 1);

        Label skillsTextLabel = createLabel(skillsTextField.getText());
        skillsTextLabel.setStyle("-fx-font-family: '" + fontField.getText() + "'; -fx-font-size: 12;");
        portfolioGrid.add(skillsTextLabel, 0, 10, 3, 1);

        Hyperlink linkedinLink = new Hyperlink("LinkedIn Profile");
        linkedinLink.setOnAction(e -> openLink(linkedinProfileField.getText()));
        portfolioGrid.add(linkedinLink, 0, 11, 3, 1);

        Button startOverButton = createButton("Start Over", e -> primaryStage.show());
        portfolioGrid.add(startOverButton, 0, 12, 3, 1);

        return portfolioGrid;
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private String toHexColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void setBorderColor(Color color) {
        selectedImageView.setStyle("-fx-border-color: " + toHexColor(color) + "; -fx-border-width: 5px;");
    }

    private ImageView getZodiacSignIcon(int month, int day) {
        String zodiacSign = calculateZodiacSign(month, day);
        ImageView zodiacIcon = getZodiacSignIcon(zodiacSign);
        return zodiacIcon;
    }

    private ImageView getZodiacSignIcon(String zodiacSign) {
        // Add logic to return the actual zodiac sign icon based on the zodiacSign
        // For example, you can use a Map<String, String> to map zodiac signs to image paths
        // Replace the placeholder image with actual zodiac sign images
        Image ariesImage = new Image("https://cdn.britannica.com/84/13384-004-ECCA3832/Aries-illumination-Book-of-Hours-Italian-Pierpont.jpg");
        Image taurusImage = new Image("https://cdn.britannica.com/96/4596-004-1E0712A4/Taurus-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image geminiImage = new Image("https://cdn.britannica.com/72/772-050-B714C90E/Gemini-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image cancerImage = new Image("https://cdn.britannica.com/21/44021-004-87FE1376/Cancer-illumination-book-Italian-New-York-City.jpg");
        Image leoImage = new Image("https://cdn.britannica.com/85/2085-004-0F0DDBB1/Leo-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image virgoImage = new Image("https://cdn.britannica.com/34/41734-050-37587D55/Virgo-book-Italian-New-York-City-Pierpont.jpg");
        Image libraImage = new Image("https://cdn.britannica.com/58/10258-004-54EE416A/Libra-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image scorpioImage = new Image("https://cdn.britannica.com/49/9449-004-5388AC68/Scorpius-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image sagittariusImage = new Image("https://cdn.britannica.com/51/9551-004-3DD87324/Sagittarius-Book-of-Hours-Italian-Pierpont-Morgan.jpg");
        Image capricornImage = new Image("https://cdn.britannica.com/74/44074-004-485D1511/Capricornus-book-Italian-New-York-City-Pierpont.jpg");
        Image aquariusImage = new Image("https://cdn.britannica.com/66/5766-004-A409E7EF/Aquarius-book-Italian-New-York-City-Pierpont.jpg");
        Image piscesImage = new Image("https://cdn.britannica.com/28/9628-050-B60BEF86/Pisces-illumination-Book-of-Hours-Italian-Pierpont.jpg");

        ImageView zodiacIcon = new ImageView();
        switch (zodiacSign.toLowerCase()) {
            case "aries":
                zodiacIcon.setImage(ariesImage);
                break;
            case "taurus":
                zodiacIcon.setImage(taurusImage);
                break;
            case "gemini":
                zodiacIcon.setImage(geminiImage);
                break;
            case "cancer":
                zodiacIcon.setImage(cancerImage);
                break;
            case "leo":
                zodiacIcon.setImage(leoImage);
                break;
            case "virgo":
                zodiacIcon.setImage(virgoImage);
                break;
            case "libra":
                zodiacIcon.setImage(libraImage);
                break;
            case "scorpio":
                zodiacIcon.setImage(scorpioImage);
                break;
            case "sagittarius":
                zodiacIcon.setImage(sagittariusImage);
                break;
            case "capricorn":
                zodiacIcon.setImage(capricornImage);
                break;
            case "aquarius":
                zodiacIcon.setImage(aquariusImage);
                break;
            case "pisces":
                zodiacIcon.setImage(piscesImage);
                break;
            default:
                // Use a default image for unknown signs
                zodiacIcon.setImage(new Image("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5OjcBCgoKDQwNGg8PGjclHyU3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3N//AABEIAJQAlAMBIgACEQEDEQH/xAAbAAEAAgMBAQAAAAAAAAAAAAAAAQQCAwUGB//EADUQAAEEAQIDBQUIAgMAAAAAAAEAAgMEEQUSEyExIkFRUmEGFHGBkSMyQqGxwdHwFTMkYvH/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8A+vZPmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKZPmKhEE5PmKKEQEREBERAREQERSBlBCLKThRECWdjHHuJwpbHuGWOY8f8AVyDBFkWOHUFYoCIiAiIgIiICIiAiIgIiICIsJJY4x23AenegzWcIzIOa58l5xOIgB6lZUZHZmnkcTsZ3oOfaLrepPDDlz37W5+iuO0O0znFLGT6EhVdCZxNTjJ57QXH6L1iDzfD1et04rmjwO4KBq9qI4nhYfR7C0r0uEc1rhhzQR6hB59msV3f7IHN9WHKvVpK9tpMDzkdWuGCFV9oq1eKCOSONrJHPx2RjIwVo9nG9ueTuDQ390HRPJQp681CAiIgIiICIiAiIgrXpHxsbscW5JBIVDmeZ6rqWI+LE5veenxXK9PBAVh7uFo87+hecft/KrlZ627hadWi73Hcf780G72Vj3TTyY5NaGj5/+L0a8zpN+LTNM4kgLpJnu2NHfjASnrtiXUWcctbC/s7AOngUHp1CpXNVqU8iWUF/kZzK4Vz2isTZbWaIW+J5u/hBv9qpvtYIgejS4j8v2K3aE3Zpj3973n6ch/K4usvPvnDe4udFG1jiTnJxzP1JXoqjODptZh67AT8+f7oMkREBERAREQEREBERAXNuR7JiQOTuYXSWi5HxITj7zeYQc9jdz2t8SAtHtJLm3HEOjI/1/oVmBzWTse7o13NY6xpUtyx7zVcyTc0AtLsdPBBVq2tNlqxQ32TMkiBDXxnI5nPRbPcNOnP/ABdTYD5ZW4XPl0y9FzfVlwPAbh+SpnLThwIPgUHafod4DdGxkzfGN4P6rXUoze/wRTQvYC8Z3NI5DmuWyR0RyxxYfEHCtf5bUOGY/e5SwjBBOfz6oJnebeovI58WbA+buS9jPhpawdGjC8joEfF1au3uaS76BeskOZHH5IMEREBERAREQEREBERAREQUHUnmQ7S3bnkcqxDVZHgklzvEreiDMSPb91xR7xKMSsZIPBwBWCIK8um6bLzdTa0nvYS39FUl9naT/wDVPLH8QHBdTooQVdJ0mLTXumdPxZCMDDcABWycnKhEBERAREQEREBERAREQVNWsyVNPlnhDTI3G0OGRzOFVfqrnUIpYmhs3HZDKx45sJOCFa1atJb0+WCLbvdjG48uRBVTU9Kknsw2Krg1+9hmaTgOAOQfig2+83bdmdlHgxxQOLDJKCS9w6gAHkFu060+w2WOdjWTwP2SNacjPUEehWgQXaNic0o4ZoZnmTY95aWOPXn4Ldp1WSATS2HtdYnfvfs6DlgAIKdvU7UcF50LYy+GwyKMEdxx1+qi7rLmaXBYrNYZ5gcNcPu7fv5HphTY02xJHcDdn2tpkrO1jsjH8KJtGc6bUHxuBE0ZbA0nAYXfe/PCCxfu2I69VtVrHWrAy1rumAMu/vqj9TL6VSWtGHz2iGxsccAHvz6Bav8AG2pbjJX2DAIYmxxGLBJ82cjxWqPSrUTXCKZpdDNxa73/AIsjtBwA5fJBerPvMsBlp9WSMg5MeWuafDB6qjR1C3aYx7rlBm52OG4YfjOMdeqzioTzajFblq1q2wlzixxc6QkY646LCjQt1WRxv0+i/a/PFc/tYznP3e5B3D1KhEQEREBERAREQEREFTU7TqkDTEwPnkkbHEw/iJVY6q73Knb2N2PkEc4OewenL5rK7TtWtSheyQQxQMJY/AcS8+nwWqDTZgy/VncJIbH2jZMAds9eXdzwUFye28ajXqRNa7e0vlJ/C0ch+arVtUdNqLoNjW13OeyGQdXOb1WunWvxV7ViZjXXpGCNgDhjAGBz+OStTtIuQ06/BsiSSs4Pji2BoJ7+18ygsWNUlibdc2OM+7zxxtznmHYyT9V1XkMBLjhoGSVxLdC0+HUNkYLprEckY3DmBjP7rfd/yF2pLAKogMhDS7ih2Gk8z9P1QZaRqUl2RzJ4RFlokhA/Ewnr8f5VUare90kt7KhiY9w2F7t5AOPqto0+5BcqWWWOOIvs3NLAzEZ/oVIaXYFaWE6ZCZ3PcWWTKAW5OQfHkg7MNt0uovr7NrRA2Xn1yT0Kr2rtpupGrWFYAQiUumcR3kY5fBYuivV9RM7IBYDq7Iy7iBvaHU81quVJ5tQbZk06Odjq7WGN0jew/JKDrVnSuhaZzEZO/hElvyyti00mbKzG+7tr4z9k0ghv0W5AREQEREBFOEwghFOEwghFOEwghFOEwghFOEwghFOEwghFOEwghFOEwghFOEwghFOFCDZw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2pw2oiBw2oiIP//Z"));
                break;
        }

        zodiacIcon.setFitWidth(30);
        zodiacIcon.setFitHeight(30);

        return zodiacIcon;
    }

    private String calculateZodiacSign(int month, int day) {
        // Implement logic to calculate the zodiac sign based on the birth month and day
        // This is a simple example; you may replace it with a more accurate algorithm
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            return "Aries";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            return "Taurus";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) {
            return "Gemini";
        } else if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) {
            return "Cancer";
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            return "Leo";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            return "Virgo";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) {
            return "Libra";
        } else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) {
            return "Scorpio";
        } else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) {
            return "Sagittarius";
        } else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
            return "Capricorn";
        } else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            return "Aquarius";
        } else {
            return "Pisces";
        }
    }

    private void openLink(String url) {
        // Implement opening a link in the default web browser
        // You can use java.awt.Desktop for this purpose
    }

    private void clearForm() {
        picturePath = null;
        biographyText = null;
        accentColor = null;
        birthdayPicker.setValue(null);
        experienceTextArea.clear();
        educationTextArea.clear();
        projectsTextArea.clear();
        twitterHandleField.clear();
        fontField.clear();
        skillsTextField.clear();
        linkedinProfileField.clear();
        selectedImageView.setImage(null);
        selectedImageView.setStyle(null);
    }

    private void exportAsPDF(GridPane gridPane) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();

            double margin = 50;
            double yStart = page.getMediaBox().getHeight() - margin;
            double yPosition = yStart;
            double tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            double yBottom = margin;
            float tableHeight = (float) (yStart - yBottom);
            float margin2 = 50;
            float yStart2 = page.getMediaBox().getHeight() - margin2;
            float nextTextX = 75;
            float nextTextY = yStart2;

            for (int i = 0; i < gridPane.getChildren().size(); i++) {
                String text = "";
                javafx.scene.Node node = gridPane.getChildren().get(i);

                if (node instanceof Label) {
                    text = ((Label) node).getText();
                } else if (node instanceof TextField) {
                    text = ((TextField) node).getText();
                } else if (node instanceof TextArea) {
                    text = ((TextArea) node).getText();
                }

                if (!text.isEmpty()) {
                    contentStream.newLineAtOffset(nextTextX, nextTextY);
                    contentStream.showText(text);
                    nextTextY -= 15;

                    if (nextTextY <= margin2) {
                        contentStream.endText();
                        contentStream.close();
                        PDPage newPage = new PDPage();
                        document.addPage(newPage);
                        contentStream = new PDPageContentStream(document, newPage);
                        contentStream.beginText();
                        nextTextY = yStart2;
                    }
                }
            }

            contentStream.endText();
            contentStream.close();

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                document.save(file);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
