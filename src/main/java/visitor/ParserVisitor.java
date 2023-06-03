package visitor;

import util.*;
import ast.*;

public class ParserVisitor extends Visitor {
  private ScopeTable scopes;
  private int level;

  public ParserVisitor() {
    this.scopes = new ScopeTable();
    this.level = scopes.getLevel();
  }

  public int getLevel() { return this.level; }

  @Override
  public void visit(AdditionAExpression node) {
    // TODO Auto-generated method stub
    System.out.println("AdditionAExpression");
  }

  @Override
  public void visit(AndExpression node) {
    // TODO Auto-generated method stub
    System.out.println("AndExpression");
  }

  @Override
  public void visit(ArrayLValue node) {
    // TODO Auto-generated method stub
    System.out.println("ArrayLValue");
  }

  @Override
  public void visit(ArrayType node) {
    // TODO Auto-generated method stub
    System.out.println("ArrayType");
  }

  @Override
  public void visit(AssignmentCommand node) {
    // TODO Auto-generated method stub
    System.out.println("AssignmentCommand");
  }

  @Override
  public void visit(BooleanBasicType node) {
    // TODO Auto-generated method stub
    System.out.println("BooleanBasicType");
  }

  @Override
  public void visit(CallCommand node) {
    // TODO Auto-generated method stub
    System.out.println("CallCommand");
  }

  @Override
  public void visit(CallPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("CallPExpression");
  }

  @Override
  public void visit(CharSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("CharSExpression");
  }

  @Override
  public void visit(CharacterBasicType node) {
    // TODO Auto-generated method stub
    System.out.println("CharacterBasicType");
  }

  @Override
  public void visit(CustomBasicType node) {
    // TODO Auto-generated method stub
    System.out.println("CustomBasicType");
  }

  @Override
  public void visit(Data node) {
    // TODO Auto-generated method stub
    System.out.println("Data");
  }

  @Override
  public void visit(DivisionMExpression node) {
    // TODO Auto-generated method stub
    System.out.println("DivisionMExpression");
  }

  @Override
  public void visit(EqualityRExpression node) {
    // TODO Auto-generated method stub
    System.out.println("EqualityRExpression");
  }

  @Override
  public void visit(FalseSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("FalseSExpression");
  }

  @Override
  public void visit(FloatBasicType node) {
    // TODO Auto-generated method stub
    System.out.println("FloatBasicType");
  }

  @Override
  public void visit(FloatSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("FloatSExpression");
  }

  @Override
  public void visit(Function node) {
    // TODO Auto-generated method stub
    System.out.println("Function");
  }

  @Override
  public void visit(IdentifierLValue node) {
    // TODO Auto-generated method stub
    System.out.println("IdentifierLValue");
  }

  @Override
  public void visit(IfCommand node) {
    // TODO Auto-generated method stub
    System.out.println("IfCommand");
  }

  @Override
  public void visit(IfElseCommand node) {
    // TODO Auto-generated method stub
    System.out.println("IfElseCommand");
  }

  @Override
  public void visit(InequalityRExpression node) {
    // TODO Auto-generated method stub
    System.out.println("InequalityRExpression");
  }

  @Override
  public void visit(IntegerBasicType node) {
    // TODO Auto-generated method stub
    System.out.println("IntegerBasicType");
  }

  @Override
  public void visit(IntegerSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("IntegerSExpression");
  }

  @Override
  public void visit(IterateCommand node) {
    // TODO Auto-generated method stub
    System.out.println("IterateCommand");
  }

  @Override
  public void visit(LessRExpression node) {
    // TODO Auto-generated method stub
    System.out.println("LessRExpression");
  }

  @Override
  public void visit(ListCommand node) {
    // TODO Auto-generated method stub
    System.out.println("ListCommand");
  }

  @Override
  public void visit(ModulusMExpression node) {
    // TODO Auto-generated method stub
    System.out.println("ModulusMExpression");
  }

  @Override
  public void visit(MultiplicationMExpression node) {
    // TODO Auto-generated method stub
    System.out.println("MultiplicationMExpression");
  }

  @Override
  public void visit(NegationSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NegationSExpression");
  }

  @Override
  public void visit(NegativeSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NegativeSExpression");
  }

  @Override
  public void visit(NewPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NewPExpression");
  }

  @Override
  public void visit(NullSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NullSExpression");
  }

  @Override
  public void visit(ObjectLValue node) {
    // TODO Auto-generated method stub
    System.out.println("ObjectLValue");
  }

  @Override
  public void visit(Parameter node) {
    // TODO Auto-generated method stub
    System.out.println("Parameter");
  }

  @Override
  public void visit(ParenthesisPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("ParenthesisPExpression");
  }

  @Override
  public void visit(PrintCommand node) {
    // TODO Auto-generated method stub
    System.out.println("PrintCommand");
  }

  @Override
  public void visit(Program node) {
    // TODO Auto-generated method stub
    System.out.println("Program");
  }

  @Override
  public void visit(ReadCommand node) {
    // TODO Auto-generated method stub
    System.out.println("ReadCommand");
  }

  @Override
  public void visit(ReturnCommand node) {
    // TODO Auto-generated method stub
    System.out.println("ReturnCommand");
  }

  @Override
  public void visit(SubtractionAExpression node) {
    // TODO Auto-generated method stub
    System.out.println("SubtractionAExpression");
  }

  @Override
  public void visit(TrueSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("TrueSExpression");
  }

  @Override
  public void visit(Type node) {
    // TODO Auto-generated method stub
    System.out.println("Type");
  }
}
