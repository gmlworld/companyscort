package com.gongml.companyscort.dao;


import com.gongml.companyscort.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyInfoDao extends JpaRepository<CompanyInfo, Long> {
    boolean existsByGpdmEquals(String  gpdm);

    CompanyInfo findOneByGpdm(String  gpdm);
}
