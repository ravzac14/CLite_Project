// Abstract syntax for the language C++Lite,
// exactly as it appears in Appendix B.

import java.util.*;


class Program {
    // CLite:   Program = Declarations decpart ; Block body
    // CliteF:  Program = Declarations global ; Functions functions
    //TODO: Test for CliteF

    VarDeclarations decpart;
	Functions functions;    //Arraylist of functions

    Program (Declarations ds) {
        decpart = new VarDeclarations();
        functions = new Functions();
        for (Declaration d : ds){
            if (d instanceof Function){
                functions.add((Function)d);
            } else {
                decpart.add((VarDeclaration)d);
            }
        }
    }
	
    //TODO: Test for CliteF Program
	public void display(){
		System.out.println("Program (abstract syntrax):");
		System.out.println("  Declarations:");
		System.out.println("    Declarations = {");
		for(Declaration d : decpart){
			d.display(6); 	//this one is hardcoded
		}
		System.out.println("    }");
		System.out.println("  Functions:");
		for(Function f : functions){
			f.display(2); 	//this one is hardcoded
		}
	}

}

class Functions extends ArrayList<Function> {
    //Functions = Function*
    //a list of functions f1, f2, ..., fn
}

class Function extends Declaration {
    //Function = Type t; String id; Declarations params, locals; Block body
    //Type t in Declaration
    String id;
    Declarations params;
    Declarations locals;
    Block body;
    
    //TODO:Add MORE? logic and helper methods
    Function (Type type, String name, Declarations parameters, Declarations localss, Block functionBody){
        super(type);
        id = name;
        params = parameters;
        locals = localss;
        body = functionBody;
    }

    Type getType() { return t; }

    void setType(Type newtype) { t = newtype; }

    String getID() { return id; }

    void setID(String newID) { id = newID; }

    //TODO: Test display method
    public void display(int ind){
        for (int i = 0; i < ind; i++){
            System.out.print(" ");
        }
        System.out.println(t+" "+id+": "); 
        for (int i = 0; i < ind; i++){
            System.out.print("  ");
        }
        System.out.println("Parameters: ");
        for (Declaration d : params){
            d.display(ind + 4);
        }
        for (int i = 0; i < ind; i++){
            System.out.print("  ");
        }
        System.out.println("Declarations local to "+id+": ");
        for (Declaration d : locals){
            d.display(ind + 4);
        }
        for (int i = 0; i < ind; i++){
            System.out.print("  ");
        }
        System.out.println(id+"'s Body: ");
        body.display(ind + 4);
    }
}

class VarDeclaration extends Declaration {
    //Type t from Declaration
    Variable v;

    VarDeclaration(Variable var, Type type){
        super(type);
        this.v = var;
    }

    public void display(int ind){
		for (int i = 0; i < ind; i++){
            System.out.print(" ");
        }
        System.out.print("<" + v + ", " + t + ">\n");
    }
}

class VarDeclarations extends ArrayList<VarDeclaration> {
    // Declarations = Declaration*
    // (a list of declarations d1, d2, ..., dn)
	
}

class Declarations extends ArrayList<Declaration> {
    // Declarations = Declaration*
    // (a list of declarations d1, d2, ..., dn)
	
}


abstract class Declaration {
// Declaration = Variable v; Type t
    //Variable v;
    Type t;

    Declaration (Type type) {
        t = type;
    } // declaration */
	
	abstract public void display(int ind);
}

class Type {
    // Clite:   Type = int | bool | char | float 
    // CliteF:  Type = int | bool | char | float | void
    //TODO: Test that this was all that was needed
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type CHAR = new Type("char");
    final static Type FLOAT = new Type("float");
    final static Type VOID = new Type("void");
    // final static Type UNDEFINED = new Type("undef");
    
    private String id;

    Type (String t) { id = t; }

    public String toString ( ) { return id; }

	//NOTE:This didn't exist in the original source code ... -.-
	public Boolean equals(Type test){ return this.id.equals(test.id); }
}

abstract class Statement {
    // CLite:   Statement = Skip | Block | Assignment | Conditional | Loop
	// CliteF:  Statement = Skip | Block | Assignment | Conditional | Loop | Call | Return
	abstract void display(int ind);
}

class Skip extends Statement {

	public void display(int ind){
		System.out.print("");
	}
}

class Block extends Statement {
    // Block = Statement*
    //         (a Vector of members)
    public ArrayList<Statement> members = new ArrayList<Statement>();
	
	public void display(int ind){
		for (Statement s : members){
			s.display(2 + ind);
		}
	}
}

class Assignment extends Statement {
    // Assignment = Variable target; Expression source
    Variable target;
    Expression source;

