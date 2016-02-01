package analyzer.model;

public interface IFieldStatement extends IStatement {
	
	public void setType(StatementType type);
	
	public IField getField();
	public void setField(IField field);

}
