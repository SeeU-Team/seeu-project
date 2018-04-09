package com.seeu.darkside.teamup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "merge")
public class MergeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_merge")
    private Long idMerge;

    @Column(name = "id_first")
    private Long idFirst;

    @Column(name = "id_second")
    private Long idSecond;

    public Long getIdMerge() {
        return idMerge;
    }

    public void setIdMerge(Long idMerge) {
        this.idMerge = idMerge;
    }

    public Long getIdFirst() {
        return idFirst;
    }

    public void setIdFirst(Long idFirst) {
        this.idFirst = idFirst;
    }

    public Long getIdSecond() {
        return idSecond;
    }

    public void setIdSecond(Long idSecond) {
        this.idSecond = idSecond;
    }
}

