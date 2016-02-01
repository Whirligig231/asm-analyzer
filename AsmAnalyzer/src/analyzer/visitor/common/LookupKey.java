package analyzer.visitor.common;

class LookupKey {
	VisitType visitType;
	Class<?> clazz;
	
	public LookupKey(VisitType visitType, Class<?> clazz) {
		this.visitType = visitType;
		this.clazz = clazz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((visitType == null) ? 0 : visitType.hashCode());
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
		LookupKey other = (LookupKey) obj;
		if (visitType != other.visitType)
			return false;
		
		if(!other.clazz.isAssignableFrom(this.clazz))
			return false;
		
		return true;
	}
	
	
}
