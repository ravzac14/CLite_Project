import java.util.*;

public class Parser {
    // Recursive descent parser that inputs a C++LiteF program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.
  
    Token token;          // current token from the input stream
    Lexer lexer;
  
    public Parser(Lexer ts) { // Open the C++Lite source program
        lexer = ts;                          // as a token stream, and
        token = lexer.next();            // retrieve its first Token
    }
  
    private String match (TokenType t) {
        String value = token.value();
        if (token.type().equals(t))
            token = lexer.next();
        else
            error(t);
        return value;
    }
  
    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    public Program program() {
        //Clite:
        // Program --> int main ( ) '{' Declarations Statements '}'
		// p1(decs,bodyAsABlock)
		// Decs = arraylist<dec>
		// Body = block = arraylist<statements>
        //CliteF:
        // Program -->  Decs 
        //              Fun1{Decs Stmts}...FunN{Decs stmts} -- where FunJ is "int main(){...}" where 0 <= J <= N
        //TODO:Test
        Program p1 = new Program(declarations());
        return p1;								
    }
  
    private Declarations declarations() {
        // Declarations --> { Declaration }
		Declarations d = new Declarations(); 
		Type t;
        String s;
        while (token.type().equals(TokenType.Int) 
                || token.type().equals(TokenType.Bool) 
                || token.type().equals(TokenType.Float) 
                || token.type().equals(TokenType.Char)
                || token.type().equals(TokenType.Void)){
			t = type();
			while (!((token.type().equals(TokenType.Semicolon)) 
                    || (token.type().equals(TokenType.RightBrace))
                    || (token.type().equals(TokenType.RightParen))
                    || (token.type().equals(TokenType.Return))
                    || (token.type().equals(TokenType.While))
                    || (token.type().equals(TokenType.If))
                    || (token.type().equals(TokenType.Main)))){
                s = match(TokenType.Identifier);
				if (token.type().equals(TokenType.LeftParen)){ //if its a left paren, its a function dec.
                    d.add(function(t,s));
                } else {    //Else it's a regular variable declaration
                    Variable v = new Variable(s);
                    d.add(new VarDeclaration(v,t)); 	
				    if (token.type().equals(TokenType.Comma)){
					    match(TokenType.Comma);
				    }
                    if (token.type().equals(TokenType.Int) //For arguments 
                        || token.type().equals(TokenType.Bool) 
                        || token.type().equals(TokenType.Float) 
                        || token.type().equals(TokenType.Char)
                        || token.type().equals(TokenType.Void)){ 
                    t = type();
                    }
                    if (token.type().equals(TokenType.Assign)){
                        assignment(s);
                    }
                }
			}
			if (token.type().equals(TokenType.Semicolon)){ match(TokenType.Semicolon); }
            if (token.type().equals(TokenType.RightBrace)){ match(TokenType.RightBrace); }
            if (token.type().equals(TokenType.Main)){ 
                s = match(TokenType.Main);
                d.add(function(t,s));
            }
		}
		return d;
    }
  
	//Didnt use declaration() just did it all in declarations()
  
    //TODO: add functions() function()?

    private Function function(Type t, String id){
        Function f;
        match(TokenType.LeftParen);
        Declarations args = declarations();
        match(TokenType.RightParen);
        match(TokenType.LeftBrace);
        Declarations locals = declarations();
        Block b = statements();
        f = new Function(t,id,args,locals,b);
        return f;
    }

    private Type type () {
        // Clite:   Type  -->  int | bool | float | char 
        // CliteF:  Type  --> int | bool |float | char | void
        Type t = null;
		if (token.type().equals(TokenType.Int)){
			t = new Type(match(TokenType.Int));
		} else if (token.type().equals(TokenType.Bool)){
			t = new Type(match(TokenType.Bool));
		} else if (token.type().equals(TokenType.Float)){
			t = new Type(match(TokenType.Float));
		} else if (token.type().equals(TokenType.Char)){
			t = new Type(match(TokenType.Char));
		} else if (token.type().equals(TokenType.Void)){
            t = new Type(match(TokenType.Void));  
        } else { error("Undefine type in type()"); } //Error if is unmatched type
        return t;    									
    }
  
    private Statement statement() {
        // Clite:   Statement --> ; | Block | Assignment | IfStatement | WhileStatement
        // CliteF:  Statement --> ; | Block | Assignment | IfStatement | WhileStatement | CallStatement | ReturnStatement
        //TODO:Test  
        Statement s = null;
		if (token.type().equals(TokenType.LeftBrace)){
			match(TokenType.LeftBrace);
			s = statements();
			match(TokenType.RightBrace);
		} else if (token.type().equals(TokenType.Identifier)){
            String id = match(TokenType.Identifier);
			if (token.type().equals(TokenType.LeftParen)){
                s = callStatement(id);
            } else {
                s = assignment(id);
            }
		} else if (token.type().equals(TokenType.If)){
			s = ifStatement();
		} else if (token.type().equals(TokenType.While)){
			s = whileStatement();
        } else if (token.type().equals(TokenType.Return)){
            s = returnStatement();
		} else { error("No matching statement type in statement()");} //Error if no statement type match	
        return s;											
    }
  
    private Block statements () {
        // Block --> '{' Statements '}'
		Block b = new Block();					//At the top level AST thinks block==statements, and they behave the same
		while (!(token.type().equals(TokenType.RightBrace))){	
			b.members.add(statement());
		}
        return b;
    }
  
