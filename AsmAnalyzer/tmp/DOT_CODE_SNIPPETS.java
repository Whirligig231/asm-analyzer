// Before doing anything:

this.write("digraph {\n\nrankdir=\"BT\";\n\nnode [shape=record];\n\n");

// Pre-visit for class:

this.write(this.trimClassName(classModel.getName()));
this.write(" [label=\"{");
this.write(this.trimClassName(classModel.getName()));
this.write("|");

// Visit for class:

this.write("|");

// Post-visit for class:

this.write("}\"];\n");
String superClass = classModel.getSuperClass();
if (!superClass.equals("java/lang/Object")) {
    this.write(this.trimClassName(classModel.getName()));
    this.write(" -> ");
    this.write(this.trimClassName(superClass));
    this.write(";\n");
}
for (int i = 0; i < classModel.getInterfaces().length; i++) {
    this.write(this.trimClassName(classModel.getName()));
    this.write(" -> ");
    this.write(this.trimClassName(classModel.getInterfaces()[i]));
    this.write(" [arrowhead=onormal];\n");
}
this.write("\n");

// Visit for field:

this.write(field.getAccessLevel().getSymbol());
this.write(" ");
this.write(this.trimClassName(field.getType()));
this.write(" ");
this.write(field.getName());
this.write("\n");

// Visit for method:

this.write(method.getAccessLevel().getSymbol());
this.write(" ");
this.write(this.trimClassName(method.getReturnType()));
this.write(" ");
this.write(method.getName());
this.write("(");
for (int i = 0; i < method.getArgTypes().length; i++) {
    if (i > 0)
        this.write(", ");
    this.write(this.trimClassName(method.getArgTypes()[i]));
}
this.write("\n");

// After everything:

this.write("}");

// The trimClassName function:

private String trimClassName(String className) {
    return className.substring(className.lastIndexOf('/') + 1);
}

// getSymbol will be implemented in AccessLevel