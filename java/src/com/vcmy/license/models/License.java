package com.vcmy.license.models;

import java.util.Date;

public class License {
	private int id;
	private String file_name;
	private Date valid_from;
	private Date valid_till;
	private Date imported_at;
	
	public License(int id, String file_name, Date valid_from, Date valid_till, Date imported_at) {
		super();
		this.id = id;
		this.file_name = file_name;
		this.valid_from = valid_from;
		this.valid_till = valid_till;
		this.imported_at = imported_at;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public Date getValid_from() {
		return valid_from;
	}
	public void setValid_from(Date valid_from) {
		this.valid_from = valid_from;
	}
	public Date getValid_till() {
		return valid_till;
	}
	public void setValid_till(Date valid_till) {
		this.valid_till = valid_till;
	}
	public Date getImported_at() {
		return imported_at;
	}
	public void setImported_at(Date imported_at) {
		this.imported_at = imported_at;
	}
}
