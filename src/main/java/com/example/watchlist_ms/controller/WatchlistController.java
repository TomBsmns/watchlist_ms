package com.example.watchlist_ms.controller;

import com.example.watchlist_ms.model.Watchlist;
import com.example.watchlist_ms.respository.WatchlistRespository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class WatchlistController {
    @Autowired
    private WatchlistRespository watchlistRespository;

    @GetMapping("/watchlist/all")
    public List<Watchlist> getAll(){
        return watchlistRespository.findAll();
    }

    @GetMapping("/watchlist/{uuid}")
    public Watchlist getOne(@PathVariable UUID uuid){
        return watchlistRespository.findFirstByUuid(uuid.toString());
    }

    @GetMapping("/watchlist/watched")
    public List<Watchlist> getWatchlistWatched(){
        return watchlistRespository.findAllByIsWatched(true);
    }

    @PostMapping("watchlist/{movieUuid}")
    public Watchlist add(@PathVariable UUID movieUuid){
        Watchlist watchlist = new Watchlist(movieUuid);
        watchlistRespository.save(watchlist);
        return watchlist;
    }

    @PutMapping("watchlist/watched/{uuid}")
    public Watchlist setWatched(@PathVariable UUID uuid){
        Watchlist watchlist = watchlistRespository.findFirstByUuid(uuid.toString());
        watchlist.setIsWatched(true);
        watchlistRespository.save(watchlist);
        return watchlist;
    }

    @DeleteMapping("watchlist/{movieUuid}")
    public JSONObject delete(@PathVariable UUID movieUuid){
        watchlistRespository.deleteWatchlistByMovieUuid(movieUuid.toString());
        JSONObject response = new JSONObject();
        response.put("message", "Movie successfully removed from watchlist");
        return response;
    }
}
