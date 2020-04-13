package br.radixeng.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class NavegacaoInfo {

    @Id @GeneratedValue
    private Long id;

    private String source;
    private String target;
    private Long distance;
}
