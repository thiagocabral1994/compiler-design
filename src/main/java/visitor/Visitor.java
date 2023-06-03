package visitor;

import ast.*;

public abstract class Visitor {
  public abstract void visit(AdditionAExpression node);
  public abstract void visit(AndExpression node);
  public abstract void visit(ArrayLValue node);
  public abstract void visit(AssignmentCommand node);
  public abstract void visit(BooleanBasicType node);
  public abstract void visit(CallCommand node);
  public abstract void visit(CallPExpression node);
  public abstract void visit(CharSExpression node);
  public abstract void visit(CharacterBasicType node);
  public abstract void visit(CustomBasicType node);
  public abstract void visit(Data node);
  public abstract void visit(DivisionMExpression node);
  public abstract void visit(EqualityRExpression node);
  public abstract void visit(FalseSExpression node);
  public abstract void visit(FloatBasicType node);
  public abstract void visit(FloatSExpression node);
  public abstract void visit(Function node);
  public abstract void visit(IdentifierLValue node);
  public abstract void visit(IfCommand node);
  public abstract void visit(IfElseCommand node);
  public abstract void visit(InequalityRExpression node);
  public abstract void visit(IntegerBasicType node);
  public abstract void visit(IntegerSExpression node);
  public abstract void visit(IterateCommand node);
  public abstract void visit(LessRExpression node);
  public abstract void visit(ListCommand node);
  public abstract void visit(ModulusMExpression node);
  public abstract void visit(MultiplicationMExpression node);
  public abstract void visit(NegationSExpression node);
  public abstract void visit(NegativeSExpression node);
  public abstract void visit(NewPExpression node);
  public abstract void visit(NullSExpression node);
  public abstract void visit(ObjectLValue node);
  public abstract void visit(Parameter node);
  public abstract void visit(ParenthesisPExpression node);
  public abstract void visit(PrintCommand node);
  public abstract void visit(Program node);
  public abstract void visit(ReadCommand node);
  public abstract void visit(ReturnCommand node);
  public abstract void visit(SubtractionAExpression node);
  public abstract void visit(TrueSExpression node);
  public abstract void visit(Type node);
}
