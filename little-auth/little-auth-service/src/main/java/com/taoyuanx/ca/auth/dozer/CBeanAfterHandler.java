package com.taoyuanx.ca.auth.dozer;

/**
 * @author 桃源
 * @description 自定义后处理 接口
 * @date 2019/6/22
 */
@FunctionalInterface
public interface CBeanAfterHandler<S,D> {
    void handle(S s, D t);
}
