// generated with ast extension for cup
// version 0.8
// 7/0/2020 23:2:4


package rs.ac.bg.etf.pp1.ast;

public class AddOpTermListMore extends AddOpTermList {

    private AddOpTermList AddOpTermList;
    private Addop Addop;
    private Term Term;

    public AddOpTermListMore (AddOpTermList AddOpTermList, Addop Addop, Term Term) {
        this.AddOpTermList=AddOpTermList;
        if(AddOpTermList!=null) AddOpTermList.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public AddOpTermList getAddOpTermList() {
        return AddOpTermList;
    }

    public void setAddOpTermList(AddOpTermList AddOpTermList) {
        this.AddOpTermList=AddOpTermList;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddOpTermList!=null) AddOpTermList.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddOpTermList!=null) AddOpTermList.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddOpTermList!=null) AddOpTermList.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddOpTermListMore(\n");

        if(AddOpTermList!=null)
            buffer.append(AddOpTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddOpTermListMore]");
        return buffer.toString();
    }
}
