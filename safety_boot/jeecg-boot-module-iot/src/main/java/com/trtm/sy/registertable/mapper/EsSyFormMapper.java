package com.trtm.sy.registertable.mapper;

import com.trtm.sy.registertable.model.SyForm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EsSyFormMapper extends ElasticsearchRepository<SyForm, String> {
}
