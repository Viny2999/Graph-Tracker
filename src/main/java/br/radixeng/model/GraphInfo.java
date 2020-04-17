package br.radixeng.model;

import javax.persistence.*;

@Entity
public class GraphInfo {

    @Id @GeneratedValue
    private Long id;

    private String source;
    private String target;
    private Long distance;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
