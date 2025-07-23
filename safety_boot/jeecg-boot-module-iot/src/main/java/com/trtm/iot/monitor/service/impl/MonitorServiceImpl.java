package com.trtm.iot.monitor.service.impl;

import com.trtm.iot.monitor.entity.Monitor;
import com.trtm.iot.monitor.mapper.MonitorMapper;
import com.trtm.iot.monitor.service.IMonitorService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: monitor
 * @Author: jeecg-boot
 * @Date:   2021-12-16
 * @Version: V1.0
 */
@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorMapper, Monitor> implements IMonitorService {

}
