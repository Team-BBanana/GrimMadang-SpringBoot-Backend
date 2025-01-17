package com.example.GrimMadang.domain.drawings.metadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, UUID> {
    // 토픽 이름으로 메타데이터 검색
    MetaData findByTopicName(String topicName);
}