    Assignment (Variable t, Expression e) {
        target = t;
        source = e;
    }
	
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}	
		System.out.print("Assignment:\n");
		target.display(2 + ind);
		source.display(2 + ind);
	}
}

class Conditional extends Statement {
// Conditional = Expression test; Statement thenbranch, elsebranch
    Expression test;
    Statement thenbranch, elsebranch;
    // elsebranch == null means "if... then"
    
    Conditional (Expression t, Statement tp) {
        test = t; thenbranch = tp; elsebranch = new Skip( );
    }
    
    Conditional (Expression t, Statement tp, Statement ep) {
        test = t; thenbranch = tp; elsebranch = ep;
    }
    
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Conditional:\n");
		test.display(2 + ind);
		for (int i = 0; i < (2 + ind); i++){
			System.out.print(" ");
		}
		System.out.print("Then Branch:\n");
		thenbranch.display(2 + ind);
		for (int i = 0; i < (2 + ind); i++){
			System.out.print(" ");
		}
		System.out.print("Else Branch?:\n");
		elsebranch.display(2 + ind);
	}
}

class Loop extends Statement {
// Loop = Expression test; Statement body
    Expression test;
    Statement body;

    Loop (Expression t, Statement b) {
        test = t; body = b;
    }
    
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Loop:\n");
		test.display(2 + ind);
		for (int i = 0; i < (2 + ind); i++){
			System.out.print(" ");
		}
		System.out.print("Block:\n");
		body.display(2 + ind);
	}
}

class CallS extends Statement {
    // CallS = String name; Expressions args
    String name;
    Expressions args;

    //TODO: Add any MORE? logic/helper methods
    CallS(String newName, Expressions arguments){
        name = newName;
        args = arguments;
    }

    String getName() { return name; }
    void setName(String newName) { name = newName; }

    //TODO: TEST display method
    public void display(int ind){
		for (int i = 0; i < (ind); i++){
			System.out.print(" ");
		}
        System.out.print("Calling " + name + ":\n");
        for (Expression e : args) {
            e.display(ind + 2);
        }
    }
}

class Return extends Statement {
    // Return = Variable target; Expression result
    Variable target = null;
    Expression result;
    //TODO: Add MORE? logic/helper methods
    Return(Variable t, Expression r){
        target = t;
        result = r;
    }

    Return(Expression r){
        result = r;
        //It gets target from the assigned call 
    }

    Variable getTarget(){ return target; }
    Expression getResult(){ return result; }
    void setTarget(Variable newTarget){ target = newTarget; }
    void setResult(Expression newResult){ result = newResult; }

    //TODO: TEST a display method
	public void display(int ind){
        for (int i = 0; i < (ind); i++){
		    System.out.print(" ");
	    }
        System.out.print("Return:\n");
	    if (target != null){
            for (int i = 0; i < (ind + 2); i++){
		        System.out.print(" ");
	        }
            target.display(ind + 4);    //Maybe just two here
            System.out.print("Target:\n");
	    }
        for (int i = 0; i < (ind + 2); i++){
		    System.out.print(" ");
	    }
        System.out.print("Result:\n");
        result.display(ind + 4);    //Maybe just two here
    }
}

class Expressions extends ArrayList<Expression> {
    //Expressions = Expresson*
    //Just a list of expressions e1, e2, ..., en
}

abstract class Expression {
    // Clite:   Expression = Variable | Value | Binary | Unary
	// CliteF:  Expression = Variable | Value | Binaru | Unary | Call
	abstract void display(int ind);
}

class Variable extends Expression {
    // Variable = String id
    private String id;

    Variable (String s) { id = s; }

    public String toString( ) { return id; }
    
    public boolean equals (Object obj) {
        String s = ((Variable) obj).id;
        return id.equals(s); // case-sensitive identifiers
    }
    
    public int hashCode ( ) { return id.hashCode( ); }
	
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Variable: " + id + "\n");
	}
}

abstract class Value extends Expression {
    // Value = IntValue | BoolValue |
    //         CharValue | FloatValue
    protected Type type;
    protected boolean undef = true;

    int intValue ( ) {
        assert false : "should never reach here";
        return 0;
    }
    
    boolean boolValue ( ) {
        assert false : "should never reach here";
        return false;
    }
    
    char charValue ( ) {
        assert false : "should never reach here";
        return ' ';
    }
    
    float floatValue ( ) {
        assert false : "should never reach here";
        return 0.0f;
    }

    boolean isUndef( ) { return undef; }

    Type type ( ) { return type; }

    static Value mkValue (Type type) {
        if (type.equals(Type.INT)) return new IntValue( );
        if (type.equals(Type.BOOL)) return new BoolValue( );
        if (type.equals(Type.CHAR)) return new CharValue( );
        if (type.equals(Type.FLOAT)) return new FloatValue( );
        throw new IllegalArgumentException("Illegal type in mkValue");
    }

