package com.example.shopping.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customerList")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class CustomerDOList {
	private List<CustomerDO> customers;

	@XmlElement(name = "customer")
	public List<CustomerDO> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerDO> customers) {
		this.customers = customers;
	}

}
