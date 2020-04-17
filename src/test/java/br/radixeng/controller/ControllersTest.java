package br.radixeng.controller;

import br.radixeng.Application;
import br.radixeng.model.GraphInfo;
import br.radixeng.model.GraphList;
import br.radixeng.repository.GraphRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GraphRepository repository;

    @Test
    public void getAllGraphs() throws Exception {
        mockMvc.perform(get("/graph")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void routeAtoC() throws Exception {
        createGraphArray();

        mockMvc.perform(post("/routes/1/from/A/to/C?maxStops=4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'routes':[{'route':'ABC','stops':2},{'route':'ADC','stops':2},{'route':'ADEBC','stops':4},{'route':'AEBC','stops':3}]}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void routeCtoC() throws Exception {
        createGraphArray();

        mockMvc.perform(post("/routes/1/from/C/to/C?maxStops=3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'routes':[{'route':'C','stops': 0}]}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void distanceAtoC() throws Exception {
        createGraphArray();

        mockMvc.perform(post("/distance/1/from/A/to/C")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'path':['A','B','C'],'distance':9}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void distanceBtoB() throws Exception {
        createGraphArray();

        mockMvc.perform(post("/distance/1/from/B/to/B")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'path':['B'],'distance':0}"))
                .andDo(MockMvcResultHandlers.print());
    }

    private void createGraphArray() {
        GraphList newGrapgArray = new GraphList();
        GraphInfo g1 = new GraphInfo();
        g1.setSource("A");
        g1.setTarget("B");
        g1.setDistance(5L);

        GraphInfo g2 = new GraphInfo();
        g2.setSource("B");
        g2.setTarget("C");
        g2.setDistance(4L);

        GraphInfo g3 = new GraphInfo();
        g3.setSource("C");
        g3.setTarget("D");
        g3.setDistance(8L);

        GraphInfo g4 = new GraphInfo();
        g4.setSource("D");
        g4.setTarget("C");
        g4.setDistance(8L);

        GraphInfo g5 = new GraphInfo();
        g5.setSource("D");
        g5.setTarget("E");
        g5.setDistance(6L);

        GraphInfo g6 = new GraphInfo();
        g6.setSource("A");
        g6.setTarget("D");
        g6.setDistance(5L);

        GraphInfo g7 = new GraphInfo();
        g7.setSource("C");
        g7.setTarget("E");
        g7.setDistance(2L);

        GraphInfo g8 = new GraphInfo();
        g8.setSource("E");
        g8.setTarget("B");
        g8.setDistance(3L);

        GraphInfo g9 = new GraphInfo();
        g9.setSource("A");
        g9.setTarget("E");
        g9.setDistance(7L);

        List<GraphInfo> graphInfos = new ArrayList<>();
        graphInfos.add(g1);
        graphInfos.add(g2);
        graphInfos.add(g3);
        graphInfos.add(g4);
        graphInfos.add(g5);
        graphInfos.add(g6);
        graphInfos.add(g7);
        graphInfos.add(g8);
        graphInfos.add(g9);

        newGrapgArray.setId(1L);
        newGrapgArray.setData(graphInfos);

        repository.save(newGrapgArray);
    }
}
