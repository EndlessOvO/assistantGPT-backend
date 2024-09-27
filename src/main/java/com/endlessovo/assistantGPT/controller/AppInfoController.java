package com.endlessovo.assistantGPT.controller;

import com.endlessovo.assistantGPT.common.ResponseVO;
import com.endlessovo.assistantGPT.common.config.AppInfoConfig;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import com.endlessovo.assistantGPT.model.vo.system.AppInfoVO;
import com.endlessovo.assistantGPT.model.vo.system.DataSourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Properties;

@Slf4j
@RestController
@RequestMapping("/app-info")
@RequiredArgsConstructor
public class AppInfoController {
    private final DataSource dataSource;
    private final LettuceConnectionFactory connectionFactory;
    private final AppInfoConfig appInfoConfig;

    @RequestMapping("/app")
    public ResponseVO<AppInfoVO> getAppInfo() {
        return ResponseVO.success(AppInfoVO.builder()
                .name(appInfoConfig.getName())
                .version(appInfoConfig.getVersion())
                .build());
    }

    @RequestMapping("/datasource")
    public ResponseVO<DataSourceVO> getDataSourceInfo() {
        try {
            DatabaseMetaData meta = dataSource.getConnection().getMetaData();
            return ResponseVO.success(DataSourceVO.builder()
                            .url(meta.getURL())
                            .name(meta.getDatabaseProductName())
                            .version(String.valueOf(meta.getDatabaseProductVersion()))
                            .driverName(meta.getDriverName())
                            .driverVersion(meta.getDriverVersion())
                            .build());
        } catch (Exception e) {
            throw new CustomException(CustomExceptionEnum.DATASOURCE_ACCESS_ERROR);
        }
    }

    @RequestMapping("/redis")
    public ResponseVO<Properties> getRedisInfo() {
        return ResponseVO.success(connectionFactory.getConnection().commands().info("server"));
    }
}
