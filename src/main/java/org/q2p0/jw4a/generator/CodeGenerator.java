package org.q2p0.jw4a.generator;

import org.q2p0.jw4a.ast.nodes.AST_Package;

public interface CodeGenerator {
    void generate(AST_Package ASTBuilder, String outputDirectory);
}
