package com.reviews.controller;

import com.reviews.domain.Review;
import com.reviews.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        model.addAttribute("review", new Review());
        return "reviews/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String create(@Valid @ModelAttribute("review") Review review, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reviews", reviewService.findAll());
            return "reviews/list";
        }
        reviewService.create(review);
        return "redirect:/reviews";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("review", reviewService.getById(id));
        return "reviews/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("review") Review review, BindingResult result) {
        if (result.hasErrors()) {
            return "reviews/edit";
        }
        reviewService.update(id, review);
        return "redirect:/reviews";
    }
}





