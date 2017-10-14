/*
 * The MIT License
 *
 * Copyright (C) 2011 by Anthony Robinson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkins_ci.plugins.run_condition.core;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.jenkins_ci.plugins.run_condition.Messages;
import org.jenkins_ci.plugins.run_condition.common.AlwaysPrebuildRunCondition;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Arrays;
import java.util.List;

public class CSVContainsCondition extends AlwaysPrebuildRunCondition {

    final String arg1;
    final String arg2;
    final boolean ignoreCase;
    private static final String OPTIONS_SEPARATOR = ",";

    @DataBoundConstructor
    public CSVContainsCondition(final String arg1, final String arg2, final boolean ignoreCase) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.ignoreCase = ignoreCase;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    @Override
    public boolean runPerform(final AbstractBuild<?, ?> build, final BuildListener listener) throws Exception {
        String availableOptions = TokenMacro.expandAll(build, listener, arg1);
        String optionToBeChecked = TokenMacro.expandAll(build, listener, arg2);
        listener.getLogger().println(Messages.cSVContainsCondition_console_args(availableOptions, optionToBeChecked));
        if (isIgnoreCase()) {
            availableOptions = availableOptions.toLowerCase().trim();
            optionToBeChecked = optionToBeChecked.toLowerCase().trim();
        }
        List<String> availOptionList = Arrays.asList(availableOptions.split(OPTIONS_SEPARATOR));
        List<String> neededOption = Arrays.asList(optionToBeChecked.split(OPTIONS_SEPARATOR));
        return availOptionList.containsAll(neededOption);
    }

    @Extension
    public static class StringsMatchDescriptor extends RunConditionDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.cSVContainsCondition_displayName();
        }

    }

}
