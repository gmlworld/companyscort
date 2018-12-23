package com.gongml.companyscort.impl.service;

import com.gongml.companyscort.dao.CompanyNewsDao;
import com.gongml.companyscort.entity.CompanyNews;
import com.gongml.companyscort.service.CompanyNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-22 22:56
 **/
@Service
public class CompanyNewsServiceImpl implements CompanyNewsService {
    @Autowired
    private CompanyNewsDao companyNewsDao;

    @Override
    public int saveCompanyNews(CompanyNews companyNews) {
        try {
            CompanyNews oneByGpdmAndEndDate = companyNewsDao.findOneByGpdmAndEndDate(companyNews.getGpdm(), companyNews.getEndDate());
            if (oneByGpdmAndEndDate != null) {
                Long id = oneByGpdmAndEndDate.getId();
                companyNews.setId(id);
            }
            companyNewsDao.save(companyNews);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<CompanyNews> selectCompanyNewsByDate(Date date) {
        try {
            return companyNewsDao.findAllByEndDateOrderByMarketRankDesc(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int deleteOldCompanyNews() {
        return -1;
    }
}
