package com.reviews.config;

import com.reviews.domain.Review;
import com.reviews.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct; // Изменено с javax на jakarta
import java.util.Arrays;

@Component
public class DataInitializer {

    @Autowired
    private ReviewRepository reviewRepository;

    @PostConstruct
    public void seed() {
        // Проверяем, есть ли уже отзывы в базе
        if (reviewRepository.count() > 0) {
            return;
        }

        // Создаем тестовые отзывы
        Review r1 = new Review();
        r1.setUserName("Мария");
        r1.setProduct("Телефон X100");
        r1.setRating(5);
        r1.setComment("Отличный телефон! Очень довольна покупкой.");

        Review r2 = new Review();
        r2.setUserName("Олег");
        r2.setProduct("Наушники Bass+");
        r2.setRating(4);
        r2.setComment("Хорошие наушники, но бас мог бы быть лучше");

        Review r3 = new Review();
        r3.setUserName("Екатерина");
        r3.setProduct("Ноутбук Ultra");
        r3.setRating(3);
        r3.setComment("Нормальный ноутбук, но греется при нагрузке");

        // Сохраняем все отзывы
        reviewRepository.saveAll(Arrays.asList(r1, r2, r3));
    }
}

