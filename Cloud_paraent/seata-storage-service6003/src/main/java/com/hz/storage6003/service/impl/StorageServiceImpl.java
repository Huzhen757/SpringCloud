package com.hz.storage6003.service.impl;

import com.hz.storage6003.dao.StorageDao;
import com.hz.storage6003.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageDao storageDao;

    @Override
    public void decrease(Long productId, Integer count) {
        log.info("*********** 库存微服务中开始 扣减库存");
        storageDao.decrease(productId, count);
        log.info("*********** 库存微服务中完成库存扣减");
    }
}
