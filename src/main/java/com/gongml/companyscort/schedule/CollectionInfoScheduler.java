package com.gongml.companyscort.schedule;

import com.gongml.companyscort.entity.CompanyInfo;
import com.gongml.companyscort.entity.CompanyNews;
import com.gongml.companyscort.service.CompanyInfoService;
import com.gongml.companyscort.service.CompanyNewsService;
import com.gongml.companyscort.service.IMailService;
import com.gongml.companyscort.utils.excel.ExportToExcel;
import com.gongml.companyscort.utils.reflect.FieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-27 16:34
 **/
@Component
@Slf4j
public class CollectionInfoScheduler {
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private CompanyNewsService companyNewsService;
    @Autowired
    private IMailService iMailService;
    @Value("${spring.mail.username}")
    private String sendToMail;

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

        try {
            LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            Date from = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            List<Map<String, Object>> resoult = new ArrayList<>();
            List<CompanyNews> companyNewsList = companyNewsService.selectCompanyNewsByDate(from);
            if (CollectionUtils.isNotEmpty(companyNewsList)) {
                CompanyNews firstCompanyNews = companyNewsList.get(0);
                String[] filedName = FieldUtils.getFiledName(firstCompanyNews);
                for (CompanyNews companyNews : companyNewsList) {
                    Map<String, Object> filedMapValues = FieldUtils.getFiledMapValues(companyNews, filedName);
                    resoult.add(filedMapValues);
                }
            }
            ExportToExcel.generateExcel(resoult, "./today.xlsx");
            iMailService.sendAttachmentsMail(sendToMail, "这是一封带附件的邮件", "邮件中有附件，请注意查收！", "./today.xlsx");
        } catch (Exception e) {
            log.error("附件邮件发送异常", e);
            e.printStackTrace();
        }

    }
}
