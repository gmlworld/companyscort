package com.gongml.companyscort.entity;

import lombok.Data;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: companyscort
 * @description: 公司最新排名信息
 * @author: Gongml
 * @create: 2018-12-22 22:15
 **/
@Data
@Entity
public class CompanyNews implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gpdm;
    private Date endDate;
    private Integer totalNum;//mainRank.TotalNum
    private Integer marketRank;//mainRank.MarketRank
    private String fl;//mainRank.SecuAbbr
    private String hy;//mainRank.SecondIndustryName
    private Integer hyNum;
    private Integer hyRank;
    private Integer jcRank;
    private Integer jsRank;
    private Integer xxRank;
    @Column(columnDefinition="varchar(2000)")
    private String zhps;//zhps
    @Column(columnDefinition="varchar(2000)")
    private String jcps; //fundamentalComment
    @Column(columnDefinition="varchar(2000)")
    private String jsps;//technicalComment
    @Column(columnDefinition="varchar(2000)")
    private String xxps; //infoComment
}
