package com.vcmy.license;

import java.util.Date;

public class License {
	private int id;
	private String filename;
	private String content;
	private Date notBefore;
	private Date notAfter;
	private Date importedAt;
	private boolean deleted;
	
	public License() {
		super();
	}

	public License(String filename, String content, Date notBefore, Date notAfter, Date importedAt, boolean deleted) {
		super();
		this.filename = filename;
		this.content = content;
		this.notBefore = notBefore;
		this.notAfter = notAfter;
		this.importedAt = importedAt;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}

	public Date getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}

	public Date getImportedAt() {
		return importedAt;
	}

	public void setImportedAt(Date importedAt) {
		this.importedAt = importedAt;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
