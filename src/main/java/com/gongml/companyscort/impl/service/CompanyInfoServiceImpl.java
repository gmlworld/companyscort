package com.gongml.companyscort.impl.service;

import com.alibaba.fastjson.JSONObject;
import com.gongml.companyscort.dao.CompanyInfoDao;
import com.gongml.companyscort.entity.CompanyInfo;
import com.gongml.companyscort.service.CompanyInfoService;
import com.gongml.companyscort.utils.httputil.HttpClientRequest;
import com.gongml.companyscort.utils.httputil.bean.Request;
import com.gongml.companyscort.utils.jsonparse.JsonParseUtil;
import com.gongml.companyscort.utils.jsonparse.bean.JsonParseRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-22 22:56
 **/
@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {
    @Autowired
    private CompanyInfoDao companyInfoDao;
    @Value("${stock.url}")
    private String url;
    @Autowired
    @Qualifier("companyInfoRule")
    private JsonParseRule companyInfoRule;
    @Autowired
    private HttpClientRequest httpClientRequest;
    @Autowired
    private JsonParseUtil jsonParseUtil;

    @Override
    public List<CompanyInfo> getStocksInfo() {
        try {
            Request request = new Request();
            request.setUri(url);
            String json = httpClientRequest.httpGet(request);
            List<Map<String, Object>> companyInfoList = jsonParseUtil.parseJsonData(json, companyInfoRule);
            String companyInfoJson = JSONObject.toJSONString(companyInfoList);
            List<CompanyInfo> companyInfos = JSONObject.parseArray(companyInfoJson, CompanyInfo.class);
            return companyInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int saveCompanyInfoById(CompanyInfo companyInfo) {
        try {
            CompanyInfo oneByGpdm = companyInfoDao.findOneByGpdm(companyInfo.getGpdm());
            if(oneByGpdm !=null){
                Long id = oneByGpdm.getId();
                companyInfo.setId(id);
            }
            companyInfoDao.save(companyInfo);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<CompanyInfo> selectAllCompanyInfo() {
        List<CompanyInfo> all = companyInfoDao.findAll();
        return all;
    }

    @Override
    public int deleteCompanyInfo(CompanyInfo companyInfo) {
        try {
            companyInfoDao.delete(companyInfo);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int existsCompanyInfo(CompanyInfo companyInfo) {
        try {
            boolean exists = companyInfoDao.existsByGpdmEquals(companyInfo.getGpdm());
            if(exists){
                return 1;
            }
            return  0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
