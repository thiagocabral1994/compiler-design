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
import java.util.Stack;

public class JasminVisitor extends Visitor {
	private static final String PREFIX = "_";
	private STGroup groupTemplate;
	private List<Pair<String, String>> renderedTemplates;
	private ST programTemplate, typeTemplate, commandTemplate;
	private SemanticType sType;
	private List<ST> functionTemplates, paramTemplates, commandTemplates;
	private Stack<ST> expressionTemplateStack;
	private int iteratorCount = 0;
	private int ifCount = 0;
	private int returnCount = 0;
	private int lessCount = 0;
	private int lastIndex;
	private LocalEnv<Pair<SemanticType, Integer>> localEnv;
	private int localSize;
	private Stack<Pair<SemanticType, Integer>> lvaluePairStack;

	private String fileName;

	private Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> env;
	private Map<String, Map<String, SemanticType>> dataMap;
	private Stack<SemanticType> lvalueTypeStack;

	public JasminVisitor(String fileName, Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> env, Map<String, Map<String, SemanticType>> map) {
		this.groupTemplate = new STGroupFile("./template/jasmin.stg");
		this.renderedTemplates = new ArrayList<>();
		this.fileName = fileName;
		this.env = env;
		this.expressionTemplateStack = new Stack<ST>();
		this.dataMap = map;
		this.lvalueTypeStack = new Stack<SemanticType>();
		this.lvaluePairStack = new Stack<>();
	}

	public String getProgramTemplate() throws IOException {
		return this.programTemplate.render();
	}

	public List<Pair<String, String>> getDataTemplates() throws IOException {
		return this.renderedTemplates;
	}

	public void visit(Program program) {
		this.programTemplate = this.groupTemplate.getInstanceOf("program");
		this.programTemplate.add("name", fileName);
		this.programTemplate.add("prefix", PREFIX);

		for (Data data : program.getDatas()) {
			data.accept(this);
		}

		this.functionTemplates = new ArrayList<ST>();
		for (Function function : program.getFunctions()) {
			function.accept(this);
		}
		this.programTemplate.add("functions", this.functionTemplates);
	}

