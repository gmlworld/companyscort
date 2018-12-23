package com.gongml.companyscort.entity;

import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: companyscort
 * @description: 公司信息
 * @author: Gongml
 * @create: 2018-12-22 22:16
 **/
@Entity
@Data
public class CompanyInfo implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gpdm;
    private String gsjc;
    private String mtcc;
    private String pyjc;
}
