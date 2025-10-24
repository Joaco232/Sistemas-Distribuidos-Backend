package com.movienow.backend.enums;

public enum Genre {

    ACCION(28, "Acción"),
    AVENTURA(12, "Aventura"),
    ANIMACION(16, "Animación"),
    COMEDIA(35, "Comedia"),
    CRIMEN(80, "Crimen"),
    DOCUMENTAL(99, "Documental"),
    DRAMA(18, "Drama"),
    FAMILIA(10751, "Familia"),
    FANTASIA(14, "Fantasía"),
    HISTORIA(36, "Historia"),
    TERROR(27, "Terror"),
    MUSICA(10402, "Música"),
    MISTERIO(9648, "Misterio"),
    ROMANCE(10749, "Romance"),
    CIENCIA_FICCION(878, "Ciencia ficción"),
    PELICULA_TV(10770, "Película de TV"),
    SUSPENSE(53, "Suspense"),
    BELICA(10752, "Bélica"),
    WESTERN(37, "Western");

    private final int id;
    private final String nombre;

    Genre(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static Genre fromId(int id) {
        for (Genre g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        return null;
    }

    public static Genre fromNombre(String nombre) {
        for (Genre g : values()) {
            if (g.nombre.equalsIgnoreCase(nombre)) {
                return g;
            }
        }
        return null;
    }
}
