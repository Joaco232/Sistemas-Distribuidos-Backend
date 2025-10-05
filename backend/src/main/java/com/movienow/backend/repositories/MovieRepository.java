package com.movienow.backend.repositories;

import com.movienow.backend.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    public Optional<Movie> findMovieById(Long id);

    public Optional<Movie> findMovieByTitle(String title);

    public boolean existsByTitle(String title);

    public List<Movie> findAllByGenresContaining(String genre);

}