	@Override
	public void visit(AdditionAExpression exp) {

		SemanticType expType = exp.getSemanticType();
		String  attrRef;

		if (expType instanceof STypeInt) {
			attrRef = "add_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "add_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "add_int";
		} else {
			attrRef = "add_bool";
		}
		ST expressionTemplate = groupTemplate.getInstanceOf(attrRef);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairL.getLeft() instanceof STypeInt) || (pairL.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairR.getLeft() instanceof STypeInt) || (pairR.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertR", "i2f");
		}
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
		processSemanticType(STypeArray.create(this.sType));
	}

	@Override
	public void visit(AssignmentCommand command) {
		Expression exp = command.getExpression();
		SemanticType expType = exp.getSemanticType();
		String  attrRef;
		if (expType instanceof STypeInt) {
			attrRef = "assignment_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "assignment_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "assignment_char";
		} else {
			attrRef = "assignment_bool";
		}
		ST localCommandTemplate = this.groupTemplate.getInstanceOf(attrRef);
		command.getLValueContext().accept(this);
		Pair<SemanticType, Integer> pair = this.lvaluePairStack.pop();
		ST lvalueLabelTemplate = this.groupTemplate.getInstanceOf("lvalue_label");
		lvalueLabelTemplate.add("label", pair.getRight());
		localCommandTemplate.add("lvalue", lvalueLabelTemplate);
		exp.accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		this.commandTemplate = localCommandTemplate;
	}

	@Override
	public void visit(BooleanBasicType node) {
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
		expressionTemplate.add("value", value.hashCode());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(CharacterBasicType node) {
		processSemanticType(STypeChar.create());
	}

	@Override
	public void visit(CustomBasicType type) {
		processSemanticType(STypeCustom.create(type.getTypeName()));
	}

	@Override
	public void visit(Data data) {
		ST dataTemplate = this.groupTemplate.getInstanceOf("data");
		dataTemplate.add("name", data.getId());
		dataTemplate.add("filename", this.fileName);
		dataTemplate.add("prefix", PREFIX);

		this.paramTemplates = new ArrayList<ST>();
		for (Parameter param : data.getDeclarations()) {
			ST paramTemplate = this.groupTemplate.getInstanceOf("field");
			paramTemplate.add("name", param.getId());
			paramTemplate.add("prefix", PREFIX);
			param.getType().accept(this);
			paramTemplate.add("type", this.typeTemplate);
			this.paramTemplates.add(paramTemplate);
		}
		dataTemplate.add("fields", this.paramTemplates);

		this.renderedTemplates.add(new Pair<>(data.getId(), dataTemplate.render()));
	}

	@Override
	public void visit(DivisionMExpression exp) {
		SemanticType expType = exp.getSemanticType();
		String  attrRef;

		if (expType instanceof STypeInt) {
			attrRef = "div_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "div_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "div_int";
		} else {
			attrRef = "div_bool";
		}
		ST expressionTemplate = groupTemplate.getInstanceOf(attrRef);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairL.getLeft() instanceof STypeInt) || (pairL.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairR.getLeft() instanceof STypeInt) || (pairR.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertR", "i2f");
		}
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
		processSemanticType(STypeFloat.create());
	}

	@Override
	public void visit(FloatSExpression exp) {
		ST expressionTemplate = groupTemplate.getInstanceOf("float_exp");
		expressionTemplate.add("value", exp.getValue());
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(Function function) {
		ST functionTemplate = this.groupTemplate.getInstanceOf(function.getReturnTypes().size() > 0 ? "function" : "function_void");
		functionTemplate.add("name", function.getId());
		functionTemplate.add("prefix", PREFIX);

		STypeFunctionKey functionKey = STypeFunctionKey.create(function.getId(), function.getFunctionType().getParams());
		this.localEnv = this.env.get(functionKey);
		this.localSize = this.localEnv.getKeys().size();
		functionTemplate.add("stack", this.localEnv.getMaxStackSize());

		List<ST> paramTemplates = new ArrayList<ST>();
		for (Parameter param : function.getParameters()) {
			ST paramTemp = this.groupTemplate.getInstanceOf("param");
			param.getType().accept(this);
			paramTemp.add("type", this.typeTemplate);
			paramTemplates.add(paramTemp);
		}
		functionTemplate.add("params", paramTemplates);

		this.commandTemplates = new ArrayList<ST>();
		for (Command command : function.getCommands()) {
			command.accept(this);
			this.commandTemplates.add(this.commandTemplate);
		}
		functionTemplate.add("commands", this.commandTemplates);

		functionTemplate.add("locals", this.localSize);
		this.functionTemplates.add(functionTemplate);
	}

	@Override
	public void visit(IdentifierLValue lvalue) {
		Pair<SemanticType, Integer> pair = this.localEnv.get(lvalue.getID());
		this.lvaluePairStack.push(pair);
		SemanticType type = pair.getLeft();
		Integer label = pair.getRight();
		String  lvalueRef;
		if (type instanceof STypeInt) {
			lvalueRef = "lvalue_int";
		} else if (type instanceof STypeFloat) {
			lvalueRef = "lvalue_float";
		} else if (type instanceof STypeChar) {
			lvalueRef = "lvalue_char";
		} else {
			lvalueRef = "lvalue_bool";
		}
		ST template = this.groupTemplate.getInstanceOf(lvalueRef);
		template.add("label", label);
		this.expressionTemplateStack.push(template);
	}

	@Override
	public void visit(IfCommand cmd) {
		this.ifCount++;
		ST localCommandTemplate = groupTemplate.getInstanceOf("if");
		localCommandTemplate.add("num", this.ifCount);
		cmd.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		cmd.getCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
		this.ifCount--;		
	}

	@Override
	public void visit(IfElseCommand cmd) {
		this.ifCount++;
		ST localCommandTemplate = groupTemplate.getInstanceOf("if");
		localCommandTemplate.add("num", this.ifCount);
		cmd.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		cmd.getIfCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		cmd.getElseCommand().accept(this);
		localCommandTemplate.add("els", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
		this.ifCount--;
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
		processSemanticType(STypeInt.create());
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
		localCommandTemplate.add("label_iterator", this.iteratorCount);
		command.getExpression().accept(this);
		localCommandTemplate.add("exp", this.expressionTemplateStack.pop());
		command.getCommand().accept(this);
		localCommandTemplate.add("thn", this.commandTemplate);
		this.commandTemplate = localCommandTemplate;
		this.iteratorCount--;
	}

	@Override
	public void visit(LessRExpression exp) {
		this.lessCount++;


		ST expressionTemplate = groupTemplate.getInstanceOf("lt_exp");
		expressionTemplate.add("num", this.lessCount);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (pairL.getLeft() instanceof STypeInt  || pairL.getLeft() instanceof STypeChar){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (pairR.getLeft() instanceof STypeInt || pairR.getLeft() instanceof STypeChar){
			expressionTemplate.add("convertR", "i2f");
		}
		this.expressionTemplateStack.push(expressionTemplate);
		this.lessCount++;
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
		SemanticType expType = exp.getSemanticType();
		String  attrRef;

		if (expType instanceof STypeInt) {
			attrRef = "mod_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "mod_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "mod_int";
		} else {
			attrRef = "mod_bool";
		}
		ST expressionTemplate = groupTemplate.getInstanceOf(attrRef);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairL.getLeft() instanceof STypeInt) || (pairL.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairR.getLeft() instanceof STypeInt) || (pairR.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertR", "i2f");
		}
		this.expressionTemplateStack.push(expressionTemplate);
	}

	@Override
	public void visit(MultiplicationMExpression exp) {
		SemanticType expType = exp.getSemanticType();
		String  attrRef;

		if (expType instanceof STypeInt) {
			attrRef = "mult_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "mult_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "mult_int";
		} else {
			attrRef = "mult_bool";
		}
		ST expressionTemplate = groupTemplate.getInstanceOf(attrRef);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairL.getLeft() instanceof STypeInt) || (pairL.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairR.getLeft() instanceof STypeInt) || (pairR.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertR", "i2f");
		}
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
		// Ignora
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
		Expression exp = command.getExpression();
		exp.accept(this);
		SemanticType expType = exp.getSemanticType();
		String  templateRef;
		if (expType instanceof STypeInt) {
			templateRef = "print_int";
		} else if (expType instanceof STypeFloat) {
			templateRef = "print_float";
		} else if (expType instanceof STypeChar) {
			templateRef = "print_char";
		} else {
			templateRef = "print_bool";
		}
		ST printTemplate = groupTemplate.getInstanceOf(templateRef);
		printTemplate.add("exp", this.expressionTemplateStack.pop());
		this.commandTemplate = printTemplate;
	}

	@Override
	public void visit(ReadCommand cmd) {
		cmd.getLValue().accept(this);
		Pair<SemanticType, Integer> pair = this.lvaluePairStack.pop();
		
		SemanticType sType = pair.getLeft();


		String  templateRef;
		if (sType instanceof STypeInt) {
			templateRef = "read_int";
		} else if (sType instanceof STypeFloat) {
			templateRef = "read_float";
		} else if (sType instanceof STypeChar) {
			templateRef = "read_char";
		} else {
			templateRef = "read_bool";
		}

		ST readTemplate = groupTemplate.getInstanceOf(templateRef);

		ST lvalueLabelTemplate = this.groupTemplate.getInstanceOf("lvalue_label");
		lvalueLabelTemplate.add("label", pair.getRight());
		readTemplate.add("lvalue", lvalueLabelTemplate);
		readTemplate.add("scanner", cmd.getLabel());
		this.localSize++;
		this.commandTemplate = readTemplate;
	}

	@Override
	public void visit(ReturnCommand cmd) {
		ST localCommandTemplate = groupTemplate.getInstanceOf("return");
		localCommandTemplate.add("size_label", cmd.getLabel());
		this.localSize++;
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
		SemanticType expType = exp.getSemanticType();
		String  attrRef;

		if (expType instanceof STypeInt) {
			attrRef = "sub_int";
		} else if (expType instanceof STypeFloat) {
			attrRef = "sub_float";
		} else if (expType instanceof STypeChar) {
			attrRef = "sub_int";
		} else {
			attrRef = "sub_bool";
		}
		ST expressionTemplate = groupTemplate.getInstanceOf(attrRef);

		exp.getLeft().accept(this);
		expressionTemplate.add("left_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairL = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairL.getLeft() instanceof STypeInt) || (pairL.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertL", "i2f");
		}
		
		exp.getRight().accept(this);
		expressionTemplate.add("right_exp", this.expressionTemplateStack.pop());

		Pair<SemanticType, Integer> pairR = this.lvaluePairStack.pop();
		if (expType instanceof STypeFloat && ((pairR.getLeft() instanceof STypeInt) || (pairR.getLeft() instanceof STypeChar))){
			expressionTemplate.add("convertR", "i2f");
		}
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
		this.sType = t;

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
			aux.add("filename", this.fileName);
			aux.add("name", t.toString());
			this.typeTemplate = aux;
		} else if (t instanceof STypeArray) {
			ST aux = this.groupTemplate.getInstanceOf("array_type");
			processSemanticType(((STypeArray) t).getType());
			aux.add("type", this.typeTemplate);
			this.typeTemplate = aux;
		}
	}

}
