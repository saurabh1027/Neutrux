package com.neutrux.api.NeutruxBlogSearchApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neutrux.api.NeutruxBlogSearchApi.ui.models.BlogElementEntity;

@Repository
public interface BlogElementSearchRepository extends JpaRepository<BlogElementEntity, Long> {

}
