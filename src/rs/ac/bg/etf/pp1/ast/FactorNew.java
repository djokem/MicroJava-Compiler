// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public class FactorNew extends Factor {

    private Type Type;
    private FactorNewExpr FactorNewExpr;

    public FactorNew (Type Type, FactorNewExpr FactorNewExpr) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.FactorNewExpr=FactorNewExpr;
        if(FactorNewExpr!=null) FactorNewExpr.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public FactorNewExpr getFactorNewExpr() {
        return FactorNewExpr;
    }

    public void setFactorNewExpr(FactorNewExpr FactorNewExpr) {
        this.FactorNewExpr=FactorNewExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(FactorNewExpr!=null) FactorNewExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(FactorNewExpr!=null) FactorNewExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(FactorNewExpr!=null) FactorNewExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNew(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorNewExpr!=null)
            buffer.append(FactorNewExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNew]");
        return buffer.toString();
    }
}
