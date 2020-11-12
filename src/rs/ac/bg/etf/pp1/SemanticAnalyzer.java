package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	Struct currentType;
	int constantValue;
	Struct constantType;
	Obj currentMethod;
	boolean isVoid;
	boolean returnExists = false;
	boolean mainExists = false;
	boolean isArray;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	/*
	 * public void report_symbol_info(String message, Obj obj) {
	 * SymbolTableVisitor stv = new SymTableVisitorBoolean(); obj.accept(stv);
	 * StringBuilder msg = new StringBuilder(message); msg.append(".  ");
	 * msg.append("Ulaz u tabeli simbola: "); msg.append(stv.getOutput());
	 * log.info(msg.toString()); }
	 */

	public SemanticAnalyzer() {
		currentType = Tab.noType;
		constantType = Tab.noType;
		currentMethod = Tab.noObj;
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", new Struct(Struct.Bool)));
	}

	public void visit(Program program) {
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();

		if (!mainExists) {
			report_error("Greska: Nije pronadjena main metoda tipa void i bez argumenata!", null);
			return;
		}
	}

	public void visit(ProgName progName) {
		// Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", new
		// Struct(Struct.Bool)));
		// Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool));
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}

	public void visit(Type type) {
		Obj t = Tab.find(type.getTypeName());
		if (t == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == t.getKind()) {
				type.struct = currentType = t.getType();
			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = Tab.noType;
			}
		}
	}

	public void visit(ConstTypeNum constTypeNum) {
		constantValue = constTypeNum.getNum();
		constantType = Tab.intType;
	}

	public void visit(ConstTypeBool constTypeBool) {
		constantValue = constTypeBool.getB() ? 1 : 0;
		constantType = Tab.find("bool").getType();
	}

	public void visit(ConstTypeChar constTypeChar) {
		constantValue = constTypeChar.getC();
		constantType = Tab.charType;
	}

	public void visit(CnstDecl cnstDecl) {
		String constName = cnstDecl.getConstName();
		if (Tab.currentScope.findSymbol(constName) != null) {
			report_error("Ime " + constName + " je vec deklarisano!", cnstDecl);
			return;
		}
		if (currentType.getKind() != constantType.getKind()) {
			report_error("Vrednost konstante " + constName + " nije kompatibilna deklarisanom tipu konstante!",
					cnstDecl);
			return;
		}
		Obj con = Tab.insert(Obj.Con, constName, currentType);
		con.setAdr(constantValue);
		constantValue = 0;
	}

	// coki, radi za VarBrackets <3

	public void visit(VDeclArr declArr) {
		String varName = declArr.getVarName();
		if (Tab.currentScope.findSymbol(varName) != null) {
			report_error("Ime " + varName + " je vec deklarisano!", declArr);
			return;
		}
		if (currentMethod != null && currentMethod != Tab.noObj && currentMethod.getName().equals(varName)) {
			report_error("Varijabla ne sme da se zove kao funkcija u cijem je dosegu deklarisana!", declArr);
			return;
		}

		Tab.insert(Obj.Var, declArr.getVarName(), new Struct(Struct.Array, currentType));

	}

	public void visit(VDeclNoArr declNoArr) {
		String varName = declNoArr.getVarName();
		if (Tab.currentScope.findSymbol(varName) != null) {
			report_error("Ime " + varName + " je vec deklarisano!", declNoArr);
			return;
		}
		if (currentMethod != null && currentMethod != Tab.noObj && currentMethod.getName().equals(varName)) {
			report_error("Varijabla ne sme da se zove kao funkcija u cijem je dosegu deklarisana!", declNoArr);
			return;
		}
		Tab.insert(Obj.Var, varName, currentType);
	}

	public void visit(MethodTypeNameVoid typeNameVoid) {
		String methName = typeNameVoid.getMethName();
		if (Tab.currentScope.findSymbol(methName) != null) {
			report_error("Ime " + methName + " je vec deklarisano!", typeNameVoid);
			return;
		}
		isVoid = true;
		currentMethod = Tab.insert(Obj.Meth, methName, Tab.noType);
		if ("main".equals(methName)) {
			mainExists = true;
		}
		typeNameVoid.obj = currentMethod;
		currentMethod.setLevel(0);
		Tab.openScope();

	}

	public void visit(MethodTypeNameNoVoid typeNameNoVoid) {
		String methName = typeNameNoVoid.getMethName();
		if (Tab.currentScope.findSymbol(methName) != null) {
			report_error("Ime " + methName + " je vec deklarisano!", typeNameNoVoid);
			return;
		}
		if ("main".equals(methName)) {
			report_error("Povratna vrednost funkcije main mora biti tipa void!", typeNameNoVoid);
			return;
		}
		// Struct methType = typeNameNoVoid.getType().struct;
		// isVoid = (methType == Tab.noType);
		currentMethod = Tab.insert(Obj.Meth, methName, currentType);
		typeNameNoVoid.obj = currentMethod;
		currentMethod.setLevel(0);
		Tab.openScope();
	}

	public void visit(MethodDecl methodDecl) {

		if (!returnExists) {
			report_error("Funkcija " + currentMethod.getName() + " mora da ima return iskaz!", null);
			return;
		}

		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		returnExists = false;
		currentMethod = Tab.noObj;

	}

	public void visit(StatementReturn stmtRet) {
		if (currentMethod == Tab.noObj) {
			report_error("Iskaz return mora se nalaziti unutar funkcije!", stmtRet);
			return;
		}
		returnExists = true;
		if (!isVoid) {
			report_error("Metod " + currentMethod.getName() + " mora imati povratnu vrednost!", stmtRet);
			return;
		}

		// da li ovde?
		isVoid = false;

	}

	public void visit(StatementReturnExpr stmtRetExpr) {
		if (currentMethod == Tab.noObj) {
			report_error("Iskaz return mora se nalaziti unutar funkcije!", stmtRetExpr);
			return;
		}
		returnExists = true;
		if (isVoid) {
			report_error("Metod " + currentMethod.getName() + " tipa void nema povratnu vrednost!", stmtRetExpr);
			return;
		}
		// deo koji proverava tip povratne vrednosti

		else {
			int exprKind = stmtRetExpr.getExpr().struct.getKind();
			int methKind = currentMethod.getType().getKind();

			if (exprKind != methKind) {
				report_error("Nekompatibilni tipovi u return iskazu! ", stmtRetExpr);
				return;
			}
		}

	}

	public void visit(FactorNumConst factorNumConst) {
		factorNumConst.struct = Tab.intType;
	}

	public void visit(FactorBoolConst factorBoolConst) {
		factorBoolConst.struct = Tab.find("bool").getType();
	}

	public void visit(FactorCharConst factorCharConst) {
		factorCharConst.struct = Tab.charType;
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(FactorNew factorNew) {
		factorNew.struct = new Struct(Struct.Array, factorNew.getType().struct);
	}

	public void visit(FactorNewExprExists factorExpr) {
		if (factorExpr.getExpr().struct.getKind() != Tab.intType.getKind()) {
			report_error("Broj elemenata niza mora biti tipa int! ", factorExpr);
			factorExpr.struct = Tab.noType;
			return;
		}
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(Term term) {
		term.struct = term.getMulopFactorList().struct;
	}

	public void visit(MulopFactorListRecursive mulopFactorList) {
		Struct factorType = mulopFactorList.getFactor().struct;
		Struct factorListType = mulopFactorList.getMulopFactorList().struct;
		int factorKind = factorType.getKind();
		int factorListKind = factorListType.getKind();

		if (factorKind == Struct.Array && factorListKind == Struct.Array
				&& factorType.getElemType().getKind() == factorListType.getElemType().getKind()
				&& factorType.getElemType().getKind() == Struct.Int) {
			mulopFactorList.struct = factorType.getElemType();
			return;
		}

		if (factorKind == Struct.Array && factorListKind == factorType.getElemType().getKind()
				&& factorType.getElemType().getKind() == Struct.Int) {
			mulopFactorList.struct = factorType;
			return;
		}

		if (factorListKind == Struct.Array && factorKind == factorListType.getElemType().getKind()
				&& factorListType.getElemType().getKind() == Struct.Int) {
			mulopFactorList.struct = factorListType;
			return;
		}

		if (factorListKind != factorKind || factorKind != Struct.Int) {
			mulopFactorList.struct = Tab.noType;
			return;
		}

		mulopFactorList.struct = factorType;
	}

	public void visit(MulopFactorListOneElem mulopFactorElem) {
		mulopFactorElem.struct = mulopFactorElem.getFactor().struct;
	}

	public void visit(AddOpTermListOne a) {
		a.struct = a.getTerm().struct;
	}

	public void visit(AddOpTermListMore tl) {
		Struct type = tl.getTerm().struct;
		Struct listType = tl.getAddOpTermList().struct;

		int kind = type.getKind();
		int listKind = listType.getKind();

		if (kind == Struct.Array && listKind == Struct.Array
				&& type.getElemType().getKind() == listType.getElemType().getKind()
				&& type.getElemType().getKind() == Struct.Int) {
			tl.struct = type.getElemType();
			return;
		}

		if (kind == Struct.Array && listKind == type.getElemType().getKind()
				&& type.getElemType().getKind() == Struct.Int) {
			tl.struct = type;
			return;
		}

		if (listKind == Struct.Array && kind == listType.getElemType().getKind()
				&& listType.getElemType().getKind() == Struct.Int) {
			tl.struct = listType;
			return;
		}

		if (listKind != kind || kind != Struct.Int) {
			tl.struct = Tab.noType;
			return;
		}

		tl.struct = type;
	}

	public void visit(ExprWithMinus exprMinus) {
		exprMinus.struct = exprMinus.getAddOpTermList().struct;
	}

	public void visit(ExprNoMinus expr) {
		expr.struct = expr.getAddOpTermList().struct;
	}

	public void visit(StatementPrintNumConst statementPrint) {

		if (currentMethod == Tab.noObj) {
			report_error("Iskaz return mora se nalaziti unutar funkcije!", statementPrint);
			return;
		}

		int exprKind = statementPrint.getExpr().struct.getKind();
		if (exprKind != Struct.Int && exprKind != Struct.Char && exprKind != Struct.Bool) {
			report_error("Tip izraza u naredbi print mora biti int, char ili bool!", statementPrint);
		}
	}

	public void visit(StatementPrintNoConst statementPrint) {

		if (currentMethod == Tab.noObj) {
			report_error("Iskaz return mora se nalaziti unutar funkcije!", statementPrint);
			return;
		}
		int exprKind = statementPrint.getExpr().struct.getKind();
		if (exprKind != Struct.Int && exprKind != Struct.Char && exprKind != Struct.Bool) {
			report_error("Tip izraza u naredbi print mora biti int, char ili bool!", statementPrint);
		}
	}

	public void visit(DesignatorScalar ds) {
		String name = ds.getName();

		Obj obj = Tab.find(name);
		if (obj == Tab.noObj) {
			report_error("Ime " + name + " nije deklarisano!", ds);
			ds.obj = Tab.noObj;
			return;
		}

		ds.obj = obj;
		String msg = "";
		// U konacnoj verziji sine komentarisi, znaci nema zajebancije
		if (obj.getFpPos() != 0 && obj.getFpPos() != Obj.NO_VALUE) {
			msg = "Pristup formalnom argumentu ";
		} else if (obj.getKind() == Obj.Var && obj.getLevel() == 0) {
			msg = "Pristup globalnoj varijabli ";
		} else if (obj.getKind() == Obj.Var && obj.getLevel() != 0) {
			msg = "Pristup lokalnoj varijabli ";
		} else if (obj.getKind() == Obj.Con) {
			msg = "Pristup simbolickoj konstanti ";
		} else {
			return;
		}

		// report_symbol_info(msg + name + " na liniji " + ds.getLine(), obj);
	}

	public void visit(DesignatorArray da) {
		String arrayName = da.getArrayIdent().getArrayName();

		Obj obj = da.getArrayIdent().obj;

		if (obj.getKind() != Obj.Var || obj.getType().getKind() != Struct.Array) {
			report_error("Ime " + arrayName + " nije niz!", da);
			da.obj = Tab.noObj;
			return;
		}
		if (da.getExpr().struct.getKind() != Struct.Int) {
			report_error("Tip izraza u zagradama [] mora biti int!", da);
			da.obj = Tab.noObj;
			return;
		}

		da.obj = new Obj(Obj.Elem, "", obj.getType().getElemType());

		// report_symbol_info("Pristup elementu niza " + arrayName + " na liniji
		// " +
		// da.getLine(), obj);
	}

	public void visit(ArrayIdent arrayIdent) {
		String arrayName = arrayIdent.getArrayName();
		arrayIdent.obj = Tab.find(arrayName);
		if (arrayIdent.obj == Tab.noObj) {
			report_error("Ime " + arrayName + " nije deklarisano!", arrayIdent);
		}
	}

	public void visit(StatementRead statementRead) {
		int kind = statementRead.getDesignator().obj.getKind();
		int typeKind = statementRead.getDesignator().obj.getType().getKind();

		if ((kind != Obj.Var && kind != Obj.Elem) || (typeKind == Struct.Array)) {
			report_error("Ime " + statementRead.getDesignator().obj.getName()
					+ " mora da oznacava skalarnu promenljivu ili element niza!", statementRead);
			return;
		}

		if (typeKind != Struct.Int && typeKind != Struct.Char && typeKind != Struct.Bool) {
			report_error("Ime " + statementRead.getDesignator().obj.getName() + " mora biti tipa int, char ili bool!",
					statementRead);
			return;
		}
	}

	public void visit(DesginatorStmtMinusMinus dmm) {

		int kind = dmm.getDesignator().obj.getKind();
		if ((kind != Obj.Var && kind != Obj.Elem) || (dmm.getDesignator().obj.getType().getKind() == Struct.Array)) {
			report_error("Ime " + dmm.getDesignator().obj.getName()
					+ " mora da oznacava skalarnu promenljivu ili element niza!", dmm);
			return;
		}
		if (dmm.getDesignator().obj.getType().getKind() != Struct.Int) {
			report_error("Ime " + dmm.getDesignator().obj.getName() + " mora biti tipa int!", dmm);
			return;
		}

	}

	public void visit(DesginatorStmtPlusPlus dpp) {

		int kind = dpp.getDesignator().obj.getKind();
		if ((kind != Obj.Var && kind != Obj.Elem) || (dpp.getDesignator().obj.getType().getKind() == Struct.Array)) {
			report_error("Ime " + dpp.getDesignator().obj.getName()
					+ " mora da oznacava skalarnu promenljivu ili element niza!", dpp);
			return;
		}
		if (dpp.getDesignator().obj.getType().getKind() != Struct.Int) {
			report_error("Ime " + dpp.getDesignator().obj.getName() + " mora biti tipa int!", dpp);
			return;
		}

	}

	public void visit(DesginatorStmtFuncCall dfc) {
		String funcName = dfc.getDesignator().obj.getName();

		if (dfc.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Ime " + funcName + " mora da oznacava globalnu funkciju!", dfc);
			return;
		}
		
	
	}

	public void visit(DesginatorStmtAssign assignment) {
		int exprKind = assignment.getExpr().struct.getKind();
		int designatorKind = assignment.getDesignator().obj.getType().getKind();

		if (exprKind != designatorKind && designatorKind != Struct.Array) {
			report_error("Nekompatibilni tipovi!", assignment);
		} else if (designatorKind == Struct.Array && exprKind == Struct.Array) {
			if (assignment.getExpr().struct.getElemType().getKind() == assignment.getDesignator().obj.getType()
					.getElemType().getKind()) {
				return;
			} else {
				report_error("Nekompatibilni tipovi!", assignment);
			}
		} else if (designatorKind == Struct.Array && assignment.getExpr().struct == Tab.nullType) {
			return;
		} else if (designatorKind == Struct.Array) {
			report_error("Nekompatibilni tipovi!", assignment);
			return;
		}
	}

	public void visit(FactorDesignator factorDesignator) {
		factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
	}

	public void visit(FactorFuncCallNoParams funcCallNoParams) {
		String funcName = funcCallNoParams.getDesignator().obj.getName();

		if (funcCallNoParams.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Ime " + funcName + " mora da oznacava globalnu funkciju!", funcCallNoParams);
			funcCallNoParams.struct = Tab.noType;
			return;
		}

		funcCallNoParams.struct = funcCallNoParams.getDesignator().obj.getType();

	}

}
