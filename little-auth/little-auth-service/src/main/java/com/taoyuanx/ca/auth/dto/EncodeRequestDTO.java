package com.taoyuanx.ca.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc 加密请求实体
 * @date 2020/2/17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncodeRequestDTO implements Serializable {
    private String data;

}
