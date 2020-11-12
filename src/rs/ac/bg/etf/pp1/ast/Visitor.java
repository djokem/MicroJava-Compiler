// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(Factor Factor);
    public void visit(Mulop Mulop);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(ConstType ConstType);
    public void visit(Expr Expr);
    public void visit(Decl Decl);
    public void visit(AddOpTermList AddOpTermList);
    public void visit(VDecl VDecl);
    public void visit(VarDeclList VarDeclList);
    public void visit(FactorNewExpr FactorNewExpr);
    public void visit(VarBrackets VarBrackets);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(MulopFactorList MulopFactorList);
    public void visit(MethVarDecl MethVarDecl);
    public void visit(Addop Addop);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(DeclList DeclList);
    public void visit(Statement Statement);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(StatementList StatementList);
    public void visit(FactorNewExprNotExist FactorNewExprNotExist);
    public void visit(FactorNewExprExists FactorNewExprExists);
    public void visit(FactorFuncCallNoParams FactorFuncCallNoParams);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(FactorNew FactorNew);
    public void visit(FactorBoolConst FactorBoolConst);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorCharConst FactorCharConst);
    public void visit(FactorNumConst FactorNumConst);
    public void visit(MulopMod MulopMod);
    public void visit(MulopDiv MulopDiv);
    public void visit(MulopTimes MulopTimes);
    public void visit(MulopFactorListOneElem MulopFactorListOneElem);
    public void visit(MulopFactorListRecursive MulopFactorListRecursive);
    public void visit(Term Term);
    public void visit(AddopMinus AddopMinus);
    public void visit(AddopPlus AddopPlus);
    public void visit(AddOpTermListOne AddOpTermListOne);
    public void visit(AddOpTermListMore AddOpTermListMore);
    public void visit(ExprNoMinus ExprNoMinus);
    public void visit(ExprWithMinus ExprWithMinus);
    public void visit(ArrayIdent ArrayIdent);
    public void visit(DesignatorArray DesignatorArray);
    public void visit(DesignatorScalar DesignatorScalar);
    public void visit(DesginatorStmtMinusMinus DesginatorStmtMinusMinus);
    public void visit(DesginatorStmtPlusPlus DesginatorStmtPlusPlus);
    public void visit(DesginatorStmtFuncCall DesginatorStmtFuncCall);
    public void visit(DesginatorStmtAssign DesginatorStmtAssign);
    public void visit(StatementReturnExpr StatementReturnExpr);
    public void visit(StatementReturn StatementReturn);
    public void visit(StatementPrintNumConst StatementPrintNumConst);
    public void visit(StatementPrintNoConst StatementPrintNoConst);
    public void visit(StatementRead StatementRead);
    public void visit(StatementDesignator StatementDesignator);
    public void visit(StatementListEmpty StatementListEmpty);
    public void visit(StatementListNotEmpty StatementListNotEmpty);
    public void visit(MethVarDeclEmpty MethVarDeclEmpty);
    public void visit(MethVarDeclNoEmpty MethVarDeclNoEmpty);
    public void visit(MethodTypeNameNoVoid MethodTypeNameNoVoid);
    public void visit(MethodTypeNameVoid MethodTypeNameVoid);
    public void visit(MethodDecl MethodDecl);
    public void visit(MethodDeclListEmpty MethodDeclListEmpty);
    public void visit(MethodDeclListNotEmpty MethodDeclListNotEmpty);
    public void visit(VDeclArr VDeclArr);
    public void visit(VDeclNoArr VDeclNoArr);
    public void visit(VarDeclListOne VarDeclListOne);
    public void visit(VarDeclListMore VarDeclListMore);
    public void visit(VarDecl VarDecl);
    public void visit(Type Type);
    public void visit(ConstTypeBool ConstTypeBool);
    public void visit(ConstTypeChar ConstTypeChar);
    public void visit(ConstTypeNum ConstTypeNum);
    public void visit(CnstDecl CnstDecl);
    public void visit(ConstDeclListOne ConstDeclListOne);
    public void visit(ConstDeclListMore ConstDeclListMore);
    public void visit(ConstDecl ConstDecl);
    public void visit(DeclVar DeclVar);
    public void visit(DeclConst DeclConst);
    public void visit(DeclListEmpty DeclListEmpty);
    public void visit(DeclListNotEmpty DeclListNotEmpty);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
