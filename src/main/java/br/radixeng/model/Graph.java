package br.radixeng.model;

import javax.persistence.*;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Graph {

    @Id @GeneratedValue
    private Long id;

    private String source;
    private String target;
    private Long distance;
}
