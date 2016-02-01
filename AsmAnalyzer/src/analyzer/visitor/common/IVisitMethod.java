package analyzer.visitor.common;

@FunctionalInterface
public interface IVisitMethod {
	public void execute(ITraverser t);
}