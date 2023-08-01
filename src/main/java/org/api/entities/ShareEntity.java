package org.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t1_share_entity")
@SQLDelete(sql = "UPDATE t1_share_entity SET del_flg = 1 WHERE id=?")
@Where(clause = "del_flg=0")
public class ShareEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_post")
    @JsonProperty("idPost")
    private PostEntity postEntity;

    @ManyToOne
    @JoinColumn(name = "id_user_create")
    @JsonProperty("idUserCreate")
    private UserEntity userEntityShare;
}
