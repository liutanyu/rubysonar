package org.yinwang.rubysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.rubysonar.Analyzer;
import org.yinwang.rubysonar.Binding;
import org.yinwang.rubysonar.State;
import org.yinwang.rubysonar.types.Type;

import java.util.List;


public class Import extends Node {

    public List<Alias> names;


    public Import(List<Alias> names, int start, int end) {
        super(start, end);
        this.names = names;
        addChildren(names);
    }


    @NotNull
    @Override
    public Type transform(@NotNull State s) {
        for (Alias a : names) {
            Type mod = Analyzer.self.loadModule(a.name, s);
            if (mod == null) {
                Analyzer.self.putProblem(this, "Cannot load module");
            } else if (a.asname != null) {
                s.insert(a.asname.id, a.asname, mod, Binding.Kind.VARIABLE);
            }
        }
        return Analyzer.self.builtins.Cont;
    }


    @NotNull
    @Override
    public String toString() {
        return "<Import:" + names + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNodes(names, v);
        }
    }
}