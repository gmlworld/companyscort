package com.gongml.companyscort.schedule;

import com.gongml.companyscort.entity.CompanyInfo;
import com.gongml.companyscort.entity.CompanyNews;
import com.gongml.companyscort.service.CompanyInfoService;
import com.gongml.companyscort.service.CompanyNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-27 16:34
 **/
@Component
public class CollectionInfoScheduler {
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private CompanyNewsService companyNewsService;

    @Scheduled(cron = "0 30 19 * * ? ")
    public void collectionCompanyInfos() {
        List<CompanyInfo> stocksInfo = companyInfoService.getStocksInfo();
        for (CompanyInfo companyInfo : stocksInfo) {
            try {
                companyInfoService.saveCompanyInfoById(companyInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "0 30 20 * * ? ")
    public void collectionCompanyNewss() {
        List<CompanyInfo> companyInfos = companyInfoService.selectAllCompanyInfo();
        for (CompanyInfo companyInfo : companyInfos) {
            try {
                CompanyNews companyNews = companyNewsService.getCompanyNews(companyInfo.getGpdm());
                companyNewsService.saveCompanyNews(companyNews);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
