package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Order> orders = new HashSet<>();
	
	@Column
	private String name;
	
	@Column
	private String email;
	
	
	@Column
	private String password;
	
	@Column
	private String address;
	
	@Column
	private String phone;
	
	@Column
	private String role;
	
	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	
	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at;

	
	public User() {
	}
	
	public User(Integer id, Set<Order> orders, String name, String email, String password, String address, String phone,
			String role, Date created_at, Date updated_at) {;
		this.id = id;
		this.orders = orders;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.role = role;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	public User(Integer id, Set<Order> orders, String name, String email, String address, String phone, String role,
			Date created_at, Date updated_at) {
		super();
		this.id = id;
		this.orders = orders;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.role = role;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
}
