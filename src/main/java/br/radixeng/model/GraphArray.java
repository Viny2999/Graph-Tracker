package br.radixeng.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class GraphArray {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade= CascadeType.ALL)
    private List<Graph> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Graph> getData() {
        return data;
    }

    public void setData(List<Graph> data) {
        this.data = data;
    }
}
