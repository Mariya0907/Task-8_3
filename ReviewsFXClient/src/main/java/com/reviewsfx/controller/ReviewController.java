package com.reviewsfx.controller;

import com.reviewsfx.model.Review;
import com.reviewsfx.service.ReviewApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class ReviewController {
    private final ReviewApiService apiService;
    private final ObservableList<Review> reviews;
    private final TableView<Review> tableView;
    private final TextField userNameField;
    private final TextField productField;
    private final Spinner<Integer> ratingSpinner;
    private final TextArea commentArea;
    private final Label statusLabel;
    private Review selectedReview;

    public ReviewController() {
        this.apiService = new ReviewApiService();
        this.reviews = FXCollections.observableArrayList();
        this.tableView = createTableView();
        this.userNameField = new TextField();
        this.productField = new TextField();
        this.ratingSpinner = new Spinner<>(1, 5, 1);
        this.commentArea = new TextArea();
        this.statusLabel = new Label();
        this.selectedReview = null;

        loadReviews();
    }

    private TableView<Review> createTableView() {
        TableView<Review> table = new TableView<>();
        table.setItems(reviews);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Review, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Review, String> userNameColumn = new TableColumn<>("Имя пользователя");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userNameColumn.setPrefWidth(150);

        TableColumn<Review, String> productColumn = new TableColumn<>("Продукт");
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        productColumn.setPrefWidth(150);

        TableColumn<Review, Integer> ratingColumn = new TableColumn<>("Рейтинг");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setPrefWidth(80);

        TableColumn<Review, String> commentColumn = new TableColumn<>("Комментарий");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        commentColumn.setPrefWidth(300);

        table.getColumns().addAll(idColumn, userNameColumn, productColumn, ratingColumn, commentColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedReview = newSelection;
                fillForm(newSelection);
            }
        });

        return table;
    }

    private void fillForm(Review review) {
        userNameField.setText(review.getUserName());
        productField.setText(review.getProduct());
        ratingSpinner.getValueFactory().setValue(review.getRating());
        commentArea.setText(review.getComment() != null ? review.getComment() : "");
    }

    private void clearForm() {
        userNameField.clear();
        productField.clear();
        ratingSpinner.getValueFactory().setValue(1);
        commentArea.clear();
        selectedReview = null;
        tableView.getSelectionModel().clearSelection();
    }

    private void loadReviews() {
        try {
            List<Review> reviewList = apiService.getAllReviews();
            reviews.clear();
            reviews.addAll(reviewList);
            showStatus("Отзывы загружены успешно", Color.GREEN);
        } catch (Exception e) {
            showStatus("Ошибка загрузки: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void createReview() {
        try {
            if (userNameField.getText().trim().isEmpty() || productField.getText().trim().isEmpty()) {
                showStatus("Заполните все обязательные поля", Color.RED);
                return;
            }

            Review review = new Review(
                    userNameField.getText().trim(),
                    productField.getText().trim(),
                    ratingSpinner.getValue(),
                    commentArea.getText().trim()
            );

            apiService.createReview(review);
            loadReviews();
            clearForm();
            showStatus("Отзыв добавлен успешно", Color.GREEN);
        } catch (Exception e) {
            showStatus("Ошибка добавления: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void updateReview() {
        try {
            if (selectedReview == null) {
                showStatus("Выберите отзыв для обновления", Color.RED);
                return;
            }

            if (userNameField.getText().trim().isEmpty() || productField.getText().trim().isEmpty()) {
                showStatus("Заполните все обязательные поля", Color.RED);
                return;
            }

            Review review = new Review(
                    userNameField.getText().trim(),
                    productField.getText().trim(),
                    ratingSpinner.getValue(),
                    commentArea.getText().trim()
            );

            apiService.updateReview(selectedReview.getId(), review);
            loadReviews();
            clearForm();
            showStatus("Отзыв обновлен успешно", Color.GREEN);
        } catch (Exception e) {
            showStatus("Ошибка обновления: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void deleteReview() {
        try {
            if (selectedReview == null) {
                showStatus("Выберите отзыв для удаления", Color.RED);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Удалить отзыв?");
            alert.setContentText("Вы уверены, что хотите удалить этот отзыв?");

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                apiService.deleteReview(selectedReview.getId());
                loadReviews();
                clearForm();
                showStatus("Отзыв удален успешно", Color.GREEN);
            }
        } catch (Exception e) {
            showStatus("Ошибка удаления: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
    }

    public VBox getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Заголовок
        Label titleLabel = new Label("Управление отзывами");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        // Таблица
        VBox tableBox = new VBox(5);
        Label tableLabel = new Label("Список отзывов:");
        tableBox.getChildren().addAll(tableLabel, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Форма
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        formBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");

        Label formLabel = new Label("Форма отзыва:");
        formLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Имя пользователя:"), 0, 0);
        formGrid.add(userNameField, 1, 0);
        formGrid.add(new Label("Продукт:"), 0, 1);
        formGrid.add(productField, 1, 1);
        formGrid.add(new Label("Рейтинг:"), 0, 2);
        formGrid.add(ratingSpinner, 1, 2);
        formGrid.add(new Label("Комментарий:"), 0, 3);
        formGrid.add(commentArea, 1, 3);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        formGrid.getColumnConstraints().addAll(col1, col2);

        commentArea.setPrefRowCount(3);
        commentArea.setWrapText(true);

        // Кнопки
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Добавить");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> createReview());

        Button updateButton = new Button("Обновить");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        updateButton.setOnAction(e -> updateReview());

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> deleteReview());

        Button clearButton = new Button("Очистить");
        clearButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        clearButton.setOnAction(e -> clearForm());

        Button refreshButton = new Button("Обновить список");
        refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        refreshButton.setOnAction(e -> loadReviews());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton, refreshButton);

        formBox.getChildren().addAll(formLabel, formGrid, buttonBox);

        // Статус
        statusLabel.setStyle("-fx-font-size: 12px;");
        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER);

        // Разделитель
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(tableBox, formBox);
        splitPane.setDividerPositions(0.6);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        root.getChildren().addAll(titleBox, splitPane, statusBox);

        return root;
    }
}

