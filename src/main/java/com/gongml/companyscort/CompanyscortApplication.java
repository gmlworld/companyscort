package com.gongml.companyscort;

import com.gongml.companyscort.schedule.CollectionInfoScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CompanyscortApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CompanyscortApplication.class, args);
        CollectionInfoScheduler bean = context.getBean(CollectionInfoScheduler.class);
//        bean.collectionCompanyInfos();
        bean.collectionCompanyNewss();
        context.close();
    }

}