    private Assignment assignment (String id) {
        // Assignment --> Identifier = Expression ;
        Variable target = new Variable(id);
		match(TokenType.Assign);
		Expression source = expression();
		match(TokenType.Semicolon);
		return new Assignment(target, source);
    }
  
    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
        Conditional c;
		match(TokenType.If);
		match(TokenType.LeftParen);
		Expression e = expression();
		match(TokenType.RightParen);
		Statement s = statement();
		if (token.type().equals(TokenType.Else)){
			match(TokenType.Else);
			Statement elseS = statement();
			c = new Conditional(e,s,elseS);
		} else {
			c = new Conditional(e,s);
		}	
		return c;  
    }
  
    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
		Loop l;
		match(TokenType.While);
		match(TokenType.LeftParen);
		Expression e = expression();
		match(TokenType.RightParen);
		Statement s = statement();
		l = new Loop(e,s);
        return l; 
    }

    private CallS callStatement (String id) {
        //TODO:TEST
        String name = id;
        match(TokenType.LeftParen);
        Expressions exps = expressions();
        match(TokenType.RightParen);
        match(TokenType.Semicolon);
        return (new CallS(name,exps));
    }

    private Return returnStatement () {
        //TODO: TEST 
        // either you can use return statements in void functions (which would allow non-targetted returns) in addition to the two cases below 
        //      OR you can only use returns in non-void functions (which can be use in a CallE (ie has a target) or in a CallS (no target))
        Return r;
        match(TokenType.Return);
        r = new Return(expression());
        match(TokenType.Semicolon);
        return r;
    }

    //TODO: TEST
    private Expressions expressions(){
        //A list of Expression(s)
        //"(" Expression(s) ")" comma delimited
        Expressions es = new Expressions();
        while (!(token.type().equals(TokenType.RightParen))){
            es.add(expression());
            if (token.type().equals(TokenType.Comma)){
                match(TokenType.Comma);
            }
        }
        return es;
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
		Expression e = conjunction();
		while (token.type().equals(TokenType.Or)){
			Operator op = new Operator(match(token.type()));
			Expression e2 = conjunction();
			e = new Binary(op,e,e2);
		}
        return e;
    }
  
    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
		Expression e = equality();
		while (token.type().equals(TokenType.And)){
			Operator op = new Operator(match(token.type()));
			Expression e2 = equality();
			e = new Binary(op,e,e2);
		}
        return e;			
    }
  
    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
		Expression e = relation();
		while (isEqualityOp()){
			Operator op = new Operator(match(token.type()));
			Expression e2 = relation();
			e = new Binary(op,e,e2);
		}
        return e;				
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition] 
		Expression e = addition();
		while (isRelationalOp()){
			Operator op = new Operator(match(token.type()));
			Expression e2 = addition();
			e = new Binary(op,e,e2);
		}
        return e;	
    }
  
    private Expression addition () {
        // Addition --> Term { AddOp Term }
        Expression e = term();
        while (isAddOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = term();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        Expression e = factor();
        while (isMultiplyOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = factor();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary 
        if (isUnaryOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term = primary();
            return new Unary(op, term);
        }
        else return primary();
    }
  
    private Expression primary () {
        // Primary --> Identifier [( Expressions )]| Literal | ( Expression )
        //             | Type ( Expression )
        //TODO: TEST
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) {
            String s = match(TokenType.Identifier);
            if (token.type().equals(TokenType.LeftParen)){
                match(TokenType.LeftParen);
                Expressions exps = expressions();
                match(TokenType.RightParen);
                e = new CallE(s,exps);
            } else {
                e = new Variable(s);
            }
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) {
            token = lexer.next();
            e = expression();       
            match(TokenType.RightParen);
        } else if (isType( )) {
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen);
            Expression term = expression();
            match(TokenType.RightParen);
            e = new Unary(op, term);
        } else {error("primary must == Identifier | Literal | ( | Type");}
        return e;
    }

    private Value literal( ) {
		// Literal --> Boolean | Integer | Char | Float
		//
		String newValue = "";
		Value realVal;
		if (token.type().equals(TokenType.IntLiteral)){				//If is intLiteral token
			newValue = match(TokenType.IntLiteral);
			realVal = new IntValue(Integer.parseInt(newValue));
		} else if (token.type().equals(TokenType.CharLiteral)){		//If is charLiteral token 
			newValue = match(TokenType.CharLiteral);
			realVal = new CharValue(newValue.charAt(0));
		} else if (token.type().equals(TokenType.FloatLiteral)){	//If is floatLiteral token
			newValue = match(TokenType.FloatLiteral);
			realVal = new FloatValue(Float.parseFloat(newValue));
		} else if (token.type().equals(TokenType.False)){			//If is boolLiteral:False token
			realVal = new BoolValue(false);
		} else if (token.type().equals(TokenType.True)){			//If is boolLoteral:True token
			realVal = new BoolValue(true);
		} else {
			error("No maching literal type");
			realVal = null;						//returns a null object if none of the literal types are found 
		}
        return realVal;  						
    }
  

    private boolean isAddOp( ) {
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) {
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }
    
    private boolean isUnaryOp( ) {
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isEqualityOp( ) {
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) {
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) {
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char)
            || token.type().equals(TokenType.Void);
    }
    
    private boolean isLiteral( ) {
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }
    
    private boolean isBooleanLiteral( ) {
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }
    
    public static void main(String args[]) {
		String myProg = args[0];
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
		System.out.println("Begin Parsing..." + myProg);
        prog.display();           // display abstract syntax tree for testing Parser alone
    } //main

} // Parser
