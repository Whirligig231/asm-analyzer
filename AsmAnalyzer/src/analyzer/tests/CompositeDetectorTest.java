package analyzer.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import analyzer.model.Class;
import analyzer.model.Field;
import analyzer.model.FieldStatement;
import analyzer.model.IClass;
import analyzer.model.IField;
import analyzer.model.IFieldStatement;
import analyzer.model.IMethod;
import analyzer.model.IMethodStatement;
import analyzer.model.IModel;
import analyzer.model.Method;
import analyzer.model.MethodStatement;
import analyzer.model.Model;
import analyzer.model.StatementType;
import analyzer.model.pattern.CompositeClass;
import analyzer.model.pattern.CompositeComponentClass;
import analyzer.model.pattern.LeafClass;
import analyzer.visitor.detect.CompositePatternDetector;

public class CompositeDetectorTest {
	
	private void detect(IModel model) {
		CompositePatternDetector cd = new CompositePatternDetector();
		cd.detect(model);
	}
	
	private IClass newClass(IModel model, String name) {
		IClass c = new Class();
		c.setName(name);
		c.setOwner(model);
		model.addClass(c);
		return c;
	}
	
	private IMethod newMethod(IClass c, String returnType, String name, String[] argTypes) {
		IMethod m = new Method(c.getOwner());
		m.setOwner(c);
		m.setName(name);
		m.setArgTypes(argTypes);
		m.setReturnType(returnType);
		c.addMethod(m);
		return m;
	}
	
	private IField newField(IClass c, String type, String name) {
		IField f = new Field(c.getOwner());
		f.setOwner(c);
		f.setName(name);
		f.setType(type);
		c.addField(f);
		return f;
	}

	@Test
	public void testPositive1() {
		
		// Very basic, "vanilla" implementation of Composite
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("IComp");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertTrue(model.getClass("IComp") instanceof CompositeComponentClass);
		assertTrue(model.getClass("Comp") instanceof CompositeClass);
		assertTrue(model.getClass("Leaf") instanceof LeafClass);
		
	}
	
	@Test
	public void testPositive2() {
		
		// Using a subclass instead of implementing an interface
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.setSuperClass(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("IComp");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.setSuperClass(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertTrue(model.getClass("IComp") instanceof CompositeComponentClass);
		assertTrue(model.getClass("Comp") instanceof CompositeClass);
		assertTrue(model.getClass("Leaf") instanceof LeafClass);
		
	}
	
	@Test
	public void testPositive3() {
		
		// Multiple leaf classes and subclasses
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("IComp");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		IClass leaf2 = this.newClass(model, "LeafTwo");
		leaf2.addInterface(component);
		IMethod getterL2 = this.newMethod(leaf2, "String", "getText", new String[]{});
		
		IClass leaf3 = this.newClass(model, "LeafThree");
		leaf3.setSuperClass(leaf2);
		
		IClass composite2 = this.newClass(model, "CompTwo");
		composite2.setSuperClass(composite);
		
		this.detect(model);
		
		assertTrue(model.getClass("IComp") instanceof CompositeComponentClass);
		assertTrue(model.getClass("Comp") instanceof CompositeClass);
		assertTrue(model.getClass("CompTwo") instanceof CompositeClass);
		assertTrue(model.getClass("Leaf") instanceof LeafClass);
		assertTrue(model.getClass("LeafTwo") instanceof LeafClass);
		assertTrue(model.getClass("LeafThree") instanceof LeafClass);
		
	}
	
	@Test
	public void testPositive4() {
		
		// Leaf and Composite implement sub-interfaces; Composite uses a three-typed generic structure
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass component2 = this.newClass(model, "ICompTwo");
		component2.addInterface(component);
		
		IClass component3 = this.newClass(model, "ICompThree");
		component3.addInterface(component);
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component2);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "TernaryMap", "subs");
		list.addTypeParameter("String");
		list.addTypeParameter("String");
		list.addTypeParameter("IComp");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component3);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertTrue(model.getClass("IComp") instanceof CompositeComponentClass);
		assertTrue(model.getClass("Comp") instanceof CompositeClass);
		assertTrue(model.getClass("Leaf") instanceof LeafClass);
		
	}
	
	@Test
	public void testNegative1() {
		
		// No leaf class
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("IComp");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		this.detect(model);
		
		assertFalse(model.getClass("IComp") instanceof CompositeComponentClass);
		assertFalse(model.getClass("Comp") instanceof CompositeClass);
		
	}
	
	@Test
	public void testNegative2() {
		
		// Composite doesn't have a list of leaves (they're integers)
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("Integer");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertFalse(model.getClass("IComp") instanceof CompositeComponentClass);
		assertFalse(model.getClass("Comp") instanceof CompositeClass);
		assertFalse(model.getClass("Leaf") instanceof LeafClass);
		
	}
	
	@Test
	public void testNegative3() {
		
		// Composite has a list of leaves but never uses it
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("IComp");

		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertFalse(model.getClass("IComp") instanceof CompositeComponentClass);
		assertFalse(model.getClass("Comp") instanceof CompositeClass);
		assertFalse(model.getClass("Leaf") instanceof LeafClass);
		
	}
	
	@Test
	public void testNegative4() {
		
		// Composite has a list of the Leaf class specifically, violating DIP
		
		IModel model = new Model();
		
		IClass component = this.newClass(model, "IComp");
		IMethod getText = this.newMethod(component, "String", "getText", new String[]{});
		
		IClass composite = this.newClass(model, "Comp");
		composite.addInterface(component);
		IMethod getter = this.newMethod(composite, "String", "getText", new String[]{});
		IMethod adder = this.newMethod(composite, "v", "addSubtext", new String[]{"IComp"});
		IMethod clearer = this.newMethod(composite, "v", "clearSubtext", new String[]{});
		IField list = this.newField(composite, "Collection", "subs");
		list.addTypeParameter("Leaf");
		
		IFieldStatement fs1 = new FieldStatement();
		fs1.setField(list);
		fs1.setType(StatementType.GET_FIELD);
		getter.addStatement(fs1);
		
		IMethodStatement ms1 = new MethodStatement();
		ms1.setMethod(getText);
		getter.addStatement(ms1);
		
		IFieldStatement fs2 = new FieldStatement();
		fs2.setField(list);
		fs2.setType(StatementType.GET_FIELD);
		adder.addStatement(fs2);
		
		IFieldStatement fs3 = new FieldStatement();
		fs3.setField(list);
		fs3.setType(StatementType.GET_FIELD);
		clearer.addStatement(fs3);
		
		IClass leaf = this.newClass(model, "Leaf");
		leaf.addInterface(component);
		IMethod getterL = this.newMethod(leaf, "String", "getText", new String[]{});
		IMethod setterL = this.newMethod(leaf, "v", "setText", new String[]{"String"});
		IField text = this.newField(leaf, "String", "text");
		
		IFieldStatement fs4 = new FieldStatement();
		fs4.setField(text);
		fs4.setType(StatementType.GET_FIELD);
		getterL.addStatement(fs4);
		
		IFieldStatement fs5 = new FieldStatement();
		fs5.setField(text);
		fs5.setType(StatementType.SET_FIELD);
		setterL.addStatement(fs5);
		
		this.detect(model);
		
		assertFalse(model.getClass("IComp") instanceof CompositeComponentClass);
		assertFalse(model.getClass("Comp") instanceof CompositeClass);
		assertFalse(model.getClass("Leaf") instanceof LeafClass);
		
	}

}
