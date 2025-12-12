package huffman.ui;

import huffman.algorithm.*;
import huffman.model.HuffmanNode;
import huffman.model.HuffmanTree;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HuffmanTreeVisualizer extends Application {

    private HuffmanTree huffmanTree;
    private List<List<HuffmanNode>> constructionSteps;
    private int currentStep = 0;
    private char currentHighlightedChar = '\0';
    private int animationStep = 0;
    private Timer animationTimer;
    private Timer autoPlayTimer;
    private boolean isAutoPlaying = false;
    private List<Character> autoPlayCharacters;
    private int currentAutoPlayIndex = 0;

    private Canvas canvas;
    private Label stepLabel;
    private Label infoLabel;
    private Button prevButton;
    private Button nextButton;
    private Button finalTreeButton;
    private Button showCodeTableButton;
    private Button showPathButton;
    private Button loadFileButton;
    private Button loadNewFileButton;
    private Button showEncodedTextButton;
    private ComboBox<String> characterComboBox;
    private Label pathInfoLabel;
    private Label fileInfoLabel;
    private Button autoPlayButton;
    private Button stopAnimationButton;

    private static final int CANVAS_WIDTH = 1600;
    private static final int CANVAS_HEIGHT = 800;
    private static final int NODE_RADIUS = 20;
    private static final Color NODE_COLOR = Color.web("#3498db");
    private static final Color LEAF_COLOR = Color.web("#2ecc71");
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color LINE_COLOR = Color.web("#34495e");
    private static final Color HIGHLIGHT_COLOR = Color.web("#E74C3C");
    private static final Color PATH_COLOR = Color.web("#F39C12");

    private File currentFile;
    private String currentText = "";

    private static class NodePosition {
        double x, y;
        NodePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        currentText = "Huffman coding is a popular algorithm used for lossless data compression. It was developed by David A. Huffman.";

        initializeHuffmanTree();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");

        VBox topSection = createTopSection();
        root.setTop(topSection);

        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        VBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        updateVisualization();

        Scene scene = new Scene(root, 1800, 900);
        primaryStage.setTitle("Visualisateur d'Arbre de Huffman - Chargement de Fichiers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeHuffmanTree() {
        huffmanTree = TreeBuilder.buildHuffmanTree(currentText);
        constructionSteps = huffmanTree.getConstructionSteps();
        huffmanTree.generateMycode();
        currentStep = 0;
    }

    private String loadTextFromFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            showErrorDialog("Erreur de lecture", "Impossible de lire le fichier: " + e.getMessage());
            return null;
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadNewFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier texte");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileContent = loadTextFromFile(selectedFile);
            if (fileContent != null && !fileContent.trim().isEmpty()) {
                currentFile = selectedFile;
                currentText = fileContent;
                initializeHuffmanTree();
                updateVisualization();
                fileInfoLabel.setText("📁 Fichier: " + selectedFile.getName() + " (" + fileContent.length() + " caractères)");
                pathInfoLabel.setText("✅ Fichier chargé avec succès - " + constructionSteps.size() + " étapes de construction");
            } else if (fileContent != null) {
                showErrorDialog("Fichier vide", "Le fichier sélectionné est vide.");
            }
        }
    }

    private String encodeText(String text) {
        if (huffmanTree == null || huffmanTree.getCode() == null) {
            return null;
        }

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            String code = huffmanTree.getCode().get(c);
            if (code != null) {
                encoded.append(code);
            }
        }
        return encoded.toString();
    }

    private void showEncodedText() {
        if (huffmanTree == null || huffmanTree.getCode() == null) {
            showErrorDialog("Erreur", "Aucun arbre de Huffman n'est disponible.");
            return;
        }

        String encodedText = encodeText(currentText);
        if (encodedText == null || encodedText.isEmpty()) {
            showErrorDialog("Erreur", "Impossible d'encoder le texte.");
            return;
        }

        Stage encodedStage = new Stage();
        encodedStage.setTitle("📝 Texte Codé - Huffman");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        String fileName = currentFile != null ? currentFile.getName() : "Texte par défaut";
        Label titleLabel = new Label("TEXTE CODÉ HUFFMAN - " + fileName.toUpperCase());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        titleLabel.setAlignment(Pos.CENTER);

        Label statsLabel = new Label();
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statsLabel.setTextFill(Color.web("#27ae60"));
        statsLabel.setAlignment(Pos.CENTER);

        int originalBits = currentText.length() * 8;
        int encodedBits = encodedText.length();
        double compressionRatio = (1 - (double) encodedBits / originalBits) * 100;

        statsLabel.setText(String.format(
                "📊 Compression: %d bits → %d bits (%.1f%% d'économie)",
                originalBits, encodedBits, compressionRatio
        ));

        VBox originalBox = new VBox(5);
        originalBox.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 10;");

        Label originalTitle = new Label("📄 TEXTE ORIGINAL:");
        originalTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        originalTitle.setTextFill(Color.web("#2C3E50"));

        TextArea originalTextArea = new TextArea();
        originalTextArea.setEditable(false);
        originalTextArea.setWrapText(true);
        originalTextArea.setPrefHeight(100);
        originalTextArea.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12;");
        originalTextArea.setText(currentText);

        Label originalStats = new Label("Longueur: " + currentText.length() + " caractères • " + originalBits + " bits (ASCII)");
        originalStats.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        originalStats.setTextFill(Color.web("#7f8c8d"));

        originalBox.getChildren().addAll(originalTitle, originalTextArea, originalStats);

        VBox encodedBox = new VBox(5);
        encodedBox.setStyle("-fx-background-color: #fff9e6; -fx-border-color: #f39c12; -fx-border-radius: 5; -fx-padding: 10;");

        Label encodedTitle = new Label("🔐 TEXTE CODÉ HUFFMAN:");
        encodedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        encodedTitle.setTextFill(Color.web("#e67e22"));

        TextArea encodedTextArea = new TextArea();
        encodedTextArea.setEditable(false);
        encodedTextArea.setWrapText(true);
        encodedTextArea.setPrefHeight(150);
        encodedTextArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11; -fx-font-weight: bold;");
        encodedTextArea.setText(formatEncodedText(encodedText));

        Label encodedStats = new Label("Longueur: " + encodedText.length() + " bits • " +
                String.format("Ratio de compression: %.1f%%", compressionRatio));
        encodedStats.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        encodedStats.setTextFill(Color.web("#e67e22"));

        encodedBox.getChildren().addAll(encodedTitle, encodedTextArea, encodedStats);

        VBox codesBox = new VBox(5);
        codesBox.setStyle("-fx-background-color: #e8f4f8; -fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 10;");

        Label codesTitle = new Label("📋 TABLE DES CODES (aperçu):");
        codesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        codesTitle.setTextFill(Color.web("#2980b9"));

        TextArea codesTextArea = new TextArea();
        codesTextArea.setEditable(false);
        codesTextArea.setPrefHeight(120);
        codesTextArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11;");
        codesTextArea.setText(generateCodesPreview());

        codesBox.getChildren().addAll(codesTitle, codesTextArea);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button copyEncodedButton = createStyledButton("📋 Copier le texte codé", "#27ae60", 12);
        copyEncodedButton.setOnAction(e -> {
            String encoded = encodedTextArea.getText().replaceAll("\\s", "");
            ClipboardContent content = new ClipboardContent();
            content.putString(encoded);
            javafx.scene.input.Clipboard.getSystemClipboard().setContent(content);
            showInfoDialog("Succès", "Texte codé copié dans le presse-papiers !");
        });

        Button closeButton = createStyledButton("Fermer", "#e74c3c", 12);
        closeButton.setOnAction(e -> encodedStage.close());

        buttonBox.getChildren().addAll(copyEncodedButton, closeButton);

        root.getChildren().addAll(titleLabel, statsLabel, originalBox, encodedBox, codesBox, buttonBox);

        Scene scene = new Scene(root, 700, 800);
        encodedStage.setScene(scene);
        encodedStage.show();
    }

    private String formatEncodedText(String encodedText) {
        StringBuilder formatted = new StringBuilder();
        int groupSize = 8;
        int count = 0;

        for (int i = 0; i < encodedText.length(); i++) {
            formatted.append(encodedText.charAt(i));
            count++;

            if (count % groupSize == 0 && i != encodedText.length() - 1) {
                formatted.append(" ");
            }
        }

        return formatted.toString();
    }

    private String generateCodesPreview() {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (Map.Entry<Character, String> entry : huffmanTree.getCode().entrySet()) {
            if (count >= 10) {
                sb.append("... et ").append(huffmanTree.getCode().size() - 10).append(" autres caractères");
                break;
            }

            char c = entry.getKey();
            String code = entry.getValue();
            String displayChar = formatCharacterForComboBox(c);
            sb.append(String.format("%-8s : %s\n", displayChar, code));
            count++;
        }

        return sb.toString();
    }

    private VBox createTopSection() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: linear-gradient(to right, #2980b9, #2c3e50);");

        Label titleLabel = new Label("🌳 Visualisateur d'Arbre de Huffman - Chargement de Fichiers");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        infoLabel = new Label("Chargez un fichier texte pour visualiser son arbre de Huffman");
        infoLabel.setFont(Font.font("Arial", 14));
        infoLabel.setTextFill(Color.web("#BDC3C7"));

        fileInfoLabel = new Label("📁 Texte par défaut (" + currentText.length() + " caractères)");
        fileInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        fileInfoLabel.setTextFill(Color.web("#BDC3C7"));

        vbox.getChildren().addAll(titleLabel, infoLabel, fileInfoLabel);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private VBox createCenterSection() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.CENTER);

        stepLabel = new Label("Étape 1 / " + constructionSteps.size());
        stepLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        stepLabel.setTextFill(Color.web("#2C3E50"));

        pathInfoLabel = new Label("");
        pathInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pathInfoLabel.setTextFill(Color.web("#E74C3C"));
        pathInfoLabel.setAlignment(Pos.CENTER);
        pathInfoLabel.setPrefWidth(CANVAS_WIDTH);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);");

        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setPrefViewportWidth(1500);
        scrollPane.setPrefViewportHeight(600);
        scrollPane.setStyle("-fx-background: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8;");
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        vbox.getChildren().addAll(stepLabel, pathInfoLabel, scrollPane);
        return vbox;
    }

    private VBox createBottomSection() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white; -fx-border-color: #ecf0f1; -fx-border-width: 1 0 0 0;");

        HBox fileControlBox = new HBox(15);
        fileControlBox.setAlignment(Pos.CENTER);

        loadFileButton = createStyledButton("📁 Charger un Fichier", "#16a085", 14);
        loadFileButton.setOnAction(e -> loadNewFile());

        loadNewFileButton = createStyledButton("🔄 Changer de Fichier", "#3498db", 14);
        loadNewFileButton.setOnAction(e -> loadNewFile());

        showEncodedTextButton = createStyledButton("🔐 Afficher le Texte Codé", "#f39c12", 14);
        showEncodedTextButton.setOnAction(e -> showEncodedText());

        fileControlBox.getChildren().addAll(loadFileButton, loadNewFileButton, showEncodedTextButton);

        HBox pathControlBox = new HBox(15);
        pathControlBox.setAlignment(Pos.CENTER);

        Label pathLabel = new Label("Afficher le chemin pour:");
        pathLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        pathLabel.setTextFill(Color.web("#2C3E50"));

        characterComboBox = new ComboBox<>();
        characterComboBox.setPrefWidth(120);
        characterComboBox.setStyle("-fx-font-size: 14;");

        showPathButton = createStyledButton("🚀 Afficher le Chemin", "#E74C3C", 14);
        showPathButton.setOnAction(e -> showCharacterPath());

        autoPlayButton = createStyledButton("▶ Lecture Auto Tous", "#9B59B6", 14);
        autoPlayButton.setOnAction(e -> startAutoPlay());

        stopAnimationButton = createStyledButton("⏹ Arrêter", "#95A5A6", 14);
        stopAnimationButton.setOnAction(e -> stopAnimation());

        pathControlBox.getChildren().addAll(pathLabel, characterComboBox, showPathButton, autoPlayButton, stopAnimationButton);

        HBox mainButtonBox = new HBox(20);
        mainButtonBox.setAlignment(Pos.CENTER);

        prevButton = createStyledButton("◀◀ Précédent", "#e74c3c", 14);
        prevButton.setOnAction(e -> previousStep());

        nextButton = createStyledButton("Suivant ▶▶", "#27ae60", 14);
        nextButton.setOnAction(e -> nextStep());

        finalTreeButton = createStyledButton("🌳 Arbre Final", "#9b59b6", 14);
        finalTreeButton.setOnAction(e -> showFinalTree());

        showCodeTableButton = createStyledButton("📊 Table des Codes", "#8e44ad", 14);
        showCodeTableButton.setOnAction(e -> showCodeTableWindow());

        mainButtonBox.getChildren().addAll(prevButton, nextButton, finalTreeButton, showCodeTableButton);

        Label instructionLabel = new Label("🎯 Chargez un fichier texte puis visualisez les chemins de codage des caractères");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        instructionLabel.setTextFill(Color.web("#27ae60"));

        Label statsLabel = new Label();
        statsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        statsLabel.setTextFill(Color.web("#7f8c8d"));
        updateStatsLabel(statsLabel);

        vbox.getChildren().addAll(fileControlBox, pathControlBox, mainButtonBox, instructionLabel, statsLabel);
        return vbox;
    }

    private void updateStatsLabel(Label statsLabel) {
        if (huffmanTree != null && huffmanTree.getCode() != null) {
            int uniqueChars = huffmanTree.getCode().size();
            statsLabel.setText("📊 Statistiques: " + uniqueChars + " caractères uniques | " +
                    currentText.length() + " caractères totaux | " +
                    constructionSteps.size() + " étapes de construction");
        }
    }

    private Button createStyledButton(String text, String color, double fontSize) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + darkenColor(color) +
                "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8;"));
        return button;
    }

    private String darkenColor(String color) {
        return color.replaceAll(".(.)(.)(.)", "#$1$1$2$2$3$3");
    }

    private void showCharacterPath() {
        if (characterComboBox.getValue() == null) return;

        String selected = characterComboBox.getValue();
        final char selectedChar;

        if (selected.equals("ESPACE")) selectedChar = ' ';
        else if (selected.equals("NEWLINE")) selectedChar = '\n';
        else if (selected.equals("TAB")) selectedChar = '\t';
        else if (selected.equals("RETURN")) selectedChar = '\r';
        else if (selected.startsWith("0x")) {
            selectedChar = (char) Integer.parseInt(selected.substring(2), 16);
        } else {
            selectedChar = selected.charAt(0);
        }

        startCharacterAnimation(selectedChar);
    }

    private void startCharacterAnimation(final char character) {
        stopAnimationTimers();
        isAutoPlaying = false;

        currentHighlightedChar = character;
        animationStep = 0;

        final String code = huffmanTree.getCode().get(character);
        if (code == null) return;

        pathInfoLabel.setText("Chemin en cours: " + formatCharacterForDisplay(character) + " → " + code);

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    animationStep++;
                    updateVisualization();

                    if (animationStep >= code.length() * 2 + 10) {
                        animationTimer.cancel();
                        pathInfoLabel.setText("✓ Chemin complet: " + formatCharacterForDisplay(character) + " = " + code);
                    }
                });
            }
        }, 0, 500);
    }

    private void startAutoPlay() {
        stopAnimationTimers();
        isAutoPlaying = true;

        autoPlayCharacters = new ArrayList<>(huffmanTree.getCode().keySet());
        Collections.sort(autoPlayCharacters, (c1, c2) -> {
            String code1 = huffmanTree.getCode().get(c1);
            String code2 = huffmanTree.getCode().get(c2);
            int lengthCompare = Integer.compare(code1.length(), code2.length());
            if (lengthCompare != 0) return lengthCompare;
            return Character.compare(c1, c2);
        });

        currentAutoPlayIndex = 0;

        if (autoPlayCharacters.isEmpty()) {
            pathInfoLabel.setText("❌ Aucun caractère à afficher");
            return;
        }

        pathInfoLabel.setText("🎬 Démarrage de la lecture auto... " + autoPlayCharacters.size() + " caractères");

        javafx.application.Platform.runLater(() -> {
            playNextCharacter();
        });
    }

    private void playNextCharacter() {
        if (!isAutoPlaying || currentAutoPlayIndex >= autoPlayCharacters.size()) {
            isAutoPlaying = false;
            currentHighlightedChar = '\0';
            pathInfoLabel.setText("✅ Lecture auto terminée - " + autoPlayCharacters.size() + " chemins affichés");
            updateVisualization();
            return;
        }

        char currentChar = autoPlayCharacters.get(currentAutoPlayIndex);
        currentHighlightedChar = currentChar;
        characterComboBox.setValue(formatCharacterForComboBox(currentChar));
        animationStep = 0;

        final String code = huffmanTree.getCode().get(currentChar);
        final int currentIndex = currentAutoPlayIndex;
        final int totalChars = autoPlayCharacters.size();

        pathInfoLabel.setText("🎬 Lecture Auto (" + (currentIndex + 1) + "/" + totalChars +
                "): " + formatCharacterForDisplay(currentChar) + " → " + code);

        if (animationTimer != null) {
            animationTimer.cancel();
        }

        int animationDuration = Math.max(code.length() * 100, 300);

        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    animationStep++;
                    updateVisualization();

                    if (animationStep >= code.length() * 2 + 10) {
                        animationTimer.cancel();

                        currentAutoPlayIndex++;
                        if (currentAutoPlayIndex < autoPlayCharacters.size()) {
                            Timer nextTimer = new Timer();
                            nextTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    javafx.application.Platform.runLater(() -> {
                                        playNextCharacter();
                                    });
                                }
                            }, 50);
                        } else {
                            isAutoPlaying = false;
                            currentHighlightedChar = '\0';
                            pathInfoLabel.setText("✅ Lecture auto terminée - " + totalChars + " chemins affichés");
                            updateVisualization();
                        }
                    }
                });
            }
        }, 0, 80);
    }

    private void stopAnimationTimers() {
        if (animationTimer != null) {
            animationTimer.cancel();
            animationTimer = null;
        }
        if (autoPlayTimer != null) {
            autoPlayTimer.cancel();
            autoPlayTimer = null;
        }
    }

    private void stopAnimation() {
        stopAnimationTimers();
        isAutoPlaying = false;
        currentHighlightedChar = '\0';
        animationStep = 0;
        pathInfoLabel.setText("⏹ Animation arrêtée");
        updateVisualization();
    }

    private void updateCharacterComboBox() {
        characterComboBox.getItems().clear();

        if (huffmanTree.getCode() == null) return;

        huffmanTree.getCode().keySet().stream()
                .sorted((c1, c2) -> {
                    String code1 = huffmanTree.getCode().get(c1);
                    String code2 = huffmanTree.getCode().get(c2);
                    int lengthCompare = Integer.compare(code1.length(), code2.length());
                    if (lengthCompare != 0) return lengthCompare;
                    return Character.compare(c1, c2);
                })
                .forEach(c -> {
                    characterComboBox.getItems().add(formatCharacterForComboBox(c));
                });

        if (!characterComboBox.getItems().isEmpty()) {
            characterComboBox.setValue(characterComboBox.getItems().get(0));
        }
    }

    private String formatCharacterForComboBox(char c) {
        switch (c) {
            case ' ': return "ESPACE";
            case '\n': return "NEWLINE";
            case '\t': return "TAB";
            case '\r': return "RETURN";
            default:
                if (c < 32 || c > 126) return String.format("0x%02X", (int) c);
                return String.valueOf(c);
        }
    }

    private String formatCharacterForDisplay(char c) {
        switch (c) {
            case ' ': return "'ESPACE'";
            case '\n': return "'NEWLINE'";
            case '\t': return "'TAB'";
            case '\r': return "'RETURN'";
            default:
                if (c < 32 || c > 126) return String.format("'0x%02X'", (int) c);
                return "'" + String.valueOf(c) + "'";
        }
    }

    private void showCodeTableWindow() {
        Stage codeTableStage = new Stage();
        codeTableStage.setTitle("📊 Table Complète des Codes Huffman");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        String fileName = currentFile != null ? currentFile.getName() : "Texte par défaut";
        Label titleLabel = new Label("TABLE DES CODES HUFFMAN - " + fileName.toUpperCase());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2C3E50"));
        titleLabel.setAlignment(Pos.CENTER);

        TextArea codeTableTextArea = new TextArea();
        codeTableTextArea.setEditable(false);
        codeTableTextArea.setPrefSize(600, 700);
        codeTableTextArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 12; " +
                "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        codeTableTextArea.setText(generateCompleteCodeTable());

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button closeButton = createStyledButton("Fermer", "#e74c3c", 12);
        closeButton.setOnAction(e -> codeTableStage.close());

        buttonBox.getChildren().addAll(closeButton);

        root.getChildren().addAll(titleLabel, codeTableTextArea, buttonBox);

        Scene scene = new Scene(root, 650, 800);
        codeTableStage.setScene(scene);
        codeTableStage.show();
    }

    private String generateCompleteCodeTable() {
        StringBuilder sb = new StringBuilder();
        String fileName = currentFile != null ? currentFile.getName() : "Texte par défaut";

        sb.append("TABLE DES CODES HUFFMAN - ").append(fileName).append("\n");
        sb.append("=".repeat(50)).append("\n\n");
        sb.append("Fichier: ").append(fileName).append("\n");
        sb.append("Taille: ").append(currentText.length()).append(" caractères\n");
        sb.append("Caractères uniques: ").append(huffmanTree.getCode().size()).append("\n\n");
        sb.append("CODES:\n");
        sb.append("-".repeat(30)).append("\n");

        huffmanTree.getCode().entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().length() - e2.getValue().length())
                .forEach(entry -> {
                    char c = entry.getKey();
                    String displayChar = formatCharacterForComboBox(c);
                    String code = entry.getValue();
                    sb.append(String.format("%-10s : %-8s (longueur: %d)\n", displayChar, code, code.length()));
                });

        return sb.toString();
    }

    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateVisualization();
        }
    }

    private void nextStep() {
        if (currentStep < constructionSteps.size() - 1) {
            currentStep++;
            updateVisualization();
        }
    }

    private void showFinalTree() {
        currentStep = constructionSteps.size() - 1;
        updateVisualization();
    }

    private void updateVisualization() {
        stepLabel.setText("Étape " + (currentStep + 1) + " / " + constructionSteps.size());
        prevButton.setDisable(currentStep == 0);
        nextButton.setDisable(currentStep == constructionSteps.size() - 1);


        boolean isFinalTree = (currentStep == constructionSteps.size() - 1);

        if (isFinalTree) {

            characterComboBox.setDisable(false);
            showPathButton.setDisable(false);
            autoPlayButton.setDisable(false);
            stopAnimationButton.setDisable(false);


            updateCharacterComboBox();
        } else {

            characterComboBox.setDisable(true);
            showPathButton.setDisable(true);
            autoPlayButton.setDisable(true);
            stopAnimationButton.setDisable(true);


            stopAnimationTimers();
            isAutoPlaying = false;
            currentHighlightedChar = '\0';
            animationStep = 0;
        }

        List<HuffmanNode> currentNodes = constructionSteps.get(currentStep);
        drawStep(currentNodes);
    }

    private void drawStep(List<HuffmanNode> nodes) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (nodes.isEmpty()) return;

        if (currentStep == constructionSteps.size() - 1 && nodes.size() == 1) {
            drawCompleteTree(gc, nodes.get(0));
        } else {
            drawNodeList(gc, nodes);
        }
    }

    private void drawCompleteTree(GraphicsContext gc, HuffmanNode root) {
        Map<HuffmanNode, NodePosition> positions = new HashMap<>();
        calculateTreePositions(root, positions, CANVAS_WIDTH / 2, 80, CANVAS_WIDTH / 3, 100);

        drawTreeLines(gc, root, positions);

        if (currentHighlightedChar != '\0') {
            drawHighlightedPath(gc, root, positions, currentHighlightedChar);
        }

        drawAllNodes(gc, positions);

        if (currentHighlightedChar != '\0') {
            String code = huffmanTree.getCode().get(currentHighlightedChar);
            if (code != null) {
                gc.setFill(HIGHLIGHT_COLOR);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                String displayText = formatCharacterForDisplay(currentHighlightedChar) + " = " + code;
                double textWidth = displayText.length() * 12;
                gc.fillText(displayText, CANVAS_WIDTH / 2 - textWidth / 2, CANVAS_HEIGHT - 30);
            }
        }
    }

    private void drawHighlightedPath(GraphicsContext gc, HuffmanNode root, Map<HuffmanNode, NodePosition> positions, char targetChar) {
        String code = huffmanTree.getCode().get(targetChar);
        if (code == null) return;

        HuffmanNode current = root;
        List<HuffmanNode> pathNodes = new ArrayList<>();
        pathNodes.add(root);

        for (int i = 0; i < code.length(); i++) {
            if (current == null) break;

            char bit = code.charAt(i);
            if (bit == '0') {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }

            if (current != null) {
                pathNodes.add(current);
            }
        }

        gc.setStroke(PATH_COLOR);
        gc.setLineWidth(4);
        gc.setLineDashes(null);

        for (int i = 0; i < pathNodes.size() - 1; i++) {
            if (i * 2 < animationStep) {
                NodePosition start = positions.get(pathNodes.get(i));
                NodePosition end = positions.get(pathNodes.get(i + 1));

                if (start != null && end != null) {
                    gc.setStroke(PATH_COLOR);
                    gc.setLineWidth(4);
                    gc.strokeLine(start.x, start.y + NODE_RADIUS, end.x, end.y - NODE_RADIUS);

                    String bit = String.valueOf(code.charAt(i));
                    gc.setFill(HIGHLIGHT_COLOR);
                    gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    double labelX = (start.x + end.x) / 2;
                    double labelY = (start.y + end.y) / 2;

                    gc.setFill(HIGHLIGHT_COLOR.deriveColor(0, 1, 1, 0.2));
                    gc.fillOval(labelX - 12, labelY - 12, 24, 24);

                    gc.setFill(HIGHLIGHT_COLOR);
                    gc.fillText(bit, labelX - 4, labelY + 4);
                }
            }
        }

        if (!pathNodes.isEmpty() && animationStep >= code.length() * 2) {
            NodePosition finalPos = positions.get(pathNodes.get(pathNodes.size() - 1));
            if (finalPos != null) {
                gc.setStroke(HIGHLIGHT_COLOR);
                gc.setLineWidth(3);
                gc.setLineDashes(new double[]{5, 5});
                gc.strokeOval(finalPos.x - NODE_RADIUS - 8, finalPos.y - NODE_RADIUS - 8,
                        (NODE_RADIUS + 8) * 2, (NODE_RADIUS + 8) * 2);
            }
        }
    }

    private void calculateTreePositions(HuffmanNode node, Map<HuffmanNode, NodePosition> positions,
                                        double x, double y, double offset, double verticalSpacing) {
        if (node == null) return;

        positions.put(node, new NodePosition(x, y));

        if (node.getLeft() != null) {
            double childOffset = offset * 0.6;
            calculateTreePositions(node.getLeft(), positions, x - childOffset, y + verticalSpacing,
                    childOffset, verticalSpacing);
        }

        if (node.getRight() != null) {
            double childOffset = offset * 0.6;
            calculateTreePositions(node.getRight(), positions, x + childOffset, y + verticalSpacing,
                    childOffset, verticalSpacing);
        }
    }

    private void drawTreeLines(GraphicsContext gc, HuffmanNode node, Map<HuffmanNode, NodePosition> positions) {
        if (node == null) return;

        NodePosition pos = positions.get(node);

        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(1.5);
        gc.setLineDashes(null);

        if (node.getLeft() != null) {
            NodePosition leftPos = positions.get(node.getLeft());
            gc.strokeLine(pos.x, pos.y + NODE_RADIUS, leftPos.x, leftPos.y - NODE_RADIUS);

            gc.setFill(Color.web("#7F8C8D"));
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            gc.fillText("0", (pos.x + leftPos.x) / 2 - 8, (pos.y + leftPos.y) / 2);
        }

        if (node.getRight() != null) {
            NodePosition rightPos = positions.get(node.getRight());
            gc.strokeLine(pos.x, pos.y + NODE_RADIUS, rightPos.x, rightPos.y - NODE_RADIUS);

            gc.setFill(Color.web("#7F8C8D"));
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            gc.fillText("1", (pos.x + rightPos.x) / 2 + 5, (pos.y + rightPos.y) / 2);
        }

        drawTreeLines(gc, node.getLeft(), positions);
        drawTreeLines(gc, node.getRight(), positions);
    }

    private void drawAllNodes(GraphicsContext gc, Map<HuffmanNode, NodePosition> positions) {
        for (Map.Entry<HuffmanNode, NodePosition> entry : positions.entrySet()) {
            drawTreeNode(gc, entry.getKey(), entry.getValue().x, entry.getValue().y);
        }
    }

    private void drawTreeNode(GraphicsContext gc, HuffmanNode node, double x, double y) {
        boolean isLeaf = node.isLeaf();
        boolean isHighlighted = currentHighlightedChar != '\0' && isLeaf && node.getCharacter() == currentHighlightedChar;

        Color nodeColor = isHighlighted ? HIGHLIGHT_COLOR : (isLeaf ? LEAF_COLOR : NODE_COLOR);
        gc.setFill(nodeColor);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        gc.setStroke(Color.web("#2C3E50"));
        gc.setLineWidth(2);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        String charText = isLeaf ? formatCharacter(node.getCharacter()) : "•";
        String freqText = String.valueOf(node.getFrequency());

        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, isLeaf ? 12 : 11));
        gc.fillText(charText, x - 5, y - 5);

        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        gc.fillText(freqText, x - 8, y + 12);
    }

    private String formatCharacter(char c) {
        switch (c) {
            case ' ': return "␣";
            case '\n': return "↵";
            case '\t': return "→";
            case '\r': return "¶";
            default:
                if (c < 32 || c > 126) return "�";
                return String.valueOf(c);
        }
    }

    private void drawNodeList(GraphicsContext gc, List<HuffmanNode> nodes) {
        int nodeCount = nodes.size();
        int spacing = Math.min(150, (CANVAS_WIDTH - 200) / Math.max(1, nodeCount));
        int startX = (CANVAS_WIDTH - (nodeCount - 1) * spacing) / 2;
        int y = CANVAS_HEIGHT / 2;

        for (int i = 0; i < nodeCount; i++) {
            double x = startX + i * spacing;
            drawTreeNode(gc, nodes.get(i), x, y);
        }

        if (nodeCount > 1) {
            gc.setFill(Color.web("#7F8C8D"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.fillText("Étape " + (currentStep + 1) + " - " + nodeCount + " nœuds à fusionner",
                    CANVAS_WIDTH / 2 - 100, CANVAS_HEIGHT - 50);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}