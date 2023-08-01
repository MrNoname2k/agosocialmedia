package org.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t1_relationship_entity")
@SQLDelete(sql = "UPDATE t1_relationship_entity SET del_flg = 1 WHERE id=?")
@Where(clause = "del_flg=0")
public class RelationshipEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "relationship", cascade = CascadeType.ALL)
    private List<MessageEntity> messages;

    @ManyToOne
    @JoinColumn(name = "id_user_one")
    @JsonProperty("idUserOne")
    private UserEntity userEntityOne;

    @ManyToOne
    @JoinColumn(name = "id_user_tow")
    @JsonProperty("idUserTow")
    private UserEntity userEntityTow;
}
