package com.seeu.darkside.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

	List<MessageEntity> getAllByDestAndTypeOrderByCreatedAsc(Long dest, ConversationType type);

	@Query("select m from MessageEntity m where (m.from = ?1 and m.dest = ?2) or (m.from = ?2 and m.dest = ?1) and m.type = ?3 order by m.created asc")
	List<MessageEntity> getAllBySwitchableFromAndDestAndType(Long first, Long second, ConversationType type);

	@Query("select m from MessageEntity m where (m.from = ?1 or m.dest = ?1) and m.type = 0 group by m.from, m.dest")
	List<MessageEntity> getDistinctMessagesWith(Long userId);
}
