package com.lyf.model;

import com.lyf.constant.RpcConstant;
import lombok.*;

import java.io.Serializable;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RpcRequest implements Serializable {
    public String service; //获取哪个请求
    public String method;//获取哪个方法

    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表  Object[] 但在反序列化时，如果不清楚 args 数组中存储的对象的具体类型，反序列化后就会被视为 Object 类型，而无法直接识别为原始类型，需要手动进行类型转换。
     */
    private Object[] args;

}
