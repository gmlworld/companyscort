package com.gongml.companyscort.impl.service;

import com.alibaba.fastjson.JSONObject;
import com.gongml.companyscort.dao.CompanyNewsDao;
import com.gongml.companyscort.entity.CompanyInfo;
import com.gongml.companyscort.entity.CompanyNews;
import com.gongml.companyscort.service.CompanyNewsService;
import com.gongml.companyscort.utils.httputil.HttpClientRequest;
import com.gongml.companyscort.utils.httputil.bean.Request;
import com.gongml.companyscort.utils.jsonparse.JsonParseUtil;
import com.gongml.companyscort.utils.jsonparse.bean.JsonParseRule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Value("${news.url}")
    private String url;
    @Autowired
    @Qualifier("companyNewsRule")
    private JsonParseRule companyNewsRule;
    @Autowired
    private HttpClientRequest httpClientRequest;
    @Autowired
    private JsonParseUtil jsonParseUtil;
    @Override
    public CompanyNews getCompanyNews(String gpdm) {
        try {
            Request request = new Request();
            request.setUri(url.replace("XXXXXX",gpdm));
            String json = httpClientRequest.httpGet(request);
            if(StringUtils.isNotBlank(json)){
                json = json.substring(1);
                int start = json.indexOf('{');
                if (json.indexOf('{')!=-1){
                    json = json.substring(start);
                    json = json.substring(0,json.length()-2);
                }
                System.out.println(json);
            }
            List<Map<String, Object>> companyNewsRuleList = jsonParseUtil.parseJsonData(json, companyNewsRule);
            String companyNewsRuleJson = JSONObject.toJSONString(companyNewsRuleList);
            List<CompanyNews> companyNewss = JSONObject.parseArray(companyNewsRuleJson, CompanyNews.class);
            if (!CollectionUtils.isEmpty(companyNewss)){
                return companyNewss.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
