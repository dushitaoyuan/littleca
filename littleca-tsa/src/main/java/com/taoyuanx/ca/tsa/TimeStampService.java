package com.taoyuanx.ca.tsa;

import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;

/**
 * @author dushitaoyuan
 * @desc 时间戳服务
 * @date 2019/7/10
 */
public interface TimeStampService {
    TimeStampResponse timestamp(TimeStampRequest timestampRequest);
}
