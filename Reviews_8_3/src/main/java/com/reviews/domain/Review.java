package com.reviews.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotBlank(message = "Название продукта не может быть пустым")
    @Column(nullable = false)
    private String product;

    @Min(value = 1, message = "Рейтинг должен быть не менее 1")
    @Max(value = 5, message = "Рейтинг должен быть не более 5")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 1000, message = "Комментарий не должен превышать 1000 символов")
    @Column(length = 1000)
    private String comment;

    // Конструктор по умолчанию (обязателен для JPA)
    public Review() {
    }

    // Конструктор со всеми полями (кроме id)
    public Review(String userName, String product, Integer rating, String comment) {
        this.userName = userName;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }
}

