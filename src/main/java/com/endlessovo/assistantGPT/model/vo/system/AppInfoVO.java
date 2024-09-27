package com.endlessovo.assistantGPT.model.vo.system;

import lombok.Builder;
import lombok.Data;

/**
 * 描述系统信息
 */
@Data
@Builder
public class AppInfoVO {
    private String name;
    private String version;
}
