package de.uni_leipzig.dbs.formRepository.dataModel;

import java.util.Date;

public class VersionMetadata {

	
	private Date from ;
	private Date to ;
	private String name ;
	private String descr;
	private String topic;
	int id;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionMetadata other = (VersionMetadata) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public VersionMetadata(int id,Date from, Date to, String name, String topic) {
		super();
		this.from = from;
		this.to = to;
		this.name = name;
		this.topic = topic;
		this.id = id;
	}
	
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
