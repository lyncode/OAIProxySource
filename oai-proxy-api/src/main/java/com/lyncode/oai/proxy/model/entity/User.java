package com.lyncode.oai.proxy.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable {
	private static final long serialVersionUID = -2813136034675659742L;
	//private static Logger log = LogManager.getLogger(User.class);
	private int id;
	private String email;
	private String password;
	private String activationKey;
	private boolean active;
	
	@Id
	public int getId () {
		return id;
	}
	@Column(name="email", nullable=false)
	public String getEmail () {
		return email;
	}
	
	@Column(name="password", nullable=false)
	public String getPassword () {
		return password;
	}
	
	@Column(name="activationkey", nullable=false)
	public String getActivationKey() {
		return activationKey;
	}
	
	@Column(name="active")
	public boolean isActive() {
		return active;
	}
	
	
	public void setId (int id) {
		this.id = id;
	}

	public void setEmail (String mail) {
		this.email = mail;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
