package com.ivmaly.transaction.controllers;

import com.ivmaly.transaction.models.Reserve;
import com.ivmaly.transaction.models.User;
import com.ivmaly.transaction.services.ReserveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReserveController.class)
public class ReserveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReserveService reserveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReserve() throws Exception {
        mockMvc.perform(post("/reserves")
                        .param("userId", "1")
                        .param("reserveAmount", "50.00")
                        .param("serviceId", "1")
                        .param("orderId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void cancelReserve() throws Exception {
        mockMvc.perform(post("/reserves/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void completeReserve() throws Exception {
        mockMvc.perform(post("/reserves/1/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getReserveById() throws Exception {
        User user = new User(new BigDecimal("100.00"));
        Reserve reserve = new Reserve(user, new BigDecimal("50.00"), 1L, 1L);
        when(reserveService.getReserveById(any(Long.class))).thenReturn(reserve);

        mockMvc.perform(get("/reserves/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reserveAmount").value(50.00));
    }
}
