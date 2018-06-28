package com.seeu.darkside.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

	List<MessageEntity> getAllByDestAndType(Long dest, ConversationType type);

	@Query("select m from MessageEntity m where (m.from = ?1 and m.dest = ?2) or (m.from = ?2 and m.dest = ?1) and m.type = ?3")
	List<MessageEntity> getAllBySwitchableFromAndDestAndType(Long first, Long second, ConversationType type);
}
