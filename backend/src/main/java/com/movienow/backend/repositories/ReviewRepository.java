package com.movienow.backend.repositories;

import com.movienow.backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    public Optional<Review> findReviewById(Long id);

    public Optional<Review> findReviewByUserIdAndMovieId(Long userId, Long movieId);

    public Optional<Review> findReviewByUserId(Long userId);






}
