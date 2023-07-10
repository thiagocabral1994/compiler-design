package visitor;

import ast.*;
import util.Pair;
import util.semantic.*;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

public class JavaVisitor extends Visitor {
	private static final String PREFIX = "_";
	private STGroup groupTemplate;
	private ST programTemplate, typeTemplate, commandTemplate;
	private SemanticType sType;
	private List<ST> dataTemplates, functionTemplates, paramTemplates, commandTemplates;
	private Stack<ST> expressionTemplateStack;
	private int iteratorCount = 0;
	private int returnCount = 0;
	private int scannerCount = 0;
	private int lastIndex;
	private LocalEnv<Pair<SemanticType, Integer>> localEnv;

	private String fileName;

	private Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> env;
	private Map<String, Map<String, SemanticType>> dataMap;
	private Stack<SemanticType> lvalueTypeStack;

	public JavaVisitor(String fileName, Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> env, Map<String, Map<String, SemanticType>> map) {
		this.groupTemplate = new STGroupFile("./template/java.stg");
		this.fileName = fileName;
		this.env = env;
		this.expressionTemplateStack = new Stack<ST>();
		this.dataMap = map;
		this.lvalueTypeStack = new Stack<SemanticType>();
	}

	public String getProgramTemplate() throws IOException {
		if (this.programTemplate == null) {
			throw new IOException("Programa não possui template");
		}
		return this.programTemplate.render();
	}

	public void visit(Program program) {
		this.programTemplate = this.groupTemplate.getInstanceOf("program");
		this.programTemplate.add("name", fileName);
		this.programTemplate.add("prefix", PREFIX);

		this.dataTemplates = new ArrayList<ST>();
		for (Data data : program.getDatas()) {
			data.accept(this);
		}
		this.programTemplate.add("datas", this.dataTemplates);

		this.functionTemplates = new ArrayList<ST>();
		for (Function function : program.getFunctions()) {
			function.accept(this);
		}
		this.programTemplate.add("functions", this.functionTemplates);
	}

