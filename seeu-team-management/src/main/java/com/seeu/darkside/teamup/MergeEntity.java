package com.seeu.darkside.teamup;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
}

