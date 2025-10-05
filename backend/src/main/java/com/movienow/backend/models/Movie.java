package com.movienow.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "MOVIES")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;

    @Column(name = "EXTERNAL_ID", length = 50)
    private String externalId;

    @Column(name = "TITLE", nullable = false, length = 255)
    @NotNull(message = "El título no puede ser nulo.")
    @Size(min = 1, max = 255, message = "El título debe contener entre 1 y 255 caracteres.")
    private String title;

    @Column(name = "ORIGINAL_TITLE", length = 255)
    @Size(max = 255, message = "El título original no puede superar 255 caracteres.")
    private String originalTitle;

    @Column(name = "CONTENT_RATING", length = 10)
    private String contentRating; // Ej: "PG-13", "R"

    @Column(name = "RATING_MOVIENOW")
    @Min(value = 0, message = "La puntuación mínima es 0.")
    @Max(value = 10, message = "La puntuación máxima es 10.")
    private Integer ratingMovieNow;

    @Column(name = "VOTE_COUNT")
    @Min(value = 0, message = "El número de votos no puede ser negativo.")
    private Integer voteCount;

    @Column(name = "STREAMING_PLATFORMS", length = 500)
    private String streamingPlatforms; // Ej: "Netflix, HBO Max"

    @Column(name = "POPULARITY")
    private Float popularity;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "DESCRIPTION", length = 2000)
    @Size(max = 2000, message = "La descripción no puede superar 2000 caracteres.")
    private String description;

    @Column(name = "POSTER_URL", length = 254)
    @Size(max = 254, message = "La URL del póster no puede superar 254 caracteres.")
    private String posterUrl;

    @Column(name = "BACKDROP_URL", length = 254)
    @Size(max = 254, message = "La URL del fondo no puede superar 254 caracteres.")
    private String backdropUrl;

    @Column(name = "DURATION")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos.")
    private Long duration; // minutos

    @Column(name = "ORIGINAL_LANGUAGE", length = 10)
    private String originalLanguage; // Ej: "en", "es"

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MOVIE_DIRECTORS", joinColumns = @JoinColumn(name = "MOVIE_ID"))
    @Column(name = "DIRECTOR")
    private List<String> directors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MOVIE_ACTORS", joinColumns = @JoinColumn(name = "MOVIE_ID"))
    @Column(name = "ACTOR")
    private List<String> actors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MOVIE_GENRES", joinColumns = @JoinColumn(name = "MOVIE_ID"))
    @Column(name = "GENRE")
    private List<String> genres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "ORIGIN_COUNTRY", length = 100)
    @Size(max = 100, message = "El país de origen no puede superar 100 caracteres.")
    private String originCountry;

    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
