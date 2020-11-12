// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public class ExprWithMinus extends Expr {

    private AddOpTermList AddOpTermList;

    public ExprWithMinus (AddOpTermList AddOpTermList) {
        this.AddOpTermList=AddOpTermList;
        if(AddOpTermList!=null) AddOpTermList.setParent(this);
    }

    public AddOpTermList getAddOpTermList() {
        return AddOpTermList;
    }

    public void setAddOpTermList(AddOpTermList AddOpTermList) {
        this.AddOpTermList=AddOpTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddOpTermList!=null) AddOpTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddOpTermList!=null) AddOpTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddOpTermList!=null) AddOpTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprWithMinus(\n");

        if(AddOpTermList!=null)
            buffer.append(AddOpTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprWithMinus]");
        return buffer.toString();
    }
}
