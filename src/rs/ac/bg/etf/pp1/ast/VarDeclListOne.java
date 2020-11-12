// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListOne extends VarDeclList {

    private VDecl VDecl;

    public VarDeclListOne (VDecl VDecl) {
        this.VDecl=VDecl;
        if(VDecl!=null) VDecl.setParent(this);
    }

    public VDecl getVDecl() {
        return VDecl;
    }

    public void setVDecl(VDecl VDecl) {
        this.VDecl=VDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VDecl!=null) VDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VDecl!=null) VDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VDecl!=null) VDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListOne(\n");

        if(VDecl!=null)
            buffer.append(VDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListOne]");
        return buffer.toString();
    }
}
