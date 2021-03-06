# asm-analyzer
Git repository for CSSE374 project

Authors:

Steven Kneisler (Github: Stevenator1)

Christian Schulz (Github: Whirligig231)

# Description

asm-analyzer is a program that uses the ASM library to produce an automatic UML diagram of a class structure. Use it by passing as command-line arguments to DesignParser the full names of the classes you want the analyzer to diagram. These must be included in the build path. For instance, "lab13.problem.FileEvent" is a valid parameter. If you provide multiple classes, the analyzer will also search for relationships between them.

asm-analyzer also includes a tool for automated sequence diagrams using the SDEdit format. To use this tool, pass SequenceGenerator an argument containing the full signature of the method, without the return type but including everything else, with fully qualified class names, e.g. "java.lang.String.substring(int, int)" is a valid parameter. Optionally, pass a second argument indicating the number of levels of method calls to show; this defaults to 5.

# Design (UML Class Diagram)

![UML Class Diagram for the project](./AsmAnalyzer/docs/project_manual.png)

# Features

asm-analyzer's UML diagram tool currently supports:

- Classes and interfaces
- Public, private, protected, and default modifiers
- Methods and fields
- Method signatures and field types
- Inheritance and implementation relationships
- Detection and annotation of Singleton classes

asm-analyzer's sequence diagram tool currently supports only class method calls, with no control structure.

# Use

To use asm-analyzer's UML diagram tool, pass DesignParser.main a list of class names you want to include in the diagram. This must be a fully qualified name, including the package.

To use asm-analyzer's sequence diagram tool, pass SequenceGenerator.main a fully qualified method signature, including the owner class (with its package), the method name, and fully qualified argument types (but *not* the return type).

# Design and implementation

## Milestone 1

![UML Class Diagram for Milestone 1](./AsmAnalyzer/docs/old_diagrams/project_manual_01.png)

The UML diagram tool has two main layers, each of which is an instance of the Visitor pattern. In the first layer, the program uses ASM visitors to traverse the classes and their methods and fields. These visitors decorate one another to access and handle different parts of the class, building a problem.asm.model.Class model structure. Note that in order to pass the structure through decorators, we need to ensure that the decorator implements the IClassHolder interface, allowing its decorators to access its current model. Christian performed the modifications to the ClassVisitor code required to implement this.

Each class is thus converted to a Class model structure, implemented by Christian. Then, a second Visitor, implemented by Steven, traverses this class model structure in order to convert it to a DOT source file, analogous to the XML car traverser we implemented in Lab 3-1. Christian, having prior experience with DOT, helped with some of the string parsing required to build the DOT source.

The code also has automated unit tests, implemented by Steven, which test the code by using some example classes to verify that the Visitors give the expected output.

## Milestone 2

![UML Class Diagram for Milestone 2](./AsmAnalyzer/docs/old_diagrams/project_manual_02.png)

In this milestone, we added support for use and association arrows. Christian changed the structure of the class model to accommodate this, adding an additional level of structure, IModel, which contains IClass classes and IRelation arrow objects. Arrows are now created as IRelation objects, with an enum to indicate the type of arrow.

Steven helped to add these additional arrows to the DOT conversion visitor and revised the unit tests.

## Milestone 3

![UML Class Diagram for Milestone 3](./AsmAnalyzer/docs/old_diagrams/project_manual_03.png)

In this milestone, sequence diagramming was added. This code works by keeping a queue of which methods need to be found. The code, created by Christian, visits these methods' classes, finds the appropriate methods, and adds them into the model. Christian also added a calls list for each method, giving the calls to other methods that it makes.

Christian then proceeded to create two new visitors, ClassSDOutputStream and MethodSDOutputStream, which are used to generate sequence diagrams. ClassSDOutputStream traverses the IModel in a similar manner to ClassUmlOutputStream to create SDEdit declarations for all of the classes. MethodSDOutputStream finds the specific method that the SequenceGenerator gives it and recursively visits all of the methods that it calls.

Steven, meanwhile, refactored our visitor code to use the Command pattern, so that we can continue to develop visitors for our IModel model without having a large number of different visit() methods, with different signatures, in IVisitor and VisitorAdapter. In the new design, VisitorAdapter is replaced by Visitor, a concrete class, and the various OutputStream classes compose a Visitor rather than extending one.

## Milestone 4

![UML Class Diagram for Milestone 4](./AsmAnalyzer/docs/old_diagrams/project_manual_04.png)

In this milestone, Christian added a way to detect Singleton classes, while Steven added test classes for this new functionality. The first step was to refactor our handling of method calls, making them a subtype of a new model type called IStatement, representing a statement within a method body. The second step for this was to add an IPattern interface to the model, allowing IModel objects to contain information about which patterns are present in the design. The third step was to create a new visitor-based class called SingletonDetector, which visits an IModel and adds detected singleton classes to it. The last step was to add a new type of OutputStream, AnnotatedUmlOutputStream, that adds color outlines and <<Singleton>> annotations to the UML diagram.

## Milestone 5

![UML Class Diagram for Milestone 5](./AsmAnalyzer/docs/old_diagrams/project_manual_05.png)

In this milestone, Christian added the logic for Adapter and Decorator detection.  This included a refactoring of our model design, with the new inclusion of AnnotatedRelation, AnnotatedClass, and their related classes.  AdapterDetector, DecoratorDetector, and DecoratorSubclassDetector contain the logic for the model visitor, and created AnnotatedClasses and AnnotatedRelations to include in the model.  Steven updated the project documentation and manual UML diagram, as well as created the testing for the Adapter and Decorator patterns.

## Milestone 6

![UML Class Diagram for Milestone 6](./AsmAnalyzer/old_diagrams/project_manual_06.png)

In this milestone, we switched roles; Steven implemented the CompositeDetector, while Christian created test cases and kept our documentation up to date. On the implementation side, there were only two small changes to existing code (besides the obvious change of adding the new detector to main()): ComponentClass was renamed to DecoratorComponentClass, as the Composite pattern uses its own component class; and generic type parameter information was added to IField. Otherwise, our design had evolved to the point where no significant changes were necessary in order to implement the new detector and its annotations.

Our method of testing also changed: rather than trying to perform integration testing with the new detector, as we knew our existing systems were working for the other detectors, we merely unit-tested the CompositeDetector itself, adding test cases for it.

## Milestone 7

![UML Class Diagram for Milestone 7](./AsmAnalyzer/docs/project_manual.png)

In this milestone, Christian first made some necessary changes to the existing code, adding Patterns back as their own class for use in the pattern tree in the GUI. He also changed the existing pattern detectors and output stream to use the Observer pattern, reporting back on what they are currently visiting for use in the loading bar.

Christian then implemented a GUI with a phase/pipeline system to automatically load a config file (.properties) and run it through various phases. The phases again use the Observer pattern to report back to the GUI. A Factory is used to generate these phases.

In order for errors to be reported correctly, a modular system for choosing an error reporting method (console, dialog, etc.) was needed. For this we used the Strategy pattern; IErrorHandler acts as a strategy for handling an error given the error string.

Steven then implemented the rest of the GUI. ***STEVEN: Please put info here about how you did it!***