package org.yinwang.rubysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.rubysonar.Analyzer;
import org.yinwang.rubysonar.State;
import org.yinwang.rubysonar.types.Type;


/**
 * A name alias.  Used for the components of import and import-from statements.
 */
public class Withitem extends Node {

    @Nullable
    public Node optional_vars;
    @NotNull
    public Node context_expr;


    public Withitem(@NotNull Node context_expr, @Nullable Node optional_vars, int start, int end) {
        super(start, end);
        this.context_expr = context_expr;
        this.optional_vars = optional_vars;
        addChildren(context_expr, optional_vars);
    }


    @NotNull
    @Override
    public String toString() {
        return "<withitem:" + context_expr + " as " + optional_vars + ">";
    }


    // dummy, will never be called
    @NotNull
    @Override
    public Type transform(State s) {
        return Analyzer.self.builtins.unknown;
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(context_expr, v);
            visitNode(optional_vars, v);
        }
    }
}