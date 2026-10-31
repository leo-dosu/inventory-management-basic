package com.inventory.reporting.web;

import com.inventory.reporting.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Boots the whole app on the dev profile (H2 plus demo data) and exercises the real endpoints. */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ApiSmokeTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void todaySummaryReturnsFourMetrics() throws Exception {
        mvc.perform(get("/api/reports/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sales.value").exists())
                .andExpect(jsonPath("$.totalOrders.changePercent").exists())
                .andExpect(jsonPath("$.revenue.value").exists())
                .andExpect(jsonPath("$.customers.value").exists());
    }

    @Test
    void ordersVsSalesReturnsOneEntryPerDay() throws Exception {
        mvc.perform(get("/api/reports/orders-vs-sales?days=7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    void topSellingRespectsLimit() throws Exception {
        mvc.perform(get("/api/reports/top-selling?limit=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void badDaysIsABadRequest() throws Exception {
        mvc.perform(get("/api/reports/orders-vs-sales?days=0"))
                .andExpect(status().isBadRequest());
    }
}
