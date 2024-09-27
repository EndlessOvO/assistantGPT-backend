package com.endlessovo.assistantGPT.model.vo.system;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataSourceVO {
    private String url;
    private String name;
    private String version;
    private String driverName;
    private String driverVersion;
}