	@Override
	public void visit(AdditionAExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("add_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(AndExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("and_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(ArrayLValue lvalueArray) {
		ST arrayLValueTemplate = groupTemplate.getInstanceOf("array_lvalue");
		lvalueArray.getLValue().accept(this);

		STypeArray parentType = (STypeArray) this.lvalueTypeStack.pop();
		arrayLValueTemplate.add("lvalue", this.expressionTemplateStack.pop());
		lvalueArray.getExpression().accept(this);
		arrayLValueTemplate.add("exp", this.expressionTemplateStack.pop());

		this.expressionTemplateStack.push(arrayLValueTemplate);
		this.lvalueTypeStack.push(parentType.getType());
	}

	@Override
	public void visit(ArrayType arrayType) {
		arrayType.getType().accept(this);
		this.sType = STypeArray.create(this.sType);
		processSemanticType(this.sType);
	}

	@Override
	public void visit(AssignmentCommand command) {
		ST localCommandTemplate = this.groupTemplate.getInstanceOf("assignment");
		command.getLValueContext().accept(this);
		localCommandTemplate.add("lvalue", this.expressionTemplateStack.pop());
		command.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		this.commandTemplate = localCommandTemplate;
	}

	@Override
	public void visit(BooleanBasicType node) {
		this.sType = STypeBool.create();
		processSemanticType(STypeBool.create());
	}

	@Override
	public void visit(CallCommand cmd) {
		ST cmdTemplate;
		List<ST> localParamTemplates = new ArrayList<>();
		List<SemanticType> paramTypes = new ArrayList<>();
		for (Expression arg : cmd.getExpressions()) {
			arg.accept(this);
			paramTypes.add(arg.getSemanticType());
			localParamTemplates.add(this.expressionTemplateStack.pop());
		}

		if (cmd.getReturnLValueContexts().size() > 0) {
			cmdTemplate = this.groupTemplate.getInstanceOf("call_return");
			STypeFunctionKey functionKey = STypeFunctionKey.create(cmd.getId(), paramTypes);
			STypeFunction sTyFunc = this.env.get(functionKey).getFunctionType();
			List<ST> callAssigments = new ArrayList<ST>();
			for (int i = 0; i < cmd.getReturnLValueContexts().size(); i++) {
				ST callAssigment = this.groupTemplate.getInstanceOf("call_return_assignment");
				cmd.getReturnLValueContexts().get(i).accept(this);
				callAssigment.add("lvalue", this.expressionTemplateStack.pop());
				callAssigment.add("rv_count", this.returnCount);
				callAssigment.add("index", i);
				this.processSemanticType(sTyFunc.getReturnTypes().get(i));
				callAssigment.add("type", this.typeTemplate);
				callAssigments.add(callAssigment);
			}
			cmdTemplate.add("rv_count", this.returnCount);
			cmdTemplate.add("call_return_assignments", callAssigments);
			this.returnCount++;
		} else {
			cmdTemplate = this.groupTemplate.getInstanceOf("call_void");
		}

		cmdTemplate.add("prefix", PREFIX);
		cmdTemplate.add("name", cmd.getId());
		cmdTemplate.add("args", localParamTemplates);

		this.commandTemplate = cmdTemplate;
	}

	@Override
	public void visit(CallPExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("call_exp");
		expressionTemplate.add("name", exp.getID());
		expressionTemplate.add("prefix", PREFIX);

		List<ST> paramTemplates = new ArrayList<ST>();
		List<SemanticType> paramTypes = new ArrayList<>();
		for (Expression arg : exp.getParamExpressions()) {
			arg.accept(this);
			paramTypes.add(arg.getSemanticType());
			paramTemplates.add(this.expressionTemplateStack.pop());
		}
		expressionTemplate.add("args", paramTemplates);

		exp.getBracketExpression().accept(this);
		expressionTemplate.add("exp", this.expressionTemplateStack.pop());

		STypeFunctionKey functionKey = STypeFunctionKey.create(exp.getID(), paramTypes);
		STypeFunction sTyFunc = this.env.get(functionKey).getFunctionType();
		this.processSemanticType(sTyFunc.getReturnTypes().get(this.lastIndex));
		expressionTemplate.add("type", this.typeTemplate);

		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(CharSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("char_exp");
		String value = String.valueOf(exp.getValue());
		if (value.equals("\n"))
			value = "\\n";
		else if (value.equals("\t"))
			value = "\\t";
		else if (value.equals("\b"))
			value = "\\b";
		else if (value.equals("\r"))
			value = "\\r";
		expressionTemplate.add("value", value);
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(CharacterBasicType node) {
		this.sType = STypeChar.create();
		processSemanticType(STypeChar.create());
	}

	@Override
	public void visit(CustomBasicType type) {
		this.sType = STypeCustom.create(type.getTypeName());
		processSemanticType(this.sType);
	}

	@Override
	public void visit(Data data) {
		ST dataTemplate = this.groupTemplate.getInstanceOf("data");
		dataTemplate.add("name", data.getId());
		dataTemplate.add("prefix", PREFIX);

		this.paramTemplates = new ArrayList<ST>();
		for (Parameter param : data.getDeclarations()) {
			param.accept(this);
		}
		dataTemplate.add("declarations", this.paramTemplates);

		this.dataTemplates.add(dataTemplate);
	}

	@Override
	public void visit(DivisionMExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("div_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(EqualityRExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("equals_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(FalseSExpression node) {
		ST falseTemplate = groupTemplate.getInstanceOf("false_exp");
		this.expressionTemplateStack.push(falseTemplate);
	}

	@Override
	public void visit(FloatBasicType node) {
		this.sType = STypeFloat.create();
		processSemanticType(STypeFloat.create());
	}

	@Override
	public void visit(FloatSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("float_exp");
		expressionTemplate.add("value", exp.getValue() + "f");
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(Function function) {
		ST functionTemplate = this.groupTemplate.getInstanceOf("function");
		functionTemplate.add("name", function.getId());
		functionTemplate.add("prefix", PREFIX);

		STypeFunctionKey functionKey = STypeFunctionKey.create(function.getId(), function.getFunctionType().getParams());
		this.localEnv = this.env.get(functionKey);

		List<Type> returnTypes = function.getReturnTypes();
		functionTemplate.add("type", returnTypes.size() > 0 ? "Object[]" : "void");

		this.paramTemplates = new ArrayList<ST>();
		Set<String> keys = this.localEnv.getKeys();
		for (Parameter param : function.getParameters()) {
			keys.remove(param.getId());
			param.accept(this);
		}
		functionTemplate.add("params", this.paramTemplates);

		this.paramTemplates = new ArrayList<ST>();
		for (String key : keys) {
			ST paramTemplate = groupTemplate.getInstanceOf("param");
			paramTemplate.add("name", key);
			paramTemplate.add("prefix", PREFIX);
			SemanticType sType = this.localEnv.get(key).getLeft();
			this.processSemanticType(sType);
			paramTemplate.add("type", this.typeTemplate);
			this.paramTemplates.add(paramTemplate);
		}
		functionTemplate.add("declarations", this.paramTemplates);

		this.commandTemplates = new ArrayList<ST>();
		for (Command command : function.getCommands()) {
			command.accept(this);
			this.commandTemplates.add(this.commandTemplate);
		}
		functionTemplate.add("commands", this.commandTemplates);

		this.functionTemplates.add(functionTemplate);
	}

	@Override
	public void visit(IdentifierLValue lvalue) {
		SemanticType lvalueType = this.localEnv.get(lvalue.getID()).getLeft();

		ST lvalueTemplate = groupTemplate.getInstanceOf("lvalue");
		lvalueTemplate.add("name", lvalue.getID());
		lvalueTemplate.add("prefix", PREFIX);
		this.expressionTemplateStack.push(lvalueTemplate);
		this.lvalueTypeStack.push(lvalueType);
	}

	@Override
	public void visit(IfCommand cmd) {
		ST localCommandTemplate = groupTemplate.getInstanceOf("if");
		cmd.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		cmd.getCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
	}

	@Override
	public void visit(IfElseCommand cmd) {
		ST localCommandTemplate = groupTemplate.getInstanceOf("if");
		cmd.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		cmd.getIfCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		cmd.getElseCommand().accept(this);
		localCommandTemplate.add("els", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
	}

	@Override
	public void visit(InequalityRExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("not_equals_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(IntegerBasicType type) {
		this.sType = STypeInt.create();
		processSemanticType(this.sType);
	}

	@Override
	public void visit(IntegerSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("int_exp");
		expressionTemplate.add("value", exp.getValue());
		this.expressionTemplateStack.push(expressionTemplate);
		this.lastIndex = exp.getValue();
	}

	@Override
	public void visit(IterateCommand command) {
		this.iteratorCount++;
		ST localCommandTemplate = groupTemplate.getInstanceOf("iterate");
		localCommandTemplate.add("iterator", this.iteratorCount);
		command.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		command.getCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
		this.iteratorCount--;
	}

	@Override
	public void visit(LessRExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("lt_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(ListCommand commands) {
		ST listTemplate = groupTemplate.getInstanceOf("list_command");
		List<ST> localCommandTemplates = new ArrayList<ST>();
		for (Command cmd : commands.getCommands()) {
			cmd.accept(this);
			localCommandTemplates.add(this.commandTemplate);
		}
		listTemplate.add("commands", localCommandTemplates);
		this.commandTemplate = listTemplate;
	}

	@Override
	public void visit(ModulusMExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("mod_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(MultiplicationMExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("mult_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(NegationSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("not_exp");
		exp.getSExpression().accept(this);
		expressionTemplate.add("exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(NegativeSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("neg_exp");
		exp.getSExpression().accept(this);
		expressionTemplate.add("exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(NewPExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("new_exp");
		Type type = exp.getType();
		int offset = 0;
		while (type instanceof ArrayType) {
			offset++;
			ArrayType arrayType = (ArrayType) type;
			type = arrayType.getType();
		}
		type.accept(this);

		expressionTemplate.add("type", this.typeTemplate);
		expressionTemplate.add("offset", new int[offset]);

		if (exp.getExpression() != null) {
			exp.getExpression().accept(this);
			expressionTemplate.add("exp", this.expressionTemplateStack.pop());
		}

		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(NullSExpression node) {
		ST nullTemplate = groupTemplate.getInstanceOf("null_exp");
		this.expressionTemplateStack.push(nullTemplate);
	}

	@Override
	public void visit(ObjectLValue lvalueObject) {
		ST objectLValueTemplate = groupTemplate.getInstanceOf("object_lvalue");
		lvalueObject.getLValue().accept(this);

		STypeCustom parentType = (STypeCustom) this.lvalueTypeStack.pop();
		Map<String, SemanticType> map = this.dataMap.get(parentType.toString());
		this.lvalueTypeStack.push(map.get(lvalueObject.getParamID()));

		objectLValueTemplate.add("lvalue", this.expressionTemplateStack.pop());
		objectLValueTemplate.add("name", lvalueObject.getParamID());
		objectLValueTemplate.add("prefix", PREFIX);
		this.expressionTemplateStack.push(objectLValueTemplate);
	}

	@Override
	public void visit(Parameter param) {
		ST paramTemplate = this.groupTemplate.getInstanceOf("param");
		paramTemplate.add("name", param.getId());
		paramTemplate.add("prefix", PREFIX);
		param.getType().accept(this);
		paramTemplate.add("type", this.typeTemplate);
		this.paramTemplates.add(paramTemplate);
	}

	@Override
	public void visit(ParenthesisPExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("parenthesis_exp");
		exp.getExpression().accept(this);
		expressionTemplate.add("exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(PrintCommand command) {
		command.getExpression().accept(this);
		ST printTemplate = groupTemplate.getInstanceOf("print");
		printTemplate.add("exp", this.expressionTemplateStack.pop());
		this.commandTemplate = printTemplate;
	}

	@Override
	public void visit(ReadCommand cmd) {
		cmd.getLValue().accept(this);
		SemanticType sType = this.lvalueTypeStack.pop();

		ST cmdTemplate;
		if (sType instanceof STypeInt)
			cmdTemplate = this.groupTemplate.getInstanceOf("read_int");
		else if (sType instanceof STypeFloat)
			cmdTemplate = this.groupTemplate.getInstanceOf("read_float");
		else if (sType instanceof STypeBool)
			cmdTemplate = this.groupTemplate.getInstanceOf("read_bool");
		else
			cmdTemplate = this.groupTemplate.getInstanceOf("read_char");

		cmdTemplate.add("lvalue", this.expressionTemplateStack.pop());
		cmdTemplate.add("scanner", "scanner" + (this.scannerCount++));
		this.commandTemplate = cmdTemplate;
	}

	@Override
	public void visit(ReturnCommand cmd) {
		ST localCommandTemplate = groupTemplate.getInstanceOf("return");
		localCommandTemplate.add("rv_count", this.returnCount++);
		localCommandTemplate.add("size", cmd.getReturnExpressions().size());
		List<ST> returnExpTemplates = new ArrayList<ST>();
		for (Expression exp : cmd.getReturnExpressions()) {
			exp.accept(this);
			returnExpTemplates.add(this.expressionTemplateStack.pop());
		}
		localCommandTemplate.add("exps", returnExpTemplates);
		this.commandTemplate = localCommandTemplate;
	}

	@Override
	public void visit(SubtractionAExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("sub_exp");
		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(TrueSExpression node) {
		ST trueTemplate = groupTemplate.getInstanceOf("true_exp");
		this.expressionTemplateStack.push(trueTemplate);
	}

	@Override
	public void visit(LValueContext lvalueCtx) {
		lvalueCtx.getLValue().accept(this);
	}

	////////////// Métodos ///////////
	private void processSemanticType(SemanticType t) {
		if (t instanceof STypeInt)
			this.typeTemplate = this.groupTemplate.getInstanceOf("int_type");
		else if (t instanceof STypeBool)
			this.typeTemplate = this.groupTemplate.getInstanceOf("boolean_type");
		else if (t instanceof STypeFloat)
			this.typeTemplate = this.groupTemplate.getInstanceOf("float_type");
		else if (t instanceof STypeChar)
			this.typeTemplate = this.groupTemplate.getInstanceOf("char_type");
		else if (t instanceof STypeCustom) {
			ST aux = this.groupTemplate.getInstanceOf("custom_type");
			aux.add("prefix", PREFIX);
			aux.add("name", t.toString());
			this.typeTemplate = aux;
		} else if (t instanceof STypeArray) {
			ST aux = this.groupTemplate.getInstanceOf("array_type");
			processSemanticType(((STypeArray) t).getType());
			aux.add("type", this.typeTemplate);
			this.typeTemplate = aux;
		} else {
			this.typeTemplate = null;
		}
	}

}
