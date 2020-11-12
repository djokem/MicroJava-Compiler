// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListMore extends ConstDeclList {

    private CnstDecl CnstDecl;
    private ConstDeclList ConstDeclList;

    public ConstDeclListMore (CnstDecl CnstDecl, ConstDeclList ConstDeclList) {
        this.CnstDecl=CnstDecl;
        if(CnstDecl!=null) CnstDecl.setParent(this);
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
    }

    public CnstDecl getCnstDecl() {
        return CnstDecl;
    }

    public void setCnstDecl(CnstDecl CnstDecl) {
        this.CnstDecl=CnstDecl;
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CnstDecl!=null) CnstDecl.accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CnstDecl!=null) CnstDecl.traverseTopDown(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CnstDecl!=null) CnstDecl.traverseBottomUp(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListMore(\n");

        if(CnstDecl!=null)
            buffer.append(CnstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListMore]");
        return buffer.toString();
    }
}
