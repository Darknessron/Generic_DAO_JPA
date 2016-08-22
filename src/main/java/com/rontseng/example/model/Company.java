/**
 * 
 */
package com.rontseng.example.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Ron
 *
 */
@Entity
@Table(name = "Company", catalog = "example")
public class Company {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "CompanyId", unique = true, nullable = false)
	private Long companyId;
	
	@Column(name = "CompanyName", nullable = false)
	private String companyName;
	

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
