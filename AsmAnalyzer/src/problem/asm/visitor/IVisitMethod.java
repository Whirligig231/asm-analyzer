package problem.asm.visitor;

@FunctionalInterface
public interface IVisitMethod {
	public void execute(ITraverser t);
}