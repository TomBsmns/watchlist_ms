package com.example.watchlist_ms;

import com.example.watchlist_ms.model.Watchlist;
import com.example.watchlist_ms.respository.WatchlistRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class WatchlistmsControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchlistRespository watchlistRespository;



    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void requestAllItemsOnWatchlist_thenReturnJsonWatchlistList() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));

        List<Watchlist> result = Collections.singletonList(watchlist);
        given(watchlistRespository.findAll()).willReturn(result);
        mockMvc.perform(get("/watchlist/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void requestOneWatchlistItem_thenReturnJsonWatchlist() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));

        given(watchlistRespository.findFirstByUuid(watchlist.getUuid())).willReturn(watchlist);
        mockMvc.perform(get("/watchlist/{uuid}", watchlist.getUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(watchlist.getUuid())))
                .andExpect(jsonPath("$.movieUuid", is(watchlist.getMovieUuid())))
                .andExpect(jsonPath("$.isWatched", is(watchlist.getIsWatched())))
                ;
    }

    @Test
    public void addWatchlistItem_thenReturnJsonWatchlist() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));

        mockMvc.perform(post("/watchlist/{uuid}", watchlist.getMovieUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieUuid", is(watchlist.getMovieUuid())))
                .andExpect(jsonPath("$.isWatched", is(false)));

    }

    @Test
    public void setWatchlistItemWatched_thenReturnJsonWatchlist() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));

        given(watchlistRespository.findFirstByUuid(watchlist.getUuid())).willReturn(watchlist);
        mockMvc.perform(post("/watchlist/watched/{uuid}", watchlist.getUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(watchlist.getUuid())))
                .andExpect(jsonPath("$.movieUuid", is(watchlist.getMovieUuid())))
                .andExpect(jsonPath("$.isWatched", is(true)))
        ;
    }

    @Test
    public void requestAllWatchedItemsOnWatchlist_thenReturnJsonWatchlistList() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));
        watchlist.setIsWatched(true);
        List<Watchlist> result = Collections.singletonList(watchlist);
        given(watchlistRespository.findAllByIsWatched(true)).willReturn(result);
        mockMvc.perform(get("/watchlist/watched"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void removeWatchlistItem_thenReturnSuccessMessage() throws Exception {
        Watchlist watchlist
                = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));
        mockMvc.perform(delete("/watchlist/{movieUuid}", watchlist.getMovieUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie successfully removed from watchlist")))
        ;
    }
}