	public void display(int ind){
		System.out.println("In Value, shouldnt display right?");
	}
}

class IntValue extends Value {
    private int value = 0;

    IntValue ( ) { type = Type.INT; }

    IntValue (int v) { this( ); value = v; undef = false; }

    int intValue ( ) {
        assert !undef : "reference to undefined int value";
        return value;
    }
	
    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }
	
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("IntValue: " + value + "\n");
	}
}

class BoolValue extends Value {
    private boolean value = false;

    BoolValue ( ) { type = Type.BOOL; }

    BoolValue (boolean v) { this( ); value = v; undef = false; }

    boolean boolValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value;
    }

    int intValue ( ) {
        assert !undef : "reference to undefined bool value";
        return value ? 1 : 0;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("BoolValue: " + value + "\n");
	}
}

class CharValue extends Value {
    private char value = ' ';

    CharValue ( ) { type = Type.CHAR; }

    CharValue (char v) { this( ); value = v; undef = false; }

    char charValue ( ) {
        assert !undef : "reference to undefined char value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("CharValue: " + value + "\n");
	}
}

class FloatValue extends Value {
    private float value = 0;

    FloatValue ( ) { type = Type.FLOAT; }

    FloatValue (float v) { this( ); value = v; undef = false; }

    float floatValue ( ) {
        assert !undef : "reference to undefined float value";
        return value;
    }

    public String toString( ) {
        if (undef)  return "undef";
        return "" + value;
    }

	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("FloatValue: " + value + "\n");
	}
}

class Binary extends Expression {
// Binary = Operator op; Expression term1, term2
    Operator op;
    Expression term1, term2;

    Binary (Operator o, Expression l, Expression r) {
        op = o; term1 = l; term2 = r;
    } // binary

	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Binary:\n");
		op.display(2 + ind);
		term1.display(2 + ind);
		term2.display(2 + ind);
	}
}

class Unary extends Expression {
    // Unary = Operator op; Expression term
    Operator op;
    Expression term;

    Unary (Operator o, Expression e) {
        op = o; term = e;
    } // unary

	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Unary:\n");
		op.display(2 + ind);
		term.display(2 + ind);
	}
}

class CallE extends Expression {
    // CallE = String name; Expressions args
    String name;
    Expressions args;
    
    //TODO: Add any logic/helper methods
    CallE(String newName, Expressions arguments){
        name = newName;
        args = arguments;
    }

    void setName(String newName){ name = newName; }
    void setArgs(Expressions newArgs){ args = newArgs; }
    String getName(){ return name; }
    Expressions getArgs(){ return args; }

    //TODO: Add a display method
    public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
        System.out.print("Calling "+name+":\n");
        for (Expression e : args){
            e.display(ind + 2);
        }
    }
}

class Operator {
    // Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
    // BooleanOp = && | ||
    final static String AND = "&&";
    final static String OR = "||";
    // RelationalOp = < | <= | == | != | >= | >
    final static String LT = "<";
    final static String LE = "<=";
    final static String EQ = "==";
    final static String NE = "!=";
    final static String GT = ">";
    final static String GE = ">=";
    // ArithmeticOp = + | - | * | /
    final static String PLUS = "+";
    final static String MINUS = "-";
    final static String TIMES = "*";
    final static String DIV = "/";
    // UnaryOp = !    
    final static String NOT = "!";
    final static String NEG = "-";
    // CastOp = int | float | char
    final static String INT = "int";
    final static String FLOAT = "float";
    final static String CHAR = "char";
    // Typed Operators
    // RelationalOp = < | <= | == | != | >= | >
    final static String INT_LT = "INT<";
    final static String INT_LE = "INT<=";
    final static String INT_EQ = "INT==";
    final static String INT_NE = "INT!=";
    final static String INT_GT = "INT>";
    final static String INT_GE = "INT>=";
    // ArithmeticOp = + | - | * | /
    final static String INT_PLUS = "INT+";
    final static String INT_MINUS = "INT-";
    final static String INT_TIMES = "INT*";
    final static String INT_DIV = "INT/";
    // UnaryOp = !    
	final static String INT_NEG = "INTU-"; 
    // RelationalOp = < | <= | == | != | >= | >
    final static String FLOAT_LT = "FLOAT<";
    final static String FLOAT_LE = "FLOAT<=";
    final static String FLOAT_EQ = "FLOAT==";
    final static String FLOAT_NE = "FLOAT!=";
    final static String FLOAT_GT = "FLOAT>";
    final static String FLOAT_GE = "FLOAT>=";
    // ArithmeticOp = + | - | * | /
    final static String FLOAT_PLUS = "FLOAT+";
    final static String FLOAT_MINUS = "FLOAT-";
    final static String FLOAT_TIMES = "FLOAT*";
    final static String FLOAT_DIV = "FLOAT/";
    // UnaryOp = !    
	final static String FLOAT_NEG = "FLOATU-";
    // RelationalOp = < | <= | == | != | >= | >
    final static String CHAR_LT = "CHAR<";
    final static String CHAR_LE = "CHAR<=";
    final static String CHAR_EQ = "CHAR==";
    final static String CHAR_NE = "CHAR!=";
    final static String CHAR_GT = "CHAR>";
    final static String CHAR_GE = "CHAR>=";
    // RelationalOp = < | <= | == | != | >= | >
    final static String BOOL_LT = "BOOL<";
    final static String BOOL_LE = "BOOL<=";
    final static String BOOL_EQ = "BOOL==";
    final static String BOOL_NE = "BOOL!=";
    final static String BOOL_GT = "BOOL>";
    final static String BOOL_GE = "BOOL>=";
	final static String BOOL_NOT = "BOOL!";
	final static String BOOL_AND = "BOOL&&";
	final static String BOOL_OR = "BOOL||";
    // Type specific cast
    final static String I2F = "I2F";
    final static String F2I = "F2I";
    final static String C2I = "C2I";
    final static String I2C = "I2C";
    
