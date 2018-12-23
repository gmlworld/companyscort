package com.gongml.companyscort.service;

import com.gongml.companyscort.entity.CompanyInfo;

import java.util.List;

/**
 * @program: companyscort
 * @description: 公司信息服务
 * @author: Gongml
 * @create: 2018-12-22 22:44
 **/
public interface CompanyInfoService {
    int saveCompanyInfoById(CompanyInfo companyInfo);
    List<CompanyInfo> selectAllCompanyInfo();
    int deleteCompanyInfo(CompanyInfo companyInfo);
    int existsCompanyInfo(CompanyInfo companyInfo);
}
