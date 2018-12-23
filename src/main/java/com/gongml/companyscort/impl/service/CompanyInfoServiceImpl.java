package com.gongml.companyscort.impl.service;

import com.gongml.companyscort.dao.CompanyInfoDao;
import com.gongml.companyscort.entity.CompanyInfo;
import com.gongml.companyscort.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
