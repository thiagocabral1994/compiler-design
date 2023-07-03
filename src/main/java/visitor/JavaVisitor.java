package visitor;

import java.util.Map;

import util.semantic.*;

public class JavaVisitor extends CodeGeneratorVisitor {
	public JavaVisitor(String fileName, TypeEnv<LocalEnv<SemanticType>> env, Map<String, Map<String, SemanticType>> map) {
		super("./template/java.stg", "_", fileName, env, map);
	}
}
