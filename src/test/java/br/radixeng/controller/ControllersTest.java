package br.radixeng.controller;



import br.radixeng.Application;
import br.radixeng.model.Graph;
import br.radixeng.model.GraphArray;
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
        GraphArray newGrapgArray = new GraphArray();
        Graph g1 = new Graph();
        g1.setSource("A");
        g1.setTarget("B");
        g1.setDistance(5L);

        Graph g2 = new Graph();
        g2.setSource("B");
        g2.setTarget("C");
        g2.setDistance(4L);

        Graph g3 = new Graph();
        g3.setSource("C");
        g3.setTarget("D");
        g3.setDistance(8L);

        Graph g4 = new Graph();
        g4.setSource("D");
        g4.setTarget("C");
        g4.setDistance(8L);

        Graph g5 = new Graph();
        g5.setSource("D");
        g5.setTarget("E");
        g5.setDistance(6L);

        Graph g6 = new Graph();
        g6.setSource("A");
        g6.setTarget("D");
        g6.setDistance(5L);

        Graph g7 = new Graph();
        g7.setSource("C");
        g7.setTarget("E");
        g7.setDistance(2L);

        Graph g8 = new Graph();
        g8.setSource("E");
        g8.setTarget("B");
        g8.setDistance(3L);

        Graph g9 = new Graph();
        g9.setSource("A");
        g9.setTarget("E");
        g9.setDistance(7L);

        List<Graph> graphs = new ArrayList<>();
        graphs.add(g1);
        graphs.add(g2);
        graphs.add(g3);
        graphs.add(g4);
        graphs.add(g5);
        graphs.add(g6);
        graphs.add(g7);
        graphs.add(g8);
        graphs.add(g9);

        newGrapgArray.setId(1L);
        newGrapgArray.setData(graphs);

        repository.save(newGrapgArray);
    }
}
