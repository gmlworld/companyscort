package com.gongml.companyscort.service;

import com.gongml.companyscort.entity.CompanyNews;

import java.util.Date;
import java.util.List;

/**
 * @program: companyscort
 * @description: 公司排名信息服务
 * @author: Gongml
 * @create: 2018-12-22 22:44
 **/
public interface CompanyNewsService {
    int saveCompanyNews(CompanyNews companyNews);
    List<CompanyNews> selectCompanyNewsByDate(Date date);
    int deleteOldCompanyNews();
}
