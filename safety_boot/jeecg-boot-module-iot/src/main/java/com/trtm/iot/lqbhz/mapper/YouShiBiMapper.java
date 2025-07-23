package com.trtm.iot.lqbhz.mapper;

import com.trtm.iot.lqbhz.entity.YouShiBi;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface YouShiBiMapper {
    List<YouShiBi> getShiyoubi(String sheBeiNo);

}
