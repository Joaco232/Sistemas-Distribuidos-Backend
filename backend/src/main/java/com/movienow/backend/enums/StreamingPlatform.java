package com.movienow.backend.enums;

public enum StreamingPlatform {

    AHA("aha"),
    AMAZON_PRIME_VIDEO("Amazon Prime Video"),
    ARGO("Argo"),
    BLOODSTREAM("Bloodstream"),
    BROADWAY_HD("BroadwayHD"),
    CRUNCHYROLL("Crunchyroll"),
    CULTPIX("Cultpix"),
    CURIOSITY_STREAM("Curiosity Stream"),
    DEKKOO("Dekkoo"),
    DIRECTV_GO("DIRECTV GO"),
    DISNEY_PLUS("Disney Plus"),
    DOCALLIANCE_FILMS("DocAlliance Films"),
    DOCSVILLE("DOCSVILLE"),
    EVENTIVE("Eventive"),
    FILMBOX_PLUS("FilmBox+"),
    FILMZIE("Filmzie"),
    FLIXOLE("FlixOlé"),
    FOUND_TV("FOUND TV"),
    HBO_MAX("HBO Max"),
    HOICHOI("Hoichoi"),
    IQIYI("iQIYI"),
    JOLT_FILM("Jolt Film"),
    JUSTWATCH_TV("JustWatchTV"),
    MAGELLAN_TV("Magellan TV"),
    MERCADO_PLAY("Mercado Play"),
    MUBI("MUBI"),
    NETFLIX("Netflix"),
    PARAMOUNT_PLUS("Paramount Plus"),
    PLEX("Plex"),
    PLUTO_TV("Pluto TV"),
    RAKUTEN_VIKI("Rakuten Viki"),
    RUNTIME("Runtime"),
    SUN_NT("Sun Nxt"),
    TAKFLIX("Takflix"),
    TENTKOTTA("Tentkotta"),
    TRUE_STORY("True Story"),
    VIX("VIX"),
    ZEE5("Zee5");

    private final String displayName;

    StreamingPlatform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StreamingPlatform fromString(String name) {
        for (StreamingPlatform platform : StreamingPlatform.values()) {
            if (platform.displayName.equalsIgnoreCase(name) || platform.name().equalsIgnoreCase(name)) {
                return platform;
            }
        }
        throw new IllegalArgumentException("Unknown platform: " + name);
    }
}
