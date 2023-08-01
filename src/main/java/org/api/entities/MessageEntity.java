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
@Table(name = "t1_message_entity")
@SQLDelete(sql = "UPDATE t1_message_entity SET del_flg = 1 WHERE id=?")
@Where(clause = "del_flg=0")
public class MessageEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "org.api.entities.CustomIdGenerator")
    @Column(name = "id", columnDefinition = "varchar(50)")
    @JsonProperty("id")
    private String id;

    @Column(name = "content", columnDefinition = "varchar(1000)")
    @JsonProperty("content")
    private String content;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_user_from")
    @JsonProperty("userFrom")
    private UserEntity userEntityFrom;

    @ManyToOne
    @JoinColumn(name = "id_user_to")
    @JsonProperty("userTo")
    private UserEntity userEntityTo;

    @ManyToOne
    @JoinColumn(name = "id_relationship")
    @JsonProperty("relationship")
    private RelationshipEntity relationship;
}
