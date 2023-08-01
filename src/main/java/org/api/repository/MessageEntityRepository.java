package org.api.repository;

import org.api.entities.MessageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageEntityRepository extends BaseRepository<MessageEntity, String> {
    @Query("SELECT m FROM MessageEntity  m " +
            "WHERE ((m.userEntityFrom.id = :firstUserId AND m.userEntityTo.id = :secondUserId) " +
            "OR (m.userEntityFrom. id = :secondUserId AND m.userEntityTo.id = :firstUserId)) " +
            "ORDER BY m.createDate")
    public List<MessageEntity>findAllMessageBetweenTwoUser(@Param("firstUserId") String firstUserId, @Param("secondUserId") String secondUserId);

    @Query(value = "select * " +
            "from t1_message_entity AS m " +
            "         INNER JOIN " +
            "    (select m.id_user_from as from_user_m1, max(m.create_date) as time_m1, count(*) as count " +
            "    from t1_message_entity AS m " +
            "    where m.id_user_to =:userId " +
            "    GROUP BY m.id_user_from) as m1 " +
            "                   ON m.id_user_from = m1.from_user_m1 and m.create_date = m1.time_m1 " +
            "where m.id_user_to =:userId " +
            "ORDER BY m.create_date DESC;", nativeQuery = true)
    public List<MessageEntity> getAllFriendMessages(@Param("userId") String loggedInUserId);

}
