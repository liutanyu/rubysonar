package org.yinwang.rubysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.rubysonar.State;
import org.yinwang.rubysonar.types.ListType;
import org.yinwang.rubysonar.types.Type;

import java.util.List;


public class GeneratorExp extends Node {

    public Node elt;
    public List<Comprehension> generators;


    public GeneratorExp(Node elt, List<Comprehension> generators, int start, int end) {
        super(start, end);
        this.elt = elt;
        this.generators = generators;
        addChildren(elt);
        addChildren(generators);
    }


    /**
     * Python's list comprehension will erase any variable used in generators.
     * This is wrong, but we "respect" this bug here.
     */
    @NotNull
    @Override
    public Type transform(State s) {
        resolveList(generators, s);
        return new ListType(transformExpr(elt, s));
    }


    @NotNull
    @Override
    public String toString() {
        return "<GeneratorExp:" + start + ":" + elt + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(elt, v);
            visitNodes(generators, v);
        }
    }
}