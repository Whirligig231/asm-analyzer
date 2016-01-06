# asm-analyzer
Git repository for CSSE374 project

Authors:

Steven Kneisler (Github: Stevenator1)

Christian Schulz (Github: Whirligig231)

# Description

asm-analyzer is a program that uses the ASM library to produce an automatic UML diagram of a class structure. Use it by passing as command-line arguments the full names of the classes you want the analyzer to diagram. These must be included in the build path. For instance, "lab13.problem.FileEvent" is a valid parameter. If you provide multiple classes, the analyzer will also search for relationships between them.

# Features

asm-analyzer currently supports:

- Classes and interfaces
- Public, private, protected, and default modifiers
- Methods and fields
- Method signatures and field types
- Inheritance and implementation relationships

# Design and implementation

The analyzer has two main layers, each of which is an instance of the Visitor pattern. In the first layer, the program uses ASM visitors to traverse the classes and their methods and fields. These visitors decorate one another to access and handle different parts of the class, building a problem.asm.model.Class model structure. Note that in order to pass the structure through decorators, we need to ensure that the decorator implements the IClassHolder interface, allowing its decorators to access its current model. Christian performed the modifications to the ClassVisitor code required to implement this.

Each class is thus converted to a Class model structure, implemented by Christian. Then, a second Visitor, implemented by Steven, traverses this class model structure in order to convert it to a DOT source file, analogous to the XML car traverser we implemented in Lab 3-1. Christian, having prior experience with DOT, helped with some of the string parsing required to build the DOT source.

The code also has automated unit tests, implemented by Steven, which test the code by using some example classes to verify that the Visitors give the expected output.