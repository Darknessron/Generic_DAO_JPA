/**
 * 
 */
package com.rontseng.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rontseng.dao.IGenericDAO;
import com.rontseng.example.model.Company;

/**
 * @author Ron
 *
 */
@RestController
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private IGenericDAO<Company, Long> companyDao;
	
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Company createCompany(@RequestBody Company company)	{
		company = companyDao.save(company);
		return company;
	}
	
	@RequestMapping(path="/{companyId}",method=RequestMethod.PUT, consumes="application/json")
	public @ResponseBody Company updateCompany(@PathVariable Long companyId, @RequestBody Company company)	{
		company.setCompanyId(companyId);
		company = companyDao.save(company);
		return company;
	}
	
	@RequestMapping(path="/{companyId}", method=RequestMethod.DELETE)
	public void deleteCompany(@PathVariable Long companyId)	{
		companyDao.setClazz(Company.class).deleteById(companyId);
	}
	
	@RequestMapping(path="/{companyId}", method=RequestMethod.GET)
	public @ResponseBody Company getCompany(@PathVariable("companyId") Long companyId)	{
		Company result = companyDao.setClazz(Company.class).findOne(companyId);
		return result;
	}
}