    String val;
    
    Operator (String s) { val = s; }

    public String toString( ) { return val; }
    public boolean equals(Object obj) { return val.equals(obj); }
    
    boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    boolean RelationalOp ( ) {
        return val.equals(LT) || val.equals(LE) || val.equals(EQ)
            || val.equals(NE) || val.equals(GT) || val.equals(GE);
    }
    boolean ArithmeticOp ( ) {
        return val.equals(PLUS) || val.equals(MINUS)
            || val.equals(TIMES) || val.equals(DIV);
    }
    boolean NotOp ( ) { return val.equals(NOT) ; }
    boolean NegateOp ( ) { return val.equals(NEG) ; }
    boolean intOp ( ) { return val.equals(INT); }
    boolean floatOp ( ) { return val.equals(FLOAT); }
    boolean charOp ( ) { return val.equals(CHAR); }

    final static String intMap[ ] [ ] = {
        {PLUS, INT_PLUS}, {MINUS, INT_MINUS},
        {TIMES, INT_TIMES}, {DIV, INT_DIV},
        {EQ, INT_EQ}, {NE, INT_NE}, {LT, INT_LT},
        {LE, INT_LE}, {GT, INT_GT}, {GE, INT_GE}
    };

    final static String intMapU[ ] [ ] = {
        {FLOAT, I2F}, 
        {CHAR, I2C},
        {NEG, INT_NEG}
    };

    final static String floatMap[ ] [ ] = {
        {PLUS, FLOAT_PLUS}, {MINUS, FLOAT_MINUS},
        {TIMES, FLOAT_TIMES}, {DIV, FLOAT_DIV},
        {EQ, FLOAT_EQ}, {NE, FLOAT_NE}, {LT, FLOAT_LT},
        {LE, FLOAT_LE}, {GT, FLOAT_GT}, {GE, FLOAT_GE}
    };

    final static String floatMapU[ ] [ ] = {
        {NEG, FLOAT_NEG},
        {INT, F2I}
    };

    final static String charMap[ ] [ ] = {
        {EQ, CHAR_EQ}, {NE, CHAR_NE}, {LT, CHAR_LT},
        {LE, CHAR_LE}, {GT, CHAR_GT}, {GE, CHAR_GE},
        {INT, C2I}
    };

    final static String boolMap[ ] [ ] = {
        {EQ, BOOL_EQ}, {NE, BOOL_NE}, {LT, BOOL_LT},
        {LE, BOOL_LE}, {GT, BOOL_GT}, {GE, BOOL_GE},
		{NOT, BOOL_NOT}, {AND, BOOL_AND}, {OR, BOOL_OR}
    };

    final static private Operator map (String[][] tmap, String op) {
        for (int i = 0; i < tmap.length; i++)
            if (tmap[i][0].equals(op))
                return new Operator(tmap[i][1]);
        assert false : "should never reach here";
        return null;
    }

    final static public Operator intMap (String op) {
        return map (intMap, op);
    }

    final static public Operator intMapU (String op){
        return map (intMapU, op);
    }

    final static public Operator floatMap (String op) {
        return map (floatMap, op);
    }

    final static public Operator floatMapU (String op) {
        return map (floatMapU, op);
    }

    final static public Operator charMap (String op) {
        return map (charMap, op);
    }

    final static public Operator boolMap (String op) {
        return map (boolMap, op);
    }
	
	public void display(int ind){
		for (int i = 0; i < ind; i++){
			System.out.print(" ");
		}
		System.out.print("Operator: " + val + "\n");
	}
}
