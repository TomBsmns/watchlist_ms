package com.example.watchlist_ms;

import com.example.watchlist_ms.model.Watchlist;
import com.example.watchlist_ms.respository.WatchlistRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WatchlistmsControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WatchlistRespository watchlistRespository;
    private int initialNumberWatchlist = 0;
    private int initialNumberWatched = 0;

    private Watchlist watchlist1 = new Watchlist(UUID.fromString("21d14364-8e94-41a3-824a-a44df76d59d8"),UUID.fromString("fbedd1a2-e847-448d-b49e-e15c23dd9db1"));
    private Watchlist watchlist2 = new Watchlist(UUID.fromString("97c428b4-1678-4576-8ed7-4db269b33ed3"),UUID.fromString("ab12ba30-ef0a-4762-97c5-f4b49acbeaea"));
    private Watchlist watchlist3 = new Watchlist(UUID.fromString("df9344cd-048f-424b-a2da-7a67850642ed"),UUID.fromString("f80aea44-58f2-48a5-b55f-8d8dfcdf1d69"));
    private Watchlist watchlist4 = new Watchlist(UUID.fromString("9b399632-edcd-49ce-98a7-cc193b85376b"),UUID.fromString("675cc8a2-75c9-4378-ab3b-34dfb27a0fac"));

    @BeforeEach
    public void beforeAllTests() {
        initialNumberWatchlist = watchlistRespository.findAll().size();
        initialNumberWatched = watchlistRespository.findAllByIsWatched(true).size();
        watchlistRespository.save(watchlist1);
        watchlistRespository.save(watchlist2);
        watchlistRespository.save(watchlist3);
    }

    @AfterEach
    public void afterAllTests() {
        watchlistRespository.deleteById(watchlist1.getId());
        watchlistRespository.deleteById(watchlist2.getId());
        watchlistRespository.deleteById(watchlist3.getId());
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void requestAllItemsOnWatchlist_thenReturnJsonWatchlistList() throws Exception {
        mockMvc.perform(get("/watchlist/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(initialNumberWatchlist + 3)));
    }

    @Test
    public void requestOneWatchlistItem_thenReturnJsonWatchlist() throws Exception {
        mockMvc.perform(get("/watchlist/{uuid}", "fbedd1a2-e847-448d-b49e-e15c23dd9db1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is("fbedd1a2-e847-448d-b49e-e15c23dd9db1")))
                .andExpect(jsonPath("$.movieUuid", is("21d14364-8e94-41a3-824a-a44df76d59d8")))
                .andExpect(jsonPath("$.isWatched", is(false)))
                ;
    }

    @Test
    public void addWatchlistItem_thenReturnJsonWatchlist() throws Exception {
        MvcResult result = mockMvc.perform(post("/watchlist/{uuid}", watchlist4.getMovieUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieUuid", is(watchlist4.getMovieUuid())))
                .andExpect(jsonPath("$.isWatched", is(false)))
                .andReturn();
        UUID uuid = UUID.fromString(JsonPath.read(result.getResponse().getContentAsString(), "$.uuid"));
        Watchlist newWatchlist = watchlistRespository.findFirstByUuid(uuid.toString());
        watchlistRespository.deleteById(newWatchlist.getId());
    }

    @Test
    public void setWatchlistItemWatched_thenReturnJsonWatchlist() throws Exception {
        watchlistRespository.save(watchlist4);
        mockMvc.perform(post("/watchlist/watched/{uuid}", watchlist4.getUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(watchlist4.getUuid())))
                .andExpect(jsonPath("$.movieUuid", is(watchlist4.getMovieUuid())))
                .andExpect(jsonPath("$.isWatched", is(true)))
        ;
        watchlistRespository.deleteById(watchlist4.getId());
    }

    @Test
    public void requestAllWatchedItemsOnWatchlist_thenReturnJsonWatchlistList() throws Exception {
        watchlist4.setIsWatched(true);
        watchlistRespository.save(watchlist4);
        mockMvc.perform(get("/watchlist/watched"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(initialNumberWatched + 1)));
        watchlistRespository.deleteById(watchlist4.getId());
    }

    @Test
    public void removeWatchlistItem_thenReturnSuccessMessage() throws Exception {
        watchlistRespository.save(watchlist4);
        mockMvc.perform(delete("/watchlist/{movieUuid}", watchlist4.getMovieUuid()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie successfully removed from watchlist")))
        ;
    }
}
