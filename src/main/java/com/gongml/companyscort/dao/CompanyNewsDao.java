package com.gongml.companyscort.dao;

import com.gongml.companyscort.entity.CompanyNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CompanyNewsDao extends JpaRepository<CompanyNews, Long> {

    CompanyNews findOneByGpdmAndEndDate(String  gpdm, Date endDate);

    List<CompanyNews> findAllByEndDateOrderByMarketRankDesc(Date date);
}
