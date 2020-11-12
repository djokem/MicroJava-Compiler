package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.AddOpTermListMore;
import rs.ac.bg.etf.pp1.ast.Addop;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.ArrayIdent;
import rs.ac.bg.etf.pp1.ast.DesginatorStmtAssign;
import rs.ac.bg.etf.pp1.ast.DesginatorStmtFuncCall;
import rs.ac.bg.etf.pp1.ast.DesginatorStmtMinusMinus;
import rs.ac.bg.etf.pp1.ast.DesginatorStmtPlusPlus;
import rs.ac.bg.etf.pp1.ast.FactorBoolConst;
import rs.ac.bg.etf.pp1.ast.FactorCharConst;
import rs.ac.bg.etf.pp1.ast.FactorDesignator;
import rs.ac.bg.etf.pp1.ast.FactorExpr;
import rs.ac.bg.etf.pp1.ast.FactorFuncCallNoParams;
import rs.ac.bg.etf.pp1.ast.FactorNew;
import rs.ac.bg.etf.pp1.ast.FactorNumConst;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeNameNoVoid;
import rs.ac.bg.etf.pp1.ast.MethodTypeNameVoid;
import rs.ac.bg.etf.pp1.ast.MulopFactorList;
import rs.ac.bg.etf.pp1.ast.MulopFactorListRecursive;
import rs.ac.bg.etf.pp1.ast.Mulop;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopTimes;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.StatementPrintNoConst;
import rs.ac.bg.etf.pp1.ast.StatementPrintNumConst;
import rs.ac.bg.etf.pp1.ast.StatementRead;
import rs.ac.bg.etf.pp1.ast.StatementReturn;
import rs.ac.bg.etf.pp1.ast.StatementReturnExpr;
import rs.ac.bg.etf.pp1.ast.Term;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	public void visit(FactorNumConst factorNumConst) {
		Obj obj = new Obj(Obj.Con, "", new Struct(Struct.Int));
		obj.setAdr(factorNumConst.getNum());
		Code.load(obj);
	}

	public void visit(FactorCharConst factorCharConst) {
		Obj obj = new Obj(Obj.Con, "", new Struct(Struct.Char));
		obj.setAdr(factorCharConst.getC());
		Code.load(obj);
	}

	public void visit(FactorBoolConst factorBoolConst) {
		Obj obj = new Obj(Obj.Con, "", new Struct(Struct.Bool));
		obj.setAdr(factorBoolConst.getB().booleanValue() ? 1 : 0);
		Code.load(obj);
	}

	public void visit(FactorExpr expr) {
		// Videcemo...
	}

	public void visit(FactorNew factorNew) {
		Code.put(Code.newarray);
		if (factorNew.struct.getKind() == Struct.Char) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	public void visit(FactorDesignator factorDesignator) {
		Code.load(factorDesignator.getDesignator().obj);
	}

	public void visit(FactorFuncCallNoParams factorFuncCallNoParams) {
		Obj functionObj = factorFuncCallNoParams.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	public void visit(Program p) {
		int size = 0;
		for (Obj obj : p.getProgName().obj.getLocalSymbols()) {
			if (obj.getKind() == Obj.Var) {
				size++;
			}
		}
		Code.dataSize = size;
	}

	public void visit(ProgName progName) {
		List<Obj> predefinedMethods = new ArrayList<>();

		predefinedMethods.add(Tab.lenObj);
		predefinedMethods.add(Tab.chrObj);
		predefinedMethods.add(Tab.ordObj);

		for (Obj obj : predefinedMethods) {
			obj.setAdr(Code.pc);
			Code.put(Code.enter);

			Code.put(obj.getLevel());
			Code.put(obj.getLocalSymbols().size());

			Code.put(Code.load_n);

			if ("len".equals(obj.getName())) {
				Code.put(Code.arraylength);
			}

			Code.put(Code.exit);
			Code.put(Code.return_);
		}
	}

	public void visit(MethodTypeNameVoid methId) {
		if ("main".equalsIgnoreCase(methId.getMethName())) {
			Code.mainPc = Code.pc;
		}

		methId.obj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(methId.obj.getLevel());
		Code.put(methId.obj.getLocalSymbols().size());
	}

	public void visit(MethodTypeNameNoVoid methId) {
		methId.obj.setAdr(Code.pc);

		Code.put(Code.enter);
		Code.put(methId.obj.getLevel());
		Code.put(methId.obj.getLocalSymbols().size());
	}

	public void visit(MethodDecl methodDecl) {
		Struct method_type = methodDecl.getMethodTypeName().obj.getType();
		if (method_type == Tab.noType) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		} else {
			Code.put(Code.trap);
			Code.put(1);
		}
	}

	public void visit(StatementRead statementRead) {
		Obj d = statementRead.getDesignator().obj;
		if (d.getType().getKind() == Struct.Char) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}
		Code.store(d);
	}

	public void visit(StatementPrintNoConst stPrint) {
		if (stPrint.getExpr().struct.getKind() == Struct.Char) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		} else {
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}

	public void visit(StatementPrintNumConst stPrint) {
		Code.loadConst(stPrint.getN1());

		if (stPrint.getExpr().struct.getKind() == Struct.Char) {
			Code.put(Code.bprint);
		} else {
			Code.put(Code.print);
		}
	}

	public void visit(DesginatorStmtAssign d) {
		Code.store(d.getDesignator().obj);
	}

	public void visit(DesginatorStmtFuncCall funcCall) {
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if (functionObj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}

	public void visit(DesginatorStmtPlusPlus pp) {
		Obj obj = pp.getDesignator().obj;

		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}

	public void visit(DesginatorStmtMinusMinus mm) {
		Obj obj = mm.getDesignator().obj;

		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(obj);
	}

	public void visit(AddOpTermListMore addopList) {
		Addop addop = addopList.getAddop();

		if (addopList.getTerm().struct.getKind() == Struct.Array) {
			arrayAdditonAsmCode(addopList);
		} else {
			if (addop instanceof AddopPlus) {
				Code.put(Code.add);
			} else {
				Code.put(Code.sub);
			}
		}
	}

	public void visit(ArrayIdent arrayIdent) {
		Code.load(arrayIdent.obj);
	}

	private void arrayAdditonAsmCode(AddOpTermListMore addopList) {
		Code.put(Code.enter);
		Code.put(2);
		Code.put(5);

		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		Code.put(Code.store_2);

		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.store_3);

		Code.put(Code.load_2);
		Code.put(Code.load_3);
		Code.putFalseJump(Code.ne, 0);
		int adr = Code.pc - 2;
		Code.put(Code.trap);
		Code.put(2);
		Code.fixup(adr);

		Code.put(Code.load_2);
		Code.put(Code.newarray);
		if (addopList.struct.getKind() == Struct.Char) {
			Code.put(0);
		} else {
			Code.put(1);
		}
		Code.put(Code.store_3);

		// da brojim od n-1 do 0
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);

		// adresa pocetka petlje
		int forBegin = Code.pc;

		// uslov petlje
		Code.put(Code.load_2);
		Code.loadConst(0);
		Code.putFalseJump(Code.ge, 0);
		int jumpPatchAdr = Code.pc - 2;

		// petlja
		// ucit elementa : adr niza, index, aload

		Code.put(Code.load_3); // adresa niza u koji se stavlja vr
		Code.put(Code.load_2); // na koju poziciju

		Code.put(Code.load_n + 0); // adresa niza 1
		Code.put(Code.load_2); // index
		if (addopList.struct.getKind() == Struct.Char) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}

		Code.put(Code.load_1); // adresa niza 2
		Code.put(Code.load_2); // index
		if (addopList.struct.getKind() == Struct.Char) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}

		Code.put(Code.add);
		if (addopList.struct.getKind() == Struct.Char) {
			Code.put(Code.bastore);
		} else {
			Code.put(Code.astore);
		}

		// kraj, dekrem brojac pa jmp na pocetak petlje
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);
		Code.putJump(forBegin);

		// ako je false ovde se skace
		Code.fixup(jumpPatchAdr);

		// kod posle for petlje
		Code.put(Code.load_3);

		Code.put(Code.exit);

	}

	public void visit(StatementReturn stReturn) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StatementReturnExpr stReturn) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MulopFactorListRecursive mulopList) {
		Mulop mulop = mulopList.getMulop();
		int factorKind = mulopList.getFactor().struct.getKind();
		int factorListKind = mulopList.getMulopFactorList().struct.getKind();
		if (factorKind == Struct.Array && factorListKind == Struct.Array) {
			arrayMultiplicationAsmCode(mulopList);
		} else if (factorListKind == Struct.Array || factorKind == Struct.Array) {
			scalarMulArrayAsmCode(mulopList);
		} else {
			if (mulop instanceof MulopTimes) {
				Code.put(Code.mul);
			} else if (mulop instanceof MulopDiv) {
				Code.put(Code.div);
			} else {
				Code.put(Code.rem);
			}
		}
	}

	private void arrayMultiplicationAsmCode(MulopFactorListRecursive mulopList) {
		Code.put(Code.enter);
		Code.put(2);
		Code.put(5);

		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		Code.put(Code.store_2);

		Code.put(Code.load_1);
		Code.put(Code.arraylength);
		Code.put(Code.store_3);

		Code.put(Code.load_2);
		Code.put(Code.load_3);
		Code.putFalseJump(Code.ne, 0);
		int adr = Code.pc - 2;
		Code.put(Code.trap);
		Code.put(2);
		Code.fixup(adr);

		Code.loadConst(0);
		Code.put(Code.store_3);

		// da brojim od n-1 do 0
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);

		// adresa pocetka petlje
		int forBegin = Code.pc;

		// uslov petlje
		Code.put(Code.load_2);
		Code.loadConst(0);
		Code.putFalseJump(Code.ge, 0);
		int jumpPatchAdr = Code.pc - 2;

		// petlja
		// ucit elementa : adr niza, index, aload

		Code.put(Code.load_n + 0); // adresa niza 1
		Code.put(Code.load_2); // index
		if (mulopList.struct.getKind() == Struct.Char) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}

		Code.put(Code.load_1); // adresa niza 2
		Code.put(Code.load_2); // index
		if (mulopList.struct.getKind() == Struct.Char) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}

		Code.put(Code.mul);
		Code.put(Code.load_3);
		Code.put(Code.add);
		Code.put(Code.store_3);

		// kraj, dekrem brojac pa jmp na pocetak petlje
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);
		Code.putJump(forBegin);

		// ako je false ovde se skace
		Code.fixup(jumpPatchAdr);

		// kod posle for petlje
		Code.put(Code.load_3);

		Code.put(Code.exit);
	}

	private void scalarMulArrayAsmCode(MulopFactorListRecursive mulopList) {
		int factorListKind = mulopList.getMulopFactorList().struct.getKind();
		boolean firstScalar = true;

		if (factorListKind == Struct.Array) {
			firstScalar = false;
		}

		Code.put(Code.enter);
		Code.put(2);
		Code.put(5);

		if (firstScalar) {
			Code.put(Code.load_1);
		} else {
			Code.put(Code.load_1);
		}
		Code.put(Code.arraylength);
		Code.put(Code.store_2);

		// prostor za novi niz
		Code.put(Code.load_2);
		Code.put(Code.newarray);
		if (mulopList.struct.getKind() == Struct.Char) {
			Code.put(0);
		} else {
			Code.put(1);
		}
		Code.put(Code.store_3);

		// da brojim od n-1 do 0
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);

		// adresa pocetka petlje
		int forBegin = Code.pc;

		// uslov petlje
		Code.put(Code.load_2);
		Code.loadConst(0);
		Code.putFalseJump(Code.ge, 0);
		int jumpPatchAdr = Code.pc - 2;

		// petlja
		// ucit elementa : adr niza, index, aload

		Code.put(Code.load_3); // adresa niza u koji se stavlja vr
		Code.put(Code.load_2); // na koju poziciju

		if (firstScalar) {
			Code.put(Code.load_1);
		} else {
			Code.put(Code.load_n + 0);
		}
		Code.put(Code.load_2); // index
		if (mulopList.struct.getKind() == Struct.Char) {
			Code.put(Code.baload);
		} else {
			Code.put(Code.aload);
		}

		if (firstScalar) {
			Code.put(Code.load_n + 0);
		} else {
			Code.put(Code.load_1);
		}
		Code.put(Code.mul);

		if (mulopList.struct.getKind() == Struct.Char) {
			Code.put(Code.bastore);
		} else {
			Code.put(Code.astore);
		}

		// kraj, dekrem brojac pa jmp na pocetak petlje
		Code.put(Code.load_2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);
		Code.putJump(forBegin);

		// ako je false ovde se skace
		Code.fixup(jumpPatchAdr);

		// kod posle for petlje
		Code.put(Code.load_3);

		Code.put(Code.exit);
	}

}
