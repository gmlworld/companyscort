package com.gongml.companyscort;

import com.gongml.companyscort.entity.CompanyNews;
import com.gongml.companyscort.schedule.CollectionInfoScheduler;
import com.gongml.companyscort.service.CompanyNewsService;
import com.gongml.companyscort.service.IMailService;
import com.gongml.companyscort.utils.excel.ExportToExcel;
import com.gongml.companyscort.utils.reflect.FieldUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyscortApplicationTests {

    @Autowired
    private CollectionInfoScheduler collectionInfoScheduler;
    @Autowired
    private CompanyNewsService companyNewsService;
    @Autowired
    private IMailService iMailService;

    @Value("${spring.mail.username}")
    private String sendToMail;

    @Test
    public void contextLoads() {
//        collectionInfoScheduler.collectionCompanyInfos();
//        collectionInfoScheduler.collectionCompanyNewss();


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
            e.printStackTrace();
        }
    }


}

