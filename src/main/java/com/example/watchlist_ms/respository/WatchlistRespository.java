package com.example.watchlist_ms.respository;

import com.example.watchlist_ms.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WatchlistRespository extends JpaRepository<Watchlist, Integer> {
    List<Watchlist> findAllByIsWatched(boolean watched);

    Watchlist findFirstByMovieUuid(String movieUuid);

    Watchlist findFirstByUuid(String uuid);

    @Transactional
    void deleteWatchlistByMovieUuid(String movieUuid);
}
