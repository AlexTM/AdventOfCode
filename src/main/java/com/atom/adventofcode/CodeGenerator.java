package com.atom.adventofcode;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JCodeModelException;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JPackage;

public class CodeGenerator {

    public static void main(String[] args) throws JCodeModelException {

        JCodeModel codeModel = new JCodeModel();

        JPackage jp = codeModel._package("com.sookocheff.example");
        JDefinedClass jc = jp._class("Generated");

        jc.javadoc().add("Generated class.");


    }
}
