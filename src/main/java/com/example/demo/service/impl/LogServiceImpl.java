package com.example.demo.service.impl;

import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.dao.ds0.entity.LogDO;
import com.example.demo.dao.ds0.repository.LogRepository;
import com.example.demo.service.ILogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * 日志信息服务实现类
 *
 * @author pax
 * @since 2020-08-31
 */
@Service
public class LogServiceImpl implements ILogService {
    @Autowired
    private LogRepository logRepository;

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'log_' + #id")
    public LogDO getLog(@Valid @NotNull String id) {
        Optional<LogDO> optionalLogDO = logRepository.findById(id);
        if (!optionalLogDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1005, id);
        }
        return optionalLogDO.get();
    }

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'log_list'")
    public List<LogDO> listLogs() {
        return logRepository.findAll();
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<LogDO> queryLogs(UnifiedQuery unifiedQuery,
                                        String level) {
        if (unifiedQuery.getCurrent() <= 0) {
            unifiedQuery.setCurrent(1);
        }
        Pageable pageable = PageRequest.of(unifiedQuery.getCurrent() - 1, unifiedQuery.getSize());
        Page<LogDO> page;
        if (!StringUtils.isBlank(level)) {
            LogDO logDO = new LogDO();
            logDO.setLevel(level);
            Example<LogDO> example = Example.of(logDO);
            page = logRepository.findAll(example, pageable);
        } else {
            page = logRepository.findAll(pageable);
        }
        UnifiedPage<LogDO> unifiedPage = UnifiedPage.ofJpa(page);
        unifiedPage.setCurrent(unifiedPage.getCurrent() + 1);
        return unifiedPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'log_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'log_' + #result.id", condition = "#result.id != 'null'")
    public LogDO saveLog(LogDO logDO) {
        logDO.setId(null);
        logDO = logRepository.save(logDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return logDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'log_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'log_' + #result.id")
    public LogDO updateLog(LogDO logDO) {
        if (!logRepository.existsById(logDO.getId())) {
            throw new UnifiedException(UnifiedCodeEnum.B1005, logDO.getId());
        }
        logRepository.save(logDO);
        return logDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'log_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'log_list'", beforeInvocation = false)
        }
    )
    public Boolean removeLog(String id) {
        if (!logRepository.existsById(id)) {
            throw new UnifiedException(UnifiedCodeEnum.B1005, id);
        }
        logRepository.deleteById(id);
        return true;
    }

}
