package de.uni_leipzig.dbs.formRepository.dataModel;


/**
 * property of an {@link GenericEntity}
 * @author christen
 *
 */
public class GenericProperty  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1193453706127589052L;
	/**
	 * name of the property
	 */
	private String name ;
	
	/**
	 * defines the importance of the property
	 */
	private String scope;
	

	private int id ;
	

	private String language ;
	
	public GenericProperty(int id,String name, String scope,String language) {
		super();
		this.name = name;
		this.scope = scope;
		if (scope==null){
			this.scope ="N/A";
		}
		this.id = id;
		this.language = language;
		if (language==null){
			this.language ="N/A";
		}
	}
	

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	
	public String getScope() {
		// TODO Auto-generated method stub
		return scope;
	}

	
	public String getLanguage() {
		// TODO Auto-generated method stub
		return language;
	}
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
		GenericProperty other = (GenericProperty) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "GenericProperty [name=" + name + ", scope=" + scope + ", id="
				+ id + ", language=" + language + "]";
	}
	

}
