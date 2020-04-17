package br.radixeng.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class GraphList {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade= CascadeType.ALL)
    private List<GraphInfo> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GraphInfo> getData() {
        return data;
    }

    public void setData(List<GraphInfo> data) {
        this.data = data;
    }
}
