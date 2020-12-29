package com.example.watchlist_ms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Watchlist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @Column(unique=true)
    private String uuid;
    private String movieUuid;
    private boolean isWatched;

    public Watchlist(){}
    public Watchlist(UUID movieUuid){
        this.movieUuid = movieUuid.toString();
        this.uuid = UUID.randomUUID().toString();
        this.isWatched = false;
    }
    public String getUuid(){return uuid;}
    public String getMovieUuid(){return movieUuid;}
    public boolean getIsWatched(){return isWatched;}
    public int getId(){return id;}
}
