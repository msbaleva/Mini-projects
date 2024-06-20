package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.LocalDateTime;

public abstract class Accommodation  {
	
		
	private String id;
	private Location location;
	private String type;

	
	public Accommodation(String type, Location location) {
		this.id = generateId(type);
		this.type = type;
		this.location = location;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Accommodation other = (Accommodation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		return "ID: " + id + " Location: " + location + " Type: " + type;
	}

	private String generateId(String type) {
		return type.substring(0, 3).toUpperCase() + "-" + getCount();
	}
	
	 protected abstract long getCount();


	 public String getId() {
		 return id;
	 }
	 
	 public Location getLocation() {
		 return location;
	 }
	 
	 public String getType() {
		 return type;
	 }

}
