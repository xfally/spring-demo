package io.github.xfally.springdemo.service;

import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.dao.ds0.entity.LogDO;

import java.util.List;

/**
 * 日志服务类
 *
 * @author pax
 * @since 2020-08-31
 */
public interface ILogService {
    LogDO getLog(String id);

    List<LogDO> listLogs();


    UnifiedPage<LogDO> queryLogs(UnifiedQuery unifiedQuery,
                                 String name,
                                 String level);

    LogDO saveLog(LogDO logDO);

    LogDO updateLog(LogDO logDO);

    Boolean removeLog(String id);
}
